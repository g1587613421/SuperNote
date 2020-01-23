package app.gaojinlei.note.module.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.app.gaojinlei.note.R;

import app.gaojinlei.manager.Data;
import app.gaojinlei.tools.StoreData;

/**
 * Created by 高金磊 on 2019/4/21.
 */

public class quick_startActivity extends Activity {
    private Switch quck_start,weather_quick;
    private TextView showdata;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quck_start_setting);
        initviews();
        initlisten();
    }

    private void initlisten() {
        quck_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            StoreData.PutKeyValues("quick_start",isChecked?"1":"0");
            if (isChecked)
            StoreData.PutKeyValues("weather_quick","0");//关闭天气显示
            initviews();
        }
    });
        weather_quick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                StoreData.PutKeyValues("quick_start","0");
                StoreData.PutKeyValues("weather_quick",isChecked?"1":"0");
                initviews();
            }
        });
    }

    private void initviews() {
        quck_start=findViewById(R.id.quick_start_switch);
        quck_start.setChecked(StoreData.getPreference().getString("quick_start","0").equals("1"));
        weather_quick=findViewById(R.id.weatherquick);
        weather_quick.setChecked(StoreData.getPreference().getString("weather_quick","0").equals("1"));
        showdata=findViewById(R.id.showdata);
        if (quck_start.isChecked()){
             showdata.setVisibility(View.VISIBLE);
        }
        else
            showdata.setVisibility(View.GONE);
    }
}
