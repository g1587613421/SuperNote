package app.gaojinlei.tools;

import android.content.SharedPreferences;


import app.gaojinlei.note.module.base.BaseActivity;

/**
 * Created by 高金磊 on 2018/10/17.
 */

public class StoreData {//设置信息&其他基本信息综合存储器
   private static SharedPreferences.Editor editor= BaseActivity.editor;
   private static SharedPreferences preference=BaseActivity.preferences;
   /**
    *
    * 一次通过数组传入多个值的方法等待实现*/
    public static boolean PutKeyValues(String Key, boolean value){
        editor.putBoolean(Key,value);
        editor.commit();
        return true;
    }
    public static boolean PutKeyValues(String Key, String value){
        editor.putString(Key,value);
        editor.commit();
        return true;
    }
    public static boolean PutKeyValues(String Key, int value){
        editor.putInt(Key,value);
        editor.commit();
        return true;
    }
    public static boolean PutKeyValues(String Key, float value){
        editor.putFloat(Key,value);
        editor.commit();
        return true;
    }
    public static boolean PutKeyValues(String Key, long value){
        editor.putLong(Key,value);
        editor.commit();
        return true;
    }

    public static SharedPreferences getPreference() {//返回Preference交托其他具体代码实现功能
        return BaseActivity.preferences;
    }

    public static SharedPreferences.Editor getEditor() {
        return editor;
    }
}
