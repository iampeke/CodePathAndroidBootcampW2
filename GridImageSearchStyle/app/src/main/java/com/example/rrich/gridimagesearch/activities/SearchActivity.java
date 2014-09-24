package com.example.rrich.gridimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.util.Log;
import android.content.Intent;
import android.widget.Toast;
import android.widget.ListView;

import com.example.rrich.gridimagesearch.adapters.ImageResultsAdapter;
import com.example.rrich.gridimagesearch.models.ImageResult;
import com.example.rrich.gridimagesearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends Activity {
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    public static final String SIZE_FILTER = "size_filter";
    public static final String COLOR_FILTER = "color_filter";
    public static final String TYPE_FILTER = "type_filter";
    public static final String SITE_FILTER = "site_filter";
    private String sizeFilterValue;
    private String colorFilterValue;
    private String typeFilterValue;
    private String siteFilterValue;
    private String searchUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        sizeFilterValue = "";
        colorFilterValue = "";
        typeFilterValue = "";
        siteFilterValue = "";
        // Creates the data source:
        imageResults = new ArrayList<ImageResult>();
        // Attaches the data source to an adapter:
        aImageResults = new ImageResultsAdapter(this, imageResults);
        // Link the adapter to the adapter view (the grid view, in this case):
        gvResults.setAdapter(aImageResults);
        // Attach the listener to the AdapterView onCreate
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        AsyncHttpClient client = new AsyncHttpClient();
        //Toast.makeText(this, "Offset: "+offset, Toast.LENGTH_SHORT).show();
        client.get(searchUrl + "&start=" + (offset-1)*8, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Decode JSON into a more reasonable form...
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    // Not ideal to start parsing everything here (was mentioned in comments for last week's work)
                    // Should be contained within the model - Should be able to create itself based on the JSON
                    // imageResults.clear(); // Clear existing images from the array (for NEW search -- not for PAGENATION)
                    // imageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    // This organization keeps the controller clean and concise by moving bulk into model and utilities
                    // Notify the Adapter that there is new content:
                    // Option 1:
                    // aImageResults.notifyDataSetChanged();
                    // New way: add images through the adapter directly...
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    // When you make changes to the adapter, it does also modify the underlying data.


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", imageResults.toString());
            }
        });
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Each particular item's click event from Grid View:
                // Want to launch new activity and pass along enough info to display image properly
                // Launch the image display activity:
                // 1. Create an Intent:
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                // this does not work here because we are in the midst of an anonymous class...
                // we need to be more specific: SearchActivity.this
                // 2. Get the image result to display:
                ImageResult result = imageResults.get(position);
                // 3. Pass the image result into the intent:
                i.putExtra("result", result);
                // needs to be serializable or parcelable: **going to use serializable
                // 4. Launch the new activity:
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.miFilter) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onImageSearch(View v) {
        String query = etQuery.getText().toString();
        // Toast.makeText(this, "Search for: " + query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        // https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
        searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        // Adding advanced filter options:
        if (this.sizeFilterValue != null && !sizeFilterValue.isEmpty()) {
            searchUrl = searchUrl + "&imgsz=" + sizeFilterValue;
        }
        if (this.colorFilterValue != null && !colorFilterValue.isEmpty()) {
            searchUrl = searchUrl + "&imgcolor=" + colorFilterValue;
        }
        if (this.typeFilterValue != null && !typeFilterValue.isEmpty()) {
            searchUrl = searchUrl + "&imgtype=" + typeFilterValue;
        }
        if (this.siteFilterValue != null && !siteFilterValue.isEmpty()) {
            searchUrl = searchUrl + "&as_sitesearch=" + siteFilterValue;
        }
        //Toast.makeText(this, "Search URL: " + searchUrl, Toast.LENGTH_SHORT).show();
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Decode JSON into a more reasonable form...
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    // Not ideal to start parsing everything here (was mentioned in comments for last week's work)
                    // Should be contained within the model - Should be able to create itself based on the JSON
                    imageResults.clear(); // Clear existing images from the array (for NEW search -- not for PAGENATION)
                    // imageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    // This organization keeps the controller clean and concise by moving bulk into model and utilities
                    // Notify the Adapter that there is new content:
                    // Option 1:
                    // aImageResults.notifyDataSetChanged();
                    // New way: add images through the adapter directly...
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    // When you make changes to the adapter, it does also modify the underlying data.


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", imageResults.toString());
            }
        });
    }

    public void onFilterMenuItem(MenuItem mi) {
        // handle menu click here:
        // Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

        // Create the intent:
        Intent i = new Intent(this, FilterActivity.class); // explicit intent

        //Define the parameters ("extras" encoded into a bundle):
        i.putExtra(SIZE_FILTER, sizeFilterValue);
        i.putExtra(COLOR_FILTER, colorFilterValue);
        i.putExtra(TYPE_FILTER, typeFilterValue);
        i.putExtra(SITE_FILTER, siteFilterValue);

        // Execute the intent:
        startActivityForResult(i, 50); //Last number could be a constant that describes that actual payload type

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 50) { // Check for the correct payload
            if (resultCode == RESULT_OK) { // Checks if the request was correct
                //Toast.makeText(this, "Filter Changed", Toast.LENGTH_SHORT).show();
                sizeFilterValue = data.getStringExtra(SIZE_FILTER);
                colorFilterValue = data.getStringExtra(COLOR_FILTER);
                typeFilterValue = data.getStringExtra(TYPE_FILTER);
                siteFilterValue = data.getStringExtra(SITE_FILTER);
            }
        }
    }


}
