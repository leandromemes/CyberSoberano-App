# Creditos: Leandro - Soberano - Versão Estável 2026
LOCAL_PATH := $(call my-dir)

# --- 1. Definir a biblioteca pré-compilada do Node.js ---
include $(CLEAR_VARS)
LOCAL_MODULE := node
# Certifique-se que o arquivo está em: app/src/main/jniLibs/arm64-v8a/libnode.so
LOCAL_SRC_FILES := ../jniLibs/$(TARGET_ARCH_ABI)/libnode.so
include $(PREBUILT_SHARED_LIBRARY)

# --- 2. Compilar a Ponte do Terminal (CyberSoberano-Term) ---
include $(CLEAR_VARS)
LOCAL_MODULE := cybersoberano-term
LOCAL_SRC_FILES := ../jni/termux.c

# Adiciona suporte para os símbolos da versão v18.20.4
LOCAL_SHARED_LIBRARIES := node
LOCAL_LDLIBS := -llog -landroid

# Comando crucial para resolver o erro 'undefined symbol: node_main'
LOCAL_LDFLAGS := -Wl,--export-dynamic -Wl,--unresolved-symbols=ignore-all
LOCAL_ALLOW_UNDEFINED_SYMBOLS := true

include $(BUILD_SHARED_LIBRARY)

# --- 3. Compilar o Local Socket ---
include $(CLEAR_VARS)
LOCAL_MODULE := local-socket
LOCAL_SRC_FILES := local-socket.cpp
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)
