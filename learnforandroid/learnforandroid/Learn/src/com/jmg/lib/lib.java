﻿package com.jmg.lib;

//import android.support.v7.app.ActionBarActivity;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;


//import com.microsoft.live.*;

import android.app.*;
import android.content.*;
//import android.runtime.*;
import android.provider.*;

public class lib
{
	public enum ReturnPreOrPostValue {
		PRE,POST
	}


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
	public static class libString
	{
		public static boolean IsNullOrEmpty(String s)
		{
			if(s == null || s == ""){return true;}
			else{return false;}
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
	/*
	   Copyright 2010,2011 Kevin Glynn (kevin.glynn@twigletsoftware.com)

	   Licensed under the Apache License, Version 2.0 (the "License");
	   you may not use this file except in compliance with the License.
	   You may obtain a copy of the License at

	       http://www.apache.org/licenses/LICENSE-2.0

	   Unless required by applicable law or agreed to in writing, software
	   distributed under the License is distributed on an "AS IS" BASIS,
	   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	   See the License for the specific language governing permissions and
	   limitations under the License.

	   Author(s):

	   Kevin Glynn (kevin.glynn@twigletsoftware.com)
	*/


	// Wraps a parameter so that it can be used as a ref or out param.
	public class RefSupport<T> {

		private T value;

		public RefSupport(T inValue) {
			value = inValue;
		}
		public RefSupport() {
		}
		
		
		/**
		 * @param value the value to set
		 */
		public void setValue(T value) {
			this.value = value;
		}

		public T setValue(T value, ReturnPreOrPostValue preOrPost) {
			T preValue = this.value;
			this.value = value;
			return (preOrPost == ReturnPreOrPostValue.POST ? this.value : preValue);
		}

		/**
		 * @return the value
		 */
		public T getValue() {
			return this.value;
		}
	}

}
