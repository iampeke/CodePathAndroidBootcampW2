package com.example.rrich.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.example.rrich.gridimagesearch.R;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rrich.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rrich on 9/20/14.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Populate data into a custom template: item_image_result.xml

        ImageResult imageInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        // clear out image from last time:
        ivImage.setImageResource(0);
        // Populate title and remote download the image url:
        tvTitle.setText(Html.fromHtml(imageInfo.title));
        // Remotely download the image data in the background (Picasso):
        Picasso.with(getContext()).load(imageInfo.thumbUrl).resize(200,200).into(ivImage);
        // Return the completed view to be displayed:
        return convertView;
    }
}
