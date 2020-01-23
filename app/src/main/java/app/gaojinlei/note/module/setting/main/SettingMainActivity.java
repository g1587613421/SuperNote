package app.gaojinlei.note.module.setting.main;

import android.view.MenuItem;

import com.app.gaojinlei.note.R;

import app.gaojinlei.note.module.base.BaseActivity;
import app.gaojinlei.note.module.setting.setting.SettingFragment;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SettingMainActivity extends BaseActivity<ISettingMainView,SettingMainPresenter> implements ISettingMainView {
    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected SettingMainPresenter initPresenter() {
        SettingMainPresenter presenter=new SettingMainPresenter();
        presenter.attch(this);
        return presenter;
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle("设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.frame_setting_content,new SettingFragment()).commit();
    }

    @Override
    protected void updateViews() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
