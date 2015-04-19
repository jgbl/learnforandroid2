package com.jmg.lib;

import com.jmg.learn.R;
import com.jmg.lib.ColorSetting.ColorItems;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ColorsArrayAdapter extends ArrayAdapter<ColorSetting>{

    private Activity _Activity;

    public ColorsArrayAdapter(Context context, int textViewResourceId) 
    {
    	super(context, textViewResourceId);
    	_Activity = (Activity) context;
    	SharedPreferences prefs = _Activity.getPreferences(Context.MODE_PRIVATE);
    	for (int i = 0; i < ColorSetting.ColorItems.values().length; i++)
    	{
    		ColorItems ColorItem = ColorSetting.ColorItems.values()[i];
    		String Name = _Activity.getResources().getStringArray(R.array.spnColors)[i];
    		int defValue = 0;
    		switch (ColorItem) {
			case word:
				defValue = Color.BLACK;
				break;
			case meaning:
				defValue = Color.BLACK;
				break;
			case comment:
				defValue = Color.BLACK;
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
				defValue = Color.BLACK;
				break;
			}
    		int Color = prefs.getInt(ColorItem.name(), defValue);
    		this.add(new ColorSetting(ColorItem,Name,Color));
    	}
    	
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

       LayoutInflater inflater = _Activity.getLayoutInflater();
       ColorSetting ColorItem = getItem(position);
       View row;
       
       if (convertView == null)
       {
    	   row=inflater.inflate(R.layout.spinnerrow, parent, false);
       }
       else
       {
    	   row = convertView;
       }
       
       TextView label=(TextView) row.findViewById(R.id.txtColors);
       label.setText(ColorItem.ColorName);

       TextView icon=(TextView)row.findViewById(R.id.txtColors2);
       icon.setBackgroundColor(ColorItem.ColorValue);
       
       return row;
    }

	

	
}