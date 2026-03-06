# Creditos: Leandro - Soberano - Versão Estável 2026
APP_ABI := arm64-v8a
APP_PLATFORM := android-24
APP_STL := c++_shared
APP_OPTIM := release

# Garante que o linker aceite o alinhamento de 4KB da libnode.so antiga
APP_LDFLAGS += -Wl,-z,max-page-size=4096
