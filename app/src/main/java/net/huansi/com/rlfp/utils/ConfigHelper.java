package net.huansi.com.rlfp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConfigHelper {
	private static ConfigHelper configHelper;
	private ConfigHelper(){}
	public static ConfigHelper getInstance(){
		if(configHelper==null){
			configHelper=new ConfigHelper();
		}
		return configHelper;
	}

	public  void saveLocalData(Context context,String sKeyName,String sKeyValue) {
		SharedPreferences preferences = context.getSharedPreferences("hsApp", Activity.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(sKeyName,sKeyValue);
		editor.commit();
	}

	public  String getLocalData(Context context,String sKeyName) {
		SharedPreferences preferences = context.getSharedPreferences("hsApp", Activity.MODE_PRIVATE);
		return preferences.getString(sKeyName,"");
	}
}
