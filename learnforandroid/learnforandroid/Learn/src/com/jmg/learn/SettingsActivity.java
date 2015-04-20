package com.jmg.learn;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

import com.jmg.lib.ColorsArrayAdapter;
import com.jmg.lib.lib;
import com.jmg.lib.lib.libString;
import com.jmg.lib.ColorSetting;
import yuku.ambilwarna.*;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;


public class SettingsActivity extends android.support.v4.app.FragmentActivity 
{
	public Spinner spnAbfragebereich;
	public Spinner spnASCII;
	public Spinner spnStep;
	public Spinner spnDisplayDurationWord;
	public Spinner spnDisplayDurationBed;
	public Spinner spnPaukRepetitions;
	public Spinner spnProbabilityFactor;
	public com.jmg.lib.NoClickSpinner spnColors;
	public Button btnColors;
	public CheckBox chkRandom;
	public CheckBox chkAskAll;
	public ColorsArrayAdapter Colors;
	public SharedPreferences prefs;
	private Intent intent = new Intent();
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
		Thread.setDefaultUncaughtExceptionHandler(ErrorHandler);
		prefs = this.getPreferences(Context.MODE_PRIVATE);
		Colors = new ColorsArrayAdapter(this,0);
		
		initSpinners();
		initCheckBoxes();
		initButtons();
		
	}
	
	private void initCheckBoxes()
	{
		chkRandom = (CheckBox) findViewById(R.id.chkRandom);
		chkAskAll = (CheckBox) findViewById(R.id.chkAskAlll);
		boolean checked = getIntent().getBooleanExtra("Random", false);
		chkRandom.setChecked(checked);
		intent.putExtra("Random", checked);
		
		chkRandom.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				intent.putExtra("Random", isChecked);
			}
			
		});
		
		checked = getIntent().getBooleanExtra("AskAll", false);
		chkAskAll.setChecked(checked);
		intent.putExtra("AskAll", checked);
		
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
			spnColors = (com.jmg.lib.NoClickSpinner) findViewById(R.id.spnColors);
			
			
			spnASCII.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnStep.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnDisplayDurationBed.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnDisplayDurationWord.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnAbfragebereich.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnPaukRepetitions.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnProbabilityFactor.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			spnColors.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
			
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
			
			spnColors.setAdapter(Colors);
			spnColors.setOnLongClickListener(new android.widget.AdapterView.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					spnColors.blnDontCallOnClick = true;
					ShowColorDialog();
					return false;
				}
			});
			spnColors.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
				
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					// TODO Auto-generated method stub
					spnColors.blnDontCallOnClick = true;
					ShowColorDialog();
					return false;
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
				for (int i = 0; i < Colors.getCount(); i++)
				{
					intent.putExtra(Colors.getItem(i).ColorItem.name(), Colors.getItem(i).ColorValue);
				}
					
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
	
	private void ShowColorDialog()
	{
		spnColors.blnDontCallOnClick = true;
		ColorSetting item = SettingsActivity.this.Colors.getItem(spnColors.getSelectedItemPosition());
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, item.ColorValue, new OnAmbilWarnaListener() {
			
			@Override
			public void onOk(AmbilWarnaDialog dialog, int color) {
				// TODO Auto-generated method stub
				ColorSetting item = SettingsActivity.this.Colors.getItem(spnColors.getSelectedItemPosition());
				item.ColorValue = color;
				Editor editor = prefs.edit();
	            editor.putInt(item.ColorItem.name(), item.ColorValue);
	            intent.putExtra(item.ColorItem.name(), item.ColorValue);;
	            editor.commit();
				Colors.notifyDataSetChanged();
				spnColors.blnDontCallOnClick = false;
			}
			
			@Override
			public void onCancel(AmbilWarnaDialog dialog) {
				// TODO Auto-generated method stub
				spnColors.blnDontCallOnClick = false;
			}
		});				    
		dialog.show();				
	
	
	}
	public UncaughtExceptionHandler ErrorHandler = new UncaughtExceptionHandler() {
		
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// TODO Auto-generated method stub
			lib.ShowException(SettingsActivity.this, ex);
		}
	};

}
