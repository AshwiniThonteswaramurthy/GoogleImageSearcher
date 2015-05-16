package com.android.ashwini.googleimagesearcher.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.ashwini.googleimagesearcher.R;
import com.android.ashwini.googleimagesearcher.helpers.NetworkHelper;
import com.android.ashwini.googleimagesearcher.helpers.TouchImageView;
import com.android.ashwini.googleimagesearcher.models.ImageResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FullScreenActivity extends ActionBarActivity {

    private ShareActionProvider shareActionProvider;
    ImageResult image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        final TouchImageView tivFullScreenImage = (TouchImageView) findViewById(R.id.tivFullScreenImage);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        //TODO placeholder
        if (NetworkHelper.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            image = getIntent().getParcelableExtra("image");
            setTitle(Html.fromHtml(image.getTitle()));

            Picasso.with(this)
                    .load(image.getImageUrl())
                    .resize(Integer.parseInt(image.getWidth()), Integer.parseInt(image.getHeight()))
                    .into(tivFullScreenImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            setupShareIntent();
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } else {
            Toast.makeText(this, getString(R.string.no_connection_message), Toast.LENGTH_SHORT).show();
        }

        //When user touches on the screen perform zoom
        tivFullScreenImage.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                tivFullScreenImage.setZoom(tivFullScreenImage);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu full_screen_menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailed_activity_full_screen, menu);

        // ShareActionProvider item
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            //TODO work on this sharing via browser feature
//            case R.id.shareViaBrowser:
//                Intent shareViaBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(image.getImageUrl()));
//                startActivity(shareViaBrowserIntent);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setupShareIntent() {
        Uri uri = getLocalBitmapUri();

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");

        shareActionProvider.setShareIntent(shareIntent);
    }

    private Uri getLocalBitmapUri() {
        ImageView imageView = (ImageView) findViewById(R.id.tivFullScreenImage);
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        //TODO get title and description from google api

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "description");
        return Uri.parse(path);
    }

}
