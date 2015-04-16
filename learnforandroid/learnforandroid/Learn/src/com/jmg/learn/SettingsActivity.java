package com.jmg.learn;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.charset.Charset;

import com.jmg.lib.lib;
import com.jmg.lib.lib.libString;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;


public class SettingsActivity extends Activity 
{
	public Spinner spnAbfragebereich;
	public Spinner spnASCII;
	public Spinner spnStep;
	public Spinner spnDisplayDurationWord;
	public Spinner spnDisplayDurationBed;
	public Spinner spnPaukRepetitions;
	public Spinner spnProbabilityFactor;
	public CheckBox chkRandom;
	public CheckBox chkAskAll;
	private Intent intent = new Intent();
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
		Thread.setDefaultUncaughtExceptionHandler(ErrorHandler);
		initSpinners();
		initCheckBoxes();
		initButtons();
	}
	
	private void initCheckBoxes()
	{
		chkRandom = (CheckBox) findViewById(R.id.chkRandom);
		chkAskAll = (CheckBox) findViewById(R.id.chkAskAlll);
		
		chkRandom.setChecked(getIntent().getBooleanExtra("Random", false));
		
		chkRandom.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				intent.putExtra("Random", isChecked);
			}
			
		});
		
		chkAskAll.setChecked(getIntent().getBooleanExtra("AskAll", false));
		
		chkAskAll.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				intent.putExtra("AskAll", isChecked);
			}
			
		});

		
	}
	
	private void initSpinners()
	{
		try
		{
			spnAbfragebereich = (Spinner) findViewById(R.id.spnAbfragebereich);
			spnASCII = (Spinner) findViewById(R.id.spnASCII);
			spnStep = (Spinner) findViewById(R.id.spnStep);
			spnDisplayDurationWord = (Spinner) findViewById(R.id.spnAnzeigedauerWord);
			spnDisplayDurationBed = (Spinner) findViewById(R.id.spnAnzeigedauerBed);
			spnPaukRepetitions = (Spinner) findViewById(R.id.spnRepetitions);
			spnProbabilityFactor = (Spinner) findViewById(R.id.spnProbabilityFactor);
			
			spnASCII.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnStep.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnDisplayDurationBed.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnDisplayDurationWord.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnAbfragebereich.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnPaukRepetitions.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnProbabilityFactor.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			
			// Create an ArrayAdapter using the string array and a default spinner layout
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
			        R.array.spnAbfragebereichEntries, android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spnAbfragebereich.setAdapter(adapter);
			spnAbfragebereich.setSelection(getIntent().getShortExtra("Abfragebereich", (short) -1)+1);
			spnAbfragebereich.setOnItemSelectedListener(new OnItemSelectedListener() {
	
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					intent.putExtra("Abfragebereich", (short)(position-1));
					
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					setResult(Activity.RESULT_CANCELED, null);
				}
			});
			ArrayAdapter<CharSequence> adapterStep = ArrayAdapter.createFromResource(this,
			        R.array.spnStepEntries, android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapterStep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spnStep.setAdapter(adapterStep);
			spnStep.setSelection(adapterStep.getPosition("" + getIntent().getShortExtra("Step", (short) 5)));
			spnStep.setOnItemSelectedListener(new OnItemSelectedListener() {
	
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					intent.putExtra("Step", (short)(Integer.parseInt((String) parent.getItemAtPosition(position))));
					
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
			spnASCII.setAdapter(adapterASCII);
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
					
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					setResult(Activity.RESULT_CANCELED, null);
				}
			});
			
			ArrayAdapter<CharSequence> adapterDDWord = ArrayAdapter.createFromResource(this,
			        R.array.spnDurations, android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapterDDWord.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spnDisplayDurationWord.setAdapter(adapterDDWord);
			String strDD = "" + getIntent().getFloatExtra("DisplayDurationWord", 1.5f); 
			strDD = strDD.replace(".0", "");
			int Pos = adapterDDWord.getPosition(strDD);
			spnDisplayDurationWord.setSelection(Pos);
			spnDisplayDurationWord.setOnItemSelectedListener(new OnItemSelectedListener() {
	
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					intent.putExtra("DisplayDurationWord", (Float.parseFloat((String) parent.getItemAtPosition(position))));
					
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					setResult(Activity.RESULT_CANCELED, null);
				}
			});
			
			ArrayAdapter<CharSequence> adapterDDBed = ArrayAdapter.createFromResource(this,
			        R.array.spnDurations, android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapterDDBed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spnDisplayDurationBed.setAdapter(adapterDDBed);
			strDD = "" + getIntent().getFloatExtra("DisplayDurationBed", 2.5f);
			strDD = strDD.replace(".0", "");
			Pos = adapterDDBed.getPosition(strDD);
			spnDisplayDurationBed.setSelection(Pos);
			spnDisplayDurationBed.setOnItemSelectedListener(new OnItemSelectedListener() {
	
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					intent.putExtra("DisplayDurationBed", (Float.parseFloat((String) parent.getItemAtPosition(position))));
					
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					setResult(Activity.RESULT_CANCELED, null);
				}
			});
			
			Pos = getIntent().getIntExtra("PaukRepetitions", 3)-1;
			spnPaukRepetitions.setSelection(Pos);
			spnPaukRepetitions.setOnItemSelectedListener(new OnItemSelectedListener() {
	
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					intent.putExtra("PaukRepetitions", (Integer.parseInt((String) parent.getItemAtPosition(position))));
					
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					setResult(Activity.RESULT_CANCELED, null);
				}
			});
	
			float ProbabilityFactor = getIntent().getFloatExtra("ProbabilityFactor", -1f); 
			if (ProbabilityFactor == -1)
			{
				strDD = "auto";
			}
			else
			{
				strDD = "" + ProbabilityFactor;
				strDD = strDD.replace(".0", "");
			}
			
			SpinnerAdapter a = spnProbabilityFactor.getAdapter();
			ArrayAdapter<CharSequence> a1 = null;
			if (a != null )
			{
				a1 = (ArrayAdapter<CharSequence>) a;
				Pos = (a1.getPosition(strDD));
			}
			spnProbabilityFactor.setSelection(Pos);
			spnProbabilityFactor.setOnItemSelectedListener(new OnItemSelectedListener() {
	
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String strDD = (String) parent.getItemAtPosition(position);
					if (strDD.equalsIgnoreCase("auto")) strDD = "-1";
					intent.putExtra("ProbabilityFactor", (Float.parseFloat(strDD)));
					
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					setResult(Activity.RESULT_CANCELED, null);
				}
			});
	
			
		}
		catch (Exception ex)
		{
			lib.ShowException(this, ex);
		}
	}
	
	private void initButtons()
	{
		Button b = (Button)findViewById(R.id.btnOK);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		b = (Button)findViewById(R.id.btnCancel);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(Activity.RESULT_CANCELED, intent);
				finish();
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
