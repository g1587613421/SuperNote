package app.gaojinlei.note.module.notes.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gaojinlei.note.R;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import app.gaojinlei.manager.Data;
import app.gaojinlei.note.adapter.NoteBottomSheetFolderAdapter;
import app.gaojinlei.note.adapter.RvNoteListAdapter;
import app.gaojinlei.note.bean.Note;
import app.gaojinlei.note.bean.NoteFolder;
import app.gaojinlei.note.constants.Constans;
import app.gaojinlei.note.constants.NoteListConstans;
import app.gaojinlei.note.module.base.BaseActivity;
import app.gaojinlei.note.module.lock.modification.LockModificationActivity;
import app.gaojinlei.note.module.lock.verification.LockActivity;
import app.gaojinlei.note.module.notes.edit.EditNoteActivity;
import app.gaojinlei.note.serve.gaodeserve;
import app.gaojinlei.note.serve.weather;
import app.gaojinlei.note.utils.ProgressDialogUtils;
import app.gaojinlei.start.launch.LaunchActivity;
import app.gaojinlei.tools.MyImageView;
import app.gaojinlei.tools.StoreData;
import app.gaojinlei.weight.MyWidgetService;
import app.gaojinlei.weight.WeightActivity;
import butterknife.BindView;

public class NoteMainActivity extends BaseActivity<INoteMainView, NoteMainPresenter>
        implements INoteMainView<List<Note>>, View.OnClickListener, View.OnTouchListener {
public static boolean isfirst=true;
    @BindView(R.id.rv_note_list)
    RecyclerView mRvNoteList;   // 便签列表

    @BindView(R.id.tv_note_list_to_privacy)
    TextView mTvToPrivacy;  // 设为私密

    @BindView(R.id.tv_note_list_delete)
    TextView mTvDelete;   // 删除

    @BindView(R.id.tv_note_list_move)
    TextView mTvMove;   // 移动

    @BindView(R.id.fab_note_list_add)
    FloatingActionButton mFabAdd;  // 添加便签

    @BindView(R.id.rl_note_list_bottom_bar)
    RelativeLayout mRlBottomBar;   // 多选操作的bottomBar

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    private MenuItem mSearchMenu;
    private MenuItem mShowModeMenu;
    private MenuItem mCheckAllMenu;

    private float mScrollLastY;
    private float mTouchSlop;

    private RvNoteListAdapter mAdapter = new RvNoteListAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (StoreData.getPreference().getInt("open_wedget_tool",0)==1)
        startService(new Intent(getApplicationContext(), MyWidgetService.class));
    }

    private void initService() {
        startService(new Intent(getApplicationContext(),gaodeserve.class));
        startService(new Intent(getApplicationContext(),weather.class));
        if (StoreData.getPreference().getInt("open_wedget_tool",2)==2&&Build.VERSION.SDK_INT <Build.VERSION_CODES.O)
            StoreData.PutKeyValues("open_wedget_tool",1);//第一次启动app判断是否默认开启该功能
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected NoteMainPresenter initPresenter() {
        NoteMainPresenter presenter = new NoteMainPresenter();
        presenter.attch(this);
        presenter.setAdapter(mAdapter);
        return presenter;
    }
    @Override
    protected void onResume() {//重新返回该界面的是否主动刷新界面信息
        super.onResume();
        initService();
        //startActivity(new Intent("weight"));//测试桌面组件
        if (StoreData.getPreference().getInt("open_wedget_tool",0)==1)
        startService(new Intent(getApplicationContext(), MyWidgetService.class));

        test();//测试服务状态
        initViews();
        turn();

    }

    private void turn() {

        Intent intent=getIntent();
        if (intent.getExtras()!=null&&intent.getExtras().getString("action")!=null&&intent.getExtras().getString("action").equals("wedgetsetting"))
            startActivity(new Intent(this, WeightActivity.class));
        else
        if (isfirst&&!Data.getUser_image_data().equals("")){//如果没有登陆禁止任何操作
            isfirst=false;
            if (StoreData.getPreference().getString("quick_start","0").equals("1"))
            {
                handler.sendEmptyMessage(0x0001);
            }
            if (StoreData.getPreference().getString("weather_quick","0").equals("1")){
               handler.sendEmptyMessage(0x0002);
            }
        }
        else
            isfirst=false;
    }
    Handler handler;
    private void test(){//测试

        handler = new Handler(){
           @Override
           public void handleMessage(Message msg) {
               if (msg.what==0x001){
                   Toast.makeText(mContext, "请等待,不要进行操作,正在为快速新建加载必须的服务", Toast.LENGTH_SHORT).show();
                   new Thread(){
                       @Override
                       public void run() {
                           try {
                               sleep(3000);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           toEditNoteForAdd();
                       }
                   }.start();

               }
               if (msg.what==0x002){
                   startActivity(new Intent("weatherweb"));
               }

           }
       };
    }
    @Override
    protected void initViews() {
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        mPresenter.initDataBase();
        initDrawer();
        initAdapter();

        mRvNoteList.setAdapter(mAdapter);
        mRvNoteList.setOnTouchListener(this);

        mPresenter.initNoteRvLayoutManager();
        inituserimage();
    }

    /**
     * 初始化侧滑栏
     */
    public void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initAdapter() {
        mAdapter.setOnItemChildClickListener(mOnRvClickListener);
        mAdapter.setOnItemChildLongClickListener(mOnRvLongClickListener);
        mAdapter.setEmptyView(getRvEmptyView());
    }

    /**
     * 便签Rv点击事件
     *
     * @describe
     */
    BaseQuickAdapter.OnItemChildClickListener mOnRvClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            mPresenter.onNoteRvClick(position);
        }
    };

    /**
     * 便签Rv长按事件
     *
     * @describe
     */
    BaseQuickAdapter.OnItemChildLongClickListener mOnRvLongClickListener = new BaseQuickAdapter.OnItemChildLongClickListener() {
        @Override
        public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
            Logger.d(NoteListConstans.isInSearch);
            if (!NoteListConstans.isInSearch) { // 搜索模式下不允许长按事件
                mPresenter.doMultiSelectActionAndChoiceThisItem(position);
            }
            return true;
        }
    };

    private View getRvEmptyView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_empty, null, false);
        return view;
    }

    @Override
    protected void updateViews() {
        mPresenter.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_note_list_add:
                toEditNoteForAdd();
                break;
            case R.id.tv_note_list_delete:
                showDeleteDialog();
                break;
            case R.id.tv_note_list_move:
                mPresenter.moveNotes();
                break;
            case R.id.tv_note_list_to_privacy:
                mPresenter.putNoteToPrivacy();
                break;
        }
    }

    //    显示dialog
    private void showDeleteDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("删除便签")
                .setMessage("确定删除选中的便签吗？")
                .setPositiveButton("删除",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.deleteNotes();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float rawY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScrollLastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                if ((rawY - mScrollLastY) > mTouchSlop) {    // 手指向下滑动
                    setAddFabIn();
                } else if ((mScrollLastY - rawY) > mTouchSlop) {  // 手指向上滑动
                    setAddFabOut();
                }
                mScrollLastY = rawY;
                break;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {//　侧滑菜单已打开

            mDrawer.closeDrawer(GravityCompat.START);
        } else if (mPresenter.isShowMultiSelectAction()) {//　已显示多选菜单
            mPresenter.cancelMultiSelectAction();
        } else {                                      // 默认
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        initOptionMemu(menu);

        return true;
    }



    /**
     * 初始化toolbar menu
     */
    public void initOptionMemu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        mSearchMenu = menu.findItem(R.id.menu_note_search);
        initSearchMenu(mSearchMenu);

        mShowModeMenu = menu.findItem(R.id.menu_note_show_mode);
        mPresenter.initShowModeMenuIcon(mShowModeMenu);

        mCheckAllMenu = menu.findItem(R.id.menu_note_check_all);
    }

    private void initSearchMenu(MenuItem searchItem) {
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // 搜索View的文字改变事件
        searchView.setOnQueryTextListener(qreryTextListener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            MenuItemCompat.setOnActionExpandListener(searchItem,
                    new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(MenuItem menuItem) {
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                            mPresenter.cancelFilter();
                            mPresenter.setOutSearch();
                            return true;
                        }
                    });
        } else {
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    mPresenter.setOutSearch();
                    mPresenter.cancelFilter();
                    return true;
                }
            });
        }
    }

    /**
     * toolbar的SearchView的文字改变事件
     */
    private SearchView.OnQueryTextListener qreryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            mPresenter.setFilter(newText);
            return true;
        }
    };

    /**
     * 更新toolbar的menu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mPresenter.isShowMultiSelectAction()) {
            setMenuForMulitiSelectionActionIsShow();
        } else {
            setMenuForMulitiSelectionActionIsNotShow();
        }

        return true;
    }

    private void setMenuForMulitiSelectionActionIsShow() {
        mSearchMenu.setVisible(false);
        mShowModeMenu.setVisible(false);
        mCheckAllMenu.setVisible(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter.isShowMultiSelectAction()) {
                    mPresenter.cancelMultiSelectAction();
                }
            }
        });
    }

    private void setMenuForMulitiSelectionActionIsNotShow() {
        mSearchMenu.setVisible(true);
        mShowModeMenu.setVisible(true);
        mCheckAllMenu.setVisible(false);
//            自定义的drawer打开关闭的事件
        MyActionBarDrawerToggle toggle = new MyActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void login(View view) {//启动登陆界面
        startActivity(new Intent("login"));

    }
    private void inituserimage() {//显示用户头像
        if (!Data.getUser_image_data().equals("")){
            if ((findViewById(R.id.img_user_header))!=null)//避免第一次启动时界面未加载造成异常
            {
                ((MyImageView)findViewById(R.id.img_user_header)).setImageURL(Data.getUser_image_data());
            }else{
                //延迟加载--一直自调用直到加载成功
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        inituserimage();
                    }
                }.start();
            }
        }else{//未登录去登陆
            login(findViewById(R.id.img_user_header));
        }
    }


    //    继承系统定义的事件。重写drawer open的事件，如果多选菜单以显示，则关闭多选菜单.
    class MyActionBarDrawerToggle extends ActionBarDrawerToggle {
        public MyActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            if (mPresenter.isShowMultiSelectAction()) {
                mPresenter.cancelMultiSelectAction();
            }
//            滑动时，容易导致fab隐藏，所以让其显示
            setAddFabIn();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_note_show_mode:
                mPresenter.changeNoteRvLayoutManagerAndMenuIcon(item);
                break;
            case R.id.menu_note_check_all:
                mPresenter.doChoiceAllNote();
                break;
            case R.id.menu_note_search:
                mPresenter.setInSearch();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setRvScrollToFirst() {
        mRvNoteList.scrollToPosition(0);
    }

    @Override
    public void showChoiceNotesCount(String title) {
        setTitle(title);
    }

    @Override
    public void showCurrentNoteFolderName(String title) {
        setTitle(title);
    }

    @Override
    public void updateOptionMenu() {
        // 更新toolbar菜单，系统会去调用onPrepareOptionsMenu（MenuItem item)方法
        supportInvalidateOptionsMenu();
    }

    @Override
    public void setAddFabOut() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFabAdd, "translationY", SizeUtils.dp2px(80));
        animator.setDuration(150);
        animator.start();
    }

    @Override
    public void setAddFabIn() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFabAdd, "translationY", SizeUtils.dp2px(0));
        animator.setDuration(150);
        animator.start();
    }

    @Override
    public void showAddFab() {
        mFabAdd.setVisibility(View.VISIBLE);
        setAddFabIn();
    }

    @Override
    public void hideAddFab() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFabAdd, "translationY", SizeUtils.dp2px(80));
        animator.setDuration(150);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFabAdd.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    @Override
    public void hideDrawer() {
        mDrawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void hideBottomBar() {
        // 下移动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(mRlBottomBar, "translationY", SizeUtils.dp2px(56));
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRlBottomBar.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    @Override
    public void showBottomBar() {
        mRlBottomBar.setVisibility(View.VISIBLE);
        //        bottombar进行一个上移的动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(mRlBottomBar, "translationY", SizeUtils.dp2px(56), 0);
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
        animator.start();
    }

    @Override
    public void setCheckMenuEnable() {
        setButtonEnabled(true, mTvDelete);
        setButtonEnabled(true, mTvMove);
        setButtonEnabled(true, mTvToPrivacy);
    }

    @Override
    public void setCheckMenuUnEnable() {
        setButtonEnabled(false, mTvDelete);
        setButtonEnabled(false, mTvMove);
        setButtonEnabled(false, mTvToPrivacy);
    }

    //    设置按钮的可使用性
    private void setButtonEnabled(boolean enabled, TextView view) {
        view.setEnabled(enabled);
        if (enabled) {
            view.setTextColor(getResources().getColor(R.color.white));
        } else {
            view.setTextColor(getResources().getColor(R.color.colorWhiteAlpha30));
        }
    }

    @Override
    public void setCheckMenuForAllAndNormal() {
        mTvToPrivacy.setText("设为私密");
        mTvDelete.setText("删除");
        mTvMove.setText("移动");

        mTvToPrivacy.setVisibility(View.VISIBLE);
        mTvDelete.setVisibility(View.VISIBLE);
        mTvMove.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCheckMenuForPrivacy() {
        mTvToPrivacy.setText("移除私密");
        mTvDelete.setText("删除");

        mTvToPrivacy.setVisibility(View.VISIBLE);
        mTvDelete.setVisibility(View.VISIBLE);
        mTvMove.setVisibility(View.GONE);
    }

    @Override
    public void setCheckMenuForRecycleBin() {
        mTvDelete.setText("删除");
        mTvMove.setText("恢复");

        mTvToPrivacy.setVisibility(View.GONE);
        mTvDelete.setVisibility(View.VISIBLE);
        mTvMove.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoveBottomSheet() {
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
//                    获取contentView
        ViewGroup contentView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View root = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_folder, contentView, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_bottom_sheet_folder);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        dialog.setContentView(root);

        recyclerView.setAdapter(getBottomSheetRvAdapter(dialog));

        dialog.show();
    }

    private NoteBottomSheetFolderAdapter getBottomSheetRvAdapter(final BottomSheetDialog dialog) {
        final NoteBottomSheetFolderAdapter folderAdapter = new NoteBottomSheetFolderAdapter();
        folderAdapter.setNewData(mPresenter.getFolderDataList());
        folderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NoteFolder folder = folderAdapter.getData().get(position);
                mPresenter.moveNotesToFolder(folder);
                dialog.cancel();
            }
        });
        return folderAdapter;
    }

    @Override
    public void showNoteRecoverDialog(final int position) {
        new AlertDialog.Builder(mContext)
                .setMessage("无法直接打开便签，是否恢复至原有便签夹？")
                .setPositiveButton("恢复",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.recoverNote(position);
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }



    @Override
    public void changeNoteRvLayoutManager(RecyclerView.LayoutManager manager) {
        mRvNoteList.setLayoutManager(manager);
        mPresenter.refreshRv();
    }

    @Override
    public void toLockActivity() {
        Intent intent;
        if (Constans.isLocked) {
            intent = new Intent(mContext, LockActivity.class);
        } else {
            intent = new Intent(mContext, LockModificationActivity.class);
        }

        // 5.0及以上则使用Activity动画
        if (Build.VERSION.SDK_INT >= 21) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivityForResult(intent, NoteListConstans.REQUEST_CODE_LOCK, bundle);
        } else {
            startActivityForResult(intent, NoteListConstans.REQUEST_CODE_LOCK);
        }
    }

    @Override
    public void toEditNoteForAdd() {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("is_add", true);
        startActivityForResult(intent, NoteListConstans.REQUEST_CODE_ADD);
    }

    @Override
    public void toEditNoteForEdit(Note note, int position) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("is_add", false);
        intent.putExtra("note_id", note.getNoteId());
        intent.putExtra("note_content", note.getNoteContent());
        intent.putExtra("modified_time", note.getModifiedTime());
        intent.putExtra("note_log",note.getNoteEditlog());
        startActivityForResult(intent, NoteListConstans.REQUEST_CODE_EDIT);
    }

    private ProgressDialogUtils mProgressDialog=new ProgressDialogUtils(this);

    @Override
    public void showLoading(String message) {
        mProgressDialog.show(message);
    }

    @Override
    public void unShowLoading() {
        mProgressDialog.hide();
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(mFabAdd, message,
                Snackbar.LENGTH_SHORT).setAction("Action", null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
