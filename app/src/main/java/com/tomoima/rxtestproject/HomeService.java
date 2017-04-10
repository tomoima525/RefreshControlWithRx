package com.tomoima.rxtestproject;


import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

class HomeService {

    private final PublishProcessor<Boolean> kickRefresh = PublishProcessor.create();
    final PublishProcessor<String> result = PublishProcessor.create();

    private  int cnt = 1;

    HomeService() {
        kickRefresh.throttleFirst(10, TimeUnit.SECONDS)
                .observeOn(Schedulers.computation())
                .flatMapCompletable(this::refreshHome)
                .subscribe();
    }

    Completable refreshHome() {
        return refreshHome(false);
    }

    private Completable refreshHome(boolean isLazy){
        return postRequest(isLazy).doAfterSuccess(result::onNext).ignoreElement();
    }

    Completable lazyRefreshHome(long time, TimeUnit timeUnit) {
        return Flowable.just(true)
                .delay(time, timeUnit)
                .doOnNext(kickRefresh::onNext).ignoreElements();
    }

    Completable triggerRefreshHome() {
        kickRefresh.onNext(false);
        return Completable.complete();
    }

    private Maybe<String> postRequest(boolean isLazy){
        return Maybe.fromCallable(() -> {
            if(isLazy) {
                cnt++;
                return "Is lazy throttle " + cnt;
            } else {
                cnt--;
                return "Is throttle " + cnt;
            }
        });
    }
}
