/*
 * Gyroscope.c
 *
 *  Created on: 2014Äê12ÔÂ22ÈÕ
 *      Author: laiyang
 */
#include <jni.h>
#include <linux/input.h>
#include <android/log.h>
#include <ctype.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dirent.h>
#include <fcntl.h>
#include <pthread.h>
#include <sys/mount.h>
#include <sys/statfs.h>
#include <dirent.h>
#include <string.h>

#include "linux/hwmsensor.h"
#include "linux/sensors_io.h"


#define MPU3000_FS_MAX_LSB 131
#define LOG_TAG "hah"
int fd = -1;
char* dev = "/dev/gyroscope";
float gyro_x;
float gyro_y;
float gyro_z;

JNIEXPORT jboolean JNICALL Java_com_malata_factorytest_item_Gyroscope_initGyro
  (JNIEnv * env, jobject thiz){
	int err, max_retry = 3, retry_period = 100,retry;
	unsigned int flags;
	if(fd == -1) {
		fd = open("/dev/gyroscope", O_RDONLY);
		if(fd < 0) {
//			__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, strcat("init gyro failed:",strerror(errno)));
			return JNI_FALSE;
		}
		retry = 0;
		while((err = ioctl(fd, GYROSCOPE_IOCTL_INIT,&flags)) && (retry ++ < max_retry)) {
			usleep(retry_period*1000);
		}
		if(err) {
			return JNI_FALSE;
		}
	}
	return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_malata_factorytest_item_Gyroscope_closeGyro
  (JNIEnv * env, jobject thiz){
	if(fd != -1) {
		close(fd);
	}
	fd = -1;
	return JNI_TRUE;
}

JNIEXPORT jstring JNICALL Java_com_malata_factorytest_item_Gyroscope_updateGyro
  (JNIEnv* env, jobject thiz){
	char buf[64];
	int err = -EINVAL;
	int x=0,y=0,z=0;
	int smtRes = 0;
	char data[64];
	memset(data,0,sizeof(data));
	memset(buf,0,sizeof(buf));
	err = ioctl(fd, GYROSCOPE_IOCTL_SMT_DATA, &smtRes);
	if(err > 0) {
//		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, strcat("read gyro data failed:",strerror(errno)));
		return (*env)->NewStringUTF(env, "read gyro data failed");
	}
	err = ioctl(fd, GYROSCOPE_IOCTL_READ_SENSORDATA, buf);
	if(err > 0) {
//		__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, strcat("read gyro data failed:",strerror(errno)));
		return (*env)->NewStringUTF(env, "read gyro data failed");
	}
	sscanf(buf, "%x %x %x", &x, &y, &z);
	gyro_x = ((float)x/MPU3000_FS_MAX_LSB);
	gyro_y = ((float)y/MPU3000_FS_MAX_LSB);
	gyro_z = ((float)z/MPU3000_FS_MAX_LSB);


	sprintf( data, "X: %+7.4f \nY: %+7.4f \nZ: %+7.4f \n", gyro_x, gyro_y, gyro_z);

	return (*env)->NewStringUTF(env, data);
}

