package app.gaojinlei.note.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.gaojinlei.note.R;
import com.tencent.tauth.Tencent;

import app.gaojinlei.manager.Data;
import app.gaojinlei.note.module.notes.main.NoteMainActivity;
import app.gaojinlei.tools.MyImageView;

/**
 * Created by 高金磊 on 2019/4/20.
 */

public class UserDataShowActivity extends Activity {
    private Tencent mTencent;
   private TextView name,sex,age,city,location;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data);
    }

    @Override
    protected void onResume() {
        init();//更新界面
        super.onResume();
    }

    private void init() {
        ((MyImageView)findViewById(R.id.myImageView)).setImageURL(Data.getUser_image_data());
        name=findViewById(R.id.name);
        sex=findViewById(R.id.sex);
        age=findViewById(R.id.age);
        city=findViewById(R.id.city);
        location=findViewById(R.id.userlocation);
        name.setText(Data.getUser_name());
        age.setText(Data.getUser_year());
        sex.setText(Data.getUser_sex());
        city.setText(Data.getUser_city());
        location.setText(Data.getUserlocation());

    }

    public void logOut(View view) {//具体的网络端退出登陆操作移到LoginActivity中
        Data.clearUserData();//清空用户信息
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this,LoginActivity.class);//从A到B--单项
        intent.putExtra("out","QQlogout");
        startActivity(intent);
    }

    public void logined(View view) {
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this,NoteMainActivity.class);//从A到B--单项
        intent.putExtra("out","QQlogout");
        startActivity(intent);
    }
}
