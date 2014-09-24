package com.example.rrich.gridimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.rrich.gridimagesearch.R;
import android.widget.ImageView;

import com.example.rrich.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        // 0. For fun, remove action bar on the image display activity:
        getActionBar().hide();
        // 1. Pull out the URL from the intent:
        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
        // 2. Find the image view:
        ImageView ivImageResult = (ImageView) findViewById(R.id.ivImageResult);
        // 3. Load the image url into the image view using Picasso:
        Picasso.with(this).load(result.fullUrl).resize(300,300).centerInside().into(ivImageResult);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
