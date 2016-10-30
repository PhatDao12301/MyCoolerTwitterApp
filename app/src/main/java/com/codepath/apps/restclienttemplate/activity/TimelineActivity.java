package com.codepath.apps.restclienttemplate.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.adapter.Adapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private Adapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private Gson mGson;
    private List<Tweet> tweets;
    private int mPage = 1;

    @BindView(R.id.pbLoading)
    View pbLoading;

    @BindView(R.id.pbLoadMore)
    ProgressBar pbLoadMore;

    @BindView(R.id.rvTweet)
    RecyclerView rvTweet;

    @BindView(R.id.tvPoint)
    TextView tvPoint;

    //@BindView(R.id.btnCancel)
    private ImageView ivCancel;

    //@BindView(R.id.btnTweet)
    private ImageView ivTweet;

    //@BindView(R.id.etCompose)
    private EditText etCompose;

    private interface Listener {
        //void onResult(JSONObject jsonObject);
        void onResult(List<Tweet> tweets);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        mGson = new Gson();

        setUpViews();
        load();
    }

    private void setId(View popUpView) {
        ivCancel = (ImageView) popUpView.findViewById(R.id.ivCancel);
        ivTweet = (ImageView) popUpView.findViewById(R.id.ivTweet);
        etCompose = (EditText) popUpView.findViewById(R.id.etCompose);
    }

    private void setUpViews() {
        mAdapter = new Adapter();
        mAdapter.setListener(new Adapter.Listener() {
            @Override
            public void OnLoadMore() {
                loadMore();
            }
        });

        mLayoutManager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);
        rvTweet.setLayoutManager(mLayoutManager);
        rvTweet.setAdapter(mAdapter);
    }

    private void loadMore() {

        pbLoadMore.setVisibility(View.VISIBLE);
        mPage += 1;
        fetchTweet(new Listener() {

            @Override
            public void onResult(List<Tweet> tweets) {
                mAdapter.addTweets(tweets);
            }
        });
    }

    private void fetchTweet(final Listener listener) {
        RestApplication.getRestClient().getHomeTimeline(mPage, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //listener.onResult(response);
                handleComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                tweets = mGson.fromJson(response.toString(),
                        new TypeToken<List<Tweet>>() {
                        }.getType());
                listener.onResult(tweets);
                handleComplete();
            }

        });
    }

    private void load() {
//        pbLoading.setVisibility(View.VISIBLE);
        mPage = 1;
        fetchTweet(new Listener() {
            @Override
            public void onResult(List<Tweet> tweets) {
                mAdapter.setTweets(tweets);
            }
        });
    }

    private void handleComplete() {
        pbLoading.setVisibility(View.GONE);
        pbLoadMore.setVisibility(View.GONE);
    }

    private void composeTweet() {
        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.activity_compose, null);
        setId(popupView);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.update();

        ivCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        ivTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTweet();
                popupWindow.dismiss();
                load();
            }
        });

        popupWindow.showAsDropDown(tvPoint, 100, 150);

    }

    private void postTweet() {
        RestApplication.getRestClient().postTweet(etCompose.getText().toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(TimelineActivity.this, "Post Suceeded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(TimelineActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(this, "refreshing", Toast.LENGTH_SHORT).show();
                load();
                break;
            case R.id.action_tweet:
                Toast.makeText(this, "tweeting", Toast.LENGTH_SHORT).show();
                composeTweet();
//                load();
                break;
            default:
                break;
        }
        return true;
    }
}
