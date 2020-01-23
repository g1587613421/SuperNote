package app.gaojinlei.note.module.lock.verification;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface ILockView {

    /**
     *  错误
     *  @describe
     */
    void onError();

    /**
     *  正确
     *  @describe
     */
    void onSuccess();
}
