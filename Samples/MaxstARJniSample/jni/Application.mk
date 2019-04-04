# Build ARMv7-A machine code.
APP_ABI := armeabi-v7a arm64-v8a x86 x86_64
APP_OPTIM := release
APP_PLATFORM := android-18
APP_STL := gnustl_static
APP_CPPFLAGS := -frtti -fexceptions
# APP_CPPFLAGS :=  -std=c++11 -frtti -fexceptions
# NDK_TOOLCHAIN_VERSION := 4.9