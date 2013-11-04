package com.zhenxin.medicine.camera;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhenxin.medicine.R;
/* Import ZBar Class files */

/**
 * This activity manages the camera for QR code scanning.
 * Upon scanning, it will send an intent to an activity page where 
 * the serial code scanned will be sent via data or SMS. 
 * TODO: wire intent first, and then connect to DB via data.
 * 
 */
public class ScannerActivity extends Activity
{
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    TextView scanText;
    Button scanButton;

    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    } 

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scanner_layout);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
        
        //Need to initialize cache here! 
        // Algorithm is simple; just a quick list of data
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }
    
    

    @Override
	protected void onResume() {
    	//TODO: bug here! The camera preview doesn't reset after onPause is called the first time.
		super.onResume();
        autoFocusHandler = new Handler();
        previewing = true;
        mCamera = getCameraInstance();
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);

        if (mPreview == null) {
	        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
	        preview.addView(mPreview);

	        scanText = (TextView)findViewById(R.id.scanText);
	
	        scanButton = (Button)findViewById(R.id.ScanButton);
	
	        scanButton.setOnClickListener(new OnClickListener() {
	                public void onClick(View v) {
	                    if (barcodeScanned) {
	                        barcodeScanned = false;
	                        scanText.setText("Scanning...");
	                        mCamera.setPreviewCallback(previewCb);
	                        previewing = true;
	                        mCamera.startPreview();
	                        mCamera.autoFocus(autoFocusCB);
	                    }
	                }
	            });
        } else {
        	preview.addView(mPreview);
        }
	}

	/** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
            mPreview = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            }
        };

    PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);
                
                if (result != 0) {
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                    	// Call the asynchronous task for scanning
                    	new ValidateCodeTask().execute(sym.getData());
                    }
                }
            }
        };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        };
    
    class ValidateCodeTask extends AsyncTask<String, Void, ProductItem> {

		@Override
		protected ProductItem doInBackground(String... params) {
			ProductItem item;
        	// This is where we change the results!
        	//MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        	List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        	//messageConverters.add(messageConverter);
        	messageConverters.add(new StringHttpMessageConverter());
        	RestTemplate restTemplate = new RestTemplate();
        	restTemplate.setMessageConverters(messageConverters);
        	
        	// Now that the http rest request template is functional, we validate the URL and then do a call
        	try {
				URL u = new URL(params[0]);
				// Possible Hashmap use?
				//ProductItem item = restTemplate.getForObject(u.toURI(), ProductItem.class);
				ObjectMapper objMapper = new ObjectMapper();
				item = objMapper.readValue(u, ProductItem.class);
				barcodeScanned = true;
			} catch (MalformedURLException e) {
				// Not valid URL!
				// Here we don't need to do anything; just need to skip this URL
				item = ProductItem.generateDefault();
			} catch (JsonParseException e) {
				//TODO: record phone, IP Address, etc. here
				barcodeScanned = true;
				item = ProductItem.generateDefault();
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				item = ProductItem.generateDefault();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				item = ProductItem.generateDefault();
			}
			return item;
		}

		@Override
		protected void onPostExecute(ProductItem result) {
			super.onPostExecute(result);
			// TODO: should move the scantext stuff here.
			if (result.getProductKey() != -1)	{
				scanText.setText("Scanned item " + result.getSerialCode());
			} else
				scanText.setText("Invalid item! This is a fake!");
		}

    	
    }
}
