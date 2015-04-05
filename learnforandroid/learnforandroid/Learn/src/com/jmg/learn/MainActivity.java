package com.jmg.learn;

import java.util.ArrayList;
import java.util.jar.JarFile;

import br.com.thinkti.android.filechooser.*;

import com.jmg.learn.vok.*;
import com.jmg.lib.lib;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
	
	private static final int FILE_CHOOSER = 34823;
	public Vokabel vok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
			vok = new Vokabel(this,(TextView) this.findViewById(R.id.txtStatus));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			lib.ShowException(this, e);
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.mnuFileOpen)
        {
        	LoadFile();
        }
        return super.onOptionsItemSelected(item);
    }
    public void LoadFile()
    {
    	Intent intent = new Intent(this, FileChooser.class);
    	ArrayList<String> extensions = new ArrayList<String>();
    	extensions.add(".k??");
    	extensions.add(".v??");
    	
    	intent.putStringArrayListExtra("filterFileExtension", extensions);
    	this.startActivityForResult(intent, FILE_CHOOSER);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
            String fileSelected = data.getStringExtra("fileSelected");
            try {
				vok.LoadFile(fileSelected);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				lib.ShowException(this,e);
			}
        }                   
    }
    
}
