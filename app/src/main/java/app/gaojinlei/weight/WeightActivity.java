package app.gaojinlei.weight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.gaojinlei.note.R;

import app.gaojinlei.note.module.notes.main.NoteMainActivity;

public class WeightActivity extends AppCompatActivity {
Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_add);
        Button btn = (Button)findViewById(R.id.btn_add);
        final EditText et = (EditText)findViewById(R.id.et);
        final EditText content=(EditText)findViewById(R.id.content);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int stcode = WeightData.addLst(et.getText().toString(),content.getText().toString()); //数据添加
                if (stcode==0) {
                    et.setText("");
                    content.setText("");
                    Toast.makeText(WeightActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    //添加待办事项 - Intent
                    Intent intent = new Intent("add.com");
                    sendBroadcast(intent); //发送 add.com 的广播
                }
                else Toast.makeText(WeightActivity.this, "不能同时为空", Toast.LENGTH_SHORT).show();
            }
        });
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0x0000){
                    et.setText("");
                    content.setText("");
                }
            }
        };
    }

    public void reset(View view) {
        handler.sendEmptyMessage(0x0000);

    }

    @Override
    public void onBackPressed() {//回退到activity
        Intent intent=new Intent(getApplicationContext(),NoteMainActivity.class);
        intent.putExtra("action","main");
        startActivity(intent);

    }
}
