package app.gaojinlei.note.serve;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.app.gaojinlei.note.R;

import app.gaojinlei.manager.Data;
import app.gaojinlei.tools.log;

/**
 * Created by 高金磊 on 2019/4/14.
 */

public class gaodeserve extends Service {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        {
            log.addlog("启动高德服务");
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
//初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //声明定位回调监听器



//设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(1000);//连续定位
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(8000);//定位请求超时时间
            //缓存机制
            mLocationOption.setLocationCacheEnable(true);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
//启动定位
            mLocationClient.startLocation();


            final AMapLocationListener mLocationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation amapLocation) {

                    if (amapLocation != null) {
                        if (amapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                            log.addlog("定位成功");
                            String middlelocation="";
                            //Data.userlocation+=amapLocation.getAddress();
                            //Data.setUserlocation(String.valueOf(amapLocation.getLatitude()));
                           middlelocation+=amapLocation.getProvince();//省信息
                           middlelocation+=amapLocation.getCity();//城市信息
                            Data.setWeathercity(amapLocation.getCity());//更新天气要查询的城市
                           middlelocation+=amapLocation.getDistrict();//城区信息
                           middlelocation+=amapLocation.getStreet();//街道信息
                           middlelocation+=amapLocation.getStreetNum();//街道门牌号信息
                            Data.setUserlocation(middlelocation);//避免内存泄漏
                            //间断式定位
                            mLocationClient.stopLocation();
                            mLocationOption.setInterval(100000);
                            mLocationClient.setLocationOption(mLocationOption);
                            new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        sleep(100000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    mLocationClient.startLocation();
                                    log.addlog("矫正位置");
                                }
                            }.start();
//启动定位
                        }else {
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError","location Error, ErrCode:"
                                    + amapLocation.getErrorCode() + ", errInfo:"
                                    + amapLocation.getErrorInfo());
                        }
                    }
                }
            };
//设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
    private NotificationManager notificationManager = null;
    boolean isCreateChannel = false;
    @SuppressLint("NewApi")
    private Notification buildNotification() {

        Notification.Builder builder = null;
        Notification notification = null;
        if(android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = getPackageName();
            if(!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        builder.setSmallIcon(R.drawable.qq)
               .setContentTitle("便签")
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            return builder.getNotification();
        }
        return notification;
    }
}
