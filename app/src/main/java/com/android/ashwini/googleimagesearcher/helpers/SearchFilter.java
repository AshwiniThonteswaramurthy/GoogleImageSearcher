package com.android.ashwini.googleimagesearcher.helpers;

import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class SearchFilter {

    private final static String IMAGE_SEARCH_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images";
    private final static String IMAGE_SEARCH_VERSION_VALUE = "1.0";
    private final static String IMAGE_SEARCH_VERSION_KEY = "v";
    private final static String IMAGE_SEARCH_RESULT_SIZE_KEY = "rsz";
    private final static String IMAGE_SEARCH_RESULT_SIZE_VALUE = "8";
    public final static String IMAGE_SEARCH_QUERY_KEY = "q";
    public static final String IMAGE_SIZE_KEY = "imgsz";
    public static final String IMAGE_COLOR_KEY = "imgcolor";
    public static final String IMAGE_TYPE_KEY = "imgtype";
    public static final String IMAGE_SEARCH_SITE_KEY = "as_sitesearch";
    public static final String IMAGE_SEARCH_START_INDEX_KEY = "start";

    private HashMap<String, String> parameters = new HashMap<>();
    private static SearchFilter searchFilter = new SearchFilter();

    public static SearchFilter getInstance() {
        return searchFilter;
    }

    private SearchFilter() {
        addDefaults();
        parameters.put(IMAGE_COLOR_KEY, null);
        parameters.put(IMAGE_SIZE_KEY, null);
        parameters.put(IMAGE_TYPE_KEY, null);
        parameters.put(IMAGE_SEARCH_SITE_KEY, null);
    }

    public void put(String key, String value) {
        parameters.put(key, value);
    }

    public void put(Map<String, String> additionalParameters) {
        parameters.putAll(additionalParameters);
    }

    public String get(String key) {
        return parameters.get(key);
    }

    public String delete(String key) {
        return parameters.remove(key);
    }

    public boolean containsKey(String key) {
        return parameters.get(key) != null;
    }

    public Map<String, String> getAll() {
        return parameters;
    }

    public Map<String, String> addDefaults() {
        parameters.put(IMAGE_SEARCH_VERSION_KEY, IMAGE_SEARCH_VERSION_VALUE);
        parameters.put(IMAGE_SEARCH_START_INDEX_KEY, String.valueOf(0));
        parameters.put(IMAGE_SEARCH_RESULT_SIZE_KEY, IMAGE_SEARCH_RESULT_SIZE_VALUE);
        return parameters;
    }

    public void nextPage() {
        parameters.put(IMAGE_SEARCH_START_INDEX_KEY, calculatePage());
    }

    public String buildSearchQuery() {
        Uri.Builder searchQueryBuilder = Uri.parse(IMAGE_SEARCH_BASE_URL).buildUpon();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String value = entry.getValue();
            if (validateString(value)) {
                if (!value.equals("all")) {
                    searchQueryBuilder.appendQueryParameter(entry.getKey(), value);
                }
            }
        }
        return searchQueryBuilder.build().toString();
    }

    private String calculatePage() {
        int currentPage = Integer.parseInt(parameters.get(IMAGE_SEARCH_START_INDEX_KEY));
        int pageSize = Integer.parseInt(parameters.get(IMAGE_SEARCH_RESULT_SIZE_KEY));
        int total = currentPage + pageSize;
        return String.valueOf(total);
    }

    private boolean validateString(String value) {
        boolean result = false;
        if (!TextUtils.isEmpty(value))
            result = true;
        else if (value != null)
            result = true;

        return result;

    }

}

