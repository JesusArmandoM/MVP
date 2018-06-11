package com.example.mcs.mvc.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mcs.mvc.R;
import com.example.mcs.mvc.RepositoryAdapter;
import com.example.mcs.mvc.models.Repository;
import com.example.mcs.mvc.presenter.MainPresenter;

import java.util.List;

import rx.Subscription;

public class MainActivity extends AppCompatActivity implements MainMvpView {


    private MainPresenter presenter;
    private static final String TAG = "MainActivity";
    private Subscription subscription;
    private RecyclerView reposRecycleView;
    private Toolbar toolbar;
    private EditText editTextUsername;
    private ProgressBar progressBar;
    private TextView infoTextView;
    private ImageButton searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         presenter = new MainPresenter();
         presenter.attachView(this);

        progressBar = findViewById(R.id.progress);
        infoTextView = findViewById(R.id.text_info);
        //Set up ToolBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Set up RecyclerView
        reposRecycleView = (RecyclerView) findViewById(R.id.repos_recycler_view);
        setupRecyclerView(reposRecycleView);

        searchButton = (ImageButton) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.loadGithubRepos(editTextUsername.getText().toString());

            }
        });

        editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        editTextUsername.addTextChangedListener(mHideShowButtonTextWatcher);
        editTextUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String username = editTextUsername.getText().toString();
                    if (username.length() > 0) presenter.loadGithubRepos(editTextUsername.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RepositoryAdapter adapter = new RepositoryAdapter();
        adapter.setCallback(new RepositoryAdapter.Callback() {
            @Override
            public void onItemClick(Repository repository) {
                startActivity(RepositoryActivity.newIntent(MainActivity.this, repository));
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private TextWatcher mHideShowButtonTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            searchButton.setVisibility(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @Override
    public void showRepositories(List<Repository> repositories) {

        RepositoryAdapter adapter = (RepositoryAdapter) reposRecycleView.getAdapter();
        adapter.setRepositories(repositories);
        adapter.notifyDataSetChanged();
        reposRecycleView.requestFocus();
        hideSoftKeyboard();
        progressBar.setVisibility(View.INVISIBLE);
        infoTextView.setVisibility(View.INVISIBLE);
        reposRecycleView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(int stringId) {

        progressBar.setVisibility(View.INVISIBLE);
        infoTextView.setVisibility(View.VISIBLE);
        reposRecycleView.setVisibility(View.INVISIBLE);
        infoTextView.setText(getString(stringId));

    }

    @Override
    public void showProgressIndicator() {
        progressBar.setVisibility(View.VISIBLE);
        infoTextView.setVisibility(View.INVISIBLE);
        reposRecycleView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
