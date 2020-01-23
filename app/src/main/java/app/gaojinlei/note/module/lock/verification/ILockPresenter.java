package app.gaojinlei.note.module.lock.verification;

import java.util.List;

/**
 * <pre>
 *     author : 高金磊
 *     e-mail : g1587613421@outlook.com
 *     time   : 2017/06/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface ILockPresenter {
    boolean verifyPassword(List<Integer> passPositions,String password);
}
