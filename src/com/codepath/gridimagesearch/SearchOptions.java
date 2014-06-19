package com.codepath.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SearchOptions extends Activity {
	
	private EditText etSiteFilter;
	private Spinner imgSizeSpinner;
	private Spinner imgColorSpinner;
	private Spinner imgTypeSpinner;
	
	private String imgSize;
	private String imgColor;
	private String imgType;
/*
	public void onItemSelected(AdapterView<?> imgSizeSpinner, View v, int pos, long id) {
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos)
	}
	
	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}
	*/
	
	public void onSaveChanges(View v) {
				
		Intent data = new Intent();
		
		// Pass relevant data back as a result
		data.putExtra("site_filter", etSiteFilter.getText().toString());
		data.putExtra("img_size", imgSizeSpinner.getSelectedItem().toString());
		data.putExtra("img_color", imgColorSpinner.getSelectedItem().toString());
		data.putExtra("img_type", imgTypeSpinner.getSelectedItem().toString());
		
		// Activity finished ok, return the data
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish(); // closes the activity, pass data to parent
	}
	
	public void setSpinnerToValue(Spinner spinner, String value) {
		int index = 0;
		SpinnerAdapter adapter = spinner.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).equals(value)) {
				index = i;
			}
		}
		spinner.setSelection(index);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_options);
		
		imgSizeSpinner = (Spinner) findViewById(R.id.imgSizeSpinner);
		//imgSizeSpinner.setOnItemSelectedListener(this);
		
		imgColorSpinner = (Spinner) findViewById(R.id.imgColorSpinner);
		//imgSizeSpinner.setOnItemSelectedListener(this);
		
		imgTypeSpinner = (Spinner) findViewById(R.id.imgTypeSpinner);
		//imgSizeSpinner.setOnItemSelectedListener(this);
		
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		
		setSpinnerToValue(imgSizeSpinner, getIntent().getStringExtra("img_size"));
		setSpinnerToValue(imgColorSpinner, getIntent().getStringExtra("img_color"));
		setSpinnerToValue(imgTypeSpinner, getIntent().getStringExtra("img_type"));
		
		etSiteFilter.setText(getIntent().getStringExtra("site_filter"));
		etSiteFilter.setSelection(etSiteFilter.length());
		
	}
}
