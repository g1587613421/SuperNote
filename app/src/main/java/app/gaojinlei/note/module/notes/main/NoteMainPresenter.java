package app.gaojinlei.note.module.notes.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.gaojinlei.note.MainApplication;
import app.gaojinlei.note.adapter.RvNoteListAdapter;
import app.gaojinlei.manager.Data;
import app.gaojinlei.note.bean.Note;
import app.gaojinlei.note.bean.NoteFolder;
import app.gaojinlei.note.constants.CacheManager;
import app.gaojinlei.note.constants.Constans;
import app.gaojinlei.note.constants.FolderListConstans;
import app.gaojinlei.note.constants.NoteListConstans;
import app.gaojinlei.note.model.INoteModel;
import app.gaojinlei.note.model.LoadDataCallBack;
import app.gaojinlei.note.model.NoteModel;
import app.gaojinlei.note.module.base.BasePresenter;
import app.gaojinlei.note.module.notes.folderList.IFolderListPresenter;
import app.gaojinlei.tools.OrtherTool;
import app.gaojinlei.tools.StoreData;

import static android.app.Activity.RESULT_OK;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class NoteMainPresenter extends BasePresenter<INoteMainView> implements INoteMainPresenter {

    // Note Model c
    private INoteModel mINoteModel = new NoteModel();

    private IFolderListPresenter mFolderListPresenter;

    private RvNoteListAdapter mAdapter;

    public static String prefix;//便签内容前缀
    public static String postfix;//便签内容后缀
    public static String user;//当前操作者名称(用户名)

    public static void setUser() {
        user=Data.getAuthor();
    }

    public static String getUser() {
        setUser();//刷新usename
        return user==null?"未知":user;
    }

    public static String getPrefix() {
        setPrefix();//更新前缀
        return Data.getTimeshow().equals("0")?"":(prefix==null?"":prefix);
    }

    public static void setPrefix() {
        prefix="时间:"+ OrtherTool.getTime();
    }

    public static String getPostfix() {
        setPostfix();//更新后缀
        return postfix;
    }

    public static void setPostfix() {
        postfix=(Data.getShowauthor().equals("0")?"":("创建人:"+getUser()))+(Data.getShowlocation().equals("0")?"":("\n地点:"+ Data.getUserlocation()))+(Data.getShowweather().equals("0")?"":("\n天气"+Data.getWeather()));


    }

    @Override
    public void setFolderPresenter(IFolderListPresenter presenter) {
        mFolderListPresenter = presenter;
    }

    @Override
    public void setAdapter(RvNoteListAdapter adapter) {
        this.mAdapter = adapter;

    }

    @Override

    public void initDataBase() {
        if (Constans.isFirst) {
            int folderId = mFolderListPresenter.initDataBase();
            mINoteModel.initNote(folderId);
            CacheManager.setAndSaveIsFirst(false);
        }
    }

    @Override
    public void start() {
        mFolderListPresenter.getFolders();
        mFolderListPresenter.choiceFolder(Constans.currentFolder, true);
    }

    @Override
    public void showNormalNote(String title, int folderId) {
        mINoteModel.loadNormalNoteList(folderId, new LoadDataCallBack() {
            @Override
            protected void onSuccedd(List list) {
                mAdapter.setNewData(list);
            }
        });
        mView.hideDrawer();
        showFolderNameFotTitle(title);
        mView.showAddFab();
    }

    @Override
    public void showAllNote() {
        mINoteModel.loadAllNoteList(new LoadDataCallBack() {
            @Override
            protected void onSuccedd(List list) {
                mAdapter.setNewData(list);
            }
        });
        mView.hideDrawer();
        showFolderNameFotTitle("全部");
        mView.showAddFab();
    }

    @Override
    public void showPrivacyNote(boolean isShow) {
        if (isShow) {
            mINoteModel.loadPrivacyNoteList(new LoadDataCallBack() {
                @Override
                protected void onSuccedd(List list) {
                    mAdapter.setNewData(list);
                }
            });
            showFolderNameFotTitle("私密");
            mFolderListPresenter.choicePrivacy();
            mView.showAddFab();
        } else {
            mView.toLockActivity();
        }
        mView.hideDrawer();
    }

    @Override
    public void showRecycleBinNote() {
        mINoteModel.loadRecycleBinNoteList(new LoadDataCallBack() {
            @Override
            protected void onSuccedd(List list) {
                mAdapter.setNewData(list);
            }
        });
        mView.hideDrawer();
        showFolderNameFotTitle("废纸篓");
        mView.hideAddFab();
    }

    private void showFolderNameFotTitle(String title) {
        mView.showCurrentNoteFolderName(title);
        NoteListConstans.selectedFolderName = title;
    }

    @Override
    public void addNote(String noteId, String content, long modifiedTime) {//增加便签--将编辑有效的便签分类存储
        Note note = new Note();
        note.setNoteId(noteId);
        note.setNoteContent(content);
        note.setCreatedTime(modifiedTime);
        note.setModifiedTime(modifiedTime);
        note.setNoteEditlog("\n创建于:"+OrtherTool.getTime()+"创建者:"+getUser()+(Data.getNote_log_detail().equals("1")?("\n内容:"+note.getNoteContent()):""));//写日志
        mINoteModel.addNote(note);                 // 保存便签
        mFolderListPresenter.addNote2Folder(note); // 添加便签到便签夹
        mAdapter.addData(note);                     //保存到本地
        mView.setRvScrollToFirst();                 //放到最上方
    }

    @Override
    public void updateNote(int position, String content, long modifiedTime) {
        Note note = mAdapter.getData().get(position);

        if (TextUtils.isEmpty(content)) {//内容为空立即删除-并刷新界面
            removeNote(position);
            deleteNote(note, true);
            refreshRv();
        } else {
            note.setNoteContent(content);
            note.setNoteEditlog(note.getNoteEditlog()+"\n修改时间:"+OrtherTool.getTime()+"修改人:"+getUser()+(Data.getNote_log_detail().equals("1")?("\n内容:"+note.getNoteContent()):""));
            note.setModifiedTime(modifiedTime);
            note.save();
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void putNoteToPrivacy() {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                // 如果当前便签夹是私密便签夹,则执行移除私密操作，否则，执行添加私密操作
                if (Constans.currentFolder == FolderListConstans.ITEM_PRIMARY) {
                    mView.showLoading("移除中...");
                } else {
                    mView.showLoading("添加中...");
                }
            }

            @Override
            protected Boolean doInBackground(String... params) {
                for (int i = mAdapter.mCheckList.size() - 1; i >= 0; i--) {
                    if (mAdapter.mCheckList.get(i)) {
                        toPrivacy(i);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();
                refreshRv();
                cancelMultiSelectAction();
            }
        }.execute();

    }

    private void toPrivacy(int position) {
        Note note = mAdapter.getData().get(position);
        if (mAdapter.mAllDataList != null) {
            mAdapter.mAllDataList.remove(note);
        }

        mAdapter.getData().remove(position);
        mAdapter.mCheckList.remove(position);

        // 如果当前便签夹是私密便签夹,则执行移除私密操作，否则，执行添加私密操作
        if (Constans.currentFolder == FolderListConstans.ITEM_PRIMARY) {
            note.setIsPrivacy(0);
            mFolderListPresenter.removePrivacyNote(note);
        } else {
            note.setIsPrivacy(1);
            mFolderListPresenter.removeNoteForFolder(note);
        }
        note.save();
    }

    @Override
    public void deleteNotes() {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                mView.showLoading("删除中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                for (int i = mAdapter.mCheckList.size() - 1; i >= 0; i--) {
                    if (mAdapter.mCheckList.get(i)) {
                        Note note = mAdapter.getData().get(i);
                        removeNote(i);
                        deleteNote(note, false);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();

                refreshRv();
                cancelMultiSelectAction();
            }
        }.execute();

    }

    private void removeNote(int position) {
        if (mAdapter.mAllDataList != null) {  // 如果已进入搜索模式
            mAdapter.mAllDataList.remove(mAdapter.getData().get(position));
        }
        Note note = mAdapter.getData().get(position);
        mAdapter.getData().remove(position);    // 从Adapter的数据中删除
        mAdapter.mCheckList.remove(position);

        mFolderListPresenter.removeNoteForFolder(note);   // 从便签夹中移除
    }

    /**
     * @param isRealDelete 是否是永久删除（不指定的话，根据便签夹进行判断）
     * @describe
     */
    private void deleteNote(Note note, boolean isRealDelete) {

        // 删除操作
        if (isRealDelete || Constans.currentFolder == FolderListConstans.ITEM_RECYCLE) {       // 当前便签夹是废纸篓，则直接永久删除
            deleteFile(note.getNoteId());
            note.delete();
        } else {
            if (Constans.isUseRecycleBin) { // 已启用废纸篓
                note.setNoteEditlog(note.getNoteEditlog()+"移进废纸篓删除时间:"+ (new Date().getTime())+"操作者:"+getUser());
                toRecycleBin(note);
            } else {                       // 已关闭废纸篓
                note.delete();
                deleteFile(note.getNoteId());
            }
        }
    }

    private void deleteFile(String mNoteId) {
        File file = Utils.getContext().getExternalFilesDir(mNoteId);
        if (file.exists()) {
            FileUtils.deleteDir(file);
        }
    }

    private void toRecycleBin(Note note) {
        note.setInRecycleBin(1);
        note.save();
    }

    @Override
    public void moveNotes() {

        // 如果是废纸篓，则执行恢复操作
        if (Constans.currentFolder == FolderListConstans.ITEM_RECYCLE) {
            recoverNotes();
        } else {
            mView.showMoveBottomSheet();
        }
    }

    private void recoverNotes() {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                mView.showLoading("恢复中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                for (int i = mAdapter.mCheckList.size() - 1; i >= 0; i--) {
                    if (mAdapter.mCheckList.get(i)) {
                        recoverNote(i);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();
                refreshRv();
                cancelMultiSelectAction();
            }
        }.execute();

    }

    @Override
    public void recoverNote(int position) {
        Note note = mAdapter.getData().get(position);
        if (mAdapter.mAllDataList != null) {
            mAdapter.mAllDataList.remove(note);
        }
        mAdapter.getData().remove(position);
        mAdapter.mCheckList.remove(position);
        mFolderListPresenter.recoverNote(note);
    }

    @Override
    public void moveNotesToFolder(final NoteFolder noteFolder) {

        new AsyncTask<String, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                mView.showLoading("移动中...");
            }

            @Override
            protected Boolean doInBackground(String... params) {
                for (int i = mAdapter.mCheckList.size() - 1; i >= 0; i--) {
                    if (mAdapter.mCheckList.get(i)) {
                        moveNote(i, noteFolder);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mView.unShowLoading();
                int count = NoteListConstans.selectedCount;
                cancelMultiSelectAction();
                refreshRv();
                mView.showSnackbar("已将" + count + "条便签移动到" + noteFolder.getFolderName());
            }
        }.execute();

    }

    private void moveNote(int notePos, NoteFolder noteFolder) {
        Note note = mAdapter.getData().get(notePos);
        // 如果当前便签为全部便签，则不用从当前NoteRv中移除
        if (Constans.currentFolder == FolderListConstans.ITEM_ALL) {
            mFolderListPresenter.moveNoteToFolder(note, noteFolder);
        } else {
            if (noteFolder.getId() != mFolderListPresenter.getCurrentFolderId()) {
                if (mAdapter.mAllDataList != null) {
                    mAdapter.mAllDataList.remove(note);
                }
                mAdapter.getData().remove(notePos);
                mAdapter.mCheckList.remove(notePos);

                mFolderListPresenter.moveNoteToFolder(note, noteFolder);

            }
        }
    }

    @Override
    public List<NoteFolder> getFolderDataList() {
        return mFolderListPresenter.getFoldersForAdapter();
    }

    @Override
    public void choiceNote(int position) {
        boolean isChoice = mAdapter.mCheckList.get(position);
        if (isChoice) {
            setNoteSelectedCount(getNoteSelectedCount() - 1);
        } else {
            setNoteSelectedCount(getNoteSelectedCount() + 1);
        }
        mAdapter.mCheckList.set(position, !isChoice);
        mAdapter.notifyItemChanged(position);
        showSelectedNoteCount();
    }


    @Override
    public void showSelectedNoteCount() {
        mView.showChoiceNotesCount(NoteListConstans.selectedCount + "");
    }

    @Override
    public void showCurrentFolderName() {
        mView.showCurrentNoteFolderName(NoteListConstans.selectedFolderName);
    }

    @Override
    public void onNoteRvClick(int position) {
        if (isShowMultiSelectAction()) {
            choiceNote(position);
        } else {
            if (Constans.currentFolder == FolderListConstans.ITEM_RECYCLE) {
                mView.showNoteRecoverDialog(position);
            } else {
                mView.toEditNoteForEdit(mAdapter.getData().get(position), position);
            }
        }
    }

    @Override
    public void initNoteRvLayoutManager() {
        if (Constans.noteListShowMode == NoteListConstans.STYLE_LINEAR) {
            mView.changeNoteRvLayoutManager(new LinearLayoutManager(MainApplication.mContext));
        } else {
            mView.changeNoteRvLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    @Override
    public void initShowModeMenuIcon(MenuItem item) {
        if (Constans.noteListShowMode == NoteListConstans.STYLE_LINEAR) {
            item.setIcon(MainApplication.mContext.getResources().getDrawable(NoteListConstans.MODE_GRID));
        } else {
            item.setIcon(MainApplication.mContext.getResources().getDrawable(NoteListConstans.MODE_LIST));
        }
    }


    @Override
    public void changeNoteRvLayoutManagerAndMenuIcon(MenuItem item) {

        if (Constans.noteListShowMode == NoteListConstans.STYLE_LINEAR) {
            CacheManager.setAndSaveNoteListShowMode(NoteListConstans.STYLE_GRID);
            mView.changeNoteRvLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            item.setIcon(MainApplication.mContext.getResources().getDrawable(NoteListConstans.MODE_LIST));
        } else {
            CacheManager.setAndSaveNoteListShowMode(NoteListConstans.STYLE_LINEAR);
            mView.changeNoteRvLayoutManager(new LinearLayoutManager(MainApplication.mContext));
            item.setIcon(MainApplication.mContext.getResources().getDrawable(NoteListConstans.MODE_GRID));
        }
    }

    @Override
    public void doMultiSelectActionAndChoiceThisItem(int position) {
        if (!NoteListConstans.isShowMultiSelectAction) {
            NoteListConstans.isShowMultiSelectAction = true;
            // 更新toolbar菜单
            mView.updateOptionMenu();
            // 隐藏添加按钮
            mView.hideAddFab();
            // 显示BottomBar
            mView.showBottomBar();
            // 设置三个按钮的可使用性
            mView.setCheckMenuEnable();
            // 设置三个按钮
            setCheckMenuForFolderType();
            // 选中当前便签
            choiceNote(position);
            // 显示已选中的数量
            mView.showChoiceNotesCount(NoteListConstans.selectedCount + "");
            // 刷新便签RecyclerView
            refreshRv();
        }
    }


    private void setCheckMenuForFolderType() {
        switch (Constans.currentFolder) {
            case FolderListConstans.ITEM_PRIMARY:
                mView.setCheckMenuForPrivacy();
                break;
            case FolderListConstans.ITEM_RECYCLE:
                mView.setCheckMenuForRecycleBin();
                break;
            default:
                mView.setCheckMenuForAllAndNormal();
                break;
        }
    }

    @Override
    public void cancelMultiSelectAction() {
        if (NoteListConstans.isShowMultiSelectAction) {
            NoteListConstans.isShowMultiSelectAction = false;
            // 更新toolbar菜单
            mView.updateOptionMenu();
            // 隐藏BottomBar
            mView.hideBottomBar();
            // 将所有便签的选中状态设为false
            unChoiceAllNote();
            // 显示已选中的便签夹名称
            mView.showCurrentNoteFolderName(NoteListConstans.selectedFolderName);
            // 刷新便签RecyclerView
            refreshRv();
            // 显示添加按钮
            mView.showAddFab();
        }
    }

    @Override
    public boolean isShowMultiSelectAction() {
        return NoteListConstans.isShowMultiSelectAction;
    }

    @Override
    public void doChoiceAllNote() {
        if (NoteListConstans.isChoiceAll) {
            unChoiceAllNote();
        } else {
            choiceAllNote();
        }
    }

    @Override
    public void setInSearch() {
        mView.hideAddFab();
        NoteListConstans.isInSearch = true;
    }

    @Override
    public void setOutSearch() {
        mView.showAddFab();
        NoteListConstans.isInSearch = false;
    }

    /*---------------------------------------------------------------------------------------*/

    public void setFilter(String text) {
        if (mAdapter.mAllDataList == null) {
            mAdapter.mAllDataList = new ArrayList<Note>();
            mAdapter.mAllDataList.addAll(mAdapter.getData());
        }
        if (mAdapter.mAllCheckList == null) {
            mAdapter.mAllCheckList = new ArrayList<Boolean>();
            mAdapter.mAllCheckList.addAll(mAdapter.mCheckList);
        }

        mAdapter.getData().clear();
        mAdapter.mCheckList.clear();
//        转换为小写
        String lowerCaseQuery = text.toLowerCase();
        //　此处使用倒叙进行检索，这样搜索出来的顺序是正序
        for (int i = mAdapter.mAllDataList.size() - 1; i >= 0; i--) {
            if (mAdapter.mAllDataList.get(i).getNoteContent().toLowerCase().contains(lowerCaseQuery)) {
                mAdapter.addData(mAdapter.mAllDataList.get(i));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void cancelFilter() {
        NoteListConstans.isInSearch = false;
        if (mAdapter.mAllDataList != null || mAdapter.mAllCheckList != null) {
            mAdapter.getData().clear();
            mAdapter.mCheckList.clear();
            mAdapter.setNewData(mAdapter.mAllDataList);
            for (int i = 0; i < mAdapter.mAllDataList.size(); i++) {
                mAdapter.mCheckList.add(false);
            }
            mAdapter.mAllDataList = null;
            mAdapter.mAllCheckList = null;
            mAdapter.notifyDataSetChanged();
        }
    }

    public int getNoteSelectedCount() {
        return NoteListConstans.selectedCount;
    }

    public void setNoteSelectedCount(int count) {
        NoteListConstans.selectedCount = count;
        setChoiceAllState();
        setBottomMenuEnable();
    }

    private void setChoiceAllState() {
        if (NoteListConstans.selectedCount == mAdapter.getData().size()) {
            NoteListConstans.isChoiceAll = true;
        } else {
            NoteListConstans.isChoiceAll = false;
        }
    }


    private void setBottomMenuEnable() {
        if (NoteListConstans.selectedCount > 0) {
            mView.setCheckMenuEnable();
        } else {
            mView.setCheckMenuUnEnable();
        }
    }

    public void choiceAllNote() {
        NoteListConstans.isChoiceAll = true;
        for (int i = 0; i < mAdapter.mCheckList.size(); i++) {
            mAdapter.mCheckList.set(i, true);
        }
        setNoteSelectedCount(mAdapter.mCheckList.size());
        mAdapter.notifyDataSetChanged();

        showSelectedNoteCount();
    }

    public void unChoiceAllNote() {
        NoteListConstans.isChoiceAll = false;
        for (int i = 0; i < mAdapter.mCheckList.size(); i++) {
            mAdapter.mCheckList.set(i, false);
        }
        setNoteSelectedCount(0);
        mAdapter.notifyDataSetChanged();

        showSelectedNoteCount();
    }

    public void uncertainChoiceNote(int position) {
        boolean isChoice = mAdapter.mCheckList.get(position);
        if (isChoice) {
            setNoteSelectedCount(getNoteSelectedCount() - 1);
        } else {
            setNoteSelectedCount(getNoteSelectedCount() + 1);
        }
        mAdapter.mCheckList.set(position, !isChoice);
        mAdapter.notifyItemChanged(position);
    }

    public void refreshRv() {
        mAdapter.notifyDataSetChanged();
        mFolderListPresenter.refreshFolderList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        com.orhanobut.logger.Logger.d("requestCode=" + requestCode + "   resultCode=" + resultCode);
        switch (requestCode) {
            case NoteListConstans.REQUEST_CODE_LOCK:
                resultForLock(resultCode);
                break;
            case NoteListConstans.REQUEST_CODE_ADD://增加便签
                resultForAddNote(resultCode, data);
                break;
            case NoteListConstans.REQUEST_CODE_EDIT:
                resultForEditNote(resultCode, data);
                break;
            case NoteListConstans.REQUEST_CODE_EDIT_FOLDER:
                resultForEditFolder(resultCode, data);
                break;
        }
    }

    @Override
    public void logCheckList() {
        for (int i = mAdapter.mCheckList.size() - 1; i >= 0; i--) {
            Logger.d("lllll" + mAdapter.mCheckList.get(i) + "   " + i);

        }
    }

    private void resultForLock(int resultCode) {
        if (resultCode == RESULT_OK) {
            showPrivacyNote(true);
        }
    }

    private void resultForAddNote(int resultCode, Intent data) {//增加便签--退出编辑的时候调用
        if (resultCode == RESULT_OK) {//如果编辑的内容不为空
            String noteId = data.getStringExtra("note_id");
            String content = data.getStringExtra("note_content");//便签内容
            long modifiedTime = data.getLongExtra("modified_time", 0);
            addNote(noteId, content, modifiedTime);
        }
    }

    private void resultForEditNote(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", 0);
            String content = data.getStringExtra("note_content");
            long modifiedTime = data.getLongExtra("modified_time", 0);
            updateNote(position, content, modifiedTime);
        }
    }

    private void resultForEditFolder(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            boolean isCurrentFolderDeleted = data.getBooleanExtra("is_current_folder_deleted", false);
            if (isCurrentFolderDeleted) {
                Constans.currentFolder = FolderListConstans.ITEM_ALL;
            }
            start();
        }
    }
}
