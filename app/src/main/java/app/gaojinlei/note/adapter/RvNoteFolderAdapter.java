package app.gaojinlei.note.adapter;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.gaojinlei.note.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import app.gaojinlei.note.MainApplication;
import app.gaojinlei.note.bean.NoteFolder;
import app.gaojinlei.note.constants.Constans;
import app.gaojinlei.note.utils.ThemeUtils;
import app.gaojinlei.note.widget.MyDrawable;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2019/04/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RvNoteFolderAdapter extends BaseQuickAdapter<NoteFolder, BaseViewHolder> {

    public RvNoteFolderAdapter() {
        super(R.layout.item_folder);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoteFolder item) {
        helper.setText(R.id.tv_folder_list_title, item.getFolderName())
                .setText(R.id.tv_folder_list_count, item.getNoteCount() + "");


        RelativeLayout rlItem=helper.getView(R.id.rl_folder_root);
        TextView tvTitle=helper.getView(R.id.tv_folder_list_title);
        TextView tvCount=helper.getView(R.id.tv_folder_list_count);
        ImageView ivIcon=helper.getView(R.id.iv_folder_list_ic);

        if(Constans.currentFolder == helper.getLayoutPosition()-getHeaderLayoutCount()){
            rlItem.setSelected(true);
            tvTitle.setTextColor(ThemeUtils.getColorPrimary(mContext));
            tvCount.setTextColor(ThemeUtils.getColorPrimary(mContext));
            ivIcon.setBackground(MyDrawable.getIcFolderSelectedDrawable( ThemeUtils.getColorPrimary(mContext)));
        } else {
            rlItem.setSelected(false);
            tvTitle.setTextColor(MainApplication.getContext().getResources().getColor(R.color.colorBlackAlpha87));
            tvCount.setTextColor(MainApplication.getContext().getResources().getColor(R.color.colorBlackAlpha54));
            ivIcon.setBackgroundResource(R.drawable.ic_folder_un_selected);
        }
    }


}
