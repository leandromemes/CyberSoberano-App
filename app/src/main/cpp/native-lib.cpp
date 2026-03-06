#include <jni.h>
#include <string>
#include <vector>
#include <stdlib.h>
#include <string.h>
#include <android/log.h>

#define LOG_TAG "CYBER_NATIVO"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" int node_start(int argc, char *argv[]);

extern "C" JNIEXPORT jint JNICALL
Java_com_cybersoberano_app_TerminalActivity_iniciarNodeNativo(JNIEnv *env, jobject thiz, jobjectArray args) {
    int argc = env->GetArrayLength(args);
    char **argv = (char **)malloc(argc * sizeof(char *));
    
    for (int i = 0; i < argc; i++) {
        jstring arg = (jstring)env->GetObjectArrayElement(args, i);
        const char *rawArg = env->GetStringUTFChars(arg, 0);
        argv[i] = strdup(rawArg);
        env->ReleaseStringUTFChars(arg, rawArg);
    }
    
    LOGI("🚀 Iniciando motor Node com %d argumentos", argc);
    int result = node_start(argc, argv);
    
    for (int i = 0; i < argc; i++) free(argv[i]);
    free(argv);
    
    return result;
}