package app.gaojinlei.note.model;

import java.util.List;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public abstract class LoadDataCallBack<T> {
    protected abstract void onSuccedd(List<T> list);

}
