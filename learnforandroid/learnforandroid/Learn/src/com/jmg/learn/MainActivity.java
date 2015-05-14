package com.jmg.learn;

import java.util.HashMap;

import com.jmg.learn.vok.Vokabel;
import com.jmg.lib.ColorSetting;
import com.jmg.lib.SoundSetting;
import com.jmg.lib.lib;
import com.jmg.lib.ColorSetting.ColorItems;
import com.jmg.lib.lib.Sounds;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private ViewPager mPager;	
	public Vokabel vok;
	public HashMap<ColorItems, ColorSetting> Colors;
	public HashMap<Sounds, SoundSetting> colSounds;
	public SharedPreferences prefs;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try 
		{
			setContentView(R.layout.fragmentactivity_main);
			        /** Getting a reference to ViewPager from the layout */
	        mPager = (ViewPager) findViewById(R.id.pager);
	        
	        /** Getting a reference to FragmentManager */
	        FragmentManager fm = getSupportFragmentManager();
	
	        /** Defining a listener for pageChange */
	        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
	                @Override
	                public void onPageSelected(int position) {
	                        super.onPageSelected(position);
	                }
	
	        };
	
	        /** Setting the pageChange listener to the viewPager */
	        mPager.setOnPageChangeListener(pageChangeListener);
	
	        /** Creating an instance of FragmentPagerAdapter */
	        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(fm);
	
	        /** Setting the FragmentPagerAdapter object to the viewPager object */
	        mPager.setAdapter(fragmentPagerAdapter);
        
			vok = new Vokabel(this,
					(TextView) fragmentPagerAdapter.fragMain.findViewById(R.id.txtStatus));
			Colors = getColorsFromPrefs();
			colSounds = getSoundsFromPrefs();
			prefs = this.getPreferences(Context.MODE_PRIVATE);
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


}
