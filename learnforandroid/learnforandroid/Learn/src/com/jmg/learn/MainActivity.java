package com.jmg.learn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.jar.JarFile;

import br.com.thinkti.android.filechooser.*;

import com.jmg.learn.vok.*;
import com.jmg.lib.*;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.res.AssetManager;
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
        String extPath = android.os.Environment.getExternalStorageDirectory().getPath();
		File F = new File(extPath);
		if (F.isDirectory() && F.exists())
		{
			String JMGDataDirectory = Path.combine(extPath, "learnforandroid","vok");
			File F1 = new File(JMGDataDirectory);
			if (F1.isDirectory() == false && !F1.exists())
			{
				F1.mkdirs();
			}
			AssetManager A = this.getAssets();
			try 
			{
				F1 = new File(Path.combine(F1.getPath(),"Spanish"));
				for (String File : A.list("Spanish"))
				{
					InputStream myInput = A.open(Path.combine("Spanish",File));
					String outFileName = Path.combine(F1.getPath(),File);
					if (F1.isDirectory() == false)
					{
						F1.mkdirs();
					}
					//Open the empty db as the output stream
					
					File file = new File(outFileName);
							
					if (file.exists())
					{
						//file.delete();
					}
					else
					{
						file.createNewFile();
						OutputStream myOutput = new FileOutputStream(file);

						byte[] buffer = new byte[1024];
						int length;
						while ((length = myInput.read(buffer,0,1024)) > 0)
						{
							myOutput.write(buffer, 0, length);
						}

						//Close the streams
						myOutput.flush();
						myOutput.close();
						myInput.close();
					}
				}
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				lib.ShowException(this, e);
			}
			
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
    	extensions.add(".K??");
    	extensions.add(".V??");
    	extensions.add(".KAR");
    	extensions.add(".VOK");
    	extensions.add(".kar");
    	extensions.add(".vok");
    	    	    	
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
