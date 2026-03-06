LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := cybersoberano-term
LOCAL_SRC_FILES := termux.c

# Inclui as bibliotecas básicas do sistema e a dl (dynamic linker)
LOCAL_LDLIBS := -llog -landroid -ldl

# Suporte para Android 15 (16kb page size) e exportação de símbolos
LOCAL_LDFLAGS := -Wl,-z,max-page-size=16384 -Wl,--export-dynamic

include $(BUILD_SHARED_LIBRARY)
