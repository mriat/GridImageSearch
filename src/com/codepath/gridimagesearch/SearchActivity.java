package com.codepath.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	
	private final int REQUEST_CODE = 20;
	
	//default values for app startup
	private String imgSize="";
	private String imgColor="";
	private String imgType="";
	private String siteFilter="";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		setupViews();
		
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		
        gvResults.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
	            // Triggered only when new data needs to be appended to the list
	            // Add whatever code is needed to append new items to your AdapterView
		        customLoadMoreDataFromApi(page); 
	            // or customLoadMoreDataFromApi(totalItemsCount); 
		    }
        });

        gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		}); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // REQUEST_CODE is defined above
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
	     // Extract name value from result extras
			
		  imgSize = data.getExtras().getString("img_size");
		  imgColor = data.getExtras().getString("img_color");
		  imgType = data.getExtras().getString("img_type");
		  siteFilter = data.getExtras().getString("site_filter");
		  
		 /* Toast the name to display temporarily on screen
	     Toast.makeText(this, imgSize, Toast.LENGTH_SHORT).show();
	     Toast.makeText(this, imgColor, Toast.LENGTH_SHORT).show();
	     Toast.makeText(this, imgType, Toast.LENGTH_SHORT).show();
	     Toast.makeText(this, siteFilter, Toast.LENGTH_SHORT).show();
	     */
		  
		  imageAdapter.clear();
		  pollAPI(0, imgColor, imgSize, imgType, siteFilter);
	  }
	}
	
	public void onSettingsAction(MenuItem mi) {
	
			// first parameter is the context, second is the class of the activity to launch
			Intent i = new Intent(SearchActivity.this, SearchOptions.class);
			
			i.putExtra("img_size", imgSize);
			i.putExtra("img_color", imgColor);
			i.putExtra("img_type", imgType);
			i.putExtra("site_filter", siteFilter);
			
			startActivityForResult(i, REQUEST_CODE); // brings up the second activity

	}
	
	// Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
      // This method probably sends out a network request and appends new data items to your adapter. 
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter
    	offset = offset+8;
    	pollAPI(offset, imgColor, imgSize, imgType, siteFilter);
    }

	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		etQuery.setSelection(etQuery.length());
		
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
	}

	public void onImageSearch(View v) {
		int offset = 0;
		pollAPI(offset, imgColor, imgSize, imgType, siteFilter);
	}
	
	public void pollAPI(int offset, String imgColor, String imgSize, String imgType, String siteFilter) {
		String query = etQuery.getText().toString();
		
		imgColor = imgColor.toLowerCase();
		imgSize = imgSize.toLowerCase();
		imgType = imgType.toLowerCase();
		siteFilter = siteFilter.toLowerCase();
		
		//Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG).show();
		//23m0s
		//https://ajax.googleapis.com/ajax/services/search/images?rsz=8&start=0&v=1.0&imgcolor=black&imgsz=huge&imgtype=photo&as_sitesearch=bimmerpost.com&q=bmw%20m4
		AsyncHttpClient client = new AsyncHttpClient(); 
		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" + "start=" + offset + "&v=1.0" + "&imgcolor=" + imgColor + "&imgsz=" + imgSize + "&imgtype=" + imgType + "&as_sitesearch=" + siteFilter + "&q=" + Uri.encode(query), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					//25m40s
					//imageResults.clear();
					// mutate data directly through adapter which will notify adapter as well as modify array (was imageResults.addAll..)
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					
					
					//Log.d("DEBUG", imageResults.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}		
		});	
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}