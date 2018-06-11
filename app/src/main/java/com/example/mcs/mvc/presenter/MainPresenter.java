package com.example.mcs.mvc.presenter;

import android.view.View;

import com.example.mcs.mvc.ArchiApplication;
import com.example.mcs.mvc.R;
import com.example.mcs.mvc.models.GithubService;
import com.example.mcs.mvc.models.Repository;
import com.example.mcs.mvc.view.MainMvpView;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainPresenter implements Presenter<MainMvpView> {

    public static String TAG = "MainPresenter";

    private MainMvpView mainMvpView;
    private Subscription subscription;
    private List<Repository> repositories;


    @Override
    public void attachView(MainMvpView view) { this.mainMvpView = view; }

    @Override
    public void detachView() {

        this.mainMvpView = null;
        if(subscription != null) subscription.unsubscribe();
    }


    public void loadGithubRepos(String userNameEntered)
    {
        String userName = userNameEntered.trim();

        if (userName.isEmpty()) return;
        mainMvpView.showProgressIndicator();

        if(subscription != null) subscription.unsubscribe();

        ArchiApplication application = ArchiApplication.get(mainMvpView.getContext());
        GithubService githubService = application.getGithubService();
        subscription = githubService.publicRepositories(userName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Repository>>() {
                    @Override
                    public void onCompleted() {

                        if(!repositories.isEmpty())
                        {
                            mainMvpView.showRepositories(repositories);

                        }else
                        {
                            mainMvpView.showMessage(R.string.text_empty_repos);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if(isHttp404(e))
                        {
                            mainMvpView.showMessage(R.string.error_username_not_found);

                        }else
                        {
                            mainMvpView.showMessage(R.string.error_loading_repos);

                        }

                    }

                    @Override
                    public void onNext(List<Repository> repositories) {

                      MainPresenter.this.repositories = repositories;
                    }
                });

    }


    private static boolean isHttp404(Throwable error)
    {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }

}
