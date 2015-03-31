package com.jmg.lib;

//import android.support.v7.app.ActionBarActivity;
import java.io.*;
import java.nio.channels.FileChannel;


//import com.microsoft.live.*;

import android.app.*;
import android.content.*;
//import android.runtime.*;
import android.provider.*;

public class lib
{
	public lib()
	{
	}
	private static String _status = "";
	private static final String ONEDRIVE_APP_ID = "48122D4E";
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
		private static class ExStateInfo
	{
		public Context context;
		public RuntimeException ex;
		public ExStateInfo(Context context, RuntimeException ex)
		{
			this.context = context;
			this.ex = ex;
		}
	}
	public static synchronized void ShowException(Context context, Exception ex)
	{
		//System.Threading.SynchronizationContext.Current.Post(new System.Threading.SendOrPostCallback(DelShowException),new ExStateInfo(context, ex));
	   AlertDialog.Builder A = new AlertDialog.Builder(context);
	   A.setPositiveButton("OK",listener());
	   A.setMessage(ex.getMessage());
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
	
}
