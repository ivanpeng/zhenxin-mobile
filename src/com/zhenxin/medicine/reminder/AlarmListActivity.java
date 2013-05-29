package com.zhenxin.medicine.reminder;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
 
public class AlarmListActivity extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list_layout);
    }
}
