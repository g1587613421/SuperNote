package app.gaojinlei.note;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.app.gaojinlei.note.R;
import com.blankj.utilcode.util.Utils;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import app.gaojinlei.note.constants.CacheManager;
import app.gaojinlei.note.constants.Constans;
import app.gaojinlei.note.constants.FolderListConstans;
import app.gaojinlei.note.constants.NoteListConstans;
import app.gaojinlei.note.utils.PreferencesUtil;
import cn.bmob.v3.Bmob;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2019/04/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MainApplication extends LitePalApplication {

    public static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);//必須先調用生成app的全局contex
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        init();
        initBmob();
        getCacheData();
        setUpdateForVersionCode1();
    }

    private void init(){
        Utils.init(getApplicationContext());
        Connector.getDatabase();
}

    private void initBmob(){
        Bmob.initialize(this,getResources().getString(R.string.bmob_app_id));
    }

    private void getCacheData(){
        Constans.isFirst= PreferencesUtil.getBoolean(Constans.IS_FIRST,true);
        Constans.currentFolder= PreferencesUtil.getInt(Constans.CURRENT_FOLDER, FolderListConstans.ITEM_ALL);
        Constans.noteListShowMode=PreferencesUtil.getInt(Constans.NOTE_LIST_SHOW_MODE, NoteListConstans.STYLE_GRID);
        Constans.theme=PreferencesUtil.getInt(Constans.THEME,Constans.theme);
        Constans.isUseRecycleBin=PreferencesUtil.getBoolean(Constans.IS_USE_RECYCLE,Constans.isUseRecycleBin);
        Constans.isLocked=PreferencesUtil.getBoolean(Constans.IS_LOCKED,Constans.isLocked);
        Constans.lockPassword=PreferencesUtil.getString(Constans.LOCK_PASSWORD,"");
    }

    // 为了兼容1.0.1版本，将其的缓存信息进行备份修改
    private void setUpdateForVersionCode1(){
        // 1.0.1版本使用的key值，如果是false，说明之前是V1.0.1版本
        boolean isFirst= PreferencesUtil.getBoolean("isFirst",true);
        if(!isFirst){
            boolean isGrid= PreferencesUtil.getBoolean("is_grid",false);
            boolean isUseRecycleBin=PreferencesUtil.getBoolean("recycle_bin",false);

            CacheManager.setAndSaveIsFirst(false);
            CacheManager.setAndSaveCurrentFolder(FolderListConstans.ITEM_ALL);
            CacheManager.setAndSaveIsUseRecycleBin(isUseRecycleBin);
            if(isGrid){
                CacheManager.setAndSaveNoteListShowMode(NoteListConstans.MODE_GRID);
            } else {
                CacheManager.setAndSaveNoteListShowMode(NoteListConstans.MODE_LIST);
            }
            // isLock、lockPassword key值一样；主题key值不用修改。

        }
    }
}
