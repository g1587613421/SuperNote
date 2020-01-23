package app.gaojinlei.note.module.setting.developer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.app.gaojinlei.note.R;

import app.gaojinlei.note.module.base.BaseActivity;
import butterknife.BindView;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/07/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DeveloperActivity extends BaseActivity<IDeveloperView,DeveloperPresenter> implements IDeveloperView, View.OnLongClickListener {

    @BindView(R.id.ll_developer_qq)
    LinearLayout mLlToGitHub;

    @BindView(R.id.ll_developer_blog)
    LinearLayout mLlToBlog;

    @BindView(R.id.ll_developer_jianshu)
    LinearLayout mLlToJianShu;

    @BindView(R.id.ll_developer_email)
    LinearLayout mLlEmail;



    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_developer;
    }

    @Override
    protected DeveloperPresenter initPresenter() {
        DeveloperPresenter presenter=new DeveloperPresenter();
        presenter.attch(this);
        return presenter;
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("关于开发者");

        mLlToGitHub.setOnLongClickListener(this);
        mLlToBlog.setOnLongClickListener(this);
        mLlToJianShu.setOnLongClickListener(this);
        mLlEmail.setOnLongClickListener(this);
    }

    @Override
    protected void updateViews() {

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_developer_qq:
                mPresenter.openqq();
                String qq = "mqqwpa://im/chat?chat_type=wpa&uin=1587613421";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq)));
                break;
            case R.id.ll_developer_blog:
                mPresenter.openBlog();
                break;
            case R.id.ll_developer_jianshu:
                mPresenter.callphone();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "18538948760"));
                //dial动作为调用拨号盘
                startActivity(intent);
                break;
            case R.id.ll_developer_email:
                mPresenter.copyEmail();
                break;
            case R.id.ll_developer_to_alipay:
                mPresenter.toAlipay();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.ll_developer_qq:
                mPresenter.copyQQ();
                break;
            case R.id.ll_developer_blog:
                mPresenter.copyBlog();
                break;
            case R.id.ll_developer_jianshu:
                mPresenter.copynumber();
                break;
            case R.id.ll_developer_email:
                mPresenter.copyEmail();
                break;
        }
        return true;
    }

    @Override
    public Activity getActivity() {
        return this;
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
