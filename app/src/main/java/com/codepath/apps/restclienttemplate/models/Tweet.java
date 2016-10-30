package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 10/29/2016.
 */

public class Tweet implements Parcelable{
    @SerializedName("user")
    private User user;

    @SerializedName("text")
    private String content;

    @SerializedName("created_at")
    private String timestamp;

    protected Tweet() {
    }

    protected Tweet(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        content = in.readString();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet t = new Tweet();
        // Deserialize json into object fields
        try {
            t.user.name = jsonObject.getString("name");
            t.content = jsonObject.getString("text");
            t.user.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return t;
    }

    public static List<Tweet> fromJson(JSONArray jsonArray) {
        JSONObject tweetJson;
        List<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
        // Process each result in json array, decode and convert to object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }

    public User getUser() {
        return user;
    }

    public String getAvatar(){
        return this.user.getProfileImageUrl();
    }

    public String getUserName(){
        return this.user.getName();
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(user, i);
        parcel.writeString(content);
    }

    private static class User implements Parcelable{
        @SerializedName("name")
        private String name;

        @SerializedName("profile_image_url")
        private String profileImageUrl;

        protected User(Parcel in) {
            name = in.readString();
            profileImageUrl = in.readString();
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };

        public String getName() {
            return name;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(name);
            parcel.writeString(profileImageUrl);
        }
    }
}
