package com.example.mcs.mvc.presenter;

import com.example.mcs.mvc.ArchiApplication;
import com.example.mcs.mvc.models.GithubService;
import com.example.mcs.mvc.models.User;
import com.example.mcs.mvc.view.RepositoryMvpView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RepositoryPresenter implements Presenter<RepositoryMvpView>{

    RepositoryMvpView repositoryMvpView;
    Subscription subscription;

    @Override
    public void attachView(RepositoryMvpView view) {
        repositoryMvpView = view;
    }

    @Override
    public void detachView() {
        repositoryMvpView = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadFullUser(String url) {
        ArchiApplication application = ArchiApplication.get(repositoryMvpView.getContext());
        GithubService githubService = application.getGithubService();
        subscription = githubService.userFromUrl(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {

                        repositoryMvpView.showUser(user);

                    }

                });

    }


}
