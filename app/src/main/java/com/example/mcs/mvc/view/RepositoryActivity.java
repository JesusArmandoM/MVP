package com.example.mcs.mvc.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcs.mvc.ArchiApplication;
import com.example.mcs.mvc.R;
import com.example.mcs.mvc.models.GithubService;
import com.example.mcs.mvc.models.Repository;
import com.example.mcs.mvc.models.User;
import com.example.mcs.mvc.presenter.RepositoryPresenter;
import com.squareup.picasso.Picasso;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RepositoryActivity extends AppCompatActivity implements RepositoryMvpView{

    private static final String EXTRA_REPOSITORY = "EXTRA_REPOSITORY";
    private static final String TAG = "RepositoryActivity";
    private RepositoryPresenter repositoryPresenter;

    private Toolbar toolbar;
    private TextView descriptionText;
    private TextView homepageText;
    private TextView languageText;
    private TextView forkText;
    private TextView ownerNameText;
    private TextView ownerEmailText;
    private TextView ownerLocationText;
    private ImageView ownerImage;
    private View ownerLayout;

    private Subscription subscription;

    public static Intent newIntent(Context context, Repository repository) {
        Intent intent = new Intent(context, RepositoryActivity.class);
        intent.putExtra(EXTRA_REPOSITORY, repository);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);

        repositoryPresenter = new RepositoryPresenter();

        repositoryPresenter.attachView(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        descriptionText = (TextView) findViewById(R.id.text_repo_description);
        homepageText = (TextView) findViewById(R.id.text_homepage);
        languageText = (TextView) findViewById(R.id.text_language);
        forkText = (TextView) findViewById(R.id.text_fork);
        ownerNameText = (TextView) findViewById(R.id.text_owner_name);
        ownerEmailText = (TextView) findViewById(R.id.text_owner_email);
        ownerLocationText = (TextView) findViewById(R.id.text_owner_location);
        ownerImage = (ImageView) findViewById(R.id.image_owner);
        ownerLayout = findViewById(R.id.layout_owner);

        Repository repository = getIntent().getParcelableExtra(EXTRA_REPOSITORY);
        bindRepositoryData(repository);

        repositoryPresenter.loadFullUser(repository.owner.url);

    }







    private void bindRepositoryData(final Repository repository) {
        setTitle(repository.name);

        descriptionText.setText(repository.description);
        homepageText.setText(repository.homepage);
        homepageText.setVisibility(repository.hasHomepage() ? View.VISIBLE : View.GONE);
        languageText.setText(getString(R.string.text_language, repository.language));
        languageText.setVisibility(repository.hasLanguage() ? View.VISIBLE : View.GONE);
        forkText.setVisibility(repository.isFork() ? View.VISIBLE : View.GONE);
        //Preload image for user because we already have it before loading the full user
        Picasso.with(this)
                .load(repository.owner.avatarUrl)
                .placeholder(R.drawable.placeholder)
                .into(ownerImage);
    }

    @Override
    protected void onDestroy() {

        repositoryPresenter.detachView();
        super.onDestroy();

    }



    @Override
    public void showUser(User owner) {

        ownerLayout.setVisibility(View.VISIBLE);

        ownerNameText.setText(owner.name);
        ownerEmailText.setText(owner.email);
        ownerEmailText.setVisibility(owner.hasEmail() ? View.VISIBLE : View.GONE);
        ownerLocationText.setText(owner.location);
        ownerLocationText.setVisibility(owner.hasLocation() ? View.VISIBLE : View.GONE);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
