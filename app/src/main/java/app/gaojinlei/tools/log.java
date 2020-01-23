package app.gaojinlei.tools;

/**
 * Created by 高金磊 on 2019/4/18.
 */

public class log {
    private static String log;
    public static void addlog(String log1){//追加日志
        log+=OrtherTool.getTime()+log1;
        System.out.println(OrtherTool.getTime()+log1);//调试日志
        StoreData.getEditor().putString("log",log);
    }

    public static String getLog() {
        return log;
    }
}
