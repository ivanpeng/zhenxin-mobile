package com.zhenxin.medicine.reminder;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents.Insert;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
 
public class SocialActivity extends ListActivity {
	
	final static int PICK_CONTACT = 999;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_layout);
        ListAdapter adapter = createAdapter();
        setListAdapter(adapter);
        
    }
    protected ListAdapter createAdapter()
    {
    	List<String> testValues = new ArrayList<String>();
    	testValues.add("Mom");
    	testValues.add("Dad");
    	
    	//TODO: get below code working
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                  String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                  String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                  if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                     Cursor pCur = cr.query(
                               ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                               null,
                               ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                               new String[]{id}, null);
                     while (pCur.moveToNext()) {
                         String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                         //Toast.makeText(context, "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                         testValues.add(name);
                     }
                    pCur.close();
                }
            }
        }
        
    	// Create some mock data
    	
 
    	// Create a simple array adapter (of type string) with the test values
    	ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
 
    	return adapter;
    }
    
    /**
     * This method simply creates a dialog for display to ask if you want to view the contact
     * or register for notifications
     * TODO: add functionality for notification registration
     */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final String[] options = {"View contact information", "Register contact for missed SMS notifications"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Contact Options");
		builder.setItems(options, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// This dialog will interpret which option is selected, and then implement
				// the choice selected
				if (which == 0)	{
					// View contact; do this by building intent, and then starting the intent
					 Intent intent = new Intent(Intent.ACTION_VIEW);
                     intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                     //TODO: fix
                     startActivity(intent);
				} else {
					// Register contact
					Toast.makeText(getApplicationContext(), "Contact has been registered for SMS notification in case of missed medication!", Toast.LENGTH_LONG).show();
				}
				
			}
			

		});
		builder.show();
	}
	
	
    
    
}