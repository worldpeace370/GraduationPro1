package com.lebron.mvp.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.CopyOnWriteArrayList;

/**Presenter<View>中的View指实现了View接口的Fragment或者Activity，使用时将接口作为泛型擦除的实际对象
 * Created by wuxiangkun on 2017/1/2.
 * Contact way wuxiangkun2015@163.com
 */

public class Presenter<View> {
    private View mView;
    private CopyOnWriteArrayList<OnDestroyListener> mOnDestroyListeners = new CopyOnWriteArrayList<>();

    /**
     * This method is called after presenter construction.
     * <p/>
     * This method is intended for overriding.
     *
     * @param savedState If the presenter is being re-instantiated after a process restart then this Bundle
     *                   contains the data it supplied in {@link #onSave}.
     */
    protected void onCreate(@Nullable Bundle savedState) {
    }

    /**
     * This method is being called when a activity/fragment resume
     * <p/>
     * This method is intended for overriding.
     */
    protected void onResume() {
    }

    /**
     * This method is being called when a activity/fragment pause
     * <p/>
     * This method is intended for overriding.
     */
    protected void onPause() {

    }

    /**
     * This method is being called when a activity's launch mode is not standard
     * && this activity has already started.
     *
     * @param intent startActivity(intent) method passed intent.
     */
    protected void onNewIntent(Intent intent) {
    }

    /**
     * This method is being called when a user leaves view.
     * <p/>
     * This method is intended for overriding.
     */
    protected void onDestroy() {

    }

    /**
     * A returned state is the state that will be passed to {@link #onCreate} for a new presenter instance after a process restart.
     * <p/>
     * This method is intended for overriding.
     *
     * @param state a non-null bundle which should be used to put presenter's state into.
     */
    protected void onSave(Bundle state) {

    }

    /**
     * This method is being called when a view gets attached to presenter.
     * Normally this happens during {@link android.app.Activity#onResume()}, {@link android.app.Fragment#onResume()}
     * and {@link android.view.View#onAttachedToWindow()}.
     * <p/>
     * This method is intended for overriding.
     *
     * @param view a view that should be taken
     */
    protected void onTakeView(View view) {
    }

    /**
     * This method is being called when a view gets detached from the presenter.
     * Normally this happens during {@link android.app.Activity#onPause()} ()}, {@link android.app.Fragment#onPause()} ()}
     * and {@link android.view.View#onDetachedFromWindow()}.
     * <p/>
     * This method is intended for overriding.
     */
    protected void onDropView() {
    }

    /**
     * A callback to be invoked when a presenter is about to be destroyed.
     */
    public interface OnDestroyListener {
        /**
         * Called before {@link Presenter#onDestroy()}.
         */
        void onDestroy();
    }

    /**
     * Adds a listener observing {@link #onDestroy}.
     *
     * @param listener a listener to add.
     */
    public void addOnDestroyListener(OnDestroyListener listener) {
        mOnDestroyListeners.add(listener);
    }

    /**
     * Removed a listener observing {@link #onDestroy}.
     *
     * @param listener a listener to remove.
     */
    public void removeOnDestroyListener(OnDestroyListener listener) {
        mOnDestroyListeners.remove(listener);
    }

    /**
     * Returns a current view attached to the presenter or null.
     * <p/>
     * View is normally available between
     * {@link android.app.Activity#onResume()} and {@link android.app.Activity#onPause()},
     * {@link android.app.Fragment#onResume()} and {@link android.app.Fragment#onPause()},
     * {@link android.view.View#onAttachedToWindow()} and {@link android.view.View#onDetachedFromWindow()}.
     * <p/>
     * Calls outside of these ranges will return null.
     * Notice here that {@link android.app.Activity#onActivityResult(int, int, Intent)} is called *before*
     * {@link android.app.Activity#onResume()}
     * so you can't use this method as a callback.
     *
     * @return a current attached view.
     */
    @Nullable
    public View getView() {
        return mView;
    }

    /**
     * Initializes the presenter.
     */
    public void create(Bundle bundle) {
        onCreate(bundle);
    }

    /**
     * when * {@link android.app.Activity#onResume()}, {@link android.app.Fragment#onResume()}
     */
    public void resume() {
        onResume();
    }

    /**
     * when * {@link android.app.Activity#onPause()}, {@link android.app.Fragment#onPause()}
     */
    public void pause() {
        onPause();
    }

    public void newIntent(Intent intent) {
        onNewIntent(intent);
    }

    /**
     * Destroys the presenter, calling all {@link Presenter.OnDestroyListener} callbacks.
     */
    public void destroy() {
        for (OnDestroyListener listener : mOnDestroyListeners) {
            listener.onDestroy();
        }
        onDestroy();
    }

    /**
     * Saves the presenter.
     */
    public void save(Bundle bundle) {
        onSave(bundle);
    }

    /**
     * Attaches a view to the presenter.
     *
     * @param view a view to attach.
     */
    public void takeView(View view) {
        mView = view;
        onTakeView(view);
    }

    /**
     * Detaches the presenter from a view.
     */
    public void dropView() {
        onDropView();
        mView = null;
    }

    public void onVisible() {

    }

    public void onInVisible() {

    }
}
