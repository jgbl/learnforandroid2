package com.jmg.learn;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
	
	final int PAGE_COUNT = 2;
	public _MainActivity fragMain;
	public SettingsActivity fragSettings;
	
	/** Constructor of the class */
	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		
	}

	/** This method will be invoked when a page is requested to create */
	@Override
	public Fragment getItem(int arg0) {
		switch(arg0){
		
			/** tab1 is selected */
			case 0:
				
				fragMain = new _MainActivity();
				return fragMain;
				
			/** tab2 is selected */
			case 1:
				fragSettings = new SettingsActivity();
				return fragSettings;	
		}
		
		return null;
	}

	/** Returns the number of pages */
	@Override
	public int getCount() {		
		return PAGE_COUNT;
	}
	
}
