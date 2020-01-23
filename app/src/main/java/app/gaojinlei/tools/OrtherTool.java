package app.gaojinlei.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 高金磊 on 2019/4/14.
 */

public class OrtherTool {//其他小工具集合
    public static String getTime(){//获取当前时间
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }
}
