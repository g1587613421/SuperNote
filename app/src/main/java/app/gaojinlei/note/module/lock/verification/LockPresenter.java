package app.gaojinlei.note.module.lock.verification;

import app.gaojinlei.note.module.base.BasePresenter;
import app.gaojinlei.note.utils.MD5Util;

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

public class LockPresenter extends BasePresenter<ILockView> implements ILockPresenter{

    @Override
    public boolean verifyPassword(List<Integer> passPositions, String password) {
        StringBuilder sb=new StringBuilder();
        for (Integer i:passPositions){
            sb.append(i.intValue());
        }
        String currentPassword= MD5Util.getMd5Value(sb.toString());
        if(currentPassword.equals(password)){
            mView.onSuccess();
            return true;
        } else{
            mView.onError();
            return false;
        }
    }
}
