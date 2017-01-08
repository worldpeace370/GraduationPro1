package com.lebron.mvp.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wuxiangkun on 2017/1/8.
 * Contact way wuxiangkun2015@163.com
 *
 * @param <T> a type of callback to get data from net.
 */

public abstract class BaseModel<T> {
    private HashMap<String, T> idToCallBack = new HashMap<>();
    private HashMap<T, String> callBackToId = new HashMap<>();

    public void addCallBack(T t) {
        String id = t.getClass().getSimpleName() + "/" + System.nanoTime() + "/" + (int) (Math.random() * Integer.MAX_VALUE);
        idToCallBack.put(id, t);
        callBackToId.put(t, id);
    }

    public <T> void removeCallBack(T t) {
        idToCallBack.remove(callBackToId.remove(t));
    }

    public ArrayList<T> getCallBacks() {
        return new ArrayList(idToCallBack.values());
    }
}
