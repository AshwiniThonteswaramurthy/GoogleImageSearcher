package com.android.ashwini.googleimagesearcher.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.ashwini.googleimagesearcher.R;
import com.android.ashwini.googleimagesearcher.activities.FullScreenActivity;
import com.android.ashwini.googleimagesearcher.models.ImageResult;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    private Activity activity;

    public ImageResultsAdapter(Context context, List<ImageResult> images, Activity activity) {
        super(context, R.layout.item_image_result, images);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ImageResult imageResult = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }

        final DynamicHeightImageView ivImage = (DynamicHeightImageView) convertView.findViewById(R.id.ivImage);

        TextView tvImageTitle = (TextView) convertView.findViewById(R.id.tvImageTitle);
        tvImageTitle.setText(Html.fromHtml(imageResult.getTitle()));

        Picasso.with(getContext())
                .load(imageResult.getThumbnailUrl())
                .resize(Integer.parseInt(imageResult.getThumbnailWidth()), Integer.parseInt(imageResult.getThumbnailHeight()))
                .into(ivImage);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FullScreenActivity.class);
                intent.putExtra("image", imageResult);
                activity.startActivityForResult(intent, 200);
            }
        });

        return convertView;
    }
}
