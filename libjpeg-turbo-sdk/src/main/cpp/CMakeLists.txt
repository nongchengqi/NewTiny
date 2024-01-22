# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.6)
project(newtiny)
get_filename_component(PARENT_DIR ../ ABSOLUTE)
# header
include_directories(include)
# pcm utils
aux_source_directory(include HEADER_LIB)

link_directories(lib/${CMAKE_ANDROID_ARCH_ABI}/)

add_definitions(-DHAVE_CONFIG_H)
#SET(CMAKE_CXX_FLAGS "-std=c++11")             # c++11
#SET(CMAKE_CXX_FLAGS_DEBUG "$ENV{CXXFLAGS} -O0 -Wall -g2 -ggdb")
#SET(CMAKE_CXX_FLAGS_RELEASE "$ENV{CXXFLAGS} -O3 -Wall")

# android wrapper
add_library(newtiny SHARED
        ${HEADER_LIB}
        com_zxy_libjpegturbo_JpegTurboCompressor.c
        )
#find_library(android-log log jnigraphics)
target_link_libraries(newtiny log jnigraphics jpeg turbojpeg)