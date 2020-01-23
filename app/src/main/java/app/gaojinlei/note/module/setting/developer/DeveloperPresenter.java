package app.gaojinlei.note.module.setting.developer;

import android.content.Intent;
import android.net.Uri;

import com.app.gaojinlei.note.R;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ToastUtils;

import app.gaojinlei.note.module.base.BasePresenter;
import app.gaojinlei.note.utils.AliPayUtils;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/07/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DeveloperPresenter extends BasePresenter<IDeveloperView> implements IDeveloperPresenter{
    @Override
    public void openqq() {
        String uri=mView.getActivity().getResources().getString(R.string.qq);
        copyQQ();

    }

    private void openUri(String uri){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(uri);
        intent.setData(content_url);
        mView.getActivity().startActivity(intent);
    }

    @Override
    public void openBlog() {
        String uri=mView.getActivity().getResources().getString(R.string.csdn);
        openUri(uri);
    }

    @Override
    public void copynumber() {
        String uri=mView.getActivity().getResources().getString(R.string.phone);

    }

    @Override
    public void toAlipay() {
        String alipayQrCode=mView.getActivity().getResources().getString(R.string.alipay_qr_code);
        AliPayUtils.openAliPay2Pay(mView.getActivity(),alipayQrCode);
    }

    @Override
    public void copyQQ() {
        String uri=mView.getActivity().getResources().getString(R.string.qq);
        ClipboardUtils.copyText(uri);
        ToastUtils.showShort("已复制");
    }

    @Override
    public void copyBlog() {
        String uri=mView.getActivity().getResources().getString(R.string.csdn);
        ClipboardUtils.copyText(uri);
        ToastUtils.showShort("已复制");
    }

    @Override
    public void callphone() {
        String uri=mView.getActivity().getResources().getString(R.string.phone);
        ClipboardUtils.copyText(uri);
        ToastUtils.showShort("已复制");
    }

    @Override
    public void copyEmail() {
        String uri=mView.getActivity().getResources().getString(R.string.my_email);
        ClipboardUtils.copyText(uri);
        ToastUtils.showShort("已复制");
    }
}
