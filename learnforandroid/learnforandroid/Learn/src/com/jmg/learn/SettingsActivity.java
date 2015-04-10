package com.jmg.learn;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.charset.Charset;

import com.jmg.lib.lib;
import com.jmg.lib.lib.libString;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class SettingsActivity extends Activity 
{
	public Spinner spnAbfragebereich;
	public Spinner spnASCII;
	private Intent intent = new Intent();
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
		Thread.setDefaultUncaughtExceptionHandler(ErrorHandler);
		spnAbfragebereich = (Spinner) findViewById(R.id.spnAbfragebereich);
		spnASCII = (Spinner) findViewById(R.id.spnASCII);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.spnAbfragebereichEntries, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spnAbfragebereich.setAdapter(
			      new com.jmg.lib.NothingSelectedSpinnerAdapter(
			            adapter,
			            R.layout.contact_spinner_row_nothing_selected,
			            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
			            this));
		//spnAbfragebereich.setAdapter(adapter);
		spnAbfragebereich.setSelection(getIntent().getShortExtra("Abfragebereich", (short) -1)+1);
		spnAbfragebereich.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				intent.putExtra("Abfragebereich", (short)(position-1));
				setResult(Activity.RESULT_OK, intent);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				setResult(Activity.RESULT_CANCELED, null);
			}
		});
		ArrayAdapter<String> adapterASCII = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);   
		//adapterASCII.addAll(Charset.availableCharsets().values());
		
		for(Charset c:Charset.availableCharsets().values())
		{
			adapterASCII.add(c.name());
		}
		// Specify the layout to use when the list of choices appears
		adapterASCII.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spnASCII.setAdapter(
			      new com.jmg.lib.NothingSelectedSpinnerAdapter(
			            adapterASCII,
			            R.layout.contact_spinner_row_nothing_selected,
			            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
			            this));
		//spnASCII.setAdapter(adapterASCII);
		String CharsetASCII = getIntent().getStringExtra("CharsetASCII");
		if (!libString.IsNullOrEmpty(CharsetASCII))
		{
			int i = 0;
			for(Charset c:Charset.availableCharsets().values())
			{
				if (c.name().equalsIgnoreCase(CharsetASCII))
				{
					break;
				}
				i++;
			}
			if (i < adapterASCII.getCount())
			{
				spnASCII.setSelection(i);
			}

		}
		spnASCII.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				intent.putExtra("CharsetASCII", ((String)
						(parent.getSelectedItem())));
				setResult(Activity.RESULT_OK, intent);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				setResult(Activity.RESULT_CANCELED, null);
			}
		});
	}
	
public UncaughtExceptionHandler ErrorHandler = new UncaughtExceptionHandler() {
		
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// TODO Auto-generated method stub
			lib.ShowException(SettingsActivity.this, ex);
		}
	};

}
