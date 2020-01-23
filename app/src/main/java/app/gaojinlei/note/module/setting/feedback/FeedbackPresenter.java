package app.gaojinlei.note.module.setting.feedback;

import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ToastUtils;
import app.gaojinlei.note.bmob.Feedback;
import app.gaojinlei.note.module.base.BasePresenter;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class FeedbackPresenter extends BasePresenter<IFeedbackView> implements IFeedbackPresenter{

    private String mLastContent="",mLastContact="";

    @Override
    public void sendFeedback(final String content, final String contact) {

        if(TextUtils.isEmpty(content)){
            ToastUtils.showShort("请至少填写反馈内容");
            return;
        }
        if(content.equals(mLastContent) && contact.equals(mLastContact)){
            ToastUtils.showShort("请不要重复提交");
            return;
        }

        Feedback feedback=getFeedback(content,contact);
        mView.showLoading();
        feedback.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
               sendBack(e,content,contact);
            }
        });
    }

    private Feedback getFeedback(String content,String contact){
        Feedback feedback=new Feedback();
        feedback.setContent(content);
        feedback.setContact(contact);
        feedback.setSdk(DeviceUtils.getSDKVersion());
        feedback.setVersion(AppUtils.getAppVersionName());
        return feedback;
    }

    private void sendBack(BmobException e,String content,String contact){
        mView.cancleLoading();
        if(e==null){
            mLastContent=content;
            mLastContact=contact;
            ToastUtils.showShort("提交成功");
        } else{
            ToastUtils.showShort("提交失败");
        }
    }
}
