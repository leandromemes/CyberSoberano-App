/* * Créditos: dev Leandro - CyberSoberano
 * Motor Nativo de Comunicação via Local Sockets.
 */

#include <cstdio>
#include <ctime>
#include <cerrno>
#include <jni.h>
#include <sstream>
#include <string>
#include <unistd.h>
#include <android/log.h>
#include <sys/ioctl.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/un.h>

#define LOG_TAG "CyberSoberano-Native"
#define JNI_EXCEPTION "jni-exception"

using namespace std;

// --- Utilitários de Conversão JNI/C++ ---

string jstring_to_stdstr(JNIEnv *env, jstring jString) {
    jclass stringClass = env->FindClass("java/lang/String");
    jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "()[B");
    jbyteArray jStringBytesArray = (jbyteArray) env->CallObjectMethod(jString, getBytes);
    jsize length = env->GetArrayLength(jStringBytesArray);
    jbyte* jStringBytes = env->GetByteArrayElements(jStringBytesArray, nullptr);
    std::string stdString((char *)jStringBytes, length);
    env->ReleaseByteArrayElements(jStringBytesArray, jStringBytes, JNI_ABORT);
    return stdString;
}

bool checkJniException(JNIEnv *env) {
    if (env->ExceptionCheck()) {
        jthrowable throwable = env->ExceptionOccurred();
        if (throwable != NULL) {
            env->ExceptionClear();
            env->Throw(throwable);
            return true;
        }
    }
    return false;
}

jobject getJniResult(JNIEnv *env, jstring title, const int retvalParam, const int errnoParam,
                     string errmsgParam, const int intDataParam) {
    jclass clazz = env->FindClass("com/cybersoberano/app/shared/jni/models/JniResult");
    if (checkJniException(env)) return NULL;

    jmethodID constructor = env->GetMethodID(clazz, "<init>", "(IILjava/lang/String;I)V");
    if (checkJniException(env)) return NULL;

    jstring jmsg = env->NewStringUTF(errmsgParam.c_str());
    return env->NewObject(clazz, constructor, retvalParam, errnoParam, jmsg, intDataParam);
}

jobject getJniResult(JNIEnv *env, jstring title, const int retvalParam, const int errnoParam) {
    return getJniResult(env, title, retvalParam, errnoParam, strerror(errnoParam), 0);
}

jobject getJniResult(JNIEnv *env, jstring title, const int intDataParam) {
    return getJniResult(env, title, 0, 0, "", intDataParam);
}

// --- Funções Principais ---

extern "C"
JNIEXPORT jobject JNICALL
Java_com_cybersoberano_app_shared_net_socket_local_LocalSocketManager_createServerSocketNative(
        JNIEnv *env, jclass clazz, jstring logTitle, jbyteArray pathArray, jint backlog) {

    int fd = socket(AF_UNIX, SOCK_STREAM, 0);
    if (fd == -1) return getJniResult(env, logTitle, -1, errno);

    jbyte* path = env->GetByteArrayElements(pathArray, nullptr);
    int chars = env->GetArrayLength(pathArray);

    struct sockaddr_un adr = {.sun_family = AF_UNIX};
    memcpy(&adr.sun_path, path, chars);

    if (::bind(fd, (struct sockaddr*)&adr, sizeof(adr)) == -1) {
        int err = errno;
        env->ReleaseByteArrayElements(pathArray, path, JNI_ABORT);
        close(fd);
        return getJniResult(env, logTitle, -1, err);
    }

    if (listen(fd, backlog) == -1) {
        int err = errno;
        env->ReleaseByteArrayElements(pathArray, path, JNI_ABORT);
        close(fd);
        return getJniResult(env, logTitle, -1, err);
    }

    env->ReleaseByteArrayElements(pathArray, path, JNI_ABORT);
    return getJniResult(env, logTitle, fd);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_cybersoberano_app_shared_net_socket_local_LocalSocketManager_sendNative(
        JNIEnv *env, jclass clazz, jstring logTitle, jint fd, jbyteArray dataArray, jlong deadline) {

    jbyte* data = env->GetByteArrayElements(dataArray, nullptr);
    int bytes = env->GetArrayLength(dataArray);

    int ret = send(fd, data, bytes, MSG_NOSIGNAL);
    int err = (ret == -1) ? errno : 0;

    env->ReleaseByteArrayElements(dataArray, data, JNI_ABORT);

    if (ret == -1) return getJniResult(env, logTitle, -1, err);
    // CORREÇÃO AQUI: Passando 0 para indicar sucesso no envio
    return getJniResult(env, logTitle, 0);
}
