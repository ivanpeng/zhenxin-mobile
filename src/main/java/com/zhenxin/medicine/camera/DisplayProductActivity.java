package com.zhenxin.medicine.camera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.zhenxin.medicine.JSONParser;
import com.zhenxin.medicine.R;

/**
 * This activity receives the JSON activity from the PHP/MySQL workflow.
 * We will parse it and display it here.
 * TODO: merge with QrScannedActivity, as we don't want to give the user 
 * a chance to decide whether or not to send the QR code.
 * @author Ivan
 *
 */
public class DisplayProductActivity extends Activity {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_product);
 
        TextView textView = (TextView) findViewById(R.id.product_information);
        // get message from QrScanned Activity
        String message = getIntent().getStringExtra(QrScannedActivity.OUTPUT_STRING_KEY);
        textView.setText(message);
 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_product, menu);
		return true;
	}
	


}
