package com.android.ashwini.googleimagesearcher.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageResult{

    private String imageUrl;
    private String thumbnailUrl;
    private String title;
    private String width;
    private String height;

    public ImageResult(JSONObject jsonObject) {
        this.imageUrl = jsonObject.optString("url");
        this.thumbnailUrl = jsonObject.optString("tbUrl");
        this.title = jsonObject.optString("title");
        this.width = jsonObject.optString("width");
        this.height = jsonObject.optString("height");
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray imageJsonArray)
    {
        ArrayList<ImageResult> results = new ArrayList<>();
        for(int i = 0 ; i < imageJsonArray.length(); i++){
            results.add(new ImageResult(imageJsonArray.optJSONObject(i)));
        }
        return results;
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

}
