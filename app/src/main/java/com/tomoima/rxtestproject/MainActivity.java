package com.tomoima.rxtestproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.request_now)
    Button request;

    @BindView(R.id.trigger_reguest)
    Button triggerRequest;

    @BindView(R.id.request_lazy)
    Button reguestLazy;

    HomeViewModel homeViewModel;

    CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        homeViewModel = new HomeViewModel();

        disposables.addAll(
                RxView.clicks(request)
                        .doOnNext(clicked -> Timber.d("====== normalRefreshHome !!"))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .flatMapCompletable(clicked -> homeViewModel.refresh())
                        .subscribe(() -> {
                        }, e -> {
                        }),

                RxView.clicks(triggerRequest)
                        .doOnNext(clicked -> Timber.d("====== trigger refresh !!"))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .flatMapCompletable(clicked -> homeViewModel.triggerRefresh())
                        .subscribe(() -> {
                        }, e -> {
                        }),

                RxView.clicks(reguestLazy)
                        .doOnNext(clicked -> Timber.d("====== lazy refresh !!"))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .flatMapCompletable(clicked -> homeViewModel.lazyRefresh())
                        .subscribe(() -> {
                        }, e -> {
                        }),
                homeViewModel.observeResult()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            Timber.d("========== " + result);
                        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
        homeViewModel.dispose();
    }
}
