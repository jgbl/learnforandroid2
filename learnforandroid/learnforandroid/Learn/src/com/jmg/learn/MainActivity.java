package com.jmg.learn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.thinkti.android.filechooser.*;

import com.jmg.learn.chart.IDemoChart;
import com.jmg.learn.vok.*;
import com.jmg.learn.vok.Vokabel.Bewertung;
import com.jmg.learn.vok.Vokabel.EnumSprachen;
import com.jmg.lib.*;
import com.jmg.lib.ColorSetting.ColorItems;
import com.jmg.lib.lib.Sounds;
import com.jmg.lib.lib.libString;

import android.support.v7.app.ActionBarActivity;
import android.text.Spanned;
import android.text.SpannedString;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends ActionBarActivity {

	private static final int FILE_CHOOSER = 34823;
	private static final int Settings_Activity = 34824;
	private Context context = this;
	private Button _btnRight;
	private Button _btnWrong;
	private Button _btnSkip;
	private Button _btnView;
	private Button _btnEdit;
	private BorderedTextView _txtWord;
	private BorderedTextView _txtKom;
	private BorderedTextView _txtStatus;
	private BorderedEditText _txtMeaning1;
	private BorderedEditText _txtMeaning2;
	private BorderedEditText _txtMeaning3;
	private float DisplayDurationWord;
	private float DisplayDurationBed;
	private int PaukRepetitions = 3;
	private double scale = 1;
	private boolean _blnEink;
	public HashMap<ColorItems, ColorSetting> Colors;
	public HashMap<Sounds, SoundSetting> colSounds;
	public Vokabel vok;
	public String CharsetASCII = "Windows-1252";
	public SharedPreferences prefs; // =
									// this.getPreferences(Context.MODE_PRIVATE);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		libLearn.gStatus = "onCreate getEink";
		try {
			_blnEink = getWindowManager().getDefaultDisplay().getRefreshRate() < 5.0;
			if (_blnEink)
				lib.ShowToast(this, "This is an Eink diplay!");
		} catch (Exception ex) {
			lib.ShowException(this, ex);
		}

		try {
			libLearn.gStatus = "onCreate setContentView";
			setContentView(R.layout.activity_main);

			Thread.setDefaultUncaughtExceptionHandler(ErrorHandler);
			try {
				libLearn.gStatus = "onCreate getPrefs";
				prefs = this.getPreferences(Context.MODE_PRIVATE);
				vok = new Vokabel(this,
						(TextView) this.findViewById(R.id.txtStatus));
				vok.setSchrittweite((short) prefs.getInt("Schrittweite", 6));
				CharsetASCII = prefs.getString("CharsetASCII", "Windows-1252");
				vok.CharsetASCII = CharsetASCII;
				vok.setAbfragebereich((short) prefs
						.getInt("Abfragebereich", -1));
				DisplayDurationWord = prefs.getFloat("DisplayDurationWord",
						1.5f);
				DisplayDurationBed = prefs.getFloat("DisplayDurationBed", 2.5f);
				PaukRepetitions = prefs.getInt("PaukRepetitions", 3);
				vok.ProbabilityFactor = prefs
						.getFloat("ProbabilityFactor", -1f);
				vok.setAbfrageZufaellig(prefs.getBoolean("Random",
						vok.getAbfrageZufaellig()));
				vok.setAskAll(prefs.getBoolean("AskAll", vok.getAskAll()));
				lib.sndEnabled = prefs.getBoolean("Sound", lib.sndEnabled);
				Colors = getColorsFromPrefs();
				colSounds = getSoundsFromPrefs();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				lib.ShowException(this, e);
			}
			libLearn.gStatus = "onCreate Copy Assets";
			CopyAssets();
			try {
				libLearn.gStatus = "onCreate InitButtons";
				InitButtons();
				libLearn.gStatus = "onCreate InitMeanings";
				InitMeanings();
				if (savedInstanceState != null) {
					libLearn.gStatus = "onCreate Load SavedInstanceState";
					String filename = savedInstanceState.getString("vokpath");
					int index = savedInstanceState.getInt("vokindex");
					int[] Lernvokabeln = savedInstanceState
							.getIntArray("Lernvokabeln");
					int Lernindex = savedInstanceState.getInt("Lernindex");
					if (!libString.IsNullOrEmpty(filename) && index > 0) {
						LoadVokabel(filename, index, Lernvokabeln, Lernindex);
					}
				} else {
					if (prefs.getString("LastFile", null) != null) {
						libLearn.gStatus = "onCreate Load Lastfile";
						String filename = prefs.getString("LastFile", "");
						int index = prefs.getInt("vokindex", 1);
						int[] Lernvokabeln = lib.getIntArrayFromPrefs(prefs,
								"Lernvokabeln");
						int Lernindex = prefs.getInt("Lernindex", 0);
						boolean Unicode = prefs.getBoolean("Unicode", true);
						_blnUniCode = Unicode;
						if (Lernvokabeln != null) {
							LoadVokabel(filename, index, Lernvokabeln,
									Lernindex);
						} else {
							LoadVokabel(filename, 1, null, 0);
						}
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				lib.ShowException(this, e);
			}

		} catch (Exception ex) {
			lib.ShowException(this, ex);
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
	protected void onSaveInstanceState(Bundle outState) {
		saveVok(true);
		// outState.putParcelable("vok", vok);
		outState.putString("vokpath", vok.getFileName());
		outState.putInt("vokindex", vok.getIndex());
		outState.putIntArray("Lernvokabeln", vok.getLernvokabeln());
		outState.putInt("Lernindex", vok.getLernIndex());

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		if (_backPressed > 0 || saveVok(false))
			super.onBackPressed();
		return;
	}

	/*
	 * @Override public void onAttachedToWindow() {
	 * super.onAtFILE_CHOOSERtachedToWindow();
	 * this.getWindow().setType(WindowManager
	 * .LayoutParams.TYPE_KEYGUARD_DIALOG); }
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_HOME) {
			saveVok(false);
		}
		return super.onKeyDown(keyCode, event);
	};

	private int _backPressed;

	private boolean saveVok(boolean dontPrompt) {
		Handler handler;
		if (vok.aend) {
			if (!dontPrompt) {
				AlertDialog.Builder A = new AlertDialog.Builder(context);
				A.setPositiveButton(getString(R.string.yes),
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									vok.SaveFile(vok.getFileName(),
											vok.getUniCode());
									vok.aend = false;
									_backPressed += 1;
									Handler handler = new Handler();
									handler.postDelayed(rSetBackPressedFalse,
											10000);
									saveFilePrefs();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									lib.ShowException(MainActivity.this, e);
								}
							}
						});
				A.setNegativeButton(getString(R.string.no),
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								lib.ShowToast(
										MainActivity.this,
										MainActivity.this
												.getString(R.string.PressBackAgain));
								_backPressed += 1;
								Handler handler = new Handler();
								handler.postDelayed(rSetBackPressedFalse, 10000);
							}

						});
				A.setMessage(getString(R.string.Save));
				A.setTitle("Question");
				A.show();
				if (!dontPrompt) {
					if (_backPressed > 0) {
						return true;
					} else {
						lib.ShowToast(this,
								this.getString(R.string.PressBackAgain));
						_backPressed += 1;
						handler = new Handler();
						handler.postDelayed(rSetBackPressedFalse, 10000);
					}

				}
			}
			if (dontPrompt) {
				try {
					vok.SaveFile();
					vok.aend = false;
					_backPressed += 1;
					handler = new Handler();
					handler.postDelayed(rSetBackPressedFalse, 10000);
					saveFilePrefs();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					lib.ShowException(this, e);
				}
			}
			return false;
		}
		return true;
	}

	private void saveFilePrefs() {
		Editor edit = prefs.edit();
		edit.putString("LastFile", vok.getFileName()).commit();
		edit.putInt("vokindex", vok.getIndex());
		lib.putIntArrayToPrefs(prefs, vok.getLernvokabeln(), "Lernvokabeln");
		edit.putInt("Lernindex", vok.getLernIndex());
		edit.putBoolean("Unicode", vok.getUniCode());
		edit.commit();
	}

	private Runnable rSetBackPressedFalse = new Runnable() {
		@Override
		public void run() {
			/* do what you need to do */
			_backPressed = 0;
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * if (_firstFocus) { _firstFocus = false; hideKeyboard(); }
		 */
		// resize();
	}

	@Override
	protected void onStart() {
		super.onStart();
		/*
		 * if (_firstFocus) { _firstFocus = false; hideKeyboard(); }
		 */
		// resize();
	}

	private int _lastIsWrongVokID;

	private void InitButtons() throws Exception {
		View v = findViewById(R.id.btnRight);
		Button b = (Button) v;
		_btnRight = b;
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (_lastIsWrongVokID == vok.getIndex()) {
						lib.playSound(MainActivity.this, Sounds.Beep);
						getVokabel(false, true);
					} else {

						int Zaehler = vok.AntwortRichtig();
						lib.playSound(MainActivity.this, Zaehler);

						getVokabel(false, false);
					}
					_lastIsWrongVokID = -1;

				} catch (Exception e) {
					// TODO Auto-generated catch block
					lib.ShowException(MainActivity.this, e);

				}

			}
		});
		v = findViewById(R.id.btnWrong);
		b = (Button) v;
		_btnWrong = b;
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					vok.AntwortFalsch();
					lib.playSound(MainActivity.this, vok.getZaehler());
					_lastIsWrongVokID = vok.getIndex();
					setBtnsEnabled(false);
					flashwords();
					// getVokabel(false,true);
					// runFlashWords();
					Handler handler = new Handler();
					handler.postDelayed(
							runnableFalse,
							(long) ((DisplayDurationWord * 1000 + vok
									.getAnzBed() * 1000 * DisplayDurationBed) * PaukRepetitions));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					lib.ShowException(MainActivity.this, e);
				}

			}
		});
		v = findViewById(R.id.btnSkip);
		b = (Button) v;
		_btnSkip = b;
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					vok.SkipVokabel();
					getVokabel(false, false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					lib.ShowException(MainActivity.this, e);
				}

			}
		});
		v = findViewById(R.id.btnView);
		b = (Button) v;
		_btnView = b;
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					getVokabel(true, false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					lib.ShowException(MainActivity.this, e);
				}

			}
		});

		v = findViewById(R.id.btnEdit);
		b = (Button) v;
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

		_txtMeaning1 = (BorderedEditText) findViewById(R.id.txtMeaning1);
		_txtMeaning1.setBackgroundResource(0);
		_txtMeaning2 = (BorderedEditText) findViewById(R.id.txtMeaning2);
		_txtMeaning2.setBackgroundResource(0);
		_txtMeaning3 = (BorderedEditText) findViewById(R.id.txtMeaning3);
		_txtMeaning3.setBackgroundResource(0);
		_txtWord = (BorderedTextView) findViewById(R.id.word);
		_txtKom = (BorderedTextView) findViewById(R.id.Comment);
		_txtStatus = (BorderedTextView) findViewById(R.id.txtStatus);
		setBtnsEnabled(false);
		setTextColors();
	}

	private void InitMeanings() {
		_txtMeaning1.setOnEditorActionListener(EditorActionListener);
		_txtMeaning2.setOnEditorActionListener(EditorActionListener);
		_txtMeaning3.setOnEditorActionListener(EditorActionListener);
	}

	private void hideKeyboard() {
		// Check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private OnEditorActionListener EditorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if (event == null) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					hideKeyboard();
					String[] Antworten;
					com.jmg.learn.vok.Vokabel.Bewertung Bew;
					String meaning1 = _txtMeaning1.getText().toString();
					String meaning2 = _txtMeaning2.getVisibility() == View.VISIBLE ? _txtMeaning2
							.getText().toString() : "";
					String meaning3 = _txtMeaning3.getVisibility() == View.VISIBLE ? _txtMeaning3
							.getText().toString() : "";
					Antworten = new String[] { meaning1, meaning2, meaning3 };
					try {
						Bew = vok.CheckAntwort(Antworten);
						if (Bew == Bewertung.AllesRichtig) {
							lib.ShowToast(MainActivity.this,
									getString(R.string.AnswerCorrect));
							_btnRight.performClick();
						} else if (Bew == Bewertung.AllesFalsch) {
							try {
								vok.AntwortFalsch();
								lib.playSound(MainActivity.this,
										vok.getZaehler());
								_lastIsWrongVokID = vok.getIndex();
								setBtnsEnabled(false);
								getVokabel(true, false);
								flashwords();
								Handler handler = new Handler();
								handler.postDelayed(
										runnableFalse,
										(long) ((DisplayDurationWord * 1000 + vok
												.getAnzBed()
												* 1000
												* DisplayDurationBed) * PaukRepetitions));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								lib.ShowException(MainActivity.this, e);
							}
						} else if (Bew == Bewertung.aehnlich) {
							lib.ShowMessage(MainActivity.this,
									getString(R.string.MeaningSimilar));
						} else if (Bew == Bewertung.TeilweiseRichtig) {
							lib.ShowMessage(MainActivity.this,
									getString(R.string.MeaningPartiallyCorrect));
						} else if (Bew == Bewertung.enthalten) {
							lib.ShowMessage(MainActivity.this,
									getString(R.string.MeaningIsSubstring));
						} else if (Bew == Bewertung.AehnlichEnthalten) {
							lib.ShowMessage(
									MainActivity.this,
									getString(R.string.MeaningIsSubstringSimilar));
						} else if (Bew == Bewertung.TeilweiseRichtigAehnlich) {
							lib.ShowMessage(
									MainActivity.this,
									getString(R.string.MeaningIsPartiallyCorrectSimilar));
						} else if (Bew == Bewertung.TeilweiseRichtigAehnlichEnthalten) {
							lib.ShowMessage(
									MainActivity.this,
									getString(R.string.MeaningIsPartiallyCorrectSimilarSubstring));
						} else if (Bew == Bewertung.TeilweiseRichtigEnthalten) {
							lib.ShowMessage(
									MainActivity.this,
									getString(R.string.MeaningIsPartiallyCorrectSubstring));
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						lib.ShowException(MainActivity.this, e);
					}
					return true;
				}
				// Capture soft enters in a singleLine EditText that is the last
				// EditText.
				else if (actionId == EditorInfo.IME_ACTION_NEXT)
					return false;
				// Capture soft enters in other singleLine EditTexts

				else
					return false; // Let system handle all other null KeyEvents

			} else if (actionId == EditorInfo.IME_NULL) {
				// Capture most soft enters in multi-line EditTexts and all hard
				// enters.
				// They supply a zero actionId and a valid KeyEvent rather than
				// a non-zero actionId and a null event like the previous cases.
				if (event.getAction() == KeyEvent.ACTION_DOWN)
					return false;
				// We capture the event when key is first pressed.
				else
					return false; // We consume the event when the key is
									// released.
			} else
				return false;
		}
	};

	private void setTextColors() {
		libLearn.gStatus = "setTextColors";
		_txtMeaning1.setTextColor(Colors.get(ColorItems.meaning).ColorValue);
		_txtMeaning2.setTextColor(Colors.get(ColorItems.meaning).ColorValue);
		_txtMeaning3.setTextColor(Colors.get(ColorItems.meaning).ColorValue);
		_txtWord.setTextColor(Colors.get(ColorItems.word).ColorValue);
		_txtKom.setTextColor(Colors.get(ColorItems.comment).ColorValue);
		/*
		 * _txtMeaning1.setBackgroundColor(Colors.get(ColorItems.background).
		 * ColorValue);
		 * _txtMeaning2.setBackgroundColor(Colors.get(ColorItems.background
		 * ).ColorValue);
		 * _txtMeaning3.setBackgroundColor(Colors.get(ColorItems.background
		 * ).ColorValue);
		 * _txtWord.setBackgroundColor(Colors.get(ColorItems.background
		 * ).ColorValue);
		 * _txtKom.setBackgroundColor(Colors.get(ColorItems.background
		 * ).ColorValue);
		 */
		findViewById(R.id.layoutMain).setBackgroundColor(
				Colors.get(ColorItems.background).ColorValue);
	}

	private void setBtnsEnabled(boolean enable) {
		libLearn.gStatus = "setBtnsEnabled";
		_btnEdit.setEnabled(enable);
		_btnRight.setEnabled(enable);
		_btnSkip.setEnabled(enable);
		_btnView.setEnabled(enable);
		_btnWrong.setEnabled(enable);

	}

	/*
	 * private Runnable runnableGetVok = new Runnable() {
	 * 
	 * @Override public void run() { // do what you need to do
	 * getVokabel(false,true); } };
	 */
	private Runnable runnableFalse = new Runnable() {
		@Override
		public void run() {
			/* do what you need to do */
			getVokabel(false, false);
		}
	};

	/*
	 * private void runFlashWords() { new Thread(new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub try {
	 * flashwords(); } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } }).start(); }
	 */
	private void flashwords() throws Exception {
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutMain);
		layout.setBackgroundColor(Colors.get(ColorItems.background_wrong).ColorValue);
		Handler handler = new Handler();
		long delay = 0;
		for (int i = 0; i < PaukRepetitions; i++) {
			// _txtWord.setBackgroundResource(R.layout.roundedbox);
			handler.postDelayed(new showWordBordersTask(), delay);
			delay += DisplayDurationWord * 1000;
			handler.postDelayed(new hideWordBordersTask(), delay);
			BorderedEditText Beds[] = { _txtMeaning1, _txtMeaning2,
					_txtMeaning3 };
			for (int ii = 0; ii < vok.getAnzBed(); ii++) {
				handler.postDelayed(new showBedBordersTask(Beds[ii]), delay);
				delay += DisplayDurationBed * 1000;
				handler.postDelayed(new hideBedBordersTask(Beds[ii]), delay);
			}

		}
		handler.postDelayed(new resetLayoutTask(layout), delay);
		delay += 1000;

	}

	private class resetLayoutTask implements Runnable {
		public RelativeLayout layout;

		public resetLayoutTask(RelativeLayout layout) {
			// TODO Auto-generated constructor stub
			this.layout = layout;
		}

		public void run() {
			layout.setBackgroundColor(Colors.get(ColorItems.background).ColorValue);
		}
	}

	private class showWordBordersTask implements Runnable {
		public void run() {
			try {
				lib.playSound(MainActivity.this, com.jmg.lib.lib.Sounds.Beep);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showWordBorders();
		}
	}

	private class hideWordBordersTask implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			hideWordBorders();
		}

	}

	private class showBedBordersTask implements Runnable {
		public BorderedEditText Bed;

		public showBedBordersTask(BorderedEditText Bed) {
			// TODO Auto-generated constructor stub
			this.Bed = Bed;
		}

		public void run() {
			// TODO Auto-generated method stub
			// Bed.setPadding(5, 5, 5, 5);
			Bed.setShowBorders(true,
					Colors.get(ColorItems.box_meaning).ColorValue);
			try {
				lib.playSound(MainActivity.this, com.jmg.lib.lib.Sounds.Beep);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class hideBedBordersTask implements Runnable {
		public BorderedEditText Bed;

		public hideBedBordersTask(BorderedEditText Bed) {
			// TODO Auto-generated constructor stub
			this.Bed = Bed;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// Bed.setPadding(0, 0, 0, 0);
			Bed.setShowBorders(false,
					Colors.get(ColorItems.background).ColorValue);
		}

	}

	/*
	 * private class CancelTimerTask extends TimerTask { public Timer T; public
	 * CancelTimerTask(Timer T) { // TODO Auto-generated constructor stub this.T
	 * = T; }
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * T.cancel(); }
	 * 
	 * }
	 */
	private void showWordBorders() {
		// TODO Auto-generated method stub
		// _txtWord.setPadding(5, 5, 5, 5);
		_txtWord.setShowBorders(true,
				Colors.get(ColorItems.box_word).ColorValue);
	}

	private void hideWordBorders() {
		// TODO Auto-generated method stub
		// _txtWord.setPadding(0, 0, 0, 0);
		_txtWord.setShowBorders(false,
				Colors.get(ColorItems.background).ColorValue);
	}

	private boolean _firstFocus;

	private void resize() {
		_firstFocus = true;
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		int height = metrics.heightPixels;
		int viewTop = findViewById(Window.ID_ANDROID_CONTENT).getTop();
		height = height - viewTop;
		scale = (double) height / (double) 950;
		/*
		 * lib.ShowMessage(this, "Meaning3 Bottom: " +_txtMeaning3.getBottom() +
		 * "\nbtnRight.Top: " + _btnRight.getTop() + "\nDisplayHeight: " +
		 * height);
		 */
		if (scale != 1) {
			lib.ShowToast(this, "Scaling font by " + scale + " Screenheight = "
					+ height);
			_txtMeaning1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_txtMeaning1.getTextSize() * scale));
			_txtMeaning2.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_txtMeaning2.getTextSize() * scale));
			_txtMeaning3.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_txtMeaning3.getTextSize() * scale));
			_txtWord.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_txtWord.getTextSize() * scale));
			_txtKom.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_txtKom.getTextSize() * scale));
			_txtStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_txtStatus.getTextSize() * scale));

			/*
			 * _txtMeaning1.setOnFocusChangeListener(new
			 * View.OnFocusChangeListener() {
			 * 
			 * @Override public void onFocusChange(View v, boolean hasFocus) {
			 * // TODO Auto-generated method stub if (_firstFocus && hasFocus) {
			 * hideKeyboard(); _firstFocus = false; } } });
			 */

			_btnRight.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_btnRight.getTextSize() * scale));
			_btnSkip.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_btnSkip.getTextSize() * scale));
			_btnView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_btnView.getTextSize() * scale));
			_btnWrong.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_btnWrong.getTextSize() * scale));
			_btnEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					(float) (_btnEdit.getTextSize() * scale));

			RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) _txtMeaning1
					.getLayoutParams();
			params.topMargin = (int) (params.topMargin * scale);
			_txtMeaning1.setLayoutParams(params);
			params = (android.widget.RelativeLayout.LayoutParams) _txtMeaning2
					.getLayoutParams();
			params.topMargin = (int) (params.topMargin * scale);
			_txtMeaning2.setLayoutParams(params);
			params = (android.widget.RelativeLayout.LayoutParams) _txtMeaning3
					.getLayoutParams();
			params.topMargin = (int) (params.topMargin * scale);
			_txtMeaning3.setLayoutParams(params);
			params = (android.widget.RelativeLayout.LayoutParams) _btnRight
					.getLayoutParams();
			params.height = (int) (params.height * scale);
			params.width = (int) (params.width * scale);
			_btnRight.setLayoutParams(params);
			params = (android.widget.RelativeLayout.LayoutParams) _btnWrong
					.getLayoutParams();
			params.height = (int) (params.height * scale);
			params.width = (int) (params.width * scale);
			_btnWrong.setLayoutParams(params);
			params = (android.widget.RelativeLayout.LayoutParams) _btnSkip
					.getLayoutParams();
			params.height = (int) (params.height * scale);
			params.width = (int) (params.width * scale);
			_btnSkip.setLayoutParams(params);
			params = (android.widget.RelativeLayout.LayoutParams) _btnView
					.getLayoutParams();
			params.height = (int) (params.height * scale);
			params.width = (int) (params.width * scale);
			_btnView.setLayoutParams(params);
			params = (android.widget.RelativeLayout.LayoutParams) _btnEdit
					.getLayoutParams();
			params.height = (int) (params.height * scale);
			params.width = (int) (params.width * scale);
			_btnEdit.setLayoutParams(params);

		}
	}

	public String JMGDataDirectory;

	private void CopyAssets() {
		libLearn.gStatus = "Copy Assets";
		File F = android.os.Environment.getExternalStorageDirectory();
		String extPath = F.getPath();
		JMGDataDirectory = Path.combine(extPath, "learnforandroid", "vok");

		if (F.isDirectory() && F.exists()) {
			File F1 = new File(JMGDataDirectory);
			if (F1.isDirectory() == false && !F1.exists()) {
				F1.mkdirs();
			}
			AssetManager A = this.getAssets();
			try {
				final String languages[] = new String[] { "Greek", "Hebrew",
						"KAR", "Spanish" };
				final String path = F1.getPath();
				for (String language : languages) {
					F1 = new File(Path.combine(path, language));
					for (String File : A.list(language)) {
						InputStream myInput = A.open(Path.combine(language,
								File));
						String outFileName = Path.combine(F1.getPath(), File);
						if (F1.isDirectory() == false) {
							F1.mkdirs();
						}
						// Open the empty db as the output stream

						File file = new File(outFileName);

						if (file.exists()) {
							// file.delete();
						} else {
							file.createNewFile();
							OutputStream myOutput = new FileOutputStream(file);

							byte[] buffer = new byte[1024];
							int length;
							while ((length = myInput.read(buffer, 0, 1024)) > 0) {
								myOutput.write(buffer, 0, length);
							}

							// Close the streams
							myOutput.flush();
							myOutput.close();
							myInput.close();
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				lib.ShowException(this, e);
				// lib.ShowMessage(this, "CopyAssets");
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		try {
			getMenuInflater().inflate(R.menu.main, menu);
			resize();
			return true;
		} catch (Exception ex) {
			lib.ShowException(this, ex);
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			ShowSettings();
		} else if (id == R.id.mnuFileOpen) {
			LoadFile(true);
		} else if (id == R.id.mnuFileOpenASCII) {
			LoadFile(false);
		} else if (id == R.id.mnuFileSave) {
			saveVok(false);
		} else if (id == R.id.mnuDelete) {
			vok.DeleteVokabel();
			getVokabel(false, false);
		} else if (id == R.id.mnuReverse) {
			vok.revert();
			getVokabel(false, false);
		} else if (id == R.id.mnuReset) {
			if (lib.ShowMessageYesNo(this,
					this.getString(R.string.ResetVocabulary))) {
				vok.reset();
			}

		} else if (id == R.id.mnuStatistics) {
			if (vok.getGesamtzahl() > 5) {
				try {
					IDemoChart chart = new com.jmg.learn.chart.LearnBarChart();
					Intent intent = chart.execute(this);
					this.startActivity(intent);
				} catch (Exception ex) {
					lib.ShowException(this, ex);
				}

			}
		}
		return super.onOptionsItemSelected(item);
	}

	public void LoadFile(boolean blnUniCode) {
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
		extensions.add(".dic");
		extensions.add(".DIC");

		intent.putStringArrayListExtra("filterFileExtension", extensions);
		intent.putExtra("blnUniCode", blnUniCode);
		intent.putExtra("DefaultDir",
				new File(JMGDataDirectory).exists() ? JMGDataDirectory
						: "/sdcard/");
		_blnUniCode = blnUniCode;

		this.startActivityForResult(intent, FILE_CHOOSER);
	}

	public void ShowSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		intent.putExtra("Abfragebereich", vok.getAbfragebereich());
		intent.putExtra("CharsetASCII", vok.CharsetASCII);
		intent.putExtra("Step", vok.getSchrittweite());
		intent.putExtra("DisplayDurationWord", DisplayDurationWord);
		intent.putExtra("DisplayDurationBed", DisplayDurationBed);
		intent.putExtra("PaukRepetitions", PaukRepetitions);
		float ProbFact = vok.ProbabilityFactor;
		intent.putExtra("ProbabilityFactor", ProbFact);
		intent.putExtra("Random", vok.getAbfrageZufaellig());
		intent.putExtra("AskAll", vok.getAskAll());
		intent.putExtra("Sound", lib.sndEnabled);
		this.startActivityForResult(intent, Settings_Activity);
	}

	boolean _blnUniCode = true;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if ((requestCode == FILE_CHOOSER)
					&& (resultCode == Activity.RESULT_OK)) {
				String fileSelected = data.getStringExtra("fileSelected");
				LoadVokabel(fileSelected, 1, null, 0);

			} else if ((requestCode == Settings_Activity)
					&& (resultCode == Activity.RESULT_OK)) {
				libLearn.gStatus = "getting values from intent";
				int oldAbfrage = vok.getAbfragebereich();
				vok.setAbfragebereich(data.getExtras().getShort(
						"Abfragebereich"));
				if (oldAbfrage != vok.getAbfragebereich())
					vok.ResetAbfrage();
				vok.setSchrittweite(data.getExtras().getShort("Step"));
				vok.CharsetASCII = (data.getExtras().getString("CharsetASCII"));
				DisplayDurationWord = data.getExtras().getFloat(
						"DisplayDurationWord");
				DisplayDurationBed = data.getExtras().getFloat(
						"DisplayDurationBed");
				PaukRepetitions = data.getExtras().getInt("PaukRepetitions");
				vok.ProbabilityFactor = data.getExtras().getFloat(
						"ProbabilityFactor");
				vok.setAbfrageZufaellig(data.getExtras().getBoolean("Random"));
				vok.setAskAll(data.getExtras().getBoolean("AskAll"));
				lib.sndEnabled = data.getExtras().getBoolean("Sound");
				Colors = getColorsFromIntent(data);
				colSounds = getSoundsFromIntent(data);

				libLearn.gStatus = "writing values to prefs";
				Editor editor = prefs.edit();
				editor.putInt("Schrittweite", vok.getSchrittweite());
				editor.putString("CharsetASCII", vok.CharsetASCII);
				editor.putInt("Abfragebereich", vok.getAbfragebereich());
				editor.putFloat("DisplayDurationWord", DisplayDurationWord);
				editor.putFloat("DisplayDurationBed", DisplayDurationBed);
				editor.putInt("PaukRepetitions", PaukRepetitions);
				editor.putFloat("ProbabilityFactor", vok.ProbabilityFactor);
				editor.putBoolean("Random", vok.getAbfrageZufaellig());
				editor.putBoolean("AskAll", vok.getAskAll());
				editor.putBoolean("Sound", lib.sndEnabled);

				for (ColorItems item : Colors.keySet()) {
					editor.putInt(item.name(), Colors.get(item).ColorValue);
				}

				for (Sounds item : colSounds.keySet()) {
					editor.putString(item.name(), colSounds.get(item).SoundPath);
				}

				editor.commit();
				libLearn.gStatus = "setTextColors";
				setTextColors();
				libLearn.gStatus = "getVokabel";
				getVokabel(false, false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			lib.ShowException(MainActivity.this, e);
		}
	}

	public void LoadVokabel(String fileSelected, int index, int[] Lernvokabeln,
			int Lernindex) {
		try {
			saveVok(false);

			vok.LoadFile(fileSelected, false, false, _blnUniCode);
			if (vok.getCardMode()) {
				_txtMeaning1.setSingleLine(false);
				_txtMeaning1.setMaxLines(30);
				_txtMeaning1.setLines(16);
				_txtMeaning1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						(float) (20 * scale));
				// _txtMeaning1.setImeOptions(EditorInfo.IME_NULL);
				// _txtMeaning1.setImeActionLabel(null, KeyEvent.KEYCODE_ENTER);
				// _txtMeaning1.setImeActionLabel("Custom text",
				// KeyEvent.KEYCODE_ENTER);
				_txtMeaning2.setVisibility(View.GONE);
				_txtMeaning3.setVisibility(View.GONE);
			} else {
				_txtMeaning1.setLines(1);
				_txtMeaning1.setSingleLine();
				_txtMeaning1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						(float) (40 * scale));
				_txtMeaning1.setMaxLines(3);
				_txtMeaning1.setHorizontallyScrolling(false);
				_txtMeaning2.setVisibility(View.VISIBLE);
				_txtMeaning2.setLines(1);
				_txtMeaning2.setSingleLine();
				_txtMeaning2.setMaxLines(3);
				_txtMeaning2.setHorizontallyScrolling(false);
				_txtMeaning3.setVisibility(View.VISIBLE);
				_txtMeaning3.setLines(1);
				_txtMeaning3.setSingleLine();
				_txtMeaning3.setMaxLines(3);
				_txtMeaning3.setHorizontallyScrolling(false);

			}

			// if (index >0 ) vok.setIndex(index);
			if (Lernvokabeln != null)
				vok.setLernvokabeln(Lernvokabeln);
			if (Lernindex > 0)
				vok.setLernIndex((short) Lernindex);
			if (vok.getGesamtzahl() > 1)
				setBtnsEnabled(true);
			getVokabel(false, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			lib.ShowException(this, e);
		}
	}

	private HashMap<ColorItems, ColorSetting> getColorsFromPrefs() {
		HashMap<ColorItems, ColorSetting> res = new HashMap<ColorItems, ColorSetting>();
		for (int i = 0; i < ColorSetting.ColorItems.values().length; i++) {
			ColorItems ColorItem = ColorSetting.ColorItems.values()[i];
			String Name = getResources().getStringArray(R.array.spnColors)[i];
			int defValue = 0;
			switch (ColorItem) {
			case word:
				defValue = 0xff000000;
				break;
			case meaning:
				defValue = 0xff000000;
				break;
			case comment:
				defValue = 0xff000000;
				break;
			case background:
				defValue = 0xffffffff;
				break;
			case background_wrong:
				defValue = 0xffc0c0c0;
				break;
			case box_word:
				defValue = 0xffffffff;
				break;
			case box_meaning:
				defValue = 0xffffffff;
				break;
			default:
				defValue = 0xff000000;
				break;
			}
			int Color = prefs.getInt(ColorItem.name(), defValue);
			res.put(ColorItem, new ColorSetting(ColorItem, Name, Color));
		}
		return res;

	}

	private HashMap<ColorItems, ColorSetting> getColorsFromIntent(Intent intent) {
		HashMap<ColorItems, ColorSetting> res = new HashMap<ColorItems, ColorSetting>();
		for (int i = 0; i < ColorSetting.ColorItems.values().length; i++) {
			ColorItems ColorItem = ColorSetting.ColorItems.values()[i];
			String Name = getResources().getStringArray(R.array.spnColors)[i];
			int defValue = 0;
			switch (ColorItem) {
			case word:
				defValue = 0xff000000;
				break;
			case meaning:
				defValue = 0xff000000;
				break;
			case comment:
				defValue = 0xff000000;
				break;
			case background:
				defValue = 0xffffffff;
				break;
			case background_wrong:
				defValue = 0xffc0c0c0;
				break;
			case box_word:
				defValue = 0xffffffff;
				break;
			case box_meaning:
				defValue = 0xffffffff;
				break;
			default:
				defValue = 0xff000000;
				break;
			}
			int Color = intent.getIntExtra(ColorItem.name(), defValue);
			res.put(ColorItem, new ColorSetting(ColorItem, Name, Color));
		}
		return res;

	}

	private HashMap<Sounds, SoundSetting> getSoundsFromIntent(Intent intent) {
		HashMap<Sounds, SoundSetting> res = new HashMap<Sounds, SoundSetting>();
		if (lib.AssetSounds[0] == null)
			lib.initSounds();
		for (int i = 0; i < lib.Sounds.values().length; i++) {
			Sounds SoundItem = Sounds.values()[i];
			String Name = getResources().getStringArray(R.array.spnSounds)[i];
			String defValue = "";
			defValue = lib.AssetSounds[SoundItem.ordinal()];
			String SoundPath = intent.getStringExtra(SoundItem.name());
			if (libString.IsNullOrEmpty(SoundPath)) {
				SoundPath = defValue;
			}
			res.put(SoundItem, new SoundSetting(SoundItem, Name, SoundPath));
		}
		return res;

	}

	private HashMap<Sounds, SoundSetting> getSoundsFromPrefs() {
		HashMap<Sounds, SoundSetting> res = new HashMap<Sounds, SoundSetting>();
		if (lib.AssetSounds[0] == null)
			lib.initSounds();
		for (int i = 0; i < lib.Sounds.values().length; i++) {
			Sounds SoundItem = Sounds.values()[i];
			String Name = getResources().getStringArray(R.array.spnSounds)[i];
			String defValue = "";
			defValue = lib.AssetSounds[SoundItem.ordinal()];
			String SoundPath = prefs.getString(SoundItem.name(), defValue);
			res.put(SoundItem, new SoundSetting(SoundItem, Name, SoundPath));
		}
		return res;

	}

	public void getVokabel(boolean showBeds, boolean LoadNext) {
		try {
			setBtnsEnabled(true);
			if (showBeds) {
				_btnRight.setEnabled(true);
				_btnWrong.setEnabled(true);
			} else {
				_btnRight.setEnabled(false);
				_btnWrong.setEnabled(false);
			}
			if (LoadNext)
				vok.setLernIndex((short) (vok.getLernIndex() + 1));

			View v = findViewById(R.id.word);
			TextView t = (TextView) v;
			t.setText(getSpanned(vok.getWort()), TextView.BufferType.SPANNABLE);
			if (vok.getSprache() == EnumSprachen.Hebrew
					|| vok.getSprache() == EnumSprachen.Griechisch
					|| (vok.getFontWort().getName() == "Cardo")) {
				t.setTypeface(vok.TypefaceCardo);
			} else {
				t.setTypeface(Typeface.DEFAULT);
			}

			v = findViewById(R.id.Comment);
			t = (TextView) v;
			t.setText(getSpanned(vok.getKommentar()),
					TextView.BufferType.SPANNABLE);
			if (vok.getSprache() == EnumSprachen.Hebrew
					|| vok.getSprache() == EnumSprachen.Griechisch
					|| (vok.getFontKom().getName() == "Cardo")) {
				t.setTypeface(vok.TypefaceCardo);
			} else {
				t.setTypeface(Typeface.DEFAULT);
			}

			v = findViewById(R.id.txtMeaning1);
			t = (TextView) v;
			t.setText((showBeds ? vok.getBedeutung1() : ""));
			if (vok.getFontBed().getName() == "Cardo") {
				t.setTypeface(vok.TypefaceCardo);
			} else {
				t.setTypeface(Typeface.DEFAULT);
			}

			v = findViewById(R.id.txtMeaning2);
			t = (TextView) v;
			t.setText((showBeds ? vok.getBedeutung2() : ""));
			if (vok.getFontBed().getName() == "Cardo") {
				t.setTypeface(vok.TypefaceCardo);
			} else {
				t.setTypeface(Typeface.DEFAULT);
			}
			if (libString.IsNullOrEmpty(vok.getBedeutung2())
					|| vok.getCardMode()) {
				t.setVisibility(View.GONE);
				_txtMeaning1.setImeOptions(EditorInfo.IME_ACTION_DONE);
			} else {
				t.setVisibility(View.VISIBLE);
				_txtMeaning1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			}

			v = findViewById(R.id.txtMeaning3);
			t = (TextView) v;
			t.setText((showBeds ? vok.getBedeutung3() : ""));
			if (vok.getFontBed().getName() == "Cardo") {
				t.setTypeface(vok.TypefaceCardo);
			} else {
				t.setTypeface(Typeface.DEFAULT);
			}
			if (libString.IsNullOrEmpty(vok.getBedeutung3())
					|| vok.getCardMode()) {
				t.setVisibility(View.GONE);
				_txtMeaning2.setImeOptions(EditorInfo.IME_ACTION_DONE);
			} else {
				t.setVisibility(View.VISIBLE);
				_txtMeaning2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				_txtMeaning3.setImeOptions(EditorInfo.IME_ACTION_DONE);
			}

			if (vok.getGesamtzahl() > 5) {
				getSupportActionBar().setTitle(
						"Learn " + (new File(vok.getFileName())).getName()
								+ " " + getString(R.string.number) + ": "
								+ vok.getIndex() + " "
								+ getString(R.string.counter) + ": "
								+ vok.getZaehler());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			lib.ShowException(this, e);
		}

	}

	public Spanned getSpanned(String txt) throws IOException {
		if (txt.startsWith("{\\rtf1\\")) {
			// txt = Java2Html.convertToHtml(txt,
			// JavaSourceConversionOptions.getDefault());
			// return Html.fromHtml(txt);
			// return new SpannedString(stripRtf(txt));
		}
		return new SpannedString(txt);

	}

	public String stripRtf(String str) {
		String basicRtfPattern = "/\\{\\*?\\\\[^{}]+}|[{}]|\\\\[A-Za-z]+\\n?(?:-?\\d+)?[ ]?/g";
		String newLineSlashesPattern = "/\\\\\\n/g";

		String stripped = str.replaceAll(basicRtfPattern, "");
		String removeNewlineSlashes = stripped.replaceAll(
				newLineSlashesPattern, "\n");
		String removeWhitespace = removeNewlineSlashes.trim();

		return removeWhitespace;
	}
}
