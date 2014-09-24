package com.example.rrich.gridimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.rrich.gridimagesearch.R;
import android.content.Intent;
import android.widget.EditText;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.Arrays;

public class FilterActivity extends Activity {
    private Spinner spSizeFilterValue;
    private Spinner spColorFilterValue;
    private Spinner spTypeFilterValue;
    private EditText etSiteFilterValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Intent i = getIntent();
        String sizeFilterValue = i.getStringExtra(SearchActivity.SIZE_FILTER);
        String colorFilterValue = i.getStringExtra(SearchActivity.COLOR_FILTER);
        String typeFilterValue = i.getStringExtra(SearchActivity.TYPE_FILTER);
        String siteFilterValue = i.getStringExtra(SearchActivity.SITE_FILTER);
        spSizeFilterValue = (Spinner) findViewById(R.id.spSizeFilterValue);
        spColorFilterValue = (Spinner) findViewById(R.id.spColorFilterValue);
        spTypeFilterValue = (Spinner) findViewById(R.id.spTypeFilterValue);
        etSiteFilterValue = (EditText) findViewById(R.id.etSiteFilterValue);
        String[] submitText = getResources().getStringArray(R.array.size_filter_array);
        int spinnerPosition = Arrays.asList(submitText).indexOf(sizeFilterValue);
        spSizeFilterValue.setSelection(spinnerPosition);
        submitText = getResources().getStringArray(R.array.color_filter_array);
        spinnerPosition = Arrays.asList(submitText).indexOf(colorFilterValue);
        spColorFilterValue.setSelection(spinnerPosition);
        submitText = getResources().getStringArray(R.array.type_filter_array);
        spinnerPosition = Arrays.asList(submitText).indexOf(typeFilterValue);
        spTypeFilterValue.setSelection(spinnerPosition);
        etSiteFilterValue.setText(siteFilterValue);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
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

    public void onFilterSave(View v) {
        // Save Button clicked from Filter Options Activity
        Intent i = new Intent();
        i.putExtra(SearchActivity.SIZE_FILTER, spSizeFilterValue.getSelectedItem().toString());
        i.putExtra(SearchActivity.COLOR_FILTER, spColorFilterValue.getSelectedItem().toString());
        i.putExtra(SearchActivity.TYPE_FILTER, spTypeFilterValue.getSelectedItem().toString());
        i.putExtra(SearchActivity.SITE_FILTER, etSiteFilterValue.getText().toString());
        setResult(RESULT_OK, i);
        finish();
    }
}
