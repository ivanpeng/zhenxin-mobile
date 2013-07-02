package com.zhenxin.medicine.camera;

import java.sql.SQLException;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhenxin.medicine.R;

/**
 * This page pops up when we send in a QR code. It will connect to the DB via data or SMS
 * and verify the serial code.
 * For now, we will have a button to verify if we want to send or not, because
 * we don't have that capability yet.
 * @author Ivan
 *
 */
public class QrScannedActivity extends Activity {

	public static final String QR_CODE_KEY = "QR_CODE";

	public static final String PRODUCT_NAME_KEY = "PRODUCT_NAME";

	public static final String OUTPUT_STRING_KEY = "OUTPUT STRING";
	
	//TODO: add url from php!
	private String url;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr_scanned);
		// Display the QR code we have; from intent
		Intent intent = getIntent();
		final String qrCode = intent.getStringExtra(QR_CODE_KEY);
		TextView qrText = (TextView) findViewById(R.id.qr_scanned_text);
		qrText.setText(qrCode);
		
		Button sendQr = (Button) findViewById(R.id.qr_scanned_button_check);
		sendQr.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// Connect to DB and send qrCode
				new QueryQrCode().execute();
				
				/*
				try {
					connectToDb();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				*/
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qr_scanned, menu);
		return true;
	}
	
	protected int connectToDb() throws SQLException	{
		return 0;
	}
	
	//===================================================================
	
	/**
	 * This private class is an asynchronous task running in background to connect to 
	 * the internet to execute an http request. We can also add a progress bar.
	 * @author Ivan
	 *
	 */
	private class QueryQrCode extends AsyncTask<String, String, String> {

		/**
		 * This function will do an httprequest with Spring REST to grab items in DB.
		 */
		@Override
		protected String doInBackground(String... arg0) {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			// TODO: We assume that the url is already in JSON form; if run, NullPointerException will occur
			ProductItem[] products = restTemplate.getForObject(url, ProductItem[].class);
			// Now that we have the products, we will want to add to intent. But that's done in main class; just return message.
			Intent intent = new Intent(getApplicationContext(), DisplayProductActivity.class);
			intent.putExtra(QrScannedActivity.OUTPUT_STRING_KEY, products[0].getProductName() + " was entered by " + products[0].getEnteredBy());
			startActivity(intent);
			return null;
		}
		
	}

}
