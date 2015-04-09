package com.jmg.learn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import br.com.thinkti.android.filechooser.*;

import com.jmg.learn.vok.*;
import com.jmg.learn.vok.Vokabel.EnumSprachen;
import com.jmg.lib.*;
import com.jmg.lib.lib.libString;

import android.support.v7.app.ActionBarActivity;
import android.text.Spanned;
import android.text.SpannedString;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends ActionBarActivity {
	
	private static final int FILE_CHOOSER = 34823;
	private Context context = this;
	private Button _btnRight;
	private Button _btnWrong;
	private Button _btnSkip;
	private Button _btnView;
	private Button _btnEdit;
	private TextView _txtWord;
	private TextView _txtKom;
	private TextView _txtStatus;
	private EditText _txtMeaning1;
	private EditText _txtMeaning2;
	private EditText _txtMeaning3;
	private double scale = 1;
	public Vokabel vok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		Thread.setDefaultUncaughtExceptionHandler(ErrorHandler);
        try {
			vok = new Vokabel(this,(TextView) this.findViewById(R.id.txtStatus));
			vok.setSchrittweite((short) 6);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			lib.ShowException(this, e);
		}
        CopyAssets();
        try {
			InitButtons();
			if (savedInstanceState != null)
			{
				String filename = savedInstanceState.getString("vokpath");
		    	int index = savedInstanceState.getInt("vokindex");
		    	int[] Lernvokabeln = savedInstanceState.getIntArray("Lernvokabeln");
		    	int Lernindex = savedInstanceState.getInt("Lernindex");
		    	if (!libString.IsNullOrEmpty(filename) && index>0)
		    	{
		    		LoadVokabel(filename, index, Lernvokabeln, Lernindex);
		    	}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			lib.ShowException(this, e);
		}
        
        
    }
    public UncaughtExceptionHandler ErrorHandler = new UncaughtExceptionHandler() {
		
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// TODO Auto-generated method stub
			lib.ShowException(MainActivity.this, ex);
		}
	};
        
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
    	saveVok(true);
    	outState.putParcelable("vok", vok);
    	outState.putString("vokpath", vok.getFileName());
    	outState.putInt("vokindex", vok.getIndex());
    	outState.putIntArray("Lernvokabeln", vok.getLernvokabeln());
    	outState.putInt("Lernindex", vok.getLernIndex());
    	
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onBackPressed() {
        if (saveVok(false)) super.onBackPressed();
        return;
    }   
    
    
    /*@Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);           
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {     

        if(keyCode == KeyEvent.KEYCODE_HOME)
        {
           saveVok(false);
        }
        return super.onKeyDown(keyCode, event);
    };
    
    private boolean saveVok(boolean dontPrompt)
    {
    	if (vok.aend)
    	{
    		if (dontPrompt || lib.ShowMessageYesNo(this,getString(R.string.Save)))
    				{
    					try {
							vok.SaveFile();
							vok.aend=false;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							lib.ShowException(this, e);
						}
    				}
    		return false;
    	}
    	return true;
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	//resize();
    }
    
    private void InitButtons() throws Exception
    {
    	View v = findViewById(R.id.btnRight);
    	Button b = (Button)v;
    	_btnRight = b;
    	b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					vok.AntwortRichtig();
					getVokabel(false,false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					lib.ShowException(MainActivity.this, e);
					
				}
				
			}
		});
    	v = findViewById(R.id.btnWrong);
    	b = (Button)v;
    	_btnWrong = b;
    	b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					vok.AntwortFalsch();
					getVokabel(false,true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					lib.ShowException(MainActivity.this, e);
				}
				
			}
		});
    	v = findViewById(R.id.btnSkip);
    	b = (Button)v;
    	_btnSkip = b;
    	b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					vok.SkipVokabel();
					getVokabel(false,false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					lib.ShowException(MainActivity.this, e);
				}
				
			}
		});
    	v = findViewById(R.id.btnView);
    	b = (Button)v;
    	_btnView = b;
    	b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					getVokabel(true,false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					lib.ShowException(MainActivity.this, e);
				}
				
			}
		});
    	
    	v = findViewById(R.id.btnEdit);
    	b = (Button)v;
    	_btnEdit = b;
    	b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					vok.setBedeutung1(_txtMeaning1.getText().toString());
					vok.setBedeutung2(_txtMeaning2.getText().toString());
					vok.setBedeutung3(_txtMeaning3.getText().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					lib.ShowException(MainActivity.this, e);
				}
				
			}
		});
    	
    	_txtMeaning1 = (EditText)findViewById(R.id.txtMeaning1);
    	_txtMeaning2 = (EditText)findViewById(R.id.txtMeaning2);
    	_txtMeaning3 = (EditText)findViewById(R.id.txtMeaning3);
    	_txtWord = (TextView)findViewById(R.id.word);
    	_txtKom = (TextView)findViewById(R.id.Comment);
    	_txtStatus = (TextView)findViewById(R.id.txtStatus);
    	    	
    	
    }
    
    private void resize()
    {
    	Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int height = metrics.heightPixels;
        int viewTop = findViewById(Window.ID_ANDROID_CONTENT).getTop();
        height = height - viewTop;
        scale = (double)height / (double)950;
    	/*lib.ShowMessage(this, "Meaning3 Bottom: " +_txtMeaning3.getBottom() 
    			+ "\nbtnRight.Top: " + _btnRight.getTop()
    			+ "\nDisplayHeight: " + height);*/
    	if (scale != 1)
    	{
    		lib.ShowMessage(this, "Scaling font by " + scale + " Screenheight = " + height);
    		_txtMeaning1.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_txtMeaning1.getTextSize() * scale));
    		_txtMeaning2.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_txtMeaning2.getTextSize() * scale));
    		_txtMeaning3.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_txtMeaning3.getTextSize() * scale));
    		_txtWord.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_txtWord.getTextSize() * scale));
    		_txtKom.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_txtKom.getTextSize() * scale));
    		_txtStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_txtStatus.getTextSize() * scale));
    		
    		_btnRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_btnRight.getTextSize() * scale));
    		_btnSkip.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_btnSkip.getTextSize() * scale));
    		_btnView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_btnView.getTextSize() * scale));
    		_btnWrong.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_btnWrong.getTextSize() * scale));
    		_btnEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (_btnEdit.getTextSize() * scale));
    		
    		
    		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) _txtMeaning1.getLayoutParams();
    		params.topMargin = (int) (params.topMargin * scale);
    		_txtMeaning1.setLayoutParams(params);
    		params = (android.widget.RelativeLayout.LayoutParams) _txtMeaning2.getLayoutParams();
    		params.topMargin = (int) (params.topMargin * scale);
    		_txtMeaning2.setLayoutParams(params);
    		params = (android.widget.RelativeLayout.LayoutParams) _txtMeaning3.getLayoutParams();
    		params.topMargin = (int) (params.topMargin * scale);
    		_txtMeaning3.setLayoutParams(params);
    		params = (android.widget.RelativeLayout.LayoutParams) _btnRight.getLayoutParams();
    		params.height = (int) (params.height * scale);
    		params.width = (int)(params.width * scale);
    		_btnRight.setLayoutParams(params);
    		params = (android.widget.RelativeLayout.LayoutParams) _btnWrong.getLayoutParams();
    		params.height = (int) (params.height * scale);
    		params.width = (int)(params.width * scale);
    		_btnWrong.setLayoutParams(params);
    		params = (android.widget.RelativeLayout.LayoutParams) _btnSkip.getLayoutParams();
    		params.height = (int) (params.height * scale);
    		params.width = (int)(params.width * scale);
    		_btnSkip.setLayoutParams(params);
    		params = (android.widget.RelativeLayout.LayoutParams) _btnView.getLayoutParams();
    		params.height = (int) (params.height * scale);
    		params.width = (int)(params.width * scale);
    		_btnView.setLayoutParams(params);
    		params = (android.widget.RelativeLayout.LayoutParams) _btnEdit.getLayoutParams();
    		params.height = (int) (params.height * scale);
    		params.width = (int)(params.width * scale);
    		_btnEdit.setLayoutParams(params);
    	}
    }
    
    public String JMGDataDirectory;
    private void CopyAssets()
    {
    	
		File F = android.os.Environment.getExternalStorageDirectory();
		String extPath = F.getPath();
		JMGDataDirectory = Path.combine(extPath, "learnforandroid","vok");
		
		if (F.isDirectory() && F.exists())
		{
			File F1 = new File(JMGDataDirectory);
			if (F1.isDirectory() == false && !F1.exists())
			{
				F1.mkdirs();
			}
			AssetManager A = this.getAssets();
			try 
			{
				final String languages [] = new String[]{"Greek","Hebrew","KAR","Spanish"};
				final String path = F1.getPath();
				for (String language:languages)
				{
					F1 = new File(Path.combine(path,language));
					for (String File : A.list(language))
					{
						InputStream myInput = A.open(Path.combine(language,File));
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
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				lib.ShowException(this, e);
				lib.ShowMessage(this, "CopyAssets");
			}
			
		}

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        resize();
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
        	LoadFile(true);
        }
        else if (id == R.id.mnuFileOpenASCII)
        {
        	LoadFile(false);
        }
        else if (id== R.id.mnuFileSave)
        {
        	saveVok(false);
        }
        if (id== R.id.mnuDelete)
        {
        	vok.DeleteVokabel();
        	getVokabel(false, false);
        }
        return super.onOptionsItemSelected(item);
    }
    public void LoadFile(boolean blnUniCode)
    {
    	Intent intent = new Intent(this, FileChooser.class);
    	ArrayList<String> extensions = new ArrayList<String>();
    	extensions.add(".k");
    	extensions.add(".v");
    	extensions.add(".K");
    	extensions.add(".V");
    	extensions.add(".KAR");
    	extensions.add(".VOK");
    	extensions.add(".kar");
    	extensions.add(".vok");
    	    	    	
    	intent.putStringArrayListExtra("filterFileExtension", extensions);
    	intent.putExtra("blnUniCode",blnUniCode);
    	intent.putExtra("DefaultDir", new File(JMGDataDirectory).exists()?JMGDataDirectory:"/sdcard/");
    	_blnUniCode = blnUniCode;
    	
    	this.startActivityForResult(intent, FILE_CHOOSER);
    }
    
    boolean _blnUniCode = true;
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
            String fileSelected = data.getStringExtra("fileSelected");
            LoadVokabel (fileSelected,1,null,0);
        }                   
    }
    public void LoadVokabel(String fileSelected, int index, int[]Lernvokabeln, int Lernindex)
    {
    	 try {
        	saveVok(false);
        	
    		vok.LoadFile(fileSelected,false,false,_blnUniCode);
    		if (vok.getCardMode())
    		{
    			_txtMeaning1.setMaxLines(30);
    			_txtMeaning1.setLines(16);
    			_txtMeaning1.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (20*scale));
    			_txtMeaning2.setVisibility(View.GONE);
    			_txtMeaning3.setVisibility(View.GONE);
    		}
    		else
    		{
    			_txtMeaning1.setMaxLines(10);
    			_txtMeaning1.setLines(2);
    			_txtMeaning1.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (40*scale));
    			_txtMeaning2.setVisibility(View.VISIBLE);
    			_txtMeaning3.setVisibility(View.VISIBLE);
    		}
    			
    		if (index >0 ) vok.setIndex(index);
    		if (Lernvokabeln != null) vok.setLernvokabeln(Lernvokabeln);
    		if (Lernindex > 0) vok.setLernIndex((short) Lernindex);
    		
    		getVokabel(false,false);
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		lib.ShowException(this,e);
    	}
    }
    
    
    public void getVokabel(boolean showBeds, boolean LoadNext)
    {
    	try {
    		if (showBeds)
    		{
    			_btnRight.setEnabled(true);
    			_btnWrong.setEnabled(true);
    		}
    		else
    		{
    			_btnRight.setEnabled(false);
    			_btnWrong.setEnabled(false);
    		}
    		if (LoadNext) vok.setLernIndex((short) (vok.getLernIndex()+1));
    		
    		View v = findViewById(R.id.word);
        	TextView t = (TextView)v;
        	t.setText(getSpanned(vok.getWort()),TextView.BufferType.SPANNABLE);
        	if (vok.getSprache() == EnumSprachen.Hebrew 
        			|| vok.getSprache() == EnumSprachen.Griechisch
        			|| (vok.getFontWort().getName() == "Cardo"))
        	{
        		t.setTypeface(vok.TypefaceCardo);
        	}
        	else
        	{
        		t.setTypeface(Typeface.DEFAULT);
        	}
        	
        	v = findViewById(R.id.Comment);
        	t = (TextView)v;
        	t.setText(getSpanned(vok.getKommentar()),TextView.BufferType.SPANNABLE);
        	if (vok.getSprache() == EnumSprachen.Hebrew 
        			|| vok.getSprache() == EnumSprachen.Griechisch
        			|| (vok.getFontKom().getName() == "Cardo"))
        	{
        		t.setTypeface(vok.TypefaceCardo);
        	}
        	else
        	{
        		t.setTypeface(Typeface.DEFAULT);
        	}
        	
        	v = findViewById(R.id.txtMeaning1);
        	t = (TextView)v;
        	t.setText((showBeds?vok.getBedeutung1():""));
        	if (vok.getFontBed().getName() == "Cardo")
        	{
        		t.setTypeface(vok.TypefaceCardo);
        	}
        	else
        	{
        		t.setTypeface(Typeface.DEFAULT);
        	}
        	
        	v = findViewById(R.id.txtMeaning2);
        	t = (TextView)v;
        	t.setText((showBeds?vok.getBedeutung2():""));
        	if (vok.getFontBed().getName() == "Cardo")
        	{
        		t.setTypeface(vok.TypefaceCardo);
        	}
        	else
        	{
        		t.setTypeface(Typeface.DEFAULT);
        	}
        	if (libString.IsNullOrEmpty(vok.getBedeutung2()) || vok.getCardMode())
        	{
        		t.setVisibility(View.GONE);
        	}
        	else
        	{
        		t.setVisibility(View.VISIBLE);
        	}
        	
        	v = findViewById(R.id.txtMeaning3);
        	t = (TextView)v;
        	t.setText((showBeds?vok.getBedeutung3():""));
        	if (vok.getFontBed().getName() == "Cardo")
        	{
        		t.setTypeface(vok.TypefaceCardo);
        	}
        	else
        	{
        		t.setTypeface(Typeface.DEFAULT);
        	}
        	if (libString.IsNullOrEmpty(vok.getBedeutung3()) || vok.getCardMode())
        	{
        		t.setVisibility(View.GONE);
        	}
        	else
        	{
        		t.setVisibility(View.VISIBLE);
        	}
        } catch (Exception e) {
			// TODO Auto-generated catch block
			lib.ShowException(this, e);
		}
    	
    }
    public Spanned getSpanned(String txt) throws IOException
    {
    	if(txt.startsWith("{\\rtf1\\"))
    			{
    				//txt = Java2Html.convertToHtml(txt, JavaSourceConversionOptions.getDefault());
    				//return Html.fromHtml(txt);
    				//return new SpannedString(stripRtf(txt));
    			}
    			return new SpannedString(txt);
    	
    }
    public String stripRtf(String str)
    {
        String basicRtfPattern = "/\\{\\*?\\\\[^{}]+}|[{}]|\\\\[A-Za-z]+\\n?(?:-?\\d+)?[ ]?/g";
        String newLineSlashesPattern = "/\\\\\\n/g";

        String stripped = str.replaceAll(basicRtfPattern,"");
        String removeNewlineSlashes = stripped.replaceAll(newLineSlashesPattern, "\n");
        String removeWhitespace = removeNewlineSlashes.trim();

        return removeWhitespace;
    }
}
