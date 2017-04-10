package com.tomoima.rxtestproject;

import android.support.annotation.VisibleForTesting;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

class HomeViewModel implements Disposable{

    private enum Action {
        NORMAL, TRIGGER, LAZY
    }

    private HomeService homeService;

    private PublishProcessor<Action> action = PublishProcessor.create();
    private CompositeDisposable disposables = new CompositeDisposable();

    HomeViewModel() {
        homeService = new HomeService();

        // Specify state change from Action
        disposables.add(
                action.observeOn(Schedulers.io())
                        .flatMapCompletable(action -> {
                            switch (action) {
                                case NORMAL:
                                    return normalRefreshHome();
                                case TRIGGER:
                                    return triggerRefreshHome();
                                case LAZY:
                                    return lazyRefreshHome();
                                default:
                                    return Completable.complete();
                            }
                        }).subscribe());
    }

    @Override
    public void dispose() {
        disposables.clear();
    }

    @Override
    public boolean isDisposed() {
        return disposables.isDisposed();
    }


    Completable refresh() {
        this.action.onNext(Action.NORMAL);
        return Completable.complete();
    }

    Completable triggerRefresh() {
        this.action.onNext(Action.TRIGGER);
        return Completable.complete();
    }

    Completable lazyRefresh() {
        this.action.onNext(Action.LAZY);
        return Completable.complete();
    }

    /**
     * Regular refresh request. It will request refresh anytime
     */
    @VisibleForTesting
    Completable normalRefreshHome(){
        return homeService.refreshHome();
    }


    /**
     * Lazy refresh request. It will be canceled once other refresh request occurs
     */
    @VisibleForTesting
    Completable lazyRefreshHome() {
        return homeService.lazyRefreshHome(5, TimeUnit.SECONDS);
    }

    /**
     * Triggers refresh request. If previous request has been done in recent 10 second, it will cancel request; otherwise request the refresh
     */
    @VisibleForTesting
    Completable triggerRefreshHome() {
        return homeService.triggerRefreshHome();
    }

    Flowable<String> observeResult() {
        return homeService.result;
    }


}