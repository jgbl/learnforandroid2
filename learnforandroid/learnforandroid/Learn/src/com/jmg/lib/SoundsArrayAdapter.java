package com.jmg.lib;

import java.io.File;

import com.jmg.learn.R;
import com.jmg.lib.lib.Sounds;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SoundsArrayAdapter extends ArrayAdapter<SoundSetting>{

    private Activity _Activity;

    public SoundsArrayAdapter(Context context, int textViewResourceId) 
    {
    	super(context, textViewResourceId);
    	if (lib.AssetSounds[0] == null) lib.initSounds();
    	_Activity = (Activity) context;
    	SharedPreferences prefs = _Activity.getPreferences(Context.MODE_PRIVATE);
    	for (int i = 0; i < lib.Sounds.values().length; i++)
    	{
    		Sounds SoundItem = Sounds.values()[i];
    		String Name = _Activity.getResources().getStringArray(R.array.spnSounds)[i];
    		String defValue = "";
    		defValue = lib.AssetSounds[SoundItem.ordinal()];
    		String Sound = prefs.getString(SoundItem.name(), defValue);
    		this.add(new SoundSetting(SoundItem,Name,Sound));
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
       SoundSetting SoundItem = getItem(position);
       View row;
       
       if (convertView == null)
       {
    	   row=inflater.inflate(R.layout.soundsspinnerrow, parent, false);
       }
       else
       {
    	   row = convertView;
       }
       
       TextView label=(TextView) row.findViewById(R.id.txtSounds1);
       label.setText(SoundItem.SoundName);

       TextView label2=(TextView)row.findViewById(R.id.txtSounds2);
       File F = new File(SoundItem.SoundPath);
       label2.setText(F.getName().substring(0,(F.getName().length()> 25)?25:F.getName().length()));
       
       return row;
    }

	

	
}