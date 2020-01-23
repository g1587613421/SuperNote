package app.gaojinlei.weight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.app.gaojinlei.note.R;

/**
 * ====================== WidgetService ========================
 *
 * @author SGamble
 */
public class MyWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetFactory(getApplicationContext(), intent);
    }

    public static class MyWidgetFactory implements RemoteViewsFactory {
        private Context mContext;

        public MyWidgetFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < 0 || position >= getCount()) {
                return null;
            }
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.weight_layout_item);
            views.setTextViewText(R.id.textView1, WeightData.getTitle().get(position));//设置 ListView 的显示
            views.setTextViewText(R.id.textView2, WeightData.getContent().get(position));//设置 ListView 的显示
            views.setOnClickFillInIntent(R.id.del, delIntent(position));//ListView - item 点击事件
            return views;
        }

        /**
         * 删除Intent
         * @author Gamble
         */
        private Intent delIntent(int position) {
            Bundle extras = new Bundle();
            extras.putInt("key", position); //传递数据 - item - position
            Intent delIntent = new Intent();
            delIntent.setAction("del.com"); //设置意图
            delIntent.putExtras(extras); //放入需要传递的数据
            return delIntent;
        }

        @Override
        public int getCount() {
            return WeightData.getTitle().size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 在调用getViewAt的过程中，显示一个LoadingView。
         * 如果return null，那么将会有一个默认的loadingView
         * @author Gamble
         */
        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onCreate() {
            Toast.makeText(mContext, "小部件管理服务已经启动", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDataSetChanged() { }

        @Override
        public void onDestroy() { }
    }
}