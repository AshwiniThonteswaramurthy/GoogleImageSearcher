package com.android.ashwini.googleimagesearcher.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageResult implements Parcelable
{

    private String imageUrl;
    private String thumbnailUrl;
    private String title;
    private String width;
    private String height;
    private String thumbnailWidth;
    private String thumbnailHeight;

    public String getThumbnailWidth() {
        return thumbnailWidth;
    }

    public String getThumbnailHeight() {
        return thumbnailHeight;
    }

    public ImageResult() {
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray imageJsonArray)
    {
        ArrayList<ImageResult> results = new ArrayList<>();
        for(int i = 0 ; i < imageJsonArray.length(); i++){
            results.add(getImageResult(imageJsonArray.optJSONObject(i)));
        }
        return results;
    }

    private static ImageResult getImageResult(JSONObject jsonImageResult) {
        ImageResult imageResult = new ImageResult();
        imageResult.imageUrl = jsonImageResult.optString("url");
        imageResult.thumbnailUrl = jsonImageResult.optString("tbUrl");
        imageResult.title = jsonImageResult.optString("title");
        imageResult.width = jsonImageResult.optString("width");
        imageResult.height = jsonImageResult.optString("height");
        imageResult.thumbnailWidth = jsonImageResult.optString("tbWidth");
        imageResult.thumbnailHeight = jsonImageResult.optString("tbHeight");
        return imageResult;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }


    @Override
    public String toString() {
        return "ImageResult{" +
                "imageUrl='" + imageUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", title='" + title + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.title);
        dest.writeString(this.width);
        dest.writeString(this.height);
        dest.writeString(this.thumbnailWidth);
        dest.writeString(this.thumbnailHeight);
    }

    private ImageResult(Parcel in) {
        this.imageUrl = in.readString();
        this.thumbnailUrl = in.readString();
        this.title = in.readString();
        this.width = in.readString();
        this.height = in.readString();
        this.thumbnailWidth = in.readString();
        this.thumbnailHeight = in.readString();
    }

    public static final Creator<ImageResult> CREATOR = new Creator<ImageResult>() {
        public ImageResult createFromParcel(Parcel source) {
            return new ImageResult(source);
        }

        public ImageResult[] newArray(int size) {
            return new ImageResult[size];
        }
    };
}
