package com.example.mcs.mvc.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();


}
