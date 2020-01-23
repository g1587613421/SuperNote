package app.gaojinlei.note.module.setting.wedget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.app.gaojinlei.note.R;

import app.gaojinlei.tools.StoreData;
import app.gaojinlei.weight.MyWidgetService;
import app.gaojinlei.weight.WeightActivity;

/**
 * Created by 高金磊 on 2019/4/20.
 */

public class WedgetSetting extends Activity {
private Switch open_wedget_tool;
private LinearLayout open_wedget_tool_layout;
private TextView open_wedget_tool_message,errTv1;
private Button repair_wedget;
private Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wedgetsetting);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
        initViewsListener();
        initHandler();

    }

    private void initHandler() {
        handler=new Handler(){
            @Override
            public void handleMessage(final Message msg) {
                if (msg.what==0x0001){
                    open_wedget_tool_layout.setVisibility(View.VISIBLE);
                    open_wedget_tool_message.setText("正在尝试启动桌面小部件后台服务");
                    new Thread(){
                        @Override
                        public void run() {
                            for (int n=0;n<5;n++){
                                try {
                                    sleep(1000);
                                    if (n>=1&n<=2)
                                        handler.sendEmptyMessage(0x00011);//  open_wedget_tool_message.setText("监听服务状态");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (n>=3){
                                  handler.sendEmptyMessage(0x00012);//  open_wedget_tool.setTextColor(Color.RED);
                                                                            //open_wedget_tool_message.setText("启动失败正在收集问题");
                                }
                            }
                            handler.sendEmptyMessage(0x00013);// errTv1.setVisibility(View.VISIBLE);
                                                                    //open_wedget_tool_layout.setVisibility(View.GONE);

                        }
                    }.start();

                }
                if (msg.what==0x0002){
                    open_wedget_tool_layout.setVisibility(View.VISIBLE);
                    open_wedget_tool_message.setText("正在尝试启动桌面小部件后台服务");
                    new Thread(){
                        @Override
                        public void run() {
                            for (int n=0;n<4;n++){
                                try {
                                    sleep(1000);
                                    if (n>=1&n<2)
                                        handler.sendEmptyMessage(0x00011);//  open_wedget_tool_message.setText("监听服务状态");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (n>=2){
                                    handler.sendEmptyMessage(0x00021);
                                }
                            }
                            handler.sendEmptyMessage(0x00013);
                        }
                    }.start();
                }
                if (msg.what==0x00021){
                    open_wedget_tool_message.setText("启动成功");
                }
                if (msg.what==0x00011){
                    open_wedget_tool_message.setText("监听服务状态");
                }
                if (msg.what==0x00012){
                    open_wedget_tool_message.setTextColor(Color.RED);
                    open_wedget_tool_message.setText("启动失败正在收集问题");
                }
                if (msg.what==0x00013){
                    errTv1.setVisibility(View.VISIBLE);
                    open_wedget_tool_layout.setVisibility(View.GONE);
                }
            }
        };
    }

    private void initViews() {
        open_wedget_tool=findViewById(R.id.open_wedget_tool);
        open_wedget_tool.setChecked(StoreData.getPreference().getInt("open_wedget_tool",0)==1);
        open_wedget_tool_layout=findViewById(R.id.open_wedget_tool_layout);
        open_wedget_tool_message=findViewById(R.id.open_wedget_tool_message);
        repair_wedget=findViewById(R.id.repair_wedget);
        errTv1=findViewById(R.id.errTv1);
    }

    private void initViewsListener() {
        open_wedget_tool.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                errTv1.setText("");
                errTv1.setVisibility(View.GONE);
                if (isChecked){//请求开启
                    if (checkstate()){
                        StoreData.PutKeyValues("open_wedget_tool",1);
                        handler.sendEmptyMessage(0x0002);
                    }else {
                        handler.sendEmptyMessage(0x0001);
                        StoreData.PutKeyValues("open_wedget_tool",0);
                    }
                }
                else {//请求关闭
                    errTv1.setText("");

                    StoreData.PutKeyValues("open_wedget_tool",0);
                    open_wedget_tool_layout.setVisibility(View.GONE);
                }
                initViews();
            }

        });


        repair_wedget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errTv1.setText("已经运行修复,请到桌面删除重新添加");
                errTv1.setVisibility(View.VISIBLE);
            }
        });
    }
    private boolean checkstate() {//检测是否开启成功..--并显示错误信息
        startService(new Intent(getApplicationContext(), MyWidgetService.class));//尝试启动服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            errTv1.setText("抱歉暂时不支持安卓8.0及以上版本");
            return false;
        }
        return true;
    }
}
