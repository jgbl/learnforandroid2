package com.jmg.lib;

//import android.support.v7.app.ActionBarActivity;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import com.jmg.learn.libLearn;


//import com.microsoft.live.*;

import android.app.*;
import android.content.*;
//import android.runtime.*;
import android.provider.*;
import android.util.Log;
import android.widget.Toast;

public class lib
{
	

	public lib()
	{
	}
	private static String _status = "";
	//private static final String ONEDRIVE_APP_ID = "48122D4E";
	private static final String ClassName = "lib.lib";
	public static final String TAG = "com.jmg.lib.lib";
	public static String getgstatus()
	{
		return _status;
	}
	
	public static void setgstatus(String value)
	{
		_status = value;
		System.out.println(value);
	}
	public static String getRealPathFromURI(Activity context, android.net.Uri contentURI)
	{
		android.database.Cursor cursor = null;
		try
		{
			String[] proj = {MediaStore.Images.Media.DATA};
			cursor = context.getContentResolver().query(contentURI, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
	}
	public static String getSizeFromURI(Context context, android.net.Uri contentURI)
	{
		android.database.Cursor cursor = null;
		try
		{
			String[] proj = {MediaStore.Images.Media.SIZE};
			cursor = context.getContentResolver().query(contentURI, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
	}
		public static void StartViewer(Context context, android.net.Uri uri)
	{
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(uri, "image/*");
		context.startActivity(i);
	}
	/*	private static class ExStateInfo
	{
		public Context context;
		public RuntimeException ex;
		public ExStateInfo(Context context, RuntimeException ex)
		{
			this.context = context;
			this.ex = ex;
		}
	}
	*/
		
	public static synchronized void ShowException(Context context, Throwable ex)
	{
		//System.Threading.SynchronizationContext.Current.Post(new System.Threading.SendOrPostCallback(DelShowException),new ExStateInfo(context, ex));
	   AlertDialog.Builder A = new AlertDialog.Builder(context);
	   A.setPositiveButton("OK",listener());
	   A.setMessage(ex.getMessage() + "\n" + (ex.getCause() == null ? "" : ex.getCause().getMessage())+ "\nStatus: " + libLearn.gStatus);
	   A.setTitle("Error");
	   A.show();
	}
	private static DialogInterface.OnClickListener listener() {
		return null;
	}
	public static void copyFile(String Source, String Dest) throws IOException {
	     File source = new File(Source);
	     File dest = new File(Dest);
		 FileChannel sourceChannel = null;
	        FileChannel destChannel = null;
	        try {
	            sourceChannel = new FileInputStream(source).getChannel();
	            destChannel = new FileOutputStream(dest).getChannel();
	            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
	           }finally{
	               sourceChannel.close();
	               destChannel.close();
	           }
	    }
	public static float convertFromDp(Context context,float input) {
		int minWidth = context.getResources().getDisplayMetrics().widthPixels;
		int minHeight = context.getResources().getDisplayMetrics().heightPixels;
		if (minHeight < minWidth) minWidth = minHeight;
	    final float scale = 768.0f / (float)minWidth;
	    return ((input - 0.5f) / scale);
	}
	public static class libString
	{
		public static boolean IsNullOrEmpty(String s)
		{
			if(s == null || s == "" || s.length() == 0){return true;}
			else{return false;}
		}
		public static int InStr(String s, String Search)
		{
			int Start = 1;
			return InStr(Start,s,Search);
		}
		public static int InStr(int Start, String s, String Search)
		{
			return s.indexOf(Search, Start-1) + 1;
		}
		public static String Chr(int Code)
		{
			char c[] = {(char)Code};
			return new String(c);
		}
		public static String Left(String s, int length)
		{
			return s.substring(0, length);
		}
		public static int Len(String s)
		{
			return s.length();
		}
		public static String Right(String wort, int i) {
			// TODO Auto-generated method stub
			return wort.substring(wort.length()-i);
		}
		
	}
	public static boolean like(final String str, final String expr)
	{
	  String regex = quotemeta(expr);
	  regex = regex.replace("_", ".").replace("*", ".*?");
	  Pattern p = Pattern.compile(regex,
	      Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	  return p.matcher(str).matches();
	}

	public static String quotemeta(String s)
	{
	  if (s == null)
	  {
	    throw new IllegalArgumentException("String cannot be null");
	  }

	  int len = s.length();
	  if (len == 0)
	  {
	    return "";
	  }

	  StringBuilder sb = new StringBuilder(len * 2);
	  for (int i = 0; i < len; i++)
	  {
	    char c = s.charAt(i);
	    if ("[](){}.*+?$^|#\\".indexOf(c) != -1)
	    {
	      sb.append("\\");
	    }
	    sb.append(c);
	  }
	  return sb.toString();
	}
	
	public static int countMatches(String str, String sub) {
	    if (libString.IsNullOrEmpty(str) || libString.IsNullOrEmpty(sub)) {
	        return 0;
	    }
	    int count = 0;
	    int idx = 0;
	    while ((idx = str.indexOf(sub, idx)) != -1) {
	        count++;
	        idx += sub.length();
	    }
	    return count;
	    
	}
	public static String MakeMask(String strBed)
	{
		try
		{
			int i = 0;
			int Len = strBed.length();
			if (Len == 0) return "";
			libLearn.gStatus = ClassName + ".MakeMask";
			for (i = 0; i <= Len -1 ; i++) {
				if ((".,;/[]()".indexOf(strBed.charAt(i)) > -1)) {
					strBed = strBed.substring(0, i) + "*" + (i < Len-1 ? strBed.substring(i+1):"");
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return strBed;

	}
	
	/*
	public static <E> getEnumByOrdinal(<E> object, int ordinal) throws RuntimeException
	{
	   E value;
		return value;
	}
	*/
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int rndInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	public static <T> T[] ResizeArray(T Array[], int newSize)
	{
		@SuppressWarnings("unchecked")
		T[] NewArr = (T[]) java.lang.reflect.Array.newInstance(Array.getClass(), newSize);
		int length = Array.length;
		if (length>newSize) length = newSize;
		System.arraycopy(Array, 0, NewArr, 0, length);
		return NewArr;
	}

	public static int[] ResizeArray(int[] Array, int newSize) {
		int[] NewArr = new int[newSize];
		int length = Array.length;
		if (length>newSize) length = newSize;
		System.arraycopy(Array, 0, NewArr, 0, length);
		return NewArr;
	}
	public static synchronized void ShowMessage(Context context, String msg)
	{
		//System.Threading.SynchronizationContext.Current.Post(new System.Threading.SendOrPostCallback(DelShowException),new ExStateInfo(context, ex));
	   AlertDialog.Builder A = new AlertDialog.Builder(context);
	   A.setPositiveButton("OK",listener);
	   A.setMessage(msg);
	   A.setTitle("Message");
	   A.show();
	}
	public static synchronized boolean ShowMessageYesNo(Context context, String msg)
	{
		//System.Threading.SynchronizationContext.Current.Post(new System.Threading.SendOrPostCallback(DelShowException),new ExStateInfo(context, ex));
	   try
	   {
			AlertDialog.Builder A = new AlertDialog.Builder(context);
		   A.setPositiveButton("Yes",listener);
		   A.setNegativeButton("No",listener);
		   A.setMessage(msg);
		   A.setTitle("Question");
		   A.show();
	   }
	   catch (Exception ex)
	   {
		   ShowException(context, ex);
	   }
	   return DialogResultYes;
	}
	public static synchronized void ShowToast(Context context, String msg)
	{
		/*Looper.prepare();*/
		Toast T = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		T.show();
	}
	private static boolean DialogResultYes = false;
	private static DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		
		@Override
	    public void onClick(DialogInterface dialog, int which) 
		{
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	            //Yes button clicked
	        	DialogResultYes = true;
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            //No button clicked
	        	DialogResultYes = false;
	            break;
	        }
		}
	};
	
	public static String getExtension(String Filename)
	{
		return getExtension(new File(Filename));
	}
	
	public static String getExtension(java.io.File F)
	{
		String extension = "";

		int i = F.getName().lastIndexOf('.');
		if (i > 0) 
		{
			extension = F.getName().substring(i);
			return extension;
		}
		else
		{
			return null;
		}
	}
	
	public static String getFilenameWithoutExtension(String Filename)
	{
		return getFilenameWithoutExtension(new File(Filename));
	}
	
	
	public static String getFilenameWithoutExtension(java.io.File F)
	{
		String Filename = F.getPath();

		int i = Filename.lastIndexOf('.');
		if (i > 0) 
		{
			Filename = Filename.substring(0,i-1);
			return Filename;
		}
		else
		{
			return Filename;
		}
	}

	/*
	public static String rtfToHtml(Reader rtf) throws IOException {
		JEditorPane p = new JEditorPane();
		p.setContentType("text/rtf");
		EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
		try {
			kitRtf.read(rtf, p.getDocument(), 0);
			kitRtf = null;
			EditorKit kitHtml = p.getEditorKitForContentType("text/html");
			Writer writer = new StringWriter();
			kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
			return writer.toString();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	private static long SLEEP_TIME = 2; // for 2 second
	private static CountDownLatch latch;		
	public static void Sleep(int Seconds) throws InterruptedException
	{
		latch = new CountDownLatch(1);
		SLEEP_TIME = Seconds;
		MyLauncher launcher = new MyLauncher();
		            launcher.start();
		//latch.await();
	}
	private static class MyLauncher extends Thread 
	{
        @Override
        /**
         * Sleep for 2 seconds as you can also change SLEEP_TIME 2 to any. 
         */
        public void run() {
            try {
                // Sleeping
                Thread.sleep(SLEEP_TIME * 1000);
                latch.countDown();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            //do something you want to do
           //And your code will be executed after 2 second
        }
	}

}

