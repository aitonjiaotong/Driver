package com.aiton.administrator.shane_library.shane.app;

/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

import android.app.Application;
import android.content.Context;
import cn.bmob.v3.Bmob;

import com.activeandroid.ActiveAndroid;
import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BaseApp extends Application {

	/**
	 * SDK初始化建议放在启动页
	 * 注意：替换自己的ID
	 */
	public static String APPID = "cf69e4f1d3a5e4c6cb59cbdbf7c1b688";

	public void onCreate() {
		super.onCreate();
		ActiveAndroid.initialize(this);
		initImageLoader(getApplicationContext());
		initConfig(getApplicationContext());
		Bmob.initialize(getApplicationContext(),APPID);
	
		// JPushInterface.setDebugMode(true); // 璁剧疆锟�?鍚棩锟�?,鍙戝竷鏃惰鍏抽棴鏃ュ織
		// JPushInterface.init(this); // 鍒濆锟�? JPush

	}


	/**
	 * 初始化文件配置
	 * @param context
	 */
	public static void initConfig(Context context) {
		BmobConfiguration config = new BmobConfiguration.Builder(context).customExternalCacheDir("Smile").build();
		BmobPro.getInstance(context).initConfig(config);
	}
	

	// @Override
	// public void onTerminate() {
	// super.onTerminate();
	// ActiveAndroid.dispose();
	// }

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}