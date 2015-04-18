package com.jmg.lib;

import com.jmg.learn.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<String>{

    private String[] mIcons;

    public CustomArrayAdapter(Context context, int textViewResourceId,
    String[] objects, String[] icons) {
        super(context, textViewResourceId, objects);
        mIcons = icons;
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

       LayoutInflater inflater = getLayoutInflater();
       View row=inflater.inflate(R.layout.spinnerrow, parent, false);
       TextView label=(TextView) findViewById(R.id.txtColors);
       label.setText(getItem(position));

       ImageView icon=(ImageView)row.findViewById(R.id.imageColors);

       String uri = "@drawable/a" + mIcons[position];
       //int imageResource = ((Object) getResources()).getIdentifier(uri, null, getPackageName());
       //icon.setImageResource(imageResource);

       return row;
    }

	

	private Object getPackageName() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	private TextView findViewById(int txtcolors) {
		// TODO Auto-generated method stub
		return null;
	}

	private LayoutInflater getLayoutInflater() {
		// TODO Auto-generated method stub
		return null;
	}
}