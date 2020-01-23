package app.gaojinlei.note.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/07/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ProgressDialogUtils {

    private ProgressDialog mProgressDialog;
    private Context mContext;

    public ProgressDialogUtils(Context context){
        this.mContext=context;
    }

    public void show(String message){
        if(mProgressDialog==null)
            mProgressDialog=new ProgressDialog(mContext);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void hide(){
        if(mProgressDialog!=null)
            mProgressDialog.cancel();
    }
}
