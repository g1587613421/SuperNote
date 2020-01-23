package app.gaojinlei.note.module.notes.edit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.app.gaojinlei.note.R;
import com.orhanobut.logger.Logger;

import app.gaojinlei.manager.Data;
import app.gaojinlei.note.bean.ImageEntity;
import app.gaojinlei.note.module.base.BaseActivity;
import app.gaojinlei.note.module.notes.main.NoteMainPresenter;
import app.gaojinlei.note.utils.PermissionUtils;
import app.gaojinlei.note.utils.ProgressDialogUtils;
import app.gaojinlei.note.widget.MyEditText;
import butterknife.BindView;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class EditNoteActivity extends BaseActivity<IEditNoteView, EditNotePresenter> implements IEditNoteView, View.OnClickListener {


    @BindView(R.id.scroll_edit_note)
    ScrollView mScrollView;

    @BindView(R.id.et_edit_note_content)
    MyEditText mEdContent;//文本编辑容器

    @BindView(R.id.ll_edit_note_to_camera)
    LinearLayout mLlToCamera;

    @BindView(R.id.ll_edit_note_to_photo)
    LinearLayout mLlToPhoto;

    private ProgressDialogUtils mProgressDialogUtils = new ProgressDialogUtils(this);

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_edit_note;
    }

    @Override
    protected EditNotePresenter initPresenter() {
        EditNotePresenter presenter = new EditNotePresenter();
        presenter.attch(this);
        return presenter;
    }

    @Override
    protected void initViews() {//初始化编辑器
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEdContent.setMinHeight(mPresenter.getNoteEditNeedHeight());
        mPresenter.initData();
        mLlToCamera.setOnClickListener(this);
        mLlToPhoto.setOnClickListener(this);
        mEdContent.setOnClickListener(this);
        //为新便签添加前后缀---信息封装在NoteMainPresenter中
        Data.setNewnotecontex(Data.getUsemodel().equals("0")?"":(NoteMainPresenter.getPrefix()+(Data.getShowusermodel().equals("0")?"\n\n\n\n\n\n\n\n\n\n\n\n\n":("\n"+Data.getUserdefmodelcontent())+"\n")+NoteMainPresenter.getPostfix()));
        mEdContent.setText(Data.getNewnotecontex());
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            Logger.d("image:"+mEdContent.mImageList.get(0).getStart()+"  "+mEdContent.mImageList.get(0).getEnd());

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Logger.d("text change:" + s + "  " + start + "  " + count + "  " + before);
            for (int i = 0; i < mEdContent.mImageList.size(); i++) {
                ImageEntity imageEntity = mEdContent.mImageList.get(i);
                if (start == imageEntity.getEnd()) {
                    mEdContent.getEditableText().replace(imageEntity.getStart(), imageEntity.getEnd(), "");
                    mEdContent.mImageList.remove(i);
                    mEdContent.mDeleteImageList.add(imageEntity);
                    break;
                }
            }
            mEdContent.setTextCountChange(start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    protected void updateViews() {
        mPresenter.parseNoteContent();
        mEdContent.setSelection(mEdContent.getText().length());
        mEdContent.addTextChangedListener(mTextWatcher);
    }

    @Override
    public Intent getActivityIntent() {
        return getIntent();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setTitle(String title) {//便签的上边的条幅显示内容
        getSupportActionBar().setTitle("");       // 主标题为空
        getSupportActionBar().setSubtitle("建立日期"+
                title); // 副标题
    }

    @Override
    public void showNoteContent(String content) {
        mEdContent.setText(content);
    }

    @Override
    public void replaceImage(String imageName, Bitmap bitmap) {
        mEdContent.replaceDrawable(bitmap, imageName);
    }

    @Override
    public void insertImage(String imageName, Bitmap bitmap) {
        mEdContent.insertDrawable(bitmap, imageName);
    }

    @Override
    public void deleteImage(ImageEntity imageEntity) {
        mEdContent.getEditableText().replace(imageEntity.getStart(), imageEntity.getEnd()+1, "");
    }

    @Override
    public void setResultAndFinish(Intent intent) {
        setResult(RESULT_OK, intent);
    }

    @Override
    public void showLoading(String message) {
        mProgressDialogUtils.show(message);
    }

    @Override
    public void unShowLoading() {
        mProgressDialogUtils.hide();
    }

    @Override
    public void showToAppSettingDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("权限设置")
                .setMessage("您已禁止应用的储存权限，请前往应用设置中开启")
                .setPositiveButton("前往",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.toAppSetting(mContext);
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void showStatisticsDialog(int imageCount, int textCount) {
        new AlertDialog.Builder(mContext)
                .setMessage("文字数量：" + textCount + "\n" + "图片数量：" + imageCount)
                .setPositiveButton("确定", null)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_edit_note_to_camera:
                mPresenter.checkPermissionAndToCamera(mContext);
                break;
            case R.id.ll_edit_note_to_photo:
                mPresenter.checkPermissionAndToPhoto(mContext);
                break;
            case R.id.et_edit_note_content:
                mPresenter.clickNoteEditText(mEdContent);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_note_statistics:
                mPresenter.calculateContentAndImageCount(mEdContent);
                break;
            case R.id.menu_note_share:
                showShareDialg();
                break;
            case R.id.menu_note_checklog ://检查日志
                shownotelog();
        }
        return true;
    }

    private void shownotelog() {
        if (Data.getShownotelog().equals("1")){
        Intent intent=new Intent("notelog");
        intent.putExtra("notelog",EditNotePresenter.getNotelog());
        startActivity(intent);}
        else Toast.makeText(mContext, "请在设置中开启日志追踪功能", Toast.LENGTH_SHORT).show();
    }

    /**
     *   显示分享Dialog
     */
    private void showShareDialg() {

        if(mEdContent.getText().length()==0){
            return;
        }

        setEditTextBeforeGetBitmap();
        String items[];

        // 没有图片时 添加：以文字形式分享的方法
        if(mEdContent.mImageList.size()==0){
            items=new String[]{"以图片形式分享","以文字形式分享"};
        } else{
            items=new String[]{"以图片形式分享"};
        }

        final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("分享")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mPresenter.shareNoteWithImage(mEdContent);
                                break;
                            case 1:
                                shareText(mEdContent.getText().toString());
                                setEditTextAfterGetBitmap();
                                break;
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        setEditTextAfterGetBitmap();
                    }
                })
                .show();
    }

    public void shareText(String content){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("text/plain");
        intent = Intent.createChooser(intent, "分享");
        startActivity(intent);
    }

    @Override
    public void setEditTextBeforeGetBitmap(){
        mPresenter.closeKeyboard(mEdContent);
        mEdContent.setMinHeight(0);
        mEdContent.setEnabled(false);
    }

    @Override
    public void setEditTextAfterGetBitmap(){
        mEdContent.setMinHeight(mPresenter.getNoteEditNeedHeight());
        mEdContent.setEnabled(true);
    }



    @Override
    public void onBackPressed() {
        mPresenter.saveNote(mEdContent.getText().toString());
        super.onBackPressed();
    }

}
