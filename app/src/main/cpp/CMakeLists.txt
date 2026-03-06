cmake_minimum_required(VERSION 3.22.1)

project("cybersoberano")

# Define onde estão as bibliotecas pré-compiladas (libnode.so)
set(lib_dir ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})

add_library(node-lib SHARED IMPORTED)
set_target_properties(node-lib PROPERTIES IMPORTED_LOCATION ${lib_dir}/libnode.so)

# Cria a sua biblioteca de ponte
add_library(native-lib SHARED native-lib.cpp)

# Localiza a biblioteca de logs do Android
find_library(log-lib log)

# Faz a união de tudo
target_link_libraries(native-lib node-lib ${log-lib})