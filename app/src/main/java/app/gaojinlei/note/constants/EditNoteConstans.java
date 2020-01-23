package app.gaojinlei.note.constants;

import android.graphics.Bitmap;

import com.blankj.utilcode.util.SizeUtils;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class EditNoteConstans {

    // 图片标记的前便签和后标签
    public static String imageTabBefore="<image>";
    public static String imageTabAfter="</image>";

    // 图片距离左右的总距离
    public static float imageMargin= SizeUtils.dp2px(32);

    // 分享时的水印文字
    public static String watermarkText="来自：便签";

    public static Bitmap shareBitmap;
}
