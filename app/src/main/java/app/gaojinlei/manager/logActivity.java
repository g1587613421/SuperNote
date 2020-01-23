package app.gaojinlei.manager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.app.gaojinlei.note.R;

/**
 * Created by 高金磊 on 2019/4/18.
 */

public class logActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showlog);
        ((TextView)findViewById(R.id.logtxt)).setText(this.getIntent().getStringExtra("notelog"));
    }
}
