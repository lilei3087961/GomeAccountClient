LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
                     
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := \
        $(call all-java-files-under, src) \
        ../../../frameworks/base/core/java/android/accounts/IAccountAuthenticator.aidl \
        ../../../frameworks/base/core/java/android/accounts/IAccountAuthenticatorResponse.aidl

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_PACKAGE_NAME := GomeAccountClient
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

#LOCAL_JAVA_LIBRARIES += telephone-common
LOCAL_STATIC_JAVA_AAR_LIBRARIES:= \
	CityPicker

LOCAL_AAPT_FLAGS := \
--auto-add-overlay \
--extra-packages citypicker.example.com.citypicker

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
 CityPicker:libs/CityPicker.aar
 
LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4 \
		android-support-v7-appcompat \
		GomeTesYuv2
#ifneq ($(INCREMENTAL_BUILDS),)
#    LOCAL_PROGUARD_ENABLED := disabled
#    LOCAL_JACK_ENABLED := incremental
#endif


include $(BUILD_PACKAGE)
#引用jar包
include $(CLEAR_VARS)  
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := GomeTesYuv2:libs/GomeTestYuv.jar
include $(BUILD_MULTI_PREBUILT)
