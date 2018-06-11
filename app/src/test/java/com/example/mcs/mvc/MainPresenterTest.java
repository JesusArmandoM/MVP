package com.example.mcs.mvc;


import com.example.mcs.mvc.models.GithubService;
import com.example.mcs.mvc.presenter.MainPresenter;
import com.example.mcs.mvc.view.MainMvpView;

import org.apache.tools.ant.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)

public class MainPresenterTest {

    MainMvpView mainMvpView;
    MainPresenter mainPresenter;
    GithubService githubService;

    @Before
    public void setup()
    {

        ArchiApplication application = (ArchiApplication) RuntimeEnvironment.application;

        githubService = mock (GithubService.class);

        application.setGithubService(githubService);
        application.setDefaultSubscribeScheduler(Schedulers.immediate());


        mainMvpView = mock(MainMvpView.class);

        mainPresenter = new MainPresenter();

        when(mainMvpView.getContext()).thenReturn(application);

        mainPresenter.attachView(mainMvpView);

    }

    @After
    public void clean()
    {

    }

    @Test
    public void test()
    {




        //general Repositorios

        //si no esta vacio pasa el test

    }


}
