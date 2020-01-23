package app.gaojinlei.note.module.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.app.gaojinlei.note.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import app.gaojinlei.note.constants.Constans;
import app.gaojinlei.note.utils.ThemeUtils;
import app.gaojinlei.start.launch.LaunchActivity;
import app.gaojinlei.tools.MyImageView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    public T mPresenter;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    public Context mContext;

    protected int theme;
    public static SharedPreferences.Editor editor;//全局键值对存储器--不要关闭---供工具包引用
    public  static SharedPreferences preferences;//全局键值对查看器--不要关闭---供工具包引用
    private static boolean isfirst=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isfirst){//优先启动
            isfirst=false;
            //startActivity(new Intent(this, LaunchActivity.class));//启动动画暂时关闭
        }
        initmyview(this);
        preferences=getSharedPreferences("default",MODE_PRIVATE);
        editor=preferences.edit();//初始化键值对写入工具
        setTheme();
        mContext = this;
        mPresenter = initPresenter();
        initBeforeSetContentView();
        setContentView(attachLayoutRes());
        ButterKnife.bind(this);
        setStatusBarBeforeApi19();
        initToolbar();
        initViews();
        updateViews();
    }

    private void initmyview(BaseActivity<V, T> vtBaseActivity) {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (theme != Constans.theme)
            recreate();
    }

    private void setTheme() {
        theme = Constans.theme;
        setTheme(theme);

    }


    private void setStatusBarBeforeApi19() {
        if (Build.VERSION.SDK_INT < 21 && Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.app_bar);
            // appBarLayout 用于有可折叠标题栏的界面
            if (appBarLayout!=null) {
                appBarLayout.setPadding(0, ThemeUtils.getStatusBarHeight(), 0, 0);
            } else if (mToolbar != null) {
                mToolbar.setPadding(0, ThemeUtils.getStatusBarHeight(), 0, 0);
            }
        }
    }

    /**
     * 绑定布局文件
     *
     * @return 布局id
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * 初始化
     *
     * @describe
     */
    protected abstract T initPresenter();

    /**
     * 在setContentView之前调用，可不重写，需要时候再重写
     *
     * @describe
     */
    protected void initBeforeSetContentView() {
    }

    ;

    /**
     * 初始化视图控件
     *
     * @describe
     */
    protected abstract void initViews();

    /**
     * 更新视图控件
     *
     * @describe
     */
    protected abstract void updateViews();

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();

    }

    /**
     * 初始化Toolbar
     *
     * @describe
     */
    protected void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

//    /**
//     * 设置Toolbar title
//     *
//     * @describe
//     */
//    protected void setTitle(String title) {
//        if (mToolbar != null) {
//            mToolbar.setTitle(title);
//        }
//    }
}
