package app.gaojinlei.weight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.app.gaojinlei.note.R;

import app.gaojinlei.note.module.notes.main.NoteMainActivity;
import app.gaojinlei.weight.MyWidgetService;

import static android.R.attr.action;
import static android.R.attr.cacheColorHint;
import static android.content.ContentValues.TAG;

/**
 * ====================== AppWidget ========================
 * @author SGamble
 */
public class MyAppWidget extends AppWidgetProvider {

    private static final String TAG = "MyWidget";
    RemoteViews remoteViews;

    /**
     * package
     */
    static ComponentName getComponentName(Context context) {
        return new ComponentName(context, MyAppWidget.class);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e(TAG, "onUpdate");

        //设置 ListView
        setListView(context, appWidgetManager, appWidgetIds);
        //获取 RemoteViews
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
        //绑定id - 按钮点击事件
        remoteViews.setOnClickPendingIntent(R.id.btn_jump, getJumpPendingIntent(context)); //跳转到主界面
        //发送 del.com 的广播
        sendDelIntentBroadcast(context);
        //更新widget
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    /**
     * 设置 ListView
     * @author Gamble
     */
    private void setListView(Context context, AppWidgetManager awm, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MyWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
            views.setRemoteAdapter(R.id.listView1, intent);
            awm.updateAppWidget(appWidgetId, views); //设置适配器
            awm.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listView1); //通知数据更新
        }
    }

    /**
     * 发送 del.com 的广播
     * @author Gamble
     */
    private void sendDelIntentBroadcast(Context context) {
        Intent intent = new Intent("del.com");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 220, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.listView1, pendingIntent);
    }

    /**
     * 点击按钮跳转到指定 Activity
     * @author Gamble
     */
    private PendingIntent getJumpPendingIntent(Context context) {
        Intent skipIntent = new Intent(context, NoteMainActivity.class);
        skipIntent.putExtra("action","wedgetsetting");//前往便签添加界面并通过主程序启动相关服务
        PendingIntent pi = PendingIntent.getActivity(context, 200, skipIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pi;
    }

    /**
     * 接收到任意广播时触发
     *  -- 广播需要在 清单 文件中设置响应
     *      <intent-filter>
     *          <action android:name="add.com"/>
     *      </intent-filter>
     * @author Gamble
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        switch (intent.getAction()){
            case "add.com":
                Log.e(TAG, "接收到MainActivity传递过来的广播 - 添加操作");
                break;
            case "del.com": //删除
                delEvent(context,intent);
                break;
        }
        updateListView(context,intent); //更新操作
    }

    /**
     * 删除点击的事项
     * @author Gamble
     */
    private void delEvent(Context context,Intent intent) {
        Bundle extras = intent.getExtras();
        int position = extras.getInt("key");
        WeightData.del(position);
    }

    /**
     * 更新操作
     *  - ListView中数据发生改变
     * @author Gamble
     */
    private void updateListView(Context context,Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        final ComponentName cn = new ComponentName(context, MyAppWidget.class);
        mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.listView1);
        mgr.updateAppWidget(cn, remoteViews);
    }
}

