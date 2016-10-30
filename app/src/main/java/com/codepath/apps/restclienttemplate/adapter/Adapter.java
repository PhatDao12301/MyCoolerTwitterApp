package com.codepath.apps.restclienttemplate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 10/19/2016.
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Tweet> mTweets;
    private Listener mListener;

    public interface Listener {
        void OnLoadMore();
    }

    public Adapter() {
        this.mTweets = new ArrayList<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setTweets(List<Tweet> tweets) {
        mTweets.clear();
        mTweets.addAll(tweets);
        notifyDataSetChanged();
    }

    public void addTweets(List<Tweet> tweets) {
        int startPosition = mTweets.size();
        mTweets.addAll(tweets);
        notifyItemRangeInserted(startPosition, tweets.size());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(itemView);
    }
    ////////////////

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);
        bindNormal(tweet, (ViewHolder) holder);
        if ((position == mTweets.size() - 1) && mListener != null)
            mListener.OnLoadMore();
    }

    private void bindNormal(Tweet tweet, ViewHolder holder) {

        holder.tvUser.setText(tweet.getUserName());
        holder.tvContent.setText(tweet.getContent());
        holder.tvTimestamp.setText(tweet.getTimestamp());

        Glide.with(holder.itemView.getContext())
                .load(tweet.getAvatar())
                .into(holder.ivAvatar);

//        Glide.with(holder.itemView.getContext())
//                .load(tweet.)
//                .into(holder.ivAvatar);
    }


    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.tvContent)
        TextView tvContent;

        @BindView(R.id.ivAvatar)
        ImageView ivAvatar;

        @BindView(R.id.tvUser)
        TextView tvUser;

        @BindView(R.id.tvTimestamp)
        TextView tvTimestamp;

        public ImageView getIvImage() {
            return ivImage;
        }

        public TextView getTvContent() {
            return tvContent;
        }

        public ImageView getIvAvatar() {
            return ivAvatar;
        }

        public TextView getTvUser() {
            return tvUser;
        }

        public TextView getTvTimestamp() {
            return tvTimestamp;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
