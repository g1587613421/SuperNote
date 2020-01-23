package app.gaojinlei.weight;


import java.util.ArrayList;
import java.util.List;

import app.gaojinlei.tools.StoreData;

/**
 * ====================== 列表数据操作类 ========================
 * 模拟数据库
 * @author SGamble
 */
public class WeightData {

    private static List<String> title = new ArrayList<>();
    private static List<String> content =new ArrayList<>();
    final static String DIVI="000000gaojinlei";
    /**
     * 获取数据
     * --初始化了一些数据
     * @author Gamble
     */
    public static List<String> getTitle() {
        //必须从存储中取
        String data= StoreData.getPreference().getString("weight_title","");
        String strings[]=data.split(DIVI);
        title=new ArrayList<>();
        if (strings!=null&&strings.length>0){
            for (String s: strings
                 ) {
                title.add(s);
            }
        }
        return title;
    }

    public static List<String> getContent() {
        String data= StoreData.getPreference().getString("weight_content","");
        String strings[]=data.split(DIVI);
        content=new ArrayList<>();
        if (strings!=null&&strings.length>0){
            for (String s: strings
                    ) {
                content.add(s);
            }
        }
        return content;
    }


    /**
     * 添加
     * @author Gamble
     */
    public static int addLst(String titlestr, String contentstr) {
        if ((titlestr+contentstr).equals(""))//全为空
            return -1;
        getTitle().add(titlestr.equals("")?"未指定":titlestr);//先刷新内存再存储---getTitle为了使数据同步
        String titles="";
        for (String s: title
                ) {
            if (s.equals(""))
                titles+=s;
            else
                titles+=s+DIVI;
        }
        StoreData.PutKeyValues("weight_title",titles);
        getContent().add(contentstr.equals("")?"未指定":contentstr);//先刷新内存再存储---getTitle为了使数据同步
        String contents="";
        for (String s: content
                ) {
            if (s.equals(""))
                contents+=s;
            else
                contents+=s+DIVI;
        }
        StoreData.PutKeyValues("weight_content",contents);
        return 0;
    }


    /**
     * 删除
     * @author Gamble
     */
    public static void del(int position) {
        title=getTitle();
        content=getContent();
        title.remove(position);
        String titles="";
        for (String s: title
                ) {
            titles+=s+DIVI;
        }
        StoreData.PutKeyValues("weight_title",titles);
       content.remove(position);
        String contents="";
        for (String s: content
                ) {
            contents+=s+DIVI;
        }
        StoreData.PutKeyValues("weight_content",contents);

    }
}
