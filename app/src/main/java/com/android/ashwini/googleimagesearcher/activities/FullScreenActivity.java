package com.android.ashwini.googleimagesearcher.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.ashwini.googleimagesearcher.R;
import com.android.ashwini.googleimagesearcher.helpers.NetworkHelper;
import com.android.ashwini.googleimagesearcher.helpers.TouchImageView;
import com.squareup.picasso.Picasso;

public class FullScreenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //TODO user can zoom or pan on images using TouchImageView
        final TouchImageView tivFullScreenImage = (TouchImageView) findViewById(R.id.tivFullScreenImage);

        if (NetworkHelper.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            Bundle data = getIntent().getExtras();
            String imageUrl = data.getString("url");
            int width = data.getInt("width");
            int height = data.getInt("height");

            Picasso.with(this)
                    .load(imageUrl)
                    .resize(width, height)
                    .into(tivFullScreenImage);
        } else {
            Toast.makeText(this, getString(R.string.no_connection_message), Toast.LENGTH_SHORT).show();
        }

        //When user touches on the screen return to the main screen
        tivFullScreenImage.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                tivFullScreenImage.setZoom(tivFullScreenImage);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_scren, menu);
        return true;
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

}
