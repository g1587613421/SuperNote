package app.gaojinlei.note.module.base;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public abstract class BasePresenter<T> {

    public T mView;

    /**
     * 绑定View 初始化时调用
     *
     * @param mView
     * @describe
     */
    public void attch(T mView) {
        this.mView = mView;
    }

    /**
     * 分离view，View销毁时调用
     *
     * @describe
     */
    public void detach() {
        mView = null;
    }


}
