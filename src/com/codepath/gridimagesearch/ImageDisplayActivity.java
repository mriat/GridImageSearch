package com.codepath.gridimagesearch;

//import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		
		ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
		TouchImageView ivImage = (TouchImageView) findViewById(R.id.ivResult);
		TextView FullTitle = (TextView) findViewById(R.id.tvFullTitle);
		
		Picasso.with(this).load(result.getFullUrl()).into(ivImage);
		//ivImage.setImageUrl();
		FullTitle.setText(result.getTitle());

	}

}
