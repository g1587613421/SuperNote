package app.gaojinlei.note.module.setting.note;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.app.gaojinlei.note.R;

import java.util.List;

import app.gaojinlei.manager.Data;

/**
 * Created by 高金磊 on 2019/4/20.
 */

public class NoteModelSetting extends Activity {
private Switch usemodel,timeshow,showauthor,showlocation,showweather,showusermodel;
private EditText defauthor,deflocation,usermodelcontent;
private LinearLayout modelLayout,nameLayout,deflocationlayout,userdefmodelcontent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_model);
        initviews();

    }
    public void initviews(){
        usemodel=findViewById(R.id.usemodel);//是否适用模板
        timeshow=findViewById(R.id.timeshow);//是否显示时间
        showauthor=findViewById(R.id.showauthor);//是否显示作者签名
        defauthor=findViewById(R.id.userauthor);//自定义作者名
        showlocation=findViewById(R.id.showlocation);//是否显示位置
        deflocation=findViewById(R.id.deflocation);//默认位置
        showweather=findViewById(R.id.showweather);//是否显示天气
        showusermodel=findViewById(R.id.showusermodel);//是否显示用户模板

        usermodelcontent=findViewById(R.id.usermodelcontent);

        modelLayout=findViewById(R.id.modelLayout);//模板设置列表
        nameLayout=findViewById(R.id.nameLayout);//修改创建者姓名
        deflocationlayout=findViewById(R.id.deflocationlayout);//修改默认位置板块
        userdefmodelcontent=findViewById(R.id.usermodelcontentlayout);//自定义内容板块
        initviewssate();
        initviewslistener();
    }

    private void initviewssate() {//初始化显示状态
        usemodel.setChecked(Data.getUsemodel().equals("1"));
        timeshow.setChecked(Data.getTimeshow().equals("1"));
        showauthor.setChecked(Data.getShowauthor().equals("1"));
        showlocation.setChecked(Data.getShowlocation().equals("1"));
        showweather.setChecked(Data.getShowweather().equals("1"));
        showusermodel.setChecked(Data.getShowusermodel().equals("1"));
        if (usemodel.isChecked()){
            modelLayout.setVisibility(View.VISIBLE);
        }
        else {
            modelLayout.setVisibility(View.GONE);
        }
        if (showauthor.isChecked()){
            nameLayout.setVisibility(View.VISIBLE);
        }
        else {
            nameLayout.setVisibility(View.GONE);
        }
        if (showlocation.isChecked()){
            deflocationlayout.setVisibility(View.VISIBLE);
        }
        else {
            deflocationlayout.setVisibility(View.GONE);
        }
        if (showusermodel.isChecked()){
            userdefmodelcontent.setVisibility(View.VISIBLE);
        }
        else {
            userdefmodelcontent.setVisibility(View.GONE);
        }
        defauthor.setText(Data.getDefauthor());
        deflocation.setText(Data.getDeflocation());
        usermodelcontent.setText(Data.getUserdefmodelcontent());

    }

    private void initviewslistener() {
        Listener listener=new Listener();
        usemodel.setOnCheckedChangeListener(listener);
        timeshow.setOnCheckedChangeListener(listener);
        showauthor.setOnCheckedChangeListener(listener);
        showlocation.setOnCheckedChangeListener(listener);
        showweather.setOnCheckedChangeListener(listener);
        showusermodel.setOnCheckedChangeListener(listener);
    }
    class Listener implements Switch.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()){
                case R.id.usemodel:
                    Data.setUsemodel(isChecked?"1":"0");
                    break;
                     case R.id.timeshow:
                    Data.setTimeshow(isChecked?"1":"0");
                    break;
                     case R.id.showauthor:
                    Data.setShowauthor(isChecked?"1":"0");
                    break;
                     case R.id.showlocation:
                    Data.setShowlocation(isChecked?"1":"0");
                    break;
                     case R.id.showweather:
                    Data.setShowweather(isChecked?"1":"0");
                    break;
                case R.id.showusermodel:
                    Data.setShowusermodel(isChecked?"1":"0");
            }
            Data.setDefauthor(defauthor.getText().toString());
            Data.setDeflocation(deflocation.getText().toString());
            Data.setUserdefmodelcontent(usermodelcontent.getText().toString());
            
            initviewssate();//刷新组件状态

        }
    }

    @Override
    public void onBackPressed() {//监听返回键
        super.onBackPressed();
        Data.setDefauthor(defauthor.getText().toString());
        Data.setDeflocation(deflocation.getText().toString());
        Data.setUserdefmodelcontent(usermodelcontent.getText().toString());
        
    }
}
