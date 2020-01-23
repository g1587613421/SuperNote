package app.gaojinlei.note.module.setting;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import com.app.gaojinlei.note.R;

import app.gaojinlei.manager.Data;
import app.gaojinlei.tools.StoreData;

/**
 * Created by 高金磊 on 2019/4/21.
 */

public class logsetting extends Activity {
    private  Switch systemlog,shownotelog,note_log_detail;
    private LinearLayout layout1,layout2;
    private SeekBar sysLogSize;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_setting_manage);
        initviews();
        initListener();
    }
   private void initviews(){
        systemlog=findViewById(R.id.systemlog);
       shownotelog=findViewById(R.id.shownotelog);
       layout1=findViewById(R.id.log_layout1);
       layout2=findViewById(R.id.log_layout2);
       sysLogSize=findViewById(R.id.sysLogSize);
       note_log_detail=findViewById(R.id.note_log_detail);

       note_log_detail.setChecked(Data.getNote_log_detail().equals("1"));
       sysLogSize.setProgress(StoreData.getPreference().getInt("sysLogSize",3));
       systemlog.setChecked(Data.getSystemlog().equals("1"));
       if (systemlog.isChecked()){
           layout1.setVisibility(View.VISIBLE);
       }
       else
           layout1.setVisibility(View.GONE);
       shownotelog.setChecked(Data.getShownotelog().equals("1"));
       if (shownotelog.isChecked())
           layout2.setVisibility(View.VISIBLE);
       else
           layout2.setVisibility(View.GONE);

   }
    private void initListener() {
       Listener listener=new Listener();
       systemlog.setOnCheckedChangeListener(listener);
        shownotelog.setOnCheckedChangeListener(listener);

        note_log_detail.setOnCheckedChangeListener(listener);

        sysLogSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                StoreData.PutKeyValues("sysLogSize",progress);

                initviews();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    class Listener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.systemlog:
                    Data.setSystemlog(isChecked?"1":"0");
                case R.id.shownotelog:
                    Data.setShownotelog(isChecked?"1":"0");
                case R.id.note_log_detail:
                    Data.setNote_log_detail(isChecked?"1":"0");
            }
            initviews();
        }
    }
}
