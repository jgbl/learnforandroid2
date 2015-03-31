package com.jmg.photoprinter;

//import android.support.v7.app.ActionBarActivity;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.microsoft.live.*;

import android.graphics.*;
import android.app.*;
import android.content.*;
import android.content.pm.ResolveInfo;
import android.net.Uri;
//import android.runtime.*;
import android.provider.*;

public class lib
{
	public lib()
	{
	}
	private static String _status = "";
	private static final String ONEDRIVE_APP_ID = "48122D4E";
	public static dbpp dbpp;
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
	public static void GetThumbnails(Activity context, boolean Internal, android.database.Cursor mediaCursor, java.util.ArrayList<ImgFolder> BMList)
	{
		if (mediaCursor.getCount() > 0)
		{
			//await System.Threading.Tasks.Task.Run (() => {
				mediaCursor.moveToFirst();
				int ColumnIndexID = mediaCursor.getColumnIndex(MediaStore.Images.Media._ID);
				int ColumnIndexData = mediaCursor.getColumnIndex(MediaStore.Images.Media.DATA);
				int ColumnIndexBucket = mediaCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
				// int ColumnIndexSize = mediaCursor.GetColumnIndex (MediaStore.Images.Media.InterfaceConsts.Size);
				try
				{
					context.setProgressBarVisibility(true);
					for (int i = 0; i <= (mediaCursor.getCount() - 1); i++)
					{
						mediaCursor.moveToPosition(i);
						context.setProgress(i);
						int imageId = mediaCursor.getInt(ColumnIndexID);

						if (true)
						{
							android.net.Uri Uri;
							//String size = "";
							if (!Internal)
							{
								Uri = MediaStore.Files.getContentUri("external", imageId);
							}
							else
							{
								Uri = MediaStore.Files.getContentUri("internal", imageId);
							}

							//if (bitmap != null) {
							String folder = mediaCursor.getString(ColumnIndexData);
							//img.Dispose();
							//System.Diagnostics.Debug.Print (lib.getRealPathFromURI (context, Uri));
							String Bucket = mediaCursor.getString(ColumnIndexBucket);
							
							ImgFolder Folder = FindFolder(BMList, Bucket);
							if (Folder == null)
							{
								Folder = new ImgFolder(Bucket);
								BMList.add(Folder);
							}
							Folder.items.add(new ImgListItem(context, imageId, (new java.io.File(folder)).getName(), Uri, folder));
							//}
						}
					}
				}
				finally
				{
					context.setProgressBarVisibility(false);
				}
			//});
		}
		else
		{

			/*
			foreach (System.IO.FileInfo F in new System.IO.DirectoryInfo(Android.OS.Environment.GetExternalStoragePublicDirectory(Android.OS.Environment.DirectoryPictures).Path).GetFiles("*.*",SearchOption.AllDirectories)) 
			{
				try{
					Bitmap B = BitmapFactory.DecodeFile(F.FullName);
					if (B != null) {
						BMList.Add(new ImgListItem(B,F.Name));
					}
				}
				catch {
				}

			}*/
			if (BMList.isEmpty())
			{
				//this.Resources.GetDrawable(Resource.Drawable.P1040598)
				ImgFolder Folder1 = new ImgFolder("Test1");
				ImgFolder Folder2 = new ImgFolder("Test2");
				BMList.add(Folder1);
				BMList.add(Folder2);
				for (int i = 1; i <= 10; i++)
				{
					ImgListItem newItem1 = new ImgListItem(context, -1, "RES", null, null);
					newItem1.setImg(BitmapFactory.decodeResource(context.getResources(), R.drawable.ressmall));
					Folder1.items.add(newItem1);
					ImgListItem newItem2 = new ImgListItem(context, -1, "RES2", null, null);
					newItem2.setImg(BitmapFactory.decodeResource(context.getResources(), R.drawable.res2small));
					Folder2.items.add(newItem2);
				}
			}
		}
	}
	
	private static LiveConnectClient client;
	private static AutoResetEvent AR = new AutoResetEvent(false);
	private static boolean Finished;
	public static void GetThumbnailsOneDrive(Activity context, java.util.ArrayList<ImgFolder> BMList) throws LiveOperationException
	{
		//com.microsoft.live.LiveAuthClient mlAuth = new com.microsoft.live.LiveAuthClient(context, clientId);
		
		if (client == null)
		{
			final com.microsoft.live.LiveAuthClient Client = new LiveAuthClient(context.getApplicationContext(), "0000000048135143");
			
			final Iterable<String> scopes = Arrays.asList("wl.signin", "wl.basic", "wl.skydrive");
			LiveAuthListener listener = new LiveAuthListener() {
				
				@Override
				public void onAuthError(LiveAuthException exception, Object userState) {
					System.out.print("Error signing in: " + exception.getMessage());        
			        client = null;
			        AR.set();
			        Finished = true;
				}
				
				@Override
				public void onAuthComplete(LiveStatus status, LiveConnectSession session,
						Object userState) {
					if(status == LiveStatus.CONNECTED) {
			            System.out.print("Signed in.");
			            client = new LiveConnectClient(session);
			        }
			        else {
			            System.out.print("Not signed in.");
			            client = null;
			        }     
					AR.set();
					Finished = true;
				}
			};
			//Client.initialize(scopes, listener);
			if (client == null) {
				Finished = false;
				Client.login(context, scopes, listener);
				while (Finished == false) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		}
		if (client != null){
			String queryString = "me/skydrive/files?filter=folders";
            LiveOperation LiveOp = client.get(queryString);
            JSONObject folders = LiveOp.getResult();
            final JSONArray data = folders.optJSONArray("data");
            for (int i = 0; i < data.length(); i++) { 
                final JSONObject oneDriveItem = data.optJSONObject(i);
                if (oneDriveItem != null) {
                    final String itemName = oneDriveItem.optString("name");
                    //final String itemType = oneDriveItem.optString("type");
                    ImgFolder Folder = new ImgFolder(itemName);
                    BMList.add(Folder);
                } 
            } 
			//  get all folders
        }
	}

	
	private static ImgFolder FindFolder(java.util.ArrayList<ImgFolder> BMList,String Bucket)
	{
		for (ImgFolder f :BMList)
		{
			if(Bucket.equals(f.Name)) return f;
		}
		return null;
	}
	
	public static void StartViewer(Context context, android.net.Uri uri)
	{
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(uri, "image/*");
		context.startActivity(i);
	}
	public static void SharePictureOnFacebook(Context context, android.net.Uri uri)
	{
		
		String urlToShare = getRealPathFromURI((Activity)context,uri);
		File F = new File(urlToShare);
		Uri URI = Uri.fromFile(F);
		Intent intent = new Intent(Intent.ACTION_SEND);
		//intent.setDataAndType(Uri.parse(urlToShare), "image/*");
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_TEXT, F.getName());
		intent.putExtra(Intent.EXTRA_SUBJECT, F.getName());
		intent.putExtra(Intent.EXTRA_TITLE, F.getName());
		
	    intent.putExtra(Intent.EXTRA_STREAM, URI);  //optional//use this when you want to send an image
	    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	    
		// See if official Facebook app is found
		boolean facebookAppFound = false;
		java.util.List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
		for (ResolveInfo info : matches) {
		    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
		        intent.setPackage(info.activityInfo.packageName);
		        facebookAppFound = true;
		        break;
		    }
		}

		// As fallback, launch sharer.php in a browser
		if (!facebookAppFound) {
		    String sharerUrl = "https://www.facebook.com/mobile/"; //"https://www.facebook.com/sharer/sharer.php?u=" + URI.getPath() + "&t=" + F.getName();
		    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		}

		context.startActivity(intent);
	}
	
	public static void SharePictureOnTwitter(Context context, android.net.Uri uri)
	{
		
		final String[] twitterApps = {
				// package // name - nb installs (thousands)
				"com.twitter.android", // official - 10 000
				"com.twidroid", // twidroyd - 5 000
				"com.handmark.tweetcaster", // Tweecaster - 5 000
				"com.thedeck.android"// TweetDeck - 5 000 };
		};
		String urlToShare = getRealPathFromURI((Activity)context,uri);
		File F = new File(urlToShare);
		Uri URI = Uri.fromFile(F);
		Intent intent = new Intent(Intent.ACTION_SEND);
		//intent.setDataAndType(Uri.parse(urlToShare), "image/*");
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_TEXT, F.getName());
		intent.putExtra(Intent.EXTRA_SUBJECT, F.getName());
		intent.putExtra(Intent.EXTRA_TITLE, F.getName());
		
	    intent.putExtra(Intent.EXTRA_STREAM, URI);  //optional//use this when you want to send an image
	    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	    
		// See if official Facebook app is found
		boolean twitterAppFound = false;
		java.util.List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
		for (ResolveInfo info : matches) {
			String s = info.activityInfo.packageName;
		    System.out.print(s);
			for (String ss : twitterApps){
				if (s.toLowerCase().startsWith(ss)) {
					intent.setPackage(s);
					twitterAppFound = true;
					break;
				}
			}
		}

		// As fallback, launch sharer.php in a browser
		if (!twitterAppFound) {
		    String sharerUrl = "https://about.twitter.com/de/products/list"; //"http://twitter.com/share?text=com.jmg.photoprinter&url=" + URI.getPath();
		    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		}

		context.startActivity(intent);
	}

	public static void SharePictureOnInstagram(Context context, android.net.Uri uri)
	{
		
		String urlToShare = getRealPathFromURI((Activity)context,uri);
		File F = new File(urlToShare);
		Uri URI = Uri.fromFile(F);
		Intent intent = new Intent(Intent.ACTION_SEND);
		//intent.setDataAndType(Uri.parse(urlToShare), "image/*");
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_TEXT, F.getName());
		intent.putExtra(Intent.EXTRA_SUBJECT, F.getName());
		intent.putExtra(Intent.EXTRA_TITLE, F.getName());
		
	    intent.putExtra(Intent.EXTRA_STREAM, URI);  //optional//use this when you want to send an image
	    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	    
		// See if official Facebook app is found
		boolean instaAppFound = false;
		java.util.List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
		for (ResolveInfo info : matches) {
			String s = info.activityInfo.packageName;
		    System.out.print(s);
			if (s.toLowerCase().startsWith("com.instagram.android")) {
		        intent.setPackage(s);
		        instaAppFound = true;
		        break;
		    }
		}

		// As fallback, launch sharer.php in a browser
		if (!instaAppFound) {
		    String sharerUrl = "http://instagram.de.uptodown.com/android"; // "https://www.instagram.com/sharer/sharer.php?u=" + urlToShare;
		    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		}

		context.startActivity(intent);
	}

	public static void SharePictureOnPinterest(Context context, android.net.Uri uri)
	{
		
		String urlToShare = getRealPathFromURI((Activity)context,uri);
		File F = new File(urlToShare);
		Uri URI = Uri.fromFile(F);
		Intent intent = new Intent(Intent.ACTION_SEND);
		//intent.setDataAndType(Uri.parse(urlToShare), "image/*");
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_TEXT, F.getName());
		intent.putExtra(Intent.EXTRA_SUBJECT, F.getName());
		intent.putExtra(Intent.EXTRA_TITLE, F.getName());
		
	    intent.putExtra(Intent.EXTRA_STREAM, URI);  //optional//use this when you want to send an image
	    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	    
		// See if official Facebook app is found
		boolean pinAppFound = false;
		java.util.List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
		for (ResolveInfo info : matches) {
			String s = info.activityInfo.packageName;
		    System.out.print(s);
			if (s.toLowerCase().startsWith("com.pinterest")) {
		        intent.setPackage(s);
		        pinAppFound = true;
		        break;
		    }
		}

		// As fallback, launch sharer.php in a browser
		if (!pinAppFound) {
		    String sharerUrl = "http://pinterest.com/pin/create/link/?url=" + URI.getPath();
		    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		}

		context.startActivity(intent);
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
