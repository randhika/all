package com.asmack;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * @author zhanghaitao
 * @date 2011-7-6
 * @version 1.0
 */
public class Util {
	private final String PREFERENCE_NAME = "gtalk";
	private Context mContext;
	private SharedPreferences mSharedPreferences;

	public Util(Context context) {
		mContext = context;
		mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
	}

	// 显示提示信息
	public void showMsg(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	// 保存配置信息
	public void saveString(String key, String value) {
		mSharedPreferences.edit().putString(key, value).commit();
	}

	// 获得配置信息
	public String getString(String key, String... defValue) {
		if (defValue.length > 0)
			return mSharedPreferences.getString(key, defValue[0]);
		else
			return mSharedPreferences.getString(key, "");

	}

	// 截取指定分隔符前面的字符串
	public static String getLeftString(String s, String separator) {
		int index = s.indexOf(separator);
		if (index > -1)
			return s.substring(0, index);
		else
			return s;
	}

}
