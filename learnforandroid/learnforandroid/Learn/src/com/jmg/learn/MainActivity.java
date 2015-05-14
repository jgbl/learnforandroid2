package com.jmg.learn;

import java.util.HashMap;

import com.jmg.learn.chart.IDemoChart;
import com.jmg.learn.vok.Vokabel;
import com.jmg.lib.ColorSetting;
import com.jmg.lib.SoundSetting;
import com.jmg.lib.lib;
import com.jmg.lib.ColorSetting.ColorItems;
import com.jmg.lib.lib.Sounds;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	private ViewPager mPager;	
	public Vokabel vok;
	public HashMap<ColorItems, ColorSetting> Colors;
	public HashMap<Sounds, SoundSetting> colSounds;
	public SharedPreferences prefs;
	public MyFragmentPagerAdapter fragmentPagerAdapter;
	public _MainActivity fragMain;
	public SettingsActivity fragSettings;
	public int LastPosition = -1;
	public ViewPager getPager()
	{
		return mPager;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try 
		{
			setContentView(R.layout.activity_main);
			        /** Getting a reference to ViewPager from the layout */
	        mPager = (ViewPager) findViewById(R.id.pager);
	        
	        /** Getting a reference to FragmentManager */
	        FragmentManager fm = getSupportFragmentManager();
	
	        /** Defining a listener for pageChange */
	        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
	                @Override
	                public void onPageSelected(int position) {
	                        super.onPageSelected(position);
	                        LastPosition = position;
	                }
	
	        };
	
	        /** Setting the pageChange listener to the viewPager */
	        mPager.setOnPageChangeListener(pageChangeListener);
	       
	        /** Creating an instance of FragmentPagerAdapter */
	        fragmentPagerAdapter = new MyFragmentPagerAdapter(fm);
	
	        /** Setting the FragmentPagerAdapter object to the viewPager object */
	        mPager.setAdapter(fragmentPagerAdapter);
        
	        prefs = this.getPreferences(Context.MODE_PRIVATE);
			Colors = getColorsFromPrefs();
			colSounds = getSoundsFromPrefs();
			
			
			vok = new Vokabel(this, null);
			mPager.setCurrentItem(0);
			
		} catch (Exception e) {
			lib.ShowException(this, e);
		}
		
    }
	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}
	
	private HashMap<Sounds, SoundSetting> getSoundsFromPrefs() {
		HashMap<Sounds, SoundSetting> res = new HashMap<Sounds, SoundSetting>();
		if (lib.AssetSounds[0] == null)
			lib.initSounds();
		for (int i = 0; i < lib.Sounds.values().length; i++) {
			Sounds SoundItem = Sounds.values()[i];
			String Name = getResources().getStringArray(R.array.spnSounds)[i];
			String defValue = "";
			defValue = lib.AssetSounds[SoundItem.ordinal()];
			String SoundPath = prefs.getString(SoundItem.name(), defValue);
			res.put(SoundItem, new SoundSetting(SoundItem, Name, SoundPath));
		}
		return res;

	}
	
	private HashMap<ColorItems, ColorSetting> getColorsFromPrefs() {
		HashMap<ColorItems, ColorSetting> res = new HashMap<ColorItems, ColorSetting>();
		for (int i = 0; i < ColorSetting.ColorItems.values().length; i++) {
			ColorItems ColorItem = ColorSetting.ColorItems.values()[i];
			String Name = getResources().getStringArray(R.array.spnColors)[i];
			int defValue = 0;
			switch (ColorItem) {
			case word:
				defValue = 0xff000000;
				break;
			case meaning:
				defValue = 0xff000000;
				break;
			case comment:
				defValue = 0xff000000;
				break;
			case background:
				defValue = 0xffffffff;
				break;
			case background_wrong:
				defValue = 0xffc0c0c0;
				break;
			case box_word:
				defValue = 0xffffffff;
				break;
			case box_meaning:
				defValue = 0xffffffff;
				break;
			default:
				defValue = 0xff000000;
				break;
			}
			int Color = prefs.getInt(ColorItem.name(), defValue);
			res.put(ColorItem, new ColorSetting(ColorItem, Name, Color));
		}
		return res;

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		try {
			getMenuInflater().inflate(R.menu.main, menu);
			//resize();
			return true;
		} catch (Exception ex) {
			lib.ShowException(this, ex);
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		try {
			if (id == R.id.action_settings) {
				fragMain.ShowSettings();
			} else if (id == R.id.mnuFileOpen) {
				fragMain.LoadFile(true);
			} else if (id == R.id.mnuNew) {
				fragmentPagerAdapter.fragMain.saveVok(false);
				vok.NewFile();
				if (lib.ShowMessageYesNo(this, getString(R.string.txtFlashCardFile)))
				{
					vok.setCardMode(true);
				}
				vok.AddVokabel();
				fragMain.getVokabel(true, false);
				fragMain.StartEdit();
			} else if (id == R.id.mnuAddWord) {
				vok.AddVokabel();
				fragMain.getVokabel(true, false);
				fragMain.StartEdit();
			} else if (id == R.id.mnuFileOpenASCII) {
				fragMain.LoadFile(false);
			} else if (id == R.id.mnuConvMulti) {
				vok.ConvertMulti();
				fragMain.getVokabel(false, false);
			} else if (id == R.id.mnuFileSave) {
				fragMain.saveVok(false);
			} else if (id == R.id.mnuSaveAs) {
				fragMain.SaveVokAs(true);
			} else if (id == R.id.mnuRestart) {
				vok.restart();
			} else if (id == R.id.mnuDelete) {
				vok.DeleteVokabel();
				fragMain.getVokabel(false, false);
			} else if (id == R.id.mnuReverse) {
				vok.revert();
				fragMain.getVokabel(false, false);
			} else if (id == R.id.mnuReset) {
				if (lib.ShowMessageYesNo(this,
						this.getString(R.string.ResetVocabulary))) {
					vok.reset();
				}

			} else if (id == R.id.mnuStatistics) {
				if (vok.getGesamtzahl() > 5) {
					try {
						IDemoChart chart = new com.jmg.learn.chart.LearnBarChart();
						Intent intent = chart.execute(this);
						this.startActivity(intent);
					} catch (Exception ex) {
						lib.ShowException(this, ex);
					}

				}
			}

		} catch (Exception ex) {
			lib.ShowException(this, ex);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_HOME) {
			try {
				fragMain.saveVok(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				lib.ShowException(this, e);
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				if (fragMain._backPressed > 0 || fragMain.saveVokAsync(false)) {
					fragMain.handlerbackpressed.removeCallbacks(fragMain.rSetBackPressedFalse);
				} else {
					return true;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("onBackPressed", e.getMessage(), e);
				lib.ShowException(this, e);
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);

	};


}
