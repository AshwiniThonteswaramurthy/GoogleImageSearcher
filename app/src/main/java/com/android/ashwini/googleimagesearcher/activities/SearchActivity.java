package com.android.ashwini.googleimagesearcher.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.ashwini.googleimagesearcher.R;
import com.android.ashwini.googleimagesearcher.adapters.ImageResultsAdapter;
import com.android.ashwini.googleimagesearcher.fragments.SettingsDialog;
import com.android.ashwini.googleimagesearcher.helpers.EndlessScrollListener;
import com.android.ashwini.googleimagesearcher.helpers.NetworkHelper;
import com.android.ashwini.googleimagesearcher.helpers.SearchFilter;
import com.android.ashwini.googleimagesearcher.models.ImageResult;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


public class SearchActivity extends ActionBarActivity implements SettingsDialog.OnSettingsSaveListener {

    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter adapter;
    private SearchFilter searchFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("Google image search");
        Activity activity = this;
        imageResults = new ArrayList<>();
        searchFilter = SearchFilter.getInstance();
        adapter = new ImageResultsAdapter(getBaseContext(), imageResults, activity);

        StaggeredGridView gvImageSearchResult = (StaggeredGridView) findViewById(R.id.sgvImageSearchResult);
        gvImageSearchResult.setAdapter(adapter);

        //Endless scrolling
        endlessScrolling(gvImageSearchResult);
        //TODO ViewHolder Pattern

        displayDetailedViewOfImage(gvImageSearchResult);
    }

    private void displayDetailedViewOfImage(StaggeredGridView gvImageSearchResult) {
        gvImageSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (NetworkHelper.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    ImageResult image = (ImageResult) adapterView.getItemAtPosition(position);
                    Intent intent = new Intent(SearchActivity.this, FullScreenActivity.class);
                    intent.putExtra("image", image);
                    startActivity(intent);
                }
            }
        });
    }

    private void endlessScrolling(StaggeredGridView gvImageSearchResult) {
        gvImageSearchResult.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                imageResults.clear();
                adapter.notifyDataSetChanged();
                searchFilter.put(SearchFilter.IMAGE_SEARCH_QUERY_KEY, query);
                searchFilter.put(SearchFilter.IMAGE_SEARCH_START_INDEX_KEY, String.valueOf(0));
                performGoogleSearch(searchFilter.getAll());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //adds more data to the adapter to loading pages
    public void paginateImageSearchResult() {
        searchFilter.nextPage();
        performGoogleSearch(searchFilter.getAll());
    }

    public void performGoogleSearch(Map<String, String> params) {

        if (params.get(SearchFilter.IMAGE_SEARCH_QUERY_KEY) != null) {
            //check for end of limit 8 pages(each page 8 images)
            int MAX_IMAGES_TO_BE_LOADED = 64;
            if (adapter.getCount() == MAX_IMAGES_TO_BE_LOADED) {
                //no more data to load so return
                return;
            }

            if (NetworkHelper.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
                AsyncHttpClient client = new AsyncHttpClient();
                String url = searchFilter.buildSearchQuery();
                Log.d("DEBUG", url);

                client.get(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray imageResultsJson = response.optJSONObject("responseData").optJSONArray("results");
                        adapter.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(getBaseContext(), getString(R.string.failure_message), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        }

    }

    public void openSettings(MenuItem item) {
        DialogFragment settingsDialog = new SettingsDialog();
        settingsDialog.show(getFragmentManager(), "settings dialog");
    }

    @Override
    public void onSave(SearchFilter searchFilter) {
        this.searchFilter.put(searchFilter.getAll());
        performGoogleSearch(this.searchFilter.getAll());
    }

    public void customLoadMoreDataFromApi(int offset) {
        if (offset == 0) {
            adapter.clear();
        }
        paginateImageSearchResult();
    }


}
