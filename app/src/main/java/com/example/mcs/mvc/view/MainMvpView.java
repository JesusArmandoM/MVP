package com.example.mcs.mvc.view;

import com.example.mcs.mvc.models.Repository;

import java.util.List;

public interface MainMvpView extends MvpView {

    void showRepositories(List<Repository> repositories);

    void showMessage(int stringId);

    void showProgressIndicator();


}
