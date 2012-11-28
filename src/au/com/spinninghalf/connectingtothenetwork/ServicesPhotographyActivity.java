package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ServicesPhotographyActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.services_photography);
		
		//find references to each button
		Button brentonFordButton = (Button) findViewById(R.id.servicesPhotographyBrentonFordButton);
		Button leikographyButton = (Button) findViewById(R.id.servicesPhotographyLeikographyButton);
		
		//attach the OnClickListeners to each button.
		brentonFordButton.setOnClickListener(brentonFordButtonListener);
		leikographyButton.setOnClickListener(leikographyButtonListener);
		
	}
	
	public OnClickListener brentonFordButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ServicesPhotographyActivity.this, ServicesPhotographyBrentonFordActivity.class);
			startActivity(intent);
		}
	};
	
	public OnClickListener leikographyButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ServicesPhotographyActivity.this, ServicesPhotographyLeikographyActivity.class);
			startActivity(intent);
		}
	};

}
