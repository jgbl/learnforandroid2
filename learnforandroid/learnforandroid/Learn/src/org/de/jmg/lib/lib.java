﻿package org.de.jmg.lib;

//import android.support.v7.app.ActionBarActivity;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import org.de.jmg.learn.MainActivity;
import org.de.jmg.learn.R;
import org.de.jmg.learn.libLearn;

//import com.microsoft.live.*;

import android.annotation.TargetApi;
import android.app.*;
import android.content.*;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
//import android.runtime.*;
import android.provider.*;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class lib {

	public lib() {
	}

	private static String _status = "";
	// private static final String ONEDRIVE_APP_ID = "48122D4E";
	private static final String ClassName = "lib.lib";
	public static final String TAG = "org.de.jmg.lib.lib";
	
	public static boolean sndEnabled = true;
	public static boolean AntwWasRichtig;
	public static String getgstatus() {
		return _status;
	}

	public static void setgstatus(String value) {
		_status = value;
		System.out.println(value);
	}


	public static boolean ExtensionMatch(String value, String extension)
	{
		String ext;
		if(value.contains("."))
		{
			ext = value.substring(value.lastIndexOf("."));
		}
		else
		{
			return false;
		}
		
		if (extension.toLowerCase().contains(ext.toLowerCase())) return true;
		
		String itext = extension;
		itext = itext.replace(".", "\\.");
		itext = itext.toLowerCase();
		ext = ext.toLowerCase();
		if (ext.matches(itext.replace("?", ".{1}").replace("*", ".*")))
				{
					return true;
				}
		
		
		return false;
		
	}
	
	public static final boolean NookSimpleTouch()
	{
		//return false;
		
		String MANUFACTURER = getBuildField("MANUFACTURER");
		@SuppressWarnings("unused")
		String MODEL = getBuildField("MODEL");
		String DEVICE = getBuildField("DEVICE");
		return (MANUFACTURER.equalsIgnoreCase("BarnesAndNoble") && DEVICE.equalsIgnoreCase("zoom2"));
		
	}
	
	private static String getBuildField(String fieldName) {
		
		try {
			return (String)Build.class.getField(fieldName).get(null);
		} catch (Exception e) {
			Log.d("cr3", "Exception while trying to check Build." + fieldName);
			return "";
		}
	}
	
	public static String getRealPathFromURI(Activity context,
			android.net.Uri contentURI) {
		android.database.Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentURI, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static String getSizeFromURI(Context context,
			android.net.Uri contentURI) {
		android.database.Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.SIZE };
			cursor = context.getContentResolver().query(contentURI, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static void StartViewer(Context context, android.net.Uri uri) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(uri, "image/*");
		context.startActivity(i);
	}

	/*
	 * private static class ExStateInfo { public Context context; public
	 * RuntimeException ex; public ExStateInfo(Context context, RuntimeException
	 * ex) { this.context = context; this.ex = ex; } }
	 */

	public static void copyFile(String Source, String Dest) throws IOException {
		File source = new File(Source);
		File dest = new File(Dest);
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destChannel = new FileOutputStream(dest).getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		} finally {
			sourceChannel.close();
			destChannel.close();
		}
	}

	public static float convertFromDp(Context context, float input) {
		int minWidth = context.getResources().getDisplayMetrics().widthPixels;
		int minHeight = context.getResources().getDisplayMetrics().heightPixels;
		if (minHeight < minWidth)
			minWidth = minHeight;
		final float scale = 768.0f / (float) minWidth;
		return ((input - 0.5f) / scale);
	}

	public static class libString {
		public static boolean IsNullOrEmpty(String s) {
			if (s == null || s == "" || s.length() == 0) {
				return true;
			} else {
				return false;
			}
		}

		public static int InStr(String s, String Search) {
			int Start = 1;
			return InStr(Start, s, Search);
		}

		public static int InStr(int Start, String s, String Search) {
			return s.indexOf(Search, Start - 1) + 1;
		}

		public static String Chr(int Code) {
			char c[] = { (char) Code };
			return new String(c);
		}

		public static String Left(String s, int length) {
			return s.substring(0, length);
		}

		public static int Len(String s) {
			return s.length();
		}

		public static String Right(String wort, int i) {
			// TODO Auto-generated method stub
			return wort.substring(wort.length() - i);
		}

	}

	public static boolean like(final String str, final String expr) {
		String regex = quotemeta(expr);
		regex = regex.replace("_", ".").replace("*", ".*?");
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE
				| Pattern.DOTALL);
		return p.matcher(str).matches();
	}

	public static String quotemeta(String s) {
		if (s == null) {
			throw new IllegalArgumentException("String cannot be null");
		}

		int len = s.length();
		if (len == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder(len * 2);
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if ("[](){}.+?$^|#\\".indexOf(c) != -1) {
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

	public static String MakeMask(String strBed) {
		try {
			int i = 0;
			int Len = strBed.length();
			if (Len == 0)
				return "";
			libLearn.gStatus = ClassName + ".MakeMask";
			for (i = 0; i <= Len - 1; i++) {
				if ((".,;/[]()".indexOf(strBed.charAt(i)) > -1)) {
					strBed = strBed.substring(0, i) + "*"
							+ (i < Len - 1 ? strBed.substring(i + 1) : "");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return strBed;

	}

	/*
	 * public static <E> getEnumByOrdinal(<E> object, int ordinal) throws
	 * RuntimeException { E value; return value; }
	 */

	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 * 
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
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

	public static <T> T[] ResizeArray(T Array[], int newSize) {
		@SuppressWarnings("unchecked")
		T[] NewArr = (T[]) java.lang.reflect.Array.newInstance(
				Array.getClass(), newSize);
		int length = Array.length;
		if (length > newSize)
			length = newSize;
		System.arraycopy(Array, 0, NewArr, 0, length);
		return NewArr;
	}

	public static int[] ResizeArray(int[] Array, int newSize) {
		int[] NewArr = new int[newSize];
		int length = Array.length;
		if (length > newSize)
			length = newSize;
		System.arraycopy(Array, 0, NewArr, 0, length);
		return NewArr;
	}

	public static synchronized void ShowMessage(Context context, String msg) {
		// System.Threading.SynchronizationContext.Current.Post(new
		// System.Threading.SendOrPostCallback(DelShowException),new
		// ExStateInfo(context, ex));
		AlertDialog.Builder A = new AlertDialog.Builder(context);
		A.setPositiveButton("OK", listener());
		A.setMessage(msg);
		A.setTitle("Message");
		A.show();
	}

	public static synchronized void ShowException(Context context, Throwable ex) {
		// System.Threading.SynchronizationContext.Current.Post(new
		// System.Threading.SendOrPostCallback(DelShowException),new
		// ExStateInfo(context, ex));
		AlertDialog.Builder A = new AlertDialog.Builder(context);
		A.setPositiveButton("OK", listener());
		A.setMessage(ex.getMessage() + "\n"
				+ (ex.getCause() == null ? "" : ex.getCause().getMessage())
				+ "\nStatus: " + libLearn.gStatus);
		A.setTitle("Error");
		A.show();
	}

	private static DialogInterface.OnClickListener listener() {
		return null;
	}

	private static Handler YesNoHandler;

	public static synchronized boolean ShowMessageYesNo(Context context,
			String msg) {
		// System.Threading.SynchronizationContext.Current.Post(new
		// System.Threading.SendOrPostCallback(DelShowException),new
		// ExStateInfo(context, ex));
		try {
			if (YesNoHandler == null)
				YesNoHandler = new Handler() {
					@Override
					public void handleMessage(Message mesg) {
						throw new RuntimeException();
					}
				};

			DialogResultYes = false;
			AlertDialog.Builder A = new AlertDialog.Builder(context);
			A.setPositiveButton(context.getString(R.string.yes), listenerYesNo);
			A.setNegativeButton(context.getString(R.string.no), listenerYesNo);
			A.setMessage(msg);
			A.setTitle("Question");
			A.show();

			try

			{
				Looper.loop();
			} catch (RuntimeException e2) {
				// Looper.myLooper().quit();
			}
		} catch (Exception ex) {
			ShowException(context, ex);
		}
		return DialogResultYes;
	}

	public static synchronized void ShowToast(Context context, String msg) {
		/* Looper.prepare(); */
		Toast T = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		T.show();
	}

	private static boolean DialogResultYes = false;
	private static DialogInterface.OnClickListener listenerYesNo = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				// Yes button clicked
				DialogResultYes = true;
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				// No button clicked
				DialogResultYes = false;
				break;
			}
			YesNoHandler.sendMessage(YesNoHandler.obtainMessage());
		}
	};

	public static String getExtension(String Filename) {
		return getExtension(new File(Filename));
	}

	public static String getExtension(java.io.File F) {
		String extension = "";

		int i = F.getName().lastIndexOf('.');
		if (i > 0) {
			extension = F.getName().substring(i);
			return extension;
		} else {
			return null;
		}
	}

	public static String getFilenameWithoutExtension(String Filename) {
		return getFilenameWithoutExtension(new File(Filename));
	}

	public static String getFilenameWithoutExtension(java.io.File F) {
		String Filename = F.getPath();

		int i = Filename.lastIndexOf('.');
		int ii = Filename.lastIndexOf(java.io.File.pathSeparatorChar);
		if (i > 0 && i > ii) {
			Filename = Filename.substring(0, i);
			return Filename;
		} else {
			return Filename;
		}
	}

	/*
	 * public static String rtfToHtml(Reader rtf) throws IOException {
	 * JEditorPane p = new JEditorPane(); p.setContentType("text/rtf");
	 * EditorKit kitRtf = p.getEditorKitForContentType("text/rtf"); try {
	 * kitRtf.read(rtf, p.getDocument(), 0); kitRtf = null; EditorKit kitHtml =
	 * p.getEditorKitForContentType("text/html"); Writer writer = new
	 * StringWriter(); kitHtml.write(writer, p.getDocument(), 0,
	 * p.getDocument().getLength()); return writer.toString(); } catch
	 * (BadLocationException e) { e.printStackTrace(); } return null; }
	 */
	private static long SLEEP_TIME = 2; // for 2 second
	private static CountDownLatch latch;

	public static void Sleep(int Seconds) throws InterruptedException {
		latch = new CountDownLatch(1);
		SLEEP_TIME = Seconds;
		MyLauncher launcher = new MyLauncher();
		launcher.start();
		// latch.await();
	}

	private static class MyLauncher extends Thread {
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
			// do something you want to do
			// And your code will be executed after 2 second
		}
	}

	public static int[] getColors() {
		int[] Colors = new int[4096];
		for (int i = 0; i < 16; i++)
			for (int ii = 0; ii < 16; ii++)
				for (int iii = 0; iii < 16; iii++)
					Colors[i + ii * 16 + iii * 16 * 16] = Color.rgb(i * 16,
							ii * 16, iii * 16);
		;
		;
		return Colors;
	}

	public static int[] getIntArrayFromPrefs(SharedPreferences prefs,
			String name) {
		int count = prefs.getInt(name, -1);
		if (count > -1) {
			int[] res = new int[count + 1];
			for (int i = 0; i <= count; i++) {
				res[i] = prefs.getInt(name + i, 0);
			}

			return res;
		} else {
			return null;
		}

	}

	public static void putIntArrayToPrefs(SharedPreferences prefs, int array[],
			String name) {
		int count = array.length - 1;
		Editor edit = prefs.edit();
		edit.putInt(name, count);
		for (int i = 0; i <= count; i++) {
			edit.putInt(name + i, array[i]);
		}

		edit.commit();

	}

	public enum Sounds {
		Richtig0, Richtig1, Richtig2, Richtig3, Richtig4, Richtig5, Falsch0, Falsch1, Falsch2, Falsch3, Falsch4, Falsch5, Beep
	}

	public static String[] AssetSounds = new String[13];

	public static void initSounds() {
		AssetSounds[0] = "snd/clapping_hurray.ogg";
		AssetSounds[1] = "snd/Fireworks Finale-SoundBible.com-370363529.ogg";
		AssetSounds[2] = "snd/Red_stag_roar-Juan_Carlos_-2004708707.ogg";
		AssetSounds[3] = "snd/Fireworks Finale-SoundBible.com-370363529.ogg";
		AssetSounds[4] = "snd/clapping_hurray.ogg";
		AssetSounds[5] = "snd/clapping_hurray.ogg";
		AssetSounds[6] = "snd/chickens_demanding_food.ogg";
		AssetSounds[7] = "snd/Cow And Bell-SoundBible.com-1243222141.ogg";
		AssetSounds[8] = "snd/gobbler_bod.ogg";
		AssetSounds[9] = "snd/Toilet_Flush.ogg";
		AssetSounds[10] = "snd/ziegengatter.ogg";
		AssetSounds[11] = "snd/ziegengatter.ogg";
		AssetSounds[12] = "snd/Pew_Pew-DKnight556-1379997159.ogg";

	}

	public static void playSound(Context context, Sounds s) throws IOException {
		MainActivity main = (MainActivity) context;
		AssetManager assets = context.getAssets();
		if (main.colSounds.size() > 0) {
			File F = new File(main.colSounds.get(s).SoundPath);
			if (F.exists())
				playSound(F);
			else if (F.getPath().startsWith("snd/"))
				playSound(assets, F.getPath());
		} else {
			if (AssetSounds[0] == null)
				initSounds();
			playSound(assets, AssetSounds[s.ordinal()]);
		}
	}

	public static void playSound(AssetManager assets, String name)
			throws IOException {
		if (!sndEnabled)
			return;
		AssetFileDescriptor afd = assets.openFd(name);
		MediaPlayer player = new MediaPlayer();
		player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
				afd.getLength());
		player.prepare();
		player.start();
	}

	public static Sounds getSoundByNumber(int Zaehler) {
		for (int i = 0; i < Sounds.values().length; i++) {
			if (Sounds.values()[i].ordinal() == Zaehler)
				return Sounds.values()[i];
		}
		return null;
	}

	public static void playSound(Context context, int Zaehler)
			throws IOException {
		MainActivity main = (MainActivity) context;
		if (main.colSounds.size() > 0) {
			if (Zaehler < -4)
				Zaehler = -4;
			else if (Zaehler > 5)
				Zaehler = 5;
			lib.Sounds Sound = null;
			if (Zaehler <= 0 && lib.AntwWasRichtig==false)
				Zaehler = Math.abs(Zaehler - 6);

			Sound = getSoundByNumber(Zaehler);

			File F = new File(main.colSounds.get(Sound).SoundPath);
			if (F.exists())
				playSound(F);
			else if (F.getPath().startsWith("snd/"))
				playSound(context.getAssets(), F.getPath());
		} else {
			AssetManager assets = context.getAssets();
			if (AssetSounds[0] == null)
				initSounds();
			if (Zaehler < -4)
				Zaehler = -4;
			else if (Zaehler > 5)
				Zaehler = 5;
			if (Zaehler > 0)
				playSound(assets, AssetSounds[Zaehler - 1]);
			else if (Zaehler <= 0)
				playSound(assets, AssetSounds[Math.abs(Zaehler - 5)]);
		}

	}

	public static void playSound(File F) throws IOException {
		if (!sndEnabled)
			return;
		MediaPlayer player = new MediaPlayer();
		player.setDataSource(F.getPath());
		player.prepare();
		player.start();
	}

	public static Drawable scaleImage(Context context, Drawable image,
			float scaleFactor) {

		if ((image == null) || !(image instanceof BitmapDrawable)) {
			throw new RuntimeException("Not BitmapDrawable!");
		}

		Bitmap b = ((BitmapDrawable) image).getBitmap();

		int sizeX = Math.round(image.getIntrinsicWidth() * scaleFactor);
		int sizeY = Math.round(image.getIntrinsicHeight() * scaleFactor);

		Bitmap bitmapResized = Bitmap
				.createScaledBitmap(b, sizeX, sizeY, false);

		image = new BitmapDrawable(context.getResources(), bitmapResized);

		return image;

	}

	@SuppressWarnings("deprecation")
	public static Drawable getDefaultCheckBoxDrawable(Context context) {
		int resID = 0;

		if (Build.VERSION.SDK_INT <= 10) {
			// pre-Honeycomb has a different way of setting the CheckBox button
			// drawable
			resID = Resources.getSystem().getIdentifier("btn_check",
					"drawable", "android");
		} else {
			// starting with Honeycomb, retrieve the theme-based indicator as
			// CheckBox button drawable
			TypedValue value = new TypedValue();
			context.getApplicationContext()
					.getTheme()
					.resolveAttribute(
							android.R.attr.listChoiceIndicatorMultiple, value,
							true);
			resID = value.resourceId;
		}
		if (Build.VERSION.SDK_INT<22)
		{
			return context.getResources().getDrawable(resID);
		}
		else
		{
			return context.getResources().getDrawable(resID, null);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
	@SuppressWarnings("deprecation") 
	public static void setBgCheckBox(CheckBox c, Drawable d) 
	{ 
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) 
		{ 
			c.setBackgroundDrawable(d); 
		} 
		else 
		{ 
			c.setBackground(d); 
		} 
		
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
	@SuppressWarnings("deprecation") 
	public static void setBg(RelativeLayout l, ShapeDrawable rectShapeDrawable) 
	{ 
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) 
		{ 
			l.setBackgroundDrawable(rectShapeDrawable); 
		} 
		else 
		{ 
			l.setBackground(rectShapeDrawable); 
		} 
		
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
	@SuppressWarnings("deprecation") 
	public static void setBgEditText(EditText e, Drawable drawable) 
	{ 
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) 
		{ 
			e.setBackgroundDrawable(drawable); 
		} 
		else 
		{ 
			e.setBackground(drawable); 
		} 
		
	}
	
	public static int dpToPx(int dp)
	{
	    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public static int pxToDp(int px)
	{
	    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}
	
	public static final int SELECT_PICTURE = 0xa3b4;
	
	public static void SelectFile(Activity context)
	{
			Intent intent = new Intent();
			intent.setType("file/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			context.startActivityForResult(Intent.createChooser(intent,"Select File"), SELECT_PICTURE);
	}
	
	public static void removeLayoutListener(ViewTreeObserver observer,
			OnGlobalLayoutListener listener)
	{
		if (Build.VERSION.SDK_INT < 16) {
			removeLayoutListenerPre16(
					observer,listener);
		} else {
			removeLayoutListenerPost16(
					observer,listener);
		}
	}
		@SuppressWarnings("deprecation")
	private static void removeLayoutListenerPre16(ViewTreeObserver observer,
			OnGlobalLayoutListener listener) {
		observer.removeGlobalOnLayoutListener(listener);
	}

	@TargetApi(16)
	private static void removeLayoutListenerPost16(ViewTreeObserver observer,
			OnGlobalLayoutListener listener) {
		observer.removeOnGlobalLayoutListener(listener);
	}

}