package app.gaojinlei.note.weather;

import android.app.Activity;

/**
 * Created by 高金磊 on 2019/4/24.
 */

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.gaojinlei.note.R;

public class weatherActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
        mWebView = (WebView) findViewById(R.id.weatherweb);
        // 设置WebView的客户端
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//超链接回调
                Toast.makeText(weatherActivity.this, "该模块尚未完善,暂时不支持查看", Toast.LENGTH_SHORT).show();
                return true;// 返回false
            }
        });
        //baiduboxapp:
        //webView加载网页后出现ERR_UNKNOWN_URL_SCHEME
        /*mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try{
                    if(url.startsWith("baiduboxapp://")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                }catch (Exception e){
                    return false;
                }
                mWebView.loadUrl(url);
                return true;
            }
        });*/
        WebSettings webSettings = mWebView.getSettings();
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置缓存路径
//        webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
        webSettings.setDefaultFontSize(12);

        // 设置WebView属性，能够执行Javascript脚本
        // mWebView.getSettings().setJavaScriptEnabled(true);
        //3、 加载需要显示的网页
        mWebView.loadUrl("https://apip.weatherdt.com/h5.html?id=MSYNwtlYPc");
        ///4、设置响应超链接，在安卓5.0系统，不使用下面语句超链接也是正常的，但在MIUI中安卓4.4.4中需要使用下面这条语句，才能响应超链接
        // mWebView.setWebViewClient(new HelloWebViewClient());
    }
    // 设置回退监听
    // 5、覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
            if (mWebView.canGoBack())
            {
                mWebView.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            }else
            {
                finish();
                return true;
            }

        }
        return false;
    }
}