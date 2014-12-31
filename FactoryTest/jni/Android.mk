LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := Gyroscopejni
LOCAL_SRC_FILES := Gyroscopejni.c
LOCAL_C_INCLUDES :=$(LOCAL_PATH) D:\android-ndk-r9\include
LOCAL_CERTIFICATE := platform
# π”√Android Log
LOCAL_LDLIBS += -llog

include $(BUILD_SHARED_LIBRARY)
