package com.loganfreeman.utahfishing.common.utils;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

/**
 * Created by HugoXie on 16/6/25.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
public class RxDrawer {

    private static final float OFFSET_THRESHOLD = 0.03f;

    public static Observable<Void> close(final DrawerLayout drawer) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                drawer.closeDrawer(GravityCompat.START);
                DrawerLayout.DrawerListener listener = new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        if (slideOffset < OFFSET_THRESHOLD) {
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        }
                    }
                };
                drawer.addDrawerListener(listener);
                subscriber.add(new MainThreadSubscription() {
                    @Override
                    protected void onUnsubscribe() {
                        drawer.removeDrawerListener(listener);
                    }
                });
            }
        });
    }
}
