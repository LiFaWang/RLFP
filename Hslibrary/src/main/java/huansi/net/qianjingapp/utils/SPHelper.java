package huansi.net.qianjingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static android.content.Context.MODE_PRIVATE;

public class SPHelper {
	public static final String RLFP_IP="rlfp_ip";//人力分派的IP

	public static void saveLocalData(Context context, String key, Object value,String  className) {
		SharedPreferences preferences = context.getSharedPreferences("QCApp", MODE_PRIVATE);
		Editor editor = preferences.edit();

		if (className.equalsIgnoreCase(Integer.class.getName())) {
			editor.putInt(key, (Integer) value);
		} else if (className.equalsIgnoreCase(String.class.getName())) {
			editor.putString(key, value.toString());
		} else if (className.equalsIgnoreCase(Boolean.class.getName())) {
			editor.putBoolean(key, (Boolean) value);
		} else if (className.equalsIgnoreCase(Float.class.getName())) {
			editor.putFloat(key, (Float) value);
		} else if (className.equalsIgnoreCase(Long.class.getName())) {
			editor.putLong(key, (Long) value);
		}
		editor.apply();
	}

	public static Object getLocalData(Context context, String key,String  className,Object defaultValue) {
		SharedPreferences preferences = context.getSharedPreferences("QCApp", MODE_PRIVATE);
		if(className.equalsIgnoreCase(Integer.class.getName())) return preferences.getInt(key, (Integer) defaultValue);
		if(className.equalsIgnoreCase(String.class.getName())) return preferences.getString(key, defaultValue.toString());
		if(className.equalsIgnoreCase(Boolean.class.getName())) return preferences.getBoolean(key, (Boolean) defaultValue);
		if(className.equalsIgnoreCase(Float.class.getName())) return preferences.getFloat(key, (Float) defaultValue);
		if(className.equalsIgnoreCase(Long.class.getName())) return preferences.getLong(key, (Long) defaultValue);
        return "";
	}
}
