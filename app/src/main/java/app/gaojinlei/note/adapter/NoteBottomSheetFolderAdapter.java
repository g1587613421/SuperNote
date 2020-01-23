package app.gaojinlei.note.adapter;

import com.app.gaojinlei.note.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import app.gaojinlei.note.bean.NoteFolder;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class NoteBottomSheetFolderAdapter extends BaseQuickAdapter<NoteFolder, BaseViewHolder> {

    public NoteBottomSheetFolderAdapter() {
        super(R.layout.item_note_bottom_folder);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoteFolder item) {
        helper.setText(R.id.tv_folder_title_bottom_sheet,item.getFolderName());
    }


}
