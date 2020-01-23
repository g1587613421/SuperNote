package app.gaojinlei.manager;

import com.tencent.connect.UserInfo;

import javax.sql.DataSource;

import app.gaojinlei.tools.StoreData;

/**
 * Created by 高金磊 on 2019/4/14.
 */

public class Data {//全局信息存储
    public static final String APPID = "101557795";
    private static String userlocation;
    private static String weathercity;
    private static String weather;
    private static String newnotecontex;//临时存储新便签的前后缀--方便空便签的处理
    private static String author;
    //用户信息相关
        private static String User_sex;
        private static String User_name;
        private static String User_image_data;
        private static String User_year;
        private  static String User_city;
//设置相关
    private static String usemodel,timeshow,showauthor,showlocation,showweather,showusermodel;//设置switch状态
    private static String defauthor,deflocation,userdefmodelcontent;
    private static String systemlog,shownotelog,note_log_detail;//日志

    public static String getNote_log_detail() {
        return note_log_detail==null?"1":note_log_detail;
    }

    public static void setNote_log_detail(String note_log_detail) {
        Data.note_log_detail = note_log_detail;
    }

    public static String getShownotelog() {
        return shownotelog=StoreData.getPreference().getString("shownotelog","1");
    }

    public static void setShownotelog(String shownotelog) {
        Data.shownotelog = shownotelog;
        StoreData.PutKeyValues("shownotelog",shownotelog);
    }

    public static String getSystemlog() {
        return systemlog=StoreData.getPreference().getString("systemlog","1");
    }

    public static void setSystemlog(String systemlog) {
        Data.systemlog = systemlog;
        StoreData.PutKeyValues("systemlog",systemlog);
    }

    public static String getUserdefmodelcontent() {
        return userdefmodelcontent=StoreData.getPreference().getString("userdefmodelcontent","");
    }

    public static void setUserdefmodelcontent(String userdefmodelcontent) {
        Data.userdefmodelcontent = userdefmodelcontent;
        StoreData.PutKeyValues("userdefmodelcontent",userdefmodelcontent);
    }

    public static String getShowusermodel() {
        return showusermodel=StoreData.getPreference().getString("showusermodel","0");
    }

    public static void setShowusermodel(String showusermodel) {
        Data.showusermodel = showusermodel;
        StoreData.PutKeyValues("showusermodel",showusermodel);
    }

    public static String getAuthor() {
        return author=getDefauthor().equals("")?getUser_name():getDefauthor();
    }

    public static String getDefauthor() {
        return Data.defauthor=StoreData.getPreference().getString("defauthor","");
    }

    public static void setDefauthor(String defauthor) {
        Data.defauthor = defauthor;
        StoreData.PutKeyValues("defauthor",defauthor);
    }

    public static String getDeflocation() {
        return deflocation=StoreData.getPreference().getString("deflocation","");
    }

    public static void setDeflocation(String deflocation) {
        Data.deflocation = deflocation;
        StoreData.PutKeyValues("deflocation",deflocation);
    }

    public static String getUsemodel() {
        return Data.usemodel=StoreData.getPreference().getString("usemodel","1");
    }

    public static void setUsemodel(String usemodel) {
        Data.usemodel = usemodel;
        StoreData.PutKeyValues("usemodel",usemodel);
    }

    public static String getTimeshow() {
        return Data.timeshow=StoreData.getPreference().getString("timeshow","1");
    }

    public static void setTimeshow(String timeshow) {
        Data.timeshow = timeshow;
        StoreData.PutKeyValues("timeshow",timeshow);
    }
    public static String getShowauthor() {
        return Data.showauthor=StoreData.getPreference().getString("showauthor","1");
    }

    public static void setShowauthor(String showauthor) {
        Data.showauthor = showauthor;
        StoreData.PutKeyValues("showauthor",showauthor);
    }
  public static String getShowlocation() {
        return Data.showlocation=StoreData.getPreference().getString("showlocation","1");
    }

    public static void setShowlocation(String showlocation) {
        Data.showlocation = showlocation;
        StoreData.PutKeyValues("showlocation",showlocation);
    }
 public static String getShowweather() {
        return Data.showweather=StoreData.getPreference().getString("showweather","1");
    }

    public static void setShowweather(String showweather) {
        Data.showweather = showweather;
        StoreData.PutKeyValues("showweather",showweather);
    }


    public static void clearUserData(){
    setUser_city("");
    setUser_year("");
    setUser_sex("");
    setUser_name("");
    setUser_image_data("");
}
    public static String getUser_image_data() {
        User_image_data=StoreData.getPreference().getString("User_image_data","");
        return User_image_data;
    }

    public static void setUser_image_data(String s) {
        User_image_data = s;
        StoreData.PutKeyValues("User_image_data",User_image_data);
    }
public static String getUser_sex() {
    User_sex=StoreData.getPreference().getString("User_sex","");
        return User_sex;
    }

    public static void setUser_sex(String s) {
        User_sex = s;
        StoreData.PutKeyValues("User_sex",User_sex);
    }
    public static String getUser_name() {
    User_name=StoreData.getPreference().getString("User_name","");
        return User_name;
    }

    public static void setUser_name(String s) {
        User_name = s;
        StoreData.PutKeyValues("User_name",User_name);
    }
    public static String getUser_year() {
    User_year=StoreData.getPreference().getString("User_year","");
        return User_year;
    }

    public static void setUser_year(String s) {
        User_year = s;
        StoreData.PutKeyValues("User_year",User_year);
    }

    public static String getUser_city() {
    User_city=StoreData.getPreference().getString("User_city","");
        return User_city;
    }

    public static void setUser_city(String s) {
        User_city = s;
        StoreData.PutKeyValues("User_city",User_city);
    }


    public static String getNewnotecontex() {
        return newnotecontex==null?"":newnotecontex;
    }

    public static void setNewnotecontex(String newnotecontex) {
        Data.newnotecontex = newnotecontex;
    }

    public static String getWeathercity() {
        String deflocation=getDeflocation().equals("")?"未知":getDeflocation();
        return (weathercity==null||weathercity.equals(""))?deflocation:weathercity;
    }

    public static void setWeathercity(String weathercity) {
        Data.weathercity = weathercity;
    }

    public static String getWeather() {
        return weather==null||weather.equals("")?"未知":weather;
    }

    public static void setWeather(String weather) {
        Data.weather = weather;
    }

    public static String getUserlocation() {
        String deflocation=getDeflocation().equals("")?"未知":getDeflocation();
        return userlocation==null||userlocation.equals("")?deflocation:userlocation;
    }

    public static void setUserlocation(String userlocation) {
        Data.userlocation = userlocation;
    }
}
