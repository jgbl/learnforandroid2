package com.jmg.learn.vok;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.jmg.learn.*;
import com.jmg.lib.WindowsBufferedReader;
import com.jmg.lib.lib;
import com.jmg.lib.RefSupport;
import com.jmg.lib.lib.libString;



public class Vokabel {

	//Learn For All New Version
		//By J.M.Goebel (jhmgbl2@t-online.dee)
		//
		//This program is free software; you can redistribute it and/or
		//modify it under the terms of the GNU General Public License
		//as published by the Free Software Foundation; either version 2
		//of the License, or (at your option) any later version.
		//
		//This program is distributed in the hope that it will be useful,
		//but WITHOUT ANY WARRANTY; without even the implied warranty of
		//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
		//GNU General Public License for more details.
		//
		//You should have received a copy of the GNU General Public License
		//along with this program; if not, write to the Free Software
		//Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
		//

		final static String className = "Vokabel";
		// ******** Events ************
		interface FehlerEventHandler
		{
			public void Fehler(String strText);
		}
		List<FehlerEventHandler> FehlerEventlisteners = new ArrayList<FehlerEventHandler>();
	    public void addFehlerEventListener(FehlerEventHandler toAdd){ FehlerEventlisteners.add(toAdd); }
	    //Set of functions that Throw Events.
	    public void ThrowFehlerEvent(String strText)
	    { 
	    	for (FehlerEventHandler hl : FehlerEventlisteners) hl.Fehler(strText);
	            System.out.println("Something thrown");
	    }
	    
	    
	    interface AbfrageEndeEventHandler
		{
			public void AbfrageEnde(String strText);
		}
		List<AbfrageEndeEventHandler> AbfrageEndeEventlisteners = new ArrayList<AbfrageEndeEventHandler>();
	    public void addAbfrageEndeEventListener(AbfrageEndeEventHandler toAdd){ AbfrageEndeEventlisteners.add(toAdd); }
	    //Set of functions that Throw Events.
	    public void ThrowAbfrageEndeEvent(String strText)
	    { 
	    	for (AbfrageEndeEventHandler hl : AbfrageEndeEventlisteners) hl.AbfrageEnde(strText);
	            System.out.println("Something thrown");
	    }
	    		//********** Public ***********
		public boolean aend;
		public boolean varHebr;
		public int AnzRichtig;

		public int AnzFalsch;
		public enum Bewertung
		{
			AllesRichtig,
			TeilweiseRichtig,
			aehnlich,
			AllesFalsch,
			undefiniert
		}
		public enum EnumSprachen
		{
		  Normal,
		  Hebrew,
		  Griechisch,
		  Sonst1,
		  Sonst2,
		  Sonst3,
		  undefiniert
		}

		public class structFonts
		{
			public typFonts Normal;
			public typFonts Hebrew;
			public typFonts Griechisch;
			public typFonts Sonst1;
			public typFonts Sonst2;
			public typFonts Sonst3;
		}
		public Activity Container;
		// ******** Types ******



		public class typFonts
		{
			//Für Fonts
			public clsFont Wort;
			public clsFont Bed;
			public clsFont Kom;
		}
		// ************* final ************

		final short ErrLernindex = 1005;
		final short ErrWrongfilename = 1001;

		final short ErrNoFileHandle = 1002;
		private ArrVok mVok;
			//enthält die Indexwerte der Lernvokabeln
		private int[] mLernVokabeln;
			//gibt an welcher Index zuletzt bei den Lernvokabeln verwendet wurde
		private int mLastIndex;
			//Abfragen ist möglich
		private boolean mblnLernInit;
			//lokale Kopie
		private int mIndex;
			//lokale Kopie
		private int mGesamtzahl;
			//lokale Kopie
		private boolean mConfirmChanges;
		//	Private mChanged As booleanean 'lokale Kopie
			//lokale Kopie
		private short mSchrittweite;
			//lokale Kopie
		private clsFont mWortFont;// = new clsFont(Container);
			//lokale Kopie
		private clsFont mBedFont;// = new clsFont(Container);
			//lokale Kopie
		private clsFont mKomFont;// = new clsFont(Container);
			//lokale Kopie
		private short mAbfragebereich;
			//lokale Kopie
		private boolean mAbfrageZufällig;
			//lokale Kopie
		private short mLerngeschwindigkeit;
			//lokale Kopie
		private EnumSprachen mSprache;
			//lokale Kopie
		private short mLernindex;
			//lokale Kopie
		private String mFileName;
		private String mVokPath;
		private TextView mSTatusO;
		private String[] mOldBed = new String[3];
		private String mSTatus;
		private structFonts mFonts;
		private boolean _cardmode;
		private String _Properties;
		private boolean _UniCode;
		
				public boolean getUniCode() {
			return _UniCode;
		}

		private String[] mAntworten;
		public String[] getAntworten() {
			return mAntworten;
		}
		public String[] getBedeutungen() throws Exception {
			return new String[] {getBedeutung1(),getBedeutung2(),getBedeutung3()}; 
		}
		public ArrVok getVokabeln() {
			return mVok; 
		}

		public String getProperties() throws Exception {
				String txt = null;
				txt = getContext().getString(R.string.TotalNumber) + ": " + this.getGesamtzahl();
				for (int i = -6; i <= 6; i++) {
					txt += "\r\n" + "z = " + i + ": " + this.Select(null, null, i).size();
				}
				return txt;
			}
		
		public String getvok_Path()	{ return mVokPath; }
		public	void setvok_Path(String value) { mVokPath = value; }
		
		public boolean getCardMode() { return _cardmode; }
		public	void setCardModoe(boolean value) { _cardmode = value; }
		
		public String getFileName()	{ return mFileName; }
		
		public String[] getOldBed() { return (mOldBed); }
		
		public structFonts getfonts() { return mFonts; }
		public	void setFonts(structFonts value) { mFonts = value; }
		
		public short  getLernIndex() {	return mLernindex;	}
		
		public	void setLernIndex(short value) throws Exception 
		{
			libLearn.gStatus = "Vokabel.LernIndex Start";
			if (value > mSchrittweite)
				value = 1;
			if (mblnLernInit == false)
				InitAbfrage();
			if (value > 0 && value <= mSchrittweite && mblnLernInit) {
				mLernindex = value;
				mIndex = mLernVokabeln[mLernindex];
			} else {
				throw new Exception("Die Abfrage konnte nicht aktualisiert werden oder Fehler!");
			}
			return;
		}

		public EnumSprachen getSprache() 
		{
			return mSprache;
		}
		public void setSprache(EnumSprachen value) 
		{
			libLearn.gStatus = "Vokabel.Sprache Start";
			mSprache = value;
			return;
		}
		
		private String[] _Trennzeichen = new String[6];
		public String getTrennzeichen() {
				libLearn.gStatus = "Vokabel.Trennzeichen Start";
				return _Trennzeichen[mSprache.ordinal()];
		}
		public void setTrennzeichen(String value) 
		{
			libLearn.gStatus = "Vokabel.Trennzeichen Start";
			_Trennzeichen[mSprache.ordinal()] = value;
			return;
		}

		public short getLerngeschwindigkeit()	{ return mLerngeschwindigkeit;}
		public	void setLerngeschwindigkeit(short value) {mLerngeschwindigkeit = value;}
		
		public boolean getAbfrageZufaellig() {return mAbfrageZufällig;}
		public void setAbfrageZufaellig(boolean value) {mAbfrageZufällig = value;}




		// Der Abfragebereicht wird durch eine Zahl zwischen -1 und 6 repräsentiert
		// <=0 repräsentiert alle Vokabeln, die ein oder mehrmals nicht gewußt wurden
		// 1 alle Zahlen, die noch gar nicht gewußt wurden, bei jeder richtigen Antwort wird
		// der Zähler um eins erhöht.
		public short getAbfragebereich() {	return mAbfragebereich;	}
		public void setAbfragebereich(short value) {mAbfragebereich = value;}
		
		public clsFont getFontKom() {return mKomFont; }
		public void setFontKom(clsFont value) { mKomFont = value; }
		
		public clsFont getFontBed() {return mBedFont; }
		public void setFontBed(clsFont value) { mBedFont = value; }
		
		public clsFont getFontWort() {return mWortFont; }
		public void setFontWort(clsFont value) { mWortFont = value; }
		
		public short getSchrittweite()  
		{
	        return mSchrittweite;
	    }

	    public void setSchrittweite(short value) throws Exception 
	    {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Schrittweite Start";
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.Schrittweite = 5
	        if (value < mGesamtzahl || mGesamtzahl <= 0)
	        {
	            mSchrittweite = value;
	        }
	        else
	        {
	            mSchrittweite = (short) mGesamtzahl;
	        } 
	        return ;
	    }






		

	    public String getStatus() throws Exception {
	        String functionReturnValue = null;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.libLearn.gStatus Start";
	        //
	        //
	        functionReturnValue = mSTatus;
	        return functionReturnValue;
	    }

	    public void setStatus(String value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.libLearn.gStatus Start";
	        if (mSTatusO != null)
	        {
	            mSTatusO.setText(value);
	        }
	         
	        mSTatus = value;
	        return ;
	    }

	    public boolean getConfirmChanges() throws Exception {
	        boolean functionReturnValue = false;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.ConfirmChanges Start";
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.ConfirmChanges
	        functionReturnValue = mConfirmChanges;
	        return functionReturnValue;
	    }

	    public void setConfirmChanges(boolean value) {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.ConfirmChanges Start";
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.ConfirmChanges = 5
	        mConfirmChanges = value;
	        return ;
	    }

	    //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	    //Syntax: Debug.Print X.Gesamtzahl
	    public int getGesamtzahl() {
	        return mGesamtzahl;
	    }

	    public int getIndex()  { return mIndex;}
	    
	    public void setIndex(int value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Index Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        if (value >= 0 & value <= mGesamtzahl)
	        {
	            mIndex = value;
	        }
	         
	    }

	    public short getZaehler() throws Exception {
	        short functionReturnValue = 0;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.ZÃƒÂ¤hler Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.ZÃƒÂ¤hler
	        functionReturnValue = mVok.get(mIndex).z;
	        return functionReturnValue;
	    }

	    public void setZaehler(short value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.ZÃƒÂ¤hler Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.ZÃƒÂ¤hler = 5
	        mVok.get(mIndex).z = value;
	        aend = true;
	    }

	    public String getKommentar() throws Exception {
	        String functionReturnValue = null;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Kommentar Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.Kommentar
	        functionReturnValue = mVok.get(mIndex).Kom;
	        return functionReturnValue;
	    }

	    public void setKommentar(String value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Kommentar Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.Kommentar = 5
	        mVok.get(mIndex).Kom = value;
	        aend = true;
	    }

	    public String getBedeutung3() throws Exception {
	        String functionReturnValue = null;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung3 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.Bedeutung3
	        functionReturnValue = mVok.get(mIndex).Bed3.trim();
	        return functionReturnValue;
	    }

	    public void setBedeutung3(String value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung3 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.Bedeutung3 = 5
	        mVok.get(mIndex).Bed3 = value;
	        aend = true;
	    }

	    public String getBedeutung2() throws Exception {
	        String functionReturnValue = null;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung2 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.Bedeutung2
	        functionReturnValue = (mVok.get(mIndex).Bed2).trim();
	        return functionReturnValue;
	    }

	    public void setBedeutung2(String value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung2 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.Bedeutung2 = 5
	        mVok.get(mIndex).Bed2 = value;
	        aend = true;
	    }

	    public String getBedeutung1() throws Exception {
	        String functionReturnValue = null;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung1 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.Bedeutung1
	        functionReturnValue = (mVok.get(mIndex).Bed1).trim();
	        return functionReturnValue;
	    }

	    public void setBedeutung1(String value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung1 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.Bedeutung1 = 5
	        if ((value).length() > 0)
	        {
	            aend = true;
	            mVok.get(mIndex).Bed1 = value;
	        }
	        else
	        {
	            throw new Exception("Bedeutung1 muß¸ Text enthalten!");
	        } 
	    }

	    // Inserted by CodeCompleter
	    public String getWort() throws Exception {
	        String functionReturnValue = null;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Wort Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.Wort
	        functionReturnValue = mVok.get(mIndex).Wort;
	        return functionReturnValue;
	    }

	    public void setWort(String value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Wort Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.Wort = 5
	        if ((value).length() > 0)
	        {
	            mVok.get(mIndex).Wort = value;
	            aend = true;
	        }
	        else
	        {
	            throw new Exception("Löschen der Vokabel mit DeleteVokabel!");
	        } 
	    }

	


	// Inserted by CodeCompleter
		public void Init(TextView StatusO)
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.Init Start";

			mSTatusO = StatusO;

			return;
					}



		public void SkipVokabel() throws Exception
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.SkipVokabel Start";
			mLernVokabeln[mLernindex] = 0;
			mLernindex += 1;
			InitAbfrage();
			//
			return;
					}



		public Bewertung CheckAnwort(String[] Antworten) throws Exception
		{
			Bewertung functionReturnValue;
			final String CodeLoc = className + ".CheckAntwort";
			functionReturnValue = Bewertung.undefiniert;
			try {
				libLearn.gStatus = "Vokabel.CheckAnwort Start";
				String[] Bedeutungen = null;
				short i = 0;
				short ii = 0;
				short Loesungen = 0;
				// Anzahl der richtigen Antworten
				short anzAntworten = 0;
				// Anzahl der eingegebenen Antworten
				short anzBedeutungen = 0;

				short Enthalten = 0;
				// Anzahl der Antworten die nur einen TeilString enthalten
				short aehnlich = 0;
				// Anzahl der aehnlichen Antworten
				short TeilweiseRichtig = 0;
				libLearn.gStatus = "Vokabel.CheckAnwort Line 248";
				// Inserted by CodeCompleter
				Bewertung TeilErgebnis = (Bewertung.undefiniert);

				mOldBed[0] = "";
				mOldBed[1] = "";
				mOldBed[2] = "";
				libLearn.gStatus = "Vokabel.CheckAnwort Line 258";
				// Inserted by CodeCompleter
				Bedeutungen = new String[] {
					getBedeutung1(),
					getBedeutung2(),
					getBedeutung3()
				};
				mAntworten = Antworten;
				libLearn.gStatus = CodeLoc + " Gleichheit Überprüfen";

				for (ii = 0; ii <= (Bedeutungen.length -1); ii++) {
					Bedeutungen[ii] = (Bedeutungen[ii]).trim();

					if (!libString.IsNullOrEmpty(Bedeutungen[ii])) {
						anzBedeutungen += 1;
					}
				}
				libLearn.gStatus = "Vokabel.CheckAnwort Line 268";
				// Inserted by CodeCompleter
				for (i = 0; i <= (Antworten.length-1); i++) {
					Antworten[i] = (Antworten[i]).trim();
					String Antwort = RemoveKomment(Antworten[i]);
					if (!libString.IsNullOrEmpty(Antworten[i])) {
						anzAntworten += 1;
						for (ii = 0; ii <= (Bedeutungen.length-1); ii++) {
							Bedeutungen[ii] = (Bedeutungen[ii]).trim();

							if (!libString.IsNullOrEmpty(Bedeutungen[ii])) {
								libLearn.gStatus = CodeLoc + " call MakeVergl";
								boolean CheckVergl = false;
								try {
									CheckVergl =  lib.like(Antwort, MakeVergl(Bedeutungen[ii]));
								} catch (Exception ex) {
									throw new Exception(ex.getMessage() + "\n" + CodeLoc + " CheckVergl");
								}
								if (CheckVergl) {
									libLearn.gStatus = "Vokabel.CheckAnwort Line 278";
									// Inserted by CodeCompleter
									mOldBed[ii] = Bedeutungen[ii];
									Bedeutungen[ii] = "";
									Antwort = "";

									Loesungen  += 1;

									break; // TODO: might not be correct. Was : Exit For
								// Falls eine Antwort mehrere Teilantworten enthält
								} else {
							        String[] s = EnthaeltTrennzeichen(RemoveKomment(Bedeutungen[ii]));
							        RefSupport<String> refVar1 = new RefSupport<String>(Antwort);
							        RefSupport<String[]> refVar2 = new RefSupport<String[]>(s);
							        short[] refii = new short[]{ii};
							        RefSupport<short[]> refVar3 = new RefSupport<short[]>(refii);
							        TeilErgebnis = TeileUeberpruefen(refVar1, refVar2, refVar3);
							        Antwort = (String) refVar1.getValue();
							        s = refVar2.getValue();
							        ii = (Short) refVar3.getValue()[0];
							        if (TeilErgebnis == Bewertung.AllesRichtig) {
										libLearn.gStatus = "Vokabel.CheckAnwort Line 288";
										// Inserted by CodeCompleter
										Loesungen  += 1;
										mOldBed[ii] = Bedeutungen[ii];
										Bedeutungen[ii] = "";
										Antwort = "";
										break; // TODO: might not be correct. Was : Exit For
									} else if (TeilErgebnis == Bewertung.TeilweiseRichtig) {
										TeilweiseRichtig  += 1;

										//Bedeutungen(ii) = ""
										//Antwort = ""
										//Exit For
										libLearn.gStatus = "Vokabel.CheckAnwort Line 298";
										// Inserted by CodeCompleter
									} else {
								        short refII[] = new short[]{ii};
										RefSupport<short[]> refVar___ii = new RefSupport<short[]>(refII);
								        boolean boolVar___0 = Aehnlichkeit(Bedeutungen[ii], Antwort, refVar___ii) > 0.5;
								        ii = (Short) refVar___ii.getValue()[0];
								        if (boolVar___0)
								        {
								            aehnlich += 1;
								        }
									}

								}
							}
						}
						libLearn.gStatus = "Vokabel.CheckAnwort Line 308";
						// Inserted by CodeCompleter
					}
				}
				libLearn.gStatus = CodeLoc + " Auswertung";
				while (true)
				{
		
					if (Loesungen < anzBedeutungen) {
						if (Loesungen > 0)
							functionReturnValue = Bewertung.TeilweiseRichtig;
	
					} else {
						//MsgBox "Alles richtig!"
						functionReturnValue = Bewertung.AllesRichtig;
						//AntwortRichtig()
						break ;
	
					}
	
	
					if (Loesungen + TeilweiseRichtig < anzBedeutungen) {
	
	
					} else {
						//MsgBox Loesungen & " richtig " & Enthalten & " teilweise richtig."
						functionReturnValue = Bewertung.TeilweiseRichtig;
						break ;
					}
	
					if (Loesungen + Enthalten + TeilweiseRichtig + aehnlich == 0) {
						//MsgBox "AllesFalsch"
						functionReturnValue = Bewertung.AllesFalsch;
						AntwortFalsch();
					} else if (functionReturnValue != Bewertung.TeilweiseRichtig) {
						//MsgBox Loesungen & " richtig, " & Enthalten & " teilweise richtig, " _
						//& aehnlich & " aehnlich."
						functionReturnValue = Bewertung.aehnlich;
					}
					break;
				}

			} catch (Exception ex) {
				throw new Exception(CodeLoc, ex);
			}
			return functionReturnValue;

		}

	    private float Aehnlichkeit(String Bedeutung, String Antwort, RefSupport<short[]> BedNR) throws Exception 
	    {
	        
	        final String CodeLoc = className + ".Aehnlichkeit";
	        libLearn.gStatus = CodeLoc + " Start";
	        short Size1 = 0;
	        Bedeutung = Bedeutung.toLowerCase(Locale.getDefault());
	        Antwort = Antwort.toLowerCase(Locale.getDefault());
	        Size1 = (short) RemoveKomment(Bedeutung).length();
	        libLearn.gStatus = CodeLoc + " RemoveKomment";
	        //Antwort = RemoveKomment(Antwort)
	        libLearn.gStatus = CodeLoc + " Levenshtein";
	        int levenshtein = LevenshteinDistance(Bedeutung, Antwort);
	        boolean blnOldBed = !libString.IsNullOrEmpty(mOldBed[BedNR.getValue()[0]]);
	        //TODO: Dim locs(Size1) As Integer
	        int LastPos = 0;
	        String Test = null;
	        //Bedeutung = Me.Bedeutungen(BedNR)
	        //mOldBed(BedNR) = ""
	        //Antwort = mAntworten(BedNR)
	        Test = new String (new char[Bedeutung.length()]).replace('\0', '*'); //new String('*', Bedeutung.length());
	        char[] tst = Test.toCharArray();
	        for (int ii = 0;ii <= Antwort.length() - 1;ii++)
	        {
	            int Pos = -1;
	            int Pos2 = -1;
	            int LastLastPos = LastPos;
	            do
	            {
	                Pos = Bedeutung.indexOf(Antwort.substring(ii, 1), LastPos);
	                if (Pos == -1)
	                    break;
	                 
	                // TODO: might not be correct. Was : Exit Do
	                if (Pos > -1 && ii < Antwort.length() - 1)
	                {
	                    Pos2 = Bedeutung.indexOf(Antwort.substring(ii + 1, 1), Pos + 1);
	                }
	                 
	                if (Pos2 != Pos + 1)
	                    LastPos = Pos + 1;
	                 
	            }
	            while (!(Pos2 == Pos + 1 || LastPos >= Antwort.length() - 1));
	            if (Pos > -1)
	            {
	                if (ii == Antwort.length() - 1 || Pos2 == Pos + 1)
	                {
	                    tst[Pos] = Bedeutung.charAt(Pos); // Bedeutung.substring(Pos, 1).toCharArray()[0];
	                    if (Pos2 == Pos + 1)
	                    {
	                        tst[Pos2] = Bedeutung.charAt(Pos2);//Bedeutung.substring(Pos2, 1).;
	                    }
	                     
	                    LastPos = Pos + 1;
	                }
	                else
	                {
	                    LastPos = LastLastPos;
	                } 
	            }
	            else
	            {
	                LastPos = LastLastPos;
	            } 
	        }
	        Test = new String(tst);
	        if (libString.IsNullOrEmpty(mOldBed[BedNR.getValue()[0]]) || blnOldBed == false)
	        {
	            mOldBed[BedNR.getValue()[0]] = Test;
	        }
	        else
	        {
	            //For iii As Integer = 0 To test.Length - 1
	            //  If test.SubString(iii, 1) <> "*" Then
	            //    Mid(mOldBed(BedNR), iii + 1, 1) = test.SubString(iii, 1)
	            //  End If
	            //Next
	            mOldBed[BedNR.getValue()[0]] = mOldBed[BedNR.getValue()[0]] + "," + Test;
	        } 
	        int AnzFalsch = lib.countMatches(Test, "*");//ClsGlobal.CountChar(Test, "*");
	        float Aehn1 = (Size1 - AnzFalsch) / Size1;
	        float Aehn2 = (Size1 - levenshtein) / Size1;
	        if (Aehn1 < Aehn2)
	        {
	            return Aehn1;
	        }
	        else
	        {
	            return Aehn2;
	        } 
	    }

	


		public int LevenshteinDistance(String Bedeutung, String Antwort)
		{

			Bedeutung = RemoveKomment(Bedeutung);
			Antwort = RemoveKomment(Antwort);

			char[] s = Bedeutung.toCharArray();
			char[] t = Antwort.toCharArray();

			// for all i and j, d[i,j] will hold the Levenshtein distance between
			// the first i characters of s and the first j characters of t;
			// note that d has (m+1)x(n+1) values
			int[][] d = new int[s.length -1 + 2][t.length-1 + 2];
			int m = s.length;
			int n = t.length;
			for (int i = 0; i <= m; i++) {
				d[i][0] = i;
				//the distance of any first String to an empty second String
			}
			for (int j = 0; j <= n; j++) {
				d[0][j] = j;
				// the distance of any second String to an empty first String
			}
			for (int j = 1; j <= n; j++) {
				for (int i = 1; i <= m; i++) {
					if (s[i - 1] == t[j - 1]) {
						d[i][j] = d[i - 1][j - 1];
						//// no operation required(()
					} else {
						d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + 1);
					}
				}
			}


			return d[m][n];
		}
		private String MakeVergl(String Bed) throws Exception
		{
			String functionReturnValue = null;
			final String CodeLoc = className + ".MakeVergl";
			libLearn.gStatus = CodeLoc + " Start";
			short i = 0;
			int f1 = 0;
			int f2 = 0;
			int intAsc = 0;
			// Optionale Teile herausfiltern
			try {
				f1 = Bed.indexOf("(", 0) ;//libString.InStr(1, Bed, "(");
				libLearn.gStatus = CodeLoc + " Klammern verarbeiten";
				while (f1>-1) {
					f2 = Bed.indexOf(")",f1+1); //libString.InStr(f1 + 1, Bed, ")");
					if (f2>-1) {
						Bed = Bed.substring(0,f1-1) + "*" + Bed.substring(f2+1, Bed.length()-f2-1); //libString.Left(Bed, f1 - 1) + "*" + libString.Mid(Bed, f2 + 1, libString.Len(Bed) - f2);
						f1 = Bed.indexOf("(",f2+1); //libString.InStr(f2 + 1, Bed, "(");
					} else {
						f1 = f2;
					}
				}
				libLearn.gStatus = CodeLoc + "Kommentare herausfiltern";
				Bed = RemoveKomment(Bed);
				libLearn.gStatus = CodeLoc + " Ungültige Zeichen ersetzen";
				if (Bed.length() > 0) {
					Bed = (Bed).toUpperCase(Locale.getDefault());

					for (i = 1; i <= (Bed).length(); i++) {
						try {
							intAsc = Bed.charAt(i-1);//libString.Asc(libString.Mid(Bed, i, 1));
							if (intAsc < 65 | intAsc > 90) {
								Bed = Bed.substring(0, i - 1) + "*" + Bed.substring(i, Bed.length() - i);
							}
						} catch (Exception ex) {
							throw new Exception("Fehler bei MakeVergl Ungültige Zeichen: \n" + ex.getMessage());
						}
					}
				}
				functionReturnValue = Bed;
			} catch (Exception ex) {
				throw new Exception("Fehler bei MakeVergl: \n" + ex.getMessage());
			}
			return functionReturnValue;



		}
		public static String RemoveKomment(String Bed)
		{
			final String CodeLoc = className + ".RemoveKomment";
			libLearn.gStatus = CodeLoc + " Start";
			int f1 = 0;
			int f2 = 0;
			f1 = Bed.indexOf("[",0); //libString.InStr(1, Bed, "[");
			while (f1 > -1) {
				f2 = Bed.indexOf("]", f1+1);//libString.InStr(f1 + 1, Bed, "]");
				libLearn.gStatus = "Vokabel.MakeVergl Line 392";
				// Inserted by CodeCompleter
				if (f2 > -1) {
					Bed = Bed.substring(0, f1-1) + Bed.substring(f2+1, Bed.length() -1); 
							//'libString.Left(Bed, f1 - 1) + "" + libString.Mid(Bed, f2 + 1, libString.Len(Bed) - f2);
					f1 = Bed.indexOf("[", f2+1);//libString.InStr(f2 + 1, Bed, "[");
				} else {
					f1 = f2;
				}
			}
			return Bed;
		}

	    private Bewertung TeileUeberpruefen(RefSupport<String> Antwort, RefSupport<String[]> teile, RefSupport<short[]>BedNR) throws Exception 
	    {
	        Bewertung functionReturnValue = Bewertung.undefiniert;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.TeileUeberpruefen Start";
	        short i = 0;
	        short ii = 0;
	        short richtig = 0;
	        short Bedeutungen = 0;
	        short aehnlich = 0;
	        String Antworten[] = null;
	        if ((teile.getValue() == null))
	        {
	            functionReturnValue = Bewertung.AllesFalsch;
	            return functionReturnValue;
	        }
	         
	        Antworten = EnthaeltTrennzeichen(Antwort.getValue());
	        for (i = 0;i <= (teile.getValue()).length -1;i++)
	        {
	            // Richtige Teilantworten finden
	            libLearn.gStatus = "Vokabel.TeileUeberpruefen Line 420";
	            // Inserted by CodeCompleter
	            if (!libString.IsNullOrEmpty(teile.getValue()[i]))
	            {
	                Bedeutungen  += 1;
	                for (ii = 0;ii <= (Antworten).length -1 ;ii++)
	                {
	                    Antworten[ii] = (Antworten[ii]).trim();
	                    if (!libString.IsNullOrEmpty(Antworten[ii]))
	                    {
	                        if (lib.like(Antworten[ii], MakeVergl(teile.getValue()[i]))) // If Antworten(ii) Like MakeVergl(teile(i)) Then
	                        {
	                           
	                            richtig += 1;
	                            int intBedNR = BedNR.getValue()[0];
	                            if ((mOldBed[intBedNR]).length() > 0)
	                            {
	                                mOldBed[intBedNR] = mOldBed[intBedNR] + "," + Antworten[ii];
	                                libLearn.gStatus = "Vokabel.TeileUeberpruefen Line 430";
	                            }
	                            else
	                            {
	                                // Inserted by CodeCompleter
	                                mOldBed[intBedNR] = Antworten[ii];
	                            } 
	                            Antworten[ii] = "";
	                            teile.getValue()[i] = "";
	                            break;
	                        }
	                         
	                    }
	                     
	                }
	            }
	             
	        }
	        // TODO: might not be correct. Was : Exit For
	        // Erst in zweitem Schritt aehnlichkeiten feststellen!
	        boolean Aehn = false;
	        float lAehnlichkeit = 0;
	        for (i = 0;i <= (teile.getValue()).length-1;i++)
	        {
	            libLearn.gStatus = "Vokabel.TeileUeberpruefen Line 420";
	            // Inserted by CodeCompleter
	            Aehn = false;
	            if (!libString.IsNullOrEmpty(teile.getValue()[i]))
	            {
	                for (ii = 0;ii <= (Antworten).length-1;ii++)
	                {
	                    Antworten[ii] = (Antworten[ii]).trim();
	                    if (!libString.IsNullOrEmpty(Antworten[ii]))
	                    {
	                        RefSupport<short[]> refVar___0 = new RefSupport<short[]>(BedNR.getValue());
	                        lAehnlichkeit = Aehnlichkeit(teile.getValue()[i], Antworten[ii], refVar___0);
	                        BedNR.setValue(refVar___0.getValue());
	                        if (lAehnlichkeit > 0.2)
	                            Aehn = true;
	                         
	                        if (lAehnlichkeit > 0.5)
	                        {
	                            aehnlich  += 1;
	                            break;
	                            
	                        }
	                         
	                    }
	                     
	                }
	                // Inserted by CodeCompleter
	                if (!Aehn)
	                {
	                    if (libString.IsNullOrEmpty(mOldBed[BedNR.getValue()[0]]))
	                    {
	                        mOldBed[BedNR.getValue()[0]] = lib.MakeMask(teile.getValue()[i]);
	                    }
	                    else
	                    {
	                        mOldBed[BedNR.getValue()[0]] = mOldBed[BedNR.getValue()[0]] + "," + lib.MakeMask(teile.getValue()[i]);
	                    } 
	                }
	                 
	            }
	             
	        }
	        //OldWord.AnzTeilBed(BedNR) = Bedeutungen
	        if (richtig == Bedeutungen)
	        {
	            functionReturnValue = Bewertung.AllesRichtig;
	        }
	        else if (richtig < Bedeutungen)
	        {
	            functionReturnValue = Bewertung.TeilweiseRichtig;
	            libLearn.gStatus = "Vokabel.TeileUeberpruefen Line 450";
	            // Inserted by CodeCompleter
	            if (richtig == 0)
	            {
	                functionReturnValue = Bewertung.AllesFalsch;
	                if (aehnlich > 0)
	                	for (Bewertung b: Bewertung.values())
	                	{
	                		if (b.ordinal()==aehnlich)
	                		{
	                			functionReturnValue = b;
	                			break;
	                		}
	                	}
	                    //lib.setEnumOrdinal(functionReturnValue, aehnlich);	                	
	            }
	             
	        }
	          
	        return functionReturnValue;
	        	        
	    }
	    

	


		private String[] EnthaeltTrennzeichen(String Antwort)
		{
			String[] functionReturnValue = null;
			// Rückgabewert ist Anzahl der Teilbedeutungen
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.EnthaeltTrennzeichen Start";
			String Trenn = null;
			String[] teile = new String[-1];
			short i = 0;
			short lastTrenn = -1;
			short Trennz = 0;
			Trenn = "/,.;:-\\";
			Antwort = RemoveKomment(Antwort);
			Antwort = Antwort + ",";

			for (i = 0; i <= (Antwort.length()-1); i++) {
				if (Trenn.indexOf(Antwort.substring(i, i+1)) > -1) {
					libLearn.gStatus = "Vokabel.EnthaeltTrennzeichen Line 464";
					// Inserted by CodeCompleter
					//String[] newteile = new String[Trennz +1];
					//System.arraycopy(teile, 0, newteile, 0, teile.length)
					teile = lib.ResizeArray(teile, Trennz+1);
					teile[Trennz] = Antwort.substring(lastTrenn+1,i );
					lastTrenn = i;
					Trennz += 1;
				}
			}

			if (Trennz > 0) {
				functionReturnValue = teile;
			} else {
				return null;
			}
			return functionReturnValue;
					}
		public void AntwortRichtig() throws Exception
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.AntwortRichtig Start";
			if (mVok.get(mIndex).z < 0)
				mVok.get(mIndex).z = 0;
			mVok.get(mIndex).z = (short) (mVok.get(mIndex).z + 1);
			aend = true;
			if (mVok.get(mIndex).z > 0) {
				if (mLernVokabeln == null) {
					this.InitAbfrage();
				}
				mLernVokabeln[mLernindex] = 0;
			}
			AnzRichtig += 1;
			InitAbfrage();

			return;
					}
		public void AntwortFalsch()
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.AntwortFalsch Start";
			if (mVok.get(mIndex).z <= 0) {
				mVok.get(mIndex).z = (short) (mVok.get(mIndex).z - 1);
			} else {
				mVok.get(mIndex).z = 0;
			}
			aend = true;
			AnzFalsch += 1;
			//InitAbfrage
			return;
					}

		public void InitAbfrage() throws Exception
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.InitAbfrage Start";
			 // ERROR: Not supported in C#: OnErrorStatement

			short i = 0;
			short voknr = 0;
			boolean blnDurch = false;
			boolean blnDurch2 = false;

			//mLastIndex = 0
			voknr = (short) mLastIndex;

			libLearn.gStatus = "Init Abfrage";
			if (mGesamtzahl > 1) {
				libLearn.gStatus = "Vokabel.InitAbfrage Line 499";
				// Inserted by CodeCompleter
				if (mSchrittweite < 5)
					mSchrittweite = 5;
				if (mSchrittweite >= mGesamtzahl) {
					if (mGesamtzahl >= 5) {
						mSchrittweite = (short) (mGesamtzahl - 1);
					} else {
						mSchrittweite = (short) mGesamtzahl;
					}
				}
				
				mLernVokabeln = lib.ResizeArray(mLernVokabeln, mSchrittweite +1);
				// Überprüfen ob Cache mit Lernvokabeln gefüllt ist
				 for (i = 1;i <= mSchrittweite;i++)
			        {
			            if (mLernVokabeln[i] == 0)
			            {
			                // falls Lernvokabel gelÃ¶scht ist neue holen
			                if (mAbfragebereich == -1 | blnDurch2 == true)
			                {
			                    RefSupport<Object> refVar___0 = new RefSupport<Object>(voknr);
			                    RefSupport<Object> refVar___1 = new RefSupport<Object>(i);
			                    vokabelVonAllenHolen(refVar___0, refVar___1);
			                    voknr = (Short) refVar___0.getValue();
			                    i = (Short) refVar___1.getValue();
			                }
			                else
			                {
			                    libLearn.gStatus = "Vokabel.InitAbfrage Line 509";
			                    // Inserted by CodeCompleter
			                    RefSupport<Object> refVar___2 = new RefSupport<Object>(voknr);
			                    RefSupport<Object> refVar___3 = new RefSupport<Object>(i);
			                    RefSupport<Object> refVar___4 = new RefSupport<Object>(blnDurch);
			                    RefSupport<Object> refVar___5 = new RefSupport<Object>(blnDurch2);
			                    Get_Vok(refVar___2, refVar___3, refVar___4, refVar___5);
			                    voknr = (Short) refVar___2.getValue();
			                    i = (Short) refVar___3.getValue();
			                    blnDurch = (Boolean) refVar___4.getValue();
			                    blnDurch2 = (Boolean) refVar___5.getValue();
			                } 
			            }
			             
			        }
				// Nächste Vokabel im Puffer einstellen
				mLernindex += 1;
				// falls wir am Ende sind wieder an den Anfang gehen
				if (mLernindex > mSchrittweite)
					mLernindex = 1;
				// Vokabelnummer aus dem Puffer holen
				libLearn.gStatus = "Vokabel.InitAbfrage Line 519";
				// Inserted by CodeCompleter
				mIndex = mLernVokabeln[mLernindex];
				mOldBed[0] = lib.MakeMask(getBedeutung1());
				mOldBed[1] = lib.MakeMask(getBedeutung2());
				mOldBed[2] = lib.MakeMask(getBedeutung3());

			} else {
				//If mLernindex < mGesamtzahl Then mLernindex += 1 Else mLernindex = 1
			}
			if (mLernVokabeln != null && mLernVokabeln[mLernindex] > 0) {
				mblnLernInit = true;
				mLastIndex = voknr;
				libLearn.gStatus = "Vokabel.InitAbfrage Line 529";
				// Inserted by CodeCompleter
			} else {
				mblnLernInit = false;
			}
			libLearn.gStatus = "";
			return;
					}
		public ArrayList<typVok> Select(String Wort, String Bedeutung)
		{
			int Zaehler = -100;
			return Select(Wort, Bedeutung, Zaehler);
		}
		
		public ArrayList<typVok> Select(String Wort, String Bedeutung, int Zaehler)
		{
			ArrayList<typVok> Sel = new ArrayList<typVok>();
			for (typVok vok: mVok) {
				if (!libString.IsNullOrEmpty(Wort)) {
					if (vok.Wort.contains(Wort)) {
						Sel.add(vok);
					}
				} else if (!libString.IsNullOrEmpty(Bedeutung)) {
					if (vok.Bed1.contains(Bedeutung) || vok.Bed2.contains(Bedeutung) || vok.Bed3.contains(Bedeutung)) {
						Sel.add(vok);
					}
				} else if (Zaehler != -100) {
					if (Zaehler > -6) {
						if (Zaehler < 6) {
							if (vok.z == Zaehler) {
								Sel.add(vok);
							}
						} else if (vok.z >= 6) {
							Sel.add(vok);
						}
					} else if (vok.z <= -6) {
						Sel.add(vok);
					}
				}

			}
			return Sel;
		}


	    public void Get_Vok(RefSupport<Object> refvokNr, RefSupport<Object> refi, RefSupport<Object> refblnDurch, RefSupport<Object> refblnDurch2) throws Exception 
	    {
	        Get_Vok:
	        refblnDurch.setValue(false);
	        short vokNr = (Short)refvokNr.getValue();
	        short i = (Short)refi.getValue();
	    	do
	        {
	            if (vokNr < mGesamtzahl)
	            {
	                vokNr+= 1;
	            }
	            else
	            {
	                vokNr=1;
	                if ((Boolean)refblnDurch.getValue() == true)
	                {
	                    refblnDurch2.setValue(true);
	                    //UPGRADE_ISSUE: Die Anweisung GoSub wird nicht unterstützt. Klicken Sie hier für weitere Informationen: 'ms-help://MS.VSExpressCC.v80/dv_commoner/local/redirect.htm?keyword="C5A1A479-AB8B-4D40-AAF4-DB19A2E5E77F"'
	                    RefSupport<Object> refVar___0 = new RefSupport<Object>(vokNr);
	                    RefSupport<Object> refVar___1 = new RefSupport<Object>(i);
	                    vokabelVonAllenHolen(refVar___0,refVar___1);
	                    vokNr=(Short)(refVar___0.getValue());
	                    i= (Short)(refVar___1.getValue());
	                    break;
	                }
	                 
	                // TODO: might not be correct. Was : Exit Do
	                refblnDurch.setValue(true);
	            } 
	            if (mVok.get(vokNr).z == mAbfragebereich 
	            		| mAbfragebereich >= 6 & mVok.get(vokNr).z >= 6 
	            		| mAbfragebereich == 0 & mVok.get(vokNr).z <= 0)
	            {
	                mLernVokabeln[i] = vokNr;
	                break;
	            }
	             
	        }
	        while (true);
	        // TODO: might not be correct. Was : Exit Do
	        RefSupport<Object> refVar___2 = new RefSupport<Object>(vokNr);
	        RefSupport<Object> refVar___3 = new RefSupport<Object>(i);
	        vokabelVonAllenHolen(refVar___2,refVar___3);
	        refvokNr.setValue(refVar___2.getValue());
	        refi.setValue(refVar___3.getValue());
	    }

	    public void vokabelVonAllenHolen(RefSupport<Object> refvokNr, RefSupport<Object> refi) throws Exception 
	    {
	    	short intVokNr = (Short) refvokNr.getValue();
            short i = (Short)refi.getValue();
        	
	        do
	        {
	            if (intVokNr < mGesamtzahl)
	            {
	                intVokNr += 1;
	        		
	            }
	            else
	            {
	            	intVokNr = 1;
	            } 
	            if (mAbfrageZufällig){
	            	intVokNr = (short) lib.rndInt(0,mGesamtzahl);
	            }
	            	
	                
	             
	            if (mVok.get(intVokNr).z <= 1)
	            {
	                mLernVokabeln[i] = intVokNr;
	                break;
	            }
	            else
	            {
	                // TODO: might not be correct. Was : Exit Do
	                if (Math.random() < 1 / mVok.get(intVokNr).z)
	                {
	                    mLernVokabeln[i] = intVokNr;
	                    break;
	                }
	                 
	            } 
	        }
	        while (true);
	    	refvokNr.setValue(intVokNr);
	    	refi.setValue(i);
	        return ;
	    }

	
		public void DeleteVokabel()
		{
			DeleteVokabel(-1);
		}
		public void DeleteVokabel(int index)
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.DeleteVokabel Start";
			//
			//
			//
			if (index == -1)
				index = mIndex;
			mVok.remove(index);
			mGesamtzahl = mVok.size();
			//mVok = lib.ResizeArray(mVok, mGesamtzahl +1 );
			return;
					}

		public void InsertVokabel()
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.InsertVokabel Start";
			//
			//
			//
			return;
					}

		public void AddVokabel()
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.AddVokabel Start";
			//
			mVok.add(new typVok("", "", "", "", "", (short) 0));
			mGesamtzahl = mVok.size();
			mIndex = mGesamtzahl-1;
			//mVok = lib.ResizeArray(mVok, mGesamtzahl +1 );
			return;
					}

		public void SaveFile(String strFileName, boolean blnUniCode) throws Exception
		{
			if (libString.IsNullOrEmpty(strFileName))
				return;
			
			java.io.OutputStreamWriter sWriter = null;
			FileOutputStream os = null;
			libLearn.gStatus = "Vokabel.SaveFile Start";
			//
			String LWort = null;
			File fname = new File(strFileName);
			short h = 0;
			short qf = 0;
			short spr = 0;
			short einst = 0;
			short tasta = 0;
			short varbed = 0;
			String fontfil = null;
			String Sprache = null;
			String tastbel = null;
			fontfil = "";
			Sprache = "";
			tastbel = "";
			

			this.mVokPath = fname.getParent();
			try {
				Charset enc = Charset.defaultCharset();
				/*
				if (fname.exists()) 
				{
					java.io.InputStream in = new java.io.FileInputStream(fname); 
					java.io.InputStreamReader r = new java.io.InputStreamReader(in,enc); 
					enc = Charset.forName(r.getEncoding());
					r.close();
					in.close();
				}
				*/
				if (blnUniCode) 
				{
					enc = Charset.defaultCharset();
				} 
				else 
				{
					if (lib.ShowMessageYesNo(getContext(), getContext().getString(R.string.SaveAsUniCode)) == false) 
					{
						enc = Charset.availableCharsets().get("Windows-1252");
					} 
					else 
					{
						enc = Charset.defaultCharset();
					}
				}
				os = new java.io.FileOutputStream(fname);
				sWriter = new java.io.OutputStreamWriter(os, enc);

				//System.Windows.Forms.Cursor.Current = System.Windows.Forms.Cursors.WaitCursor;
				spr = (short) (spr | -(tasta) * 32);
				spr = (short) (spr | -(varbed) * 64);
				spr = (short) (spr | (varHebr ? 16 : 0));
				spr = (short) (spr | this.mSprache.ordinal());

				if ((fontfil).length() < 15) {
					fontfil = (this.mSprache.ordinal() + 1) + "," + (this.mSprache.name());
					fontfil += "," + (getFontWort().getSize());
					fontfil += "," + getFontWort().getName();
					fontfil += "," + (getFontBed().getSize());
					fontfil += "," + getFontBed().getName();
					fontfil += "," + (getFontKom().getSize());
					fontfil += "," + getFontKom().getName();

				}


				if (!libString.IsNullOrEmpty(tastbel) | !libString.IsNullOrEmpty(fontfil)) 
				{
					sWriter.write((spr | 128 | einst) +"\n");
					sWriter.write(tastbel+"\n");
					sWriter.write(fontfil+"\n");
				} else {
					sWriter.write((spr | einst)+"\n");
				}
				for (h = 0; h <= mVok.size() -1; h++) {
					if (!libString.IsNullOrEmpty(mVok.get(h).Wort)) {
						LWort = mVok.get(h).Wort;
						if (!libString.IsNullOrEmpty(mVok.get(h).Kom))
							LWort += (char)8 + mVok.get(h).Kom;
						if (!libString.IsNullOrEmpty(LWort))
							LWort = LWort.replace("\r", "{CR}").replace("\n", "{LF}");
						sWriter.write(LWort+"\n");
						LWort = mVok.get(h).Bed1;
						if (!libString.IsNullOrEmpty(LWort))
							LWort = LWort.replace("\r", "{CR}").replace("\n", "{LF}");
						qf = 0;
						sWriter.write(LWort+"\n");
						LWort = mVok.get(h).Bed2;
						if (!libString.IsNullOrEmpty(LWort))
							LWort = LWort.replace("\r", "{CR}").replace("\n", "{LF}");
						qf = 0;
						sWriter.write(LWort+"\n");
						LWort = mVok.get(h).Bed3;
						if (!libString.IsNullOrEmpty(LWort))
							LWort = LWort.replace("\r", "{CR}").replace("\n", "{LF}");
						qf = 0;
						sWriter.write(LWort+"\n");
						sWriter.write((mVok.get(h).z)+"\n");
					}

				}
			} catch (Exception ex) {
				throw new Exception("SaveVokError", ex);
			} finally {
				sWriter.close();
				sWriter = null;
				os.close();
				os = null;
			}
			aend = false;

			spr = (short) (spr & 7);
			//System.Windows.Forms.Cursor.Current = System.Windows.Forms.Cursors.Default;
		}
		public void revert()
		{

			for (int h = 0; h <= mVok.size()-1; h++) {
				String vok = mVok.get(h).Wort;
				mVok.get(h).Wort = mVok.get(h).Bed1;
				if (!libString.IsNullOrEmpty(mVok.get(h).Bed2)) {
					mVok.get(h).Wort += "/" + mVok.get(h).Bed2;
					mVok.get(h).Bed2 = "";
					if (!libString.IsNullOrEmpty(mVok.get(h).Bed3)) {
						mVok.get(h).Wort += "/" + mVok.get(h).Bed3;
						mVok.get(h).Bed3 = "";
					}
				}
				mVok.get(h).Bed1 = vok;
				mVok.get(h).z = 0;
			}
		}

		public void reset()
		{
			for (int h = 0; h <= mVok.size()-1; h++) {
				mVok.get(h).z = 0;
			}
		}

		int static_GetNextLineFromString_startLine;
		//Private Function GetNextLineFromString(ByRef strContent As String, Optional ByRef strRef As String = "nihxyz", Optional ByRef FirstLine As Single = 0) As Boolean
		private boolean GetNextLineFromString(String strContent) throws Exception {
			int FirstLine = 0;
			RefSupport<String> strRef = new RefSupport<String>("nihxyz");
			return GetNextLineFromString(strContent, strRef, FirstLine);
		}
		private boolean GetNextLineFromString(String strContent, RefSupport<String> strRef) throws Exception {
			int FirstLine = 0;
			return GetNextLineFromString(strContent, strRef, FirstLine);
		}
			  
		 private boolean GetNextLineFromString(String strContent, RefSupport<String> strRef, int FirstLine) throws Exception {
		        boolean functionReturnValue = false;
		        // ERROR: Not supported in C#: OnErrorStatement
		        libLearn.gStatus = "Vokabel.GetNextLineFromString Start";
		        int crFound = 0;
		        if (libString.IsNullOrEmpty(strContent))
		        {
		            throw new RuntimeException("GetNextLineFromString\n" + "String ist empty!");
		        }
		         
		        if ((strRef.getValue().equals("nihxyz")))
		        {
		            functionReturnValue = !(static_GetNextLineFromString_startLine > (strContent.length()));
		            return functionReturnValue;
		        }
		         
		        // Inserted by CodeCompleter
		        if (FirstLine != 0)
		            static_GetNextLineFromString_startLine = 1;
		         
		        crFound =strContent.indexOf("\r", static_GetNextLineFromString_startLine -1) + 1;
		        if (crFound == 0)
		            crFound = strContent.length();
		         
		        strRef.setValue(strContent.substring(static_GetNextLineFromString_startLine, crFound - 1));
		        static_GetNextLineFromString_startLine = crFound + 2;
		        return functionReturnValue;
		    }

		
		 
		 public void LoadFromString(String strContent) throws Exception
		 {
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.LoadFromString Start";

			short qf = 0;
			short hh = 0;
			short h = 0;
			short sp = 0;
			short n = 0;
			short lad = 0;
			short indexlang = 0;
			String fontfil = null;
			String tastbel = null;
			String strTmp = null;
			mLernVokabeln = new int[mSchrittweite + 1];
			mLastIndex = 0;
			 // ERROR: Not supported in C#: OnErrorStatement

			mFileName = "";
			fontfil = "";
			tastbel = "";
			strTmp = "";

			libLearn.gStatus = "Vokabel.LoadFromString Line 669";
			// Inserted by CodeCompleter

			RefSupport<String>refStrTmp = new RefSupport<String>(strTmp);
			GetNextLineFromString(strContent, refStrTmp, -1);
			strTmp = refStrTmp.getValue();
			//SPRACHE LADEN
			sp = (short) Integer.parseInt(strTmp);
			indexlang = (short) (sp & 7);
			libLearn.gStatus = "Vokabel.LoadFromString Line 679";
			// Inserted by CodeCompleter
			//lib.setEnumOrdinal(mSprache, indexlang);
			for (EnumSprachen Sprache: EnumSprachen.values())
			{
				if (Sprache.ordinal() == indexlang)
				{
					mSprache = Sprache;
					break;
				}
			}
			if ((sp & 128) != 0) {
				refStrTmp.setValue(tastbel);
				GetNextLineFromString(strContent, refStrTmp);
				tastbel= refStrTmp.getValue();
				refStrTmp.setValue(fontfil);
				GetNextLineFromString(strContent, refStrTmp);
				fontfil = refStrTmp.getValue();
				 	RefSupport<String> refVar___0 = new RefSupport<String>(fontfil);
			        RefSupport<Object> refVar___1 = new RefSupport<Object>(hh);
			        RefSupport<Object> refVar___2 = new RefSupport<Object>(h);
			        RefSupport<Object> refVar___3 = new RefSupport<Object>(indexlang);
			        RefSupport<Object> refVar___4 = new RefSupport<Object>(qf);
			        RefSupport<Object> refVar___5 = new RefSupport<Object>(lad);
			        Getfonts(refVar___0, refVar___1, refVar___2, refVar___3, refVar___4, refVar___5);
			        fontfil = (String) refVar___0.getValue();
			        hh = (Short) refVar___1.getValue();
			        h = (Short) refVar___2.getValue();
			        indexlang = (Short) refVar___3.getValue();
			        qf = (Short) refVar___4.getValue();
			        lad = (Short) refVar___5.getValue();
				//Windows Fonts extrahieren
			} else {
				lad = 0;
			}
			while ((GetNextLineFromString(strContent))) {
				libLearn.gStatus = "Vokabel.LoadFromString Line 689";
				// Inserted by CodeCompleter
				//System.Windows.Forms.Application.DoEvents();
				mVok.add(new typVok());
				n = (short) (mVok.size()-1);
				//lib.ResizeArray(mVok, n + 1);
				RefSupport<String>refWort = new RefSupport<String>(mVok.get(n).Wort);
				GetNextLineFromString(strContent, refWort);
				mVok.get(n).Wort = refWort.getValue();
				qf = (short) (mVok.get(n).Wort.indexOf(0)+1);
				if (qf == 0)
					qf = (short) (mVok.get(n).Wort.indexOf(8)+1);
				if (qf != 0) {
					mVok.get(n).Kom = mVok.get(n).Wort.substring(qf);
					libLearn.gStatus = "Vokabel.LoadFromString Line 699";
					// Inserted by CodeCompleter
					mVok.get(n).Wort = mVok.get(n).Wort.substring(qf - 1);
				}
				refStrTmp.setValue(mVok.get(n).Bed1);
				GetNextLineFromString(strContent, refStrTmp);
				mVok.get(n).Bed1 = refStrTmp.getValue();
				
				refStrTmp.setValue(mVok.get(n).Bed2);
				GetNextLineFromString(strContent, refStrTmp);
				mVok.get(n).Bed2 = refStrTmp.getValue();
				
				refStrTmp.setValue(mVok.get(n).Bed3);
				GetNextLineFromString(strContent, refStrTmp);
				mVok.get(n).Bed3 = refStrTmp.getValue();
				
				refStrTmp.setValue(strTmp);
				GetNextLineFromString(strContent, refStrTmp);
				strTmp = refStrTmp.getValue();
				
				mVok.get(n).z = (short) Integer.parseInt(strTmp);
				if (libString.IsNullOrEmpty(mVok.get(n).Wort)) {
					libLearn.gStatus = "Vokabel.LoadFromString Line 709";
					// Inserted by CodeCompleter
					mVok.remove(n);
					n  = (short) (mVok.size()-1);
					//lib.ResizeArray(mVok, n + 1);
				}

			}
			mGesamtzahl = n;
			mIndex = 1;
			

			// ******** Hier gehts hin wenn ein Fehler auftrit oder wenn _
			//' ******** Schluß ist.....

			//Defmouse 0
			sp = (short) (sp & 7);
			if (sp >= 0 & sp <= 3) {
				indexlang = sp;
			}
			switch (indexlang) {
				//         Case 0: mSprache = "Deutsch"
				//         Case 1: mSprache = "Hebräisch"
				//         Case 2: mSprache = "Griechisch"
				//         Case Is > 2: Sprache = "Sonstige"
			}
			//If Sprache <> "" Then mSprache = Sprache
			if (mGesamtzahl > 5) {
				InitAbfrage();
				mFileName = "String";
			} else {
				mblnLernInit = false;
			}
			aend = false;
			return;
			}

		    public void Getfonts(RefSupport<String> fontfil, RefSupport<Object> refhh, RefSupport<Object> refh, RefSupport<Object> refindexLang, RefSupport<Object> refqf, RefSupport<Object> reflad) throws Exception 
		    {
		        //getfonts:,// ********** Hier werden die Fonts 'extrahiert'
		        short h = (Short)refh.getValue();
		    	short hh = (Short)refhh.getValue();
		    	short indexLang = (Short) refindexLang.getValue();
		    	short qf = (Short)refqf.getValue();
		    	String tmpStr = null;
		        hh = 1;
		        fontfil.setValue(fontfil.getValue().trim());
		        if ((fontfil.getValue().indexOf(",")) > -1)
		        {
		            fontfil.setValue(fontfil.getValue() + ",");
		            h=(short) ((fontfil.getValue().indexOf(",",hh-1))+1);
		            if (h != 0 && h - hh > 0)
		            	indexLang=(short) (Integer.parseInt(fontfil.getValue().substring(hh-1, h-1)));
		             
		            hh = (short) (h+1);
		            h = (short) ((fontfil.getValue().indexOf(",", hh-1) )+1);
		            try
		            {
		                if (h != 0 && h - hh > 0)
		                    mSprache = EnumSprachen.valueOf((fontfil.getValue().substring(hh-1, h-1)));
		            }
		            catch (Exception __dummyCatchVar0)
		            {
		                if (fontfil.getValue().substring(hh-1, h-1).equals("Hebräisch"))
		                {
		                    mSprache = EnumSprachen.Hebrew;
		                }
		                else
		                {
		                    mSprache = EnumSprachen.Normal;
		                } 
		            }

		            hh=(short) (h + 1);
		            for (qf=1;qf <= 3;qf++)
		            {
		                h=(short) ((fontfil.getValue().indexOf(",",hh-1))+1);
		                if (h != 0 && h - hh > 0)
		                {
		                    switch(qf)
		                    {
		                        case 1: 
		                            mWortFont.setSize(Integer.parseInt(fontfil.getValue().substring(hh-1, h-1).trim()));
		                            break;
		                        case 2: 
		                            mBedFont.setSize(Integer.parseInt(fontfil.getValue().substring(hh-1, h-1).trim()));
		                            break;
		                        case 3: 
		                            mKomFont.setSize(Integer.parseInt(fontfil.getValue().substring(hh-1, h-1).trim()));
		                            break;
		                    
		                    }
		                }
		                 
		                hh=(short) (h+1);
		                h=(short) ((fontfil.getValue().indexOf(",",hh-1))+1);
		                if (h != 0 & h - hh > 0)
		                {
		                    switch(qf)
		                    {
		                        case 1: 
		                            mWortFont.setName(fontfil.getValue().substring(hh-1, h-1).trim());
		                            break;
		                        case 2: 
		                            mBedFont.setName(fontfil.getValue().substring(hh-1, h-1).trim());
		                            break;
		                        case 3: 
		                            mKomFont.setName(fontfil.getValue().substring(hh-1, h-1).trim());
		                            break;
		                    
		                    }
		                }
		                 
		                hh=(short) (h + 1);
		            }
		            reflad.setValue((short)-1);
		        }
		        refh.setValue(h);
		        refhh.setValue(hh);
		        refindexLang.setValue(indexLang);
		        refqf.setValue(qf);
		    }
	    

		


		public void NewFile()
		{
			mLernVokabeln = new int[mSchrittweite + 1];
			mVok = new ArrVok();
			mVok.add(new typVok());
			mLastIndex = 0;
			mGesamtzahl = 0;
			mIndex = 0;
		}
		
		public void LoadFile(String strFileName) throws Exception
		{
			LoadFile(strFileName, false, false, false);
		}
		
		public void LoadFile(String strFileName, boolean blnSingleLine, boolean blnAppend, boolean blnUnicode) throws Exception
		{
			try
			{
				final String CodeLoc = "Vokabel.LoadFile";
				libLearn.gStatus = CodeLoc + " Start";
	
				short sp = 0;
				short h = 0;
				short hh = 0;
				short qf = 0;
				short n = 0;
				short lad = 0;
				short indexlang = 0;
				String fontfil = null;
				String strTmp = null;
				java.io.InputStreamReader isr = null;
				java.io.FileInputStream  is = null;
				WindowsBufferedReader sr = null;
				String tmp = null;
				fontfil = "";
				strTmp = "";
				mLernVokabeln = new int[mSchrittweite + 1];
				mLastIndex = 0;
				 // ERROR: Not supported in C#: OnErrorStatement
	
				libLearn.gStatus = "Load File: " + strFileName;
				
				mFileName = "";
	
				libLearn.gStatus = CodeLoc + " Open Stream";
				// Inserted by CodeCompleter
				java.io.File F = new java.io.File(strFileName);
				do
				{
					if (F.exists()) {
						is = new java.io.FileInputStream(F);
						isr = new java.io.InputStreamReader(is, (blnUnicode ? Charset.defaultCharset() : Charset.availableCharsets().get("Windows-1252")));
						sr = new WindowsBufferedReader(isr);
					} else {
						lib.ShowMessage(getContext(), getContext().getString(R.string.FileDoesNotExist));
						//Call Err.Raise(vbObjectError + ErrWrongfilename, CodeLoc & "", "Dateiname_ungültig", "", "")
						return;
					}
					_UniCode = (isr.getEncoding().equals("Unicode") || isr.getEncoding().equals("UTF8"));
					if (lib.getExtension(F).toLowerCase(Locale.getDefault()).indexOf(".k") != -1)
						_cardmode = true;
					else
						_cardmode = false;
					libLearn.gStatus = CodeLoc + " ReadLine1";
					tmp = sr.readLine();
					try
					{
						sp = (short) Integer.parseInt(tmp.trim());
					}
					catch (NumberFormatException ex)
					{
						lib.ShowException(getContext(), ex);
						sp -=1;
						blnUnicode = !blnUnicode;
						if (sr!= null) sr.close();
						if (isr!= null) isr.close();
						if (is!=null) is.close();
					}
				}
				while (sp==-1);
				varHebr = (sp & 16) != 0;
				libLearn.gStatus = CodeLoc + " Line 819";
				// Inserted by CodeCompleter
				indexlang = (short) (sp & 7);
				if (!blnAppend)
				{
					for (EnumSprachen Sprache: EnumSprachen.values())
					{
						if (Sprache.ordinal() == indexlang)
						{
							mSprache = Sprache;
							break;
						}
					}
				}
				if ((sp & 128) != 0) {
					String x;
					while ((x = sr.readLine()).length()==0);
					fontfil = x;
					if (!blnAppend)
					{
						RefSupport<String> refVar___0 = new RefSupport<String>(fontfil);
				        RefSupport<Object> refVar___1 = new RefSupport<Object>(hh);
				        RefSupport<Object> refVar___2 = new RefSupport<Object>(h);
				        RefSupport<Object> refVar___3 = new RefSupport<Object>(indexlang);
				        RefSupport<Object> refVar___4 = new RefSupport<Object>(qf);
				        RefSupport<Object> refVar___5 = new RefSupport<Object>(lad);
				        Getfonts(refVar___0, refVar___1, refVar___2, refVar___3, refVar___4, refVar___5);
				        fontfil = (String) refVar___0.getValue();
				        hh = (Short) refVar___1.getValue();
				        h = (Short) refVar___2.getValue();
				        indexlang = (Short) refVar___3.getValue();
				        qf = (Short) refVar___4.getValue();
				        lad = (Short) refVar___5.getValue();
					}
				        //Windows Fonts extrahieren
				} else {
					lad = 0;
				}
				libLearn.gStatus = CodeLoc + " Line 829";
				// Inserted by CodeCompleter
				if (blnAppend)
					n = (short) mGesamtzahl;
				for (String x = sr.readLine(); x != null; x = sr.readLine()) 
				{
					int Len = x.length();
					if (Len==0) continue;
					typVok CurVok = new typVok();
					mVok.add(CurVok);
					n  = (short)(mVok.size()-1);
					//mVok = lib.ResizeArray(mVok, n + 1);
					libLearn.gStatus = CodeLoc + " ReadLine2";
					CurVok.Wort = x.replace("{CR}", "\r").replace("{LF}", "\n");
					qf = (short) libString.InStr(CurVok.Wort, "\0");
					if (qf == 0)
						qf = (short) libString.InStr(CurVok.Wort, libString.Chr(8));
					if (qf != 0) {
						CurVok.Kom = libString.Right(CurVok.Wort, libString.Len(CurVok.Wort) - qf);
						libLearn.gStatus = CodeLoc + " Line 839";
						// Inserted by CodeCompleter
						CurVok.Wort = libString.Left(CurVok.Wort, qf - 1);
					} else {
						CurVok.Kom = "";
					}
					libLearn.gStatus = CodeLoc + " ReadLine3";
					if (!((x=sr.readLine()) == null)) {
						CurVok.Bed1 = x.replace("{CR}", "\r").replace("{LF}", "\n");
					}
					if (!blnSingleLine) {
						if (!((x=sr.readLine()) == null)) {
							libLearn.gStatus = CodeLoc + " ReadLine4";
							CurVok.Bed2 = x.replace("{CR}", "\r").replace("{LF}", "\n");
						}
						libLearn.gStatus = CodeLoc + " Line 849";
						// Inserted by CodeCompleter
						if (!((x=sr.readLine()) == null)) {
							libLearn.gStatus = CodeLoc + " ReadLine5";
							CurVok.Bed3 = x.replace("{CR}", "\r").replace("{LF}", "\n");
						}
					} else {
						CurVok.Bed2 = "";
						CurVok.Bed3 = "";
					}
					if (!((x=sr.readLine()) == null)) {
						libLearn.gStatus = CodeLoc + " ReadLine6";
						strTmp = x;
						CurVok.z = (short) Integer.parseInt(strTmp);
					}
					if (libString.IsNullOrEmpty(CurVok.Wort)) {
						mVok.remove(n);
						n  = (short) (mVok.size()-1);
						libLearn.gStatus = CodeLoc + " Line 859";
						// Inserted by CodeCompleter
						//mVok = lib.ResizeArray(mVok, n + 1);
					} else {
						CurVok.Wort = CurVok.Wort.replace("ùú", "\r\n");
						CurVok.Kom = CurVok.Kom.replace("ùú", "\r\n");
						CurVok.Bed1 = CurVok.Bed1.replace("ùú", "\r\n");
						CurVok.Bed2 = CurVok.Bed2.replace("ùú", "\r\n");
						CurVok.Bed3 = CurVok.Bed3.replace("ùú", "\r\n");
					}
					libLearn.gStatus = CodeLoc + " End While";
				}
				mGesamtzahl = n;
				if (!blnAppend)
					mIndex = 1;
	
				// ******** Hier gehts hin wenn ein Fehler auftrit oder wenn _
				//' ******** Schluß ist.....
				libLearn.gStatus = CodeLoc + " CloseFile";
				closefile:
				// Inserted by CodeCompleter
				sr.close();
				isr.close();
				is.close();
				//Defmouse 0
				sp = (short) (sp & 7);
				if (sp >= 0 & sp <= 3) {
					indexlang = sp;
				}
				switch (indexlang) {
					//         Case 0: mSprache = "Deutsch"
	
					//         Case 1: mSprache = "Hebräisch"
					//         Case 2: mSprache = "Griechisch"
					//         Case Is > 2: Sprache = "Sonstige"
				}
				//If Sprache <> "" Then mSprache = Sprache
				if (mGesamtzahl > 5) {
					InitAbfrage();
					if (!blnAppend)
						mFileName = strFileName;
				} else {
					libLearn.gStatus = CodeLoc + " Line 889";
					// Inserted by CodeCompleter
					mblnLernInit = false;
				}
				aend = false;
				return;
			}
			catch (Exception ex)
			{
				throw new RuntimeException("Error in Loadfile", ex);
			}	
		}
		/*
		public void LoadFileAndConvert(String strFileName)
		{
			boolean blnSingleLine = false; boolean blnAppend = false;
			LoadFileAndConvert(strFileName,blnSingleLine,blnAppend);
		}
		public void LoadFileAndConvert(String strFileName, boolean blnSingleLine, boolean blnAppend)
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.LoadFile Start";

			short sp = 0;
			short h = 0;
			short hh = 0;
			short qf = 0;
			short einst = 0;
			short tasta = 0;
			String ext = new String(' ', 3);
			short n = 0;
			short lad = 0;
			short indexlang = 0;
			short varbed = 0;
			String fontfil = null;
			String Sprache = null;
			String tastbel = null;
			String strTmp = null;
			System.IO.StreamReader sr = null;
			fontfil = "";
			Sprache = "";
			tastbel = "";
			strTmp = "";
			mLernVokabeln = new int[mSchrittweite + 1];
			mLastIndex = 0;
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Load File: " + strFileName;
			mFileName = "";

			libLearn.gStatus = "Vokabel.LoadFile Line 799";
			// Inserted by CodeCompleter

			if (!libString.IsNullOrEmpty(FileSystem.Dir(strFileName))) {
				sr = new System.IO.StreamReader(strFileName, System.Text.Encoding.GetEncoding(1252));
			} else {
				Interaction.MsgBox(ClsGlobal.GetLang("FileDoesNotExist", "Dateiname existiert nicht!", ));
				//Call Err.Raise(vbObjectError + ErrWrongfilename, "Vokabel.Loadfile", "Dateiname_ungültig", "", "")
				return;
			}
			if (System.IO.Path.GetExtension(strFileName).IndexOf(".k", System.StringComparison.CurrentCultureIgnoreCase) != -1)
				_cardmode = true;
			else
				_cardmode = false;

			sp = sr.readLine();
			einst = sp & ((Math.Pow(2, 16)) - 256);
			varHebr = (sp & 16) != 0;
			varbed = (sp & 64) != 0;
			tasta = (sp & 32) != 0;
			libLearn.gStatus = "Vokabel.LoadFile Line 819";
			// Inserted by CodeCompleter
			indexlang = sp & 7;
			if (!blnAppend)
				mSprache = indexlang;
			if (sp & 128) {
				tastbel = sr.readLine();
				fontfil = sr.readLine();
				if (!blnAppend)
					getfonts(ref fontfil, ref hh, ref h, ref indexlang, ref qf, ref lad);
				//Windows Fonts extrahieren
			} else {
				lad = false;
			}
			libLearn.gStatus = "Vokabel.LoadFile Line 829";
			// Inserted by CodeCompleter
			if (blnAppend)
				n = mGesamtzahl;
			while (!sr.EndOfStream) {
				n = n + 1;
				Array.Resize(ref mVok, n + 1);
				mVok.get(n).Wort = sr.readLine().replace("{CR}", "\r").replace("{LF}", "\n");
				qf = libString.InStr(mVok.get(n).Wort, libString.Chr(0));
				if (qf == 0)
					qf = libString.InStr(mVok.get(n).Wort, libString.Chr(8));
				if (qf != 0) {
					mVok.get(n).Kom = libString.Right(mVok.get(n].Wort, libString.Len(mVok[n).Wort) - qf);
					libLearn.gStatus = "Vokabel.LoadFile Line 839";
					// Inserted by CodeCompleter
					mVok.get(n).Wort = libString.Left(mVok.get(n).Wort, qf - 1);
				} else {
					mVok.get(n).Kom = "";
				}
				String WordConvert = "";
				char cTest = '\0';
				foreach (char c in mVok.get(n).Wort) {
					String cConv = c;
					if (this.Sprache == EnumSprachen.Hebrew) {
						this.aend = true;
						cConv = ConvHebrew(c);
						WordConvert = cConv + WordConvert;
					} else if (this.Sprache == EnumSprachen.Griechisch) {
						this.aend = true;
						cConv = ConvGreek(c);
						WordConvert += cConv;
					}

				}

				mVok.get(n).Wort = WordConvert;
				if (!sr.EndOfStream) {
					mVok.get(n).Bed1 = sr.readLine().replace("{CR}", "\r").replace("{LF}", "\n");
				}
				if (!blnSingleLine) {
					if (!sr.EndOfStream) {
						mVok.get(n).Bed2 = sr.readLine().replace("{CR}", "\r").replace("{LF}", "\n");
					}
					libLearn.gStatus = "Vokabel.LoadFile Line 849";
					// Inserted by CodeCompleter
					if (!sr.EndOfStream) {
						mVok.get(n).Bed3 = sr.readLine().replace("{CR}", "\r").replace("{LF}", "\n");
					}
				} else {
					mVok.get(n).Bed2 = "";
					mVok.get(n).Bed3 = "";
				}
				if (!sr.EndOfStream) {
					strTmp = sr.readLine();
					mVok.get(n).z = Conversion.Val(strTmp);
				}
				if (libString.IsNullOrEmpty(mVok.get(n).Wort)) {
					n = n - 1;
					libLearn.gStatus = "Vokabel.LoadFile Line 859";
					// Inserted by CodeCompleter
					Array.Resize(ref mVok, n + 1);
				} else {
					mVok.get(n).Wort = mVok.get(n).Wort.replace("ùú", "\r\n");
					mVok.get(n).Kom = mVok.get(n).Kom.replace("ùú", "\r\n");
					mVok.get(n).Bed1 = mVok.get(n).Bed1.replace("ùú", "\r\n");
					mVok.get(n).Bed2 = mVok.get(n).Bed2.replace("ùú", "\r\n");
					mVok.get(n).Bed3 = mVok.get(n).Bed3.replace("ùú", "\r\n");
				}

			}
			if (this.aend == true) {
				if (this.Sprache == EnumSprachen.Hebrew || this.Sprache == EnumSprachen.Griechisch) {
					this.FontWort.Name = "Cardo";
					varHebr = false;
				}
			}
			mGesamtzahl = n;
			if (!blnAppend)
				mIndex = 1;

			// ******** Hier gehts hin wenn ein Fehler auftrit oder wenn _
			//' ******** Schluß ist.....
			libLearn.gStatus = "Vokabel.LoadFile Line 869";
			closefile:
			// Inserted by CodeCompleter
			sr.Close();
			sr.Dispose();
			sr = null;
			//Defmouse 0
			sp = sp & 7;
			if (sp >= 0 & sp <= 3) {
				indexlang = sp;
			}
			switch (indexlang) {
				//         Case 0: mSprache = "Deutsch"

				//         Case 1: mSprache = "Hebräisch"
				//         Case 2: mSprache = "Griechisch"
				//         Case Is > 2: Sprache = "Sonstige"
			}
			//If Sprache <> "" Then mSprache = Sprache
			if (mGesamtzahl > 5) {
				InitAbfrage();
				if (!blnAppend)
					mFileName = strFileName;
			} else {
				libLearn.gStatus = "Vokabel.LoadFile Line 889";
				// Inserted by CodeCompleter
				mblnLernInit = false;
			}
			aend = false;
			return;

			// FErr:
			if (Err().Number == 59){Interaction.MsgBox("Wort zu lang!"); // ERROR: Not supported in C#: ResumeStatement
	}

			if (Interaction.MsgBox("Fileerror " + "\r\n" + Err().Description, MsgBoxStyle.RetryCancel) == MsgBoxResult.Retry) {
				 // ERROR: Not supported in C#: ResumeStatement

			}
			 // ERROR: Not supported in C#: OnErrorStatement

			goto closefile;
			return;
					}
		public String ConvHebrew(char c)
		{
			char cTest = '\0';
			String cConv = c;
			if (c != 'I') {
				cTest = (char.ToLower(c));
			} else {
				cTest = c;
			}
			switch (libString.Asc(cTest)) {
				case 0x61:
				case 0x62:
					cConv = libString.ChrW(libString.Asc(c) - 0x61 + 0x5d0);
					break;
				case 0x63:
					cConv = libString.ChrW(0x5e1);
					break;
				case 0x64:
					cConv = libString.ChrW(0x5d3);
					break;
				case 0x65:
					cConv = libString.ChrW(0x5b6);
					break;
				case 0x66:
					cConv = libString.ChrW(0x5b8);
					break;
				case 0x67:
					cConv = libString.ChrW(0x5d2);
					break;
				case 0x68:
					cConv = libString.ChrW(0x5d4);
					break;
				case 0x69:
					cConv = libString.ChrW(0x5e2);
					break;
				case 0x6a:
					cConv = libString.ChrW(0x5e6);
					break;
				case 0x6b:
					cConv = libString.ChrW(0x5db);
					break;
				case 0x6c:
					cConv = libString.ChrW(0x5dc);
					break;
				case 0x6d:
					cConv = libString.ChrW(0x5de);
					break;
				case 0x6e:
					cConv = libString.ChrW(0x5e0);
					break;
				case 0x6f:
					//o
					cConv = libString.ChrW(0x5b9);
					break;
				case libString.Asc('p'):
					cConv = libString.ChrW(0x5e4);
					break;
				case libString.Asc('q'):
					cConv = libString.ChrW(0x5e7);
					break;
				case libString.Asc('r'):
					cConv = libString.ChrW(0x5e8);
					break;
				case libString.Asc('s'):
					cConv = libString.ChrW(0x5e9) + libString.ChrW(0x5c2);
					break;
				case libString.Asc('t'):
					cConv = libString.ChrW(0x5ea);
					break;
				case libString.Asc('u'):
					cConv = libString.ChrW(0x5d8);
					break;
				case libString.Asc('v'):
					cConv = libString.ChrW(0x5d5);
					break;
				case libString.Asc('w'):
					cConv = libString.ChrW(0x5e9) + libString.ChrW(0x5c1);
					break;
				case libString.Asc('x'):
					cConv = libString.ChrW(0x5d7);
					break;
				case libString.Asc('y'):
					cConv = libString.ChrW(0x5d9);
					break;
				case libString.Asc('z'):
					cConv = libString.ChrW(0x5d6);
					break;
				case libString.Asc('('):
					cConv = libString.ChrW(0x5e2);
					break;
				case libString.Asc(')'):
					cConv = libString.ChrW(0x5d0);
					break;
				case libString.Asc('+'):
					cConv = libString.ChrW(0x5b7);
					break;
				case libString.Asc('-'):
					cConv = libString.ChrW(0x5b7);
					break;
				case libString.Asc('0'):
					cConv = libString.ChrW(0x5c2);
					break;
				case libString.Asc('1'):
					cConv = libString.ChrW(0x5c5);
					break;
				case libString.Asc('2'):
					cConv = libString.ChrW(0x5b0);
					break;
				case libString.Asc('3'):
					cConv = libString.ChrW(0x5a6);
					break;
				case libString.Asc('4'):
					cConv = "";
					break;
				case libString.Asc('5'):
					cConv = "";
					break;
				case libString.Asc('6'):
					cConv = "";
					break;
				case libString.Asc('7'):
					cConv = "";
					break;
				case libString.Asc('9'):
					cConv = libString.ChrW(0x5bf);
					break;
				case libString.Asc('"'):
					cConv = libString.ChrW(0x5b5);
					break;
				case libString.Asc('I'):
					cConv = libString.ChrW(0x5b4);
					break;
				case libString.Asc('f'):
					cConv = libString.ChrW(0x5b8);
					break;
				case libString.Asc('o'):
					cConv = libString.ChrW(0x5c1);
					break;
				case libString.Asc('e'):
					cConv = libString.ChrW(0x5b6);
					break;
				case libString.Asc(':'):
					cConv = libString.ChrW(0x5b0);
					break;
				case libString.Asc('_'):
					cConv = libString.ChrW(0x5b2);
					break;
				case libString.Asc('.'):
					cConv = libString.ChrW(0x5c3);
					break;
				case libString.Asc('^'):
					cConv = libString.ChrW(0x5ab);
					break;
				case libString.Asc(']'):
					cConv = libString.ChrW(0x5df);
					break;
				case libString.Asc('%'):
					cConv = libString.ChrW(0x5da) + libString.ChrW(0x5bc) + libString.ChrW(0x5b3);

					break;

			}
			return cConv;
		}
		public String ConvGreek(char c)
		{
			char cTest = '\0';
			String cConv = c;
			//If c <> "I"c Then
			//  cTest = (Char.ToLower(c))
			//Else 
			cTest = c;
			// End If
			switch (libString.Asc(cTest)) {
				case libString.Asc('A'):
					cConv = libString.ChrW(0x391);
					break;
				case libString.Asc('B'):
					cConv = libString.ChrW(0x392);
					break;
				case libString.Asc('C'):
					cConv = libString.ChrW(0x3a7);
					break;
				case libString.Asc('D'):
					cConv = libString.ChrW(0x394);
					break;
				case libString.Asc('E'):
					cConv = libString.ChrW(0x395);
					break;
				case libString.Asc('F'):
					cConv = libString.ChrW(0x3a6);
					break;
				case libString.Asc('G'):
					cConv = libString.ChrW(0x393);
					break;
				case libString.Asc('H'):
					cConv = libString.ChrW(0x397);
					break;
				case libString.Asc('I'):
					cConv = libString.ChrW(0x399);
					break;
				case libString.Asc('J'):
					cConv = "ῳͅ";
					break;
				case libString.Asc('K'):
					cConv = libString.ChrW(0x39a);
					break;
				case libString.Asc('L'):
					cConv = libString.ChrW(0x39b);
					break;
				case libString.Asc('M'):
					cConv = libString.ChrW(0x39c);
					break;
				case libString.Asc('N'):
					cConv = libString.ChrW(0x39d);
					break;
				case libString.Asc('O'):
					cConv = libString.ChrW(0x39f);
					break;
				case libString.Asc('P'):
					cConv = libString.ChrW(0x3a0);
					break;
				case libString.Asc('Q'):
					cConv = libString.ChrW(0x398);
					break;
				case libString.Asc('R'):
					cConv = libString.ChrW(0x3a1);
					break;
				case libString.Asc('S'):
					cConv = libString.ChrW(0x3a3);
					break;
				case libString.Asc('T'):
					cConv = libString.ChrW(0x3a4);
					break;
				case libString.Asc('U'):
					cConv = libString.ChrW(0x3a5);
					break;
				case libString.Asc('V'):
					cConv = "ῃ";
					break;
				case libString.Asc('W'):
					cConv = libString.ChrW(0x3a9);
					break;
				case libString.Asc('X'):
					cConv = libString.ChrW(0x39e);
					break;
				case libString.Asc('Y'):
					cConv = libString.ChrW(0x3a8);
					break;
				case libString.Asc('Z'):
					cConv = libString.ChrW(0x396);
					break;
				case libString.Asc('…'):
					cConv = "ί";
					break;
				case libString.Asc('š'):
					cConv = "έ";
					break;
				case libString.Asc('Œ'):
					cConv = "ἷ";
					break;
				case libString.Asc('ƒ'):
					cConv = "ἱ";
					break;
				case libString.Asc('†'):
					cConv = "ἵ";
					break;
				case libString.Asc('„'):
					cConv = "ἰ";
					break;
				case libString.Asc('ˆ'):
					cConv = "ὶ";
					break;
				case libString.Asc('$'):
					cConv = "ϛ";
					break;
				case libString.Asc('%'):
					cConv = "ϙ";
					break;
				case libString.Asc('#'):
					cConv = "ϝ";
					break;
				case 0x61: // TODO: to 0x7a
					cConv = "αβχδεφγηιςκλμνοπθρστυᾳωξψζ".SubString("abcdefghijklmnopqrstuvwxyz".IndexOf(cTest), 1);
					break;
				case libString.Asc('·'): // TODO: to libString.Asc('Ï')
					cConv = "ῥῤἡἠήἥἤὴἣἢῆἧἦͺᾑᾐῄᾕᾔῂᾓᾒῇᾗᾖ".SubString("·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏ".IndexOf(cTest), 1);
					break;
				case libString.Asc('\u008d'): // TODO: to libString.Asc('¶')
					int Index = "\u008d\u008e\u008f\u0090\u009d\u009e¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶".IndexOf(cTest);
					if (Index > -1) {
						cConv = "ἶῑΐῒὲἓἁἀάἅἄὰἃἂᾶἇἆᾁᾴᾅᾄᾲᾃᾄᾷᾇᾆ".SubString(Index, 1);
					}
					break;
				case libString.Asc('Ð'): // TODO: to libString.Asc('å')
					int Index = "ÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäå".IndexOf(cTest);
					if (Index > -1) {
						cConv = "ὁὀόὅὄὸὃὂὑὐύὕὔὺὓὔῦὗὖϋΰῢ".SubString(Index, 1);
					}
					break;
				case libString.Asc('æ'): // TODO: to libString.Asc('û')
					int Index = "æçèéêëìíîïðñòóôõö÷øùúû".IndexOf(cTest);
					if (Index > -1) {
						cConv = "ὡὠώὥὤὼὣὢῶὧὦᾡᾠῴᾥᾤῲᾣᾢῷᾧᾦ".SubString(Index, 1);
					}
					break;
				case libString.Asc('ü'): // TODO: to libString.Asc('∙')
					int Index = "üýŒœŠšŸƒˆ˜–—‘’\"„†‡•…‰‹›™∙".IndexOf(cTest);
					if (Index > -1) {
						cConv = "εοἷἔἒἲέἒἱὶἑ῍΅῟῏῞῎ἰἵἴ῝ίἳῖἕἐῥ".SubString(Index, 1);
					}

					break;

			}
			return cConv;
		}
		*/
		
		//UPGRADE_NOTE: Class_Initialize wurde aktualisiert auf Init. Klicken Sie hier für weitere Informationen: 'ms-help://MS.VSExpressCC.v80/dv_commoner/local/redirect.htm?keyword="A9E4979A-37FA-4718-9994-97DD76ED70A7"'
		private void Init() throws Exception
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.Class_Initialize Start";
			mVok = new ArrVok();

            mVok.add(new typVok("empty", "empty", "empty", "empty", "empty", (short) 0));
			libLearn.gStatus = "Vokabel.Class_Initialize Line 1228";
			// Inserted by CodeCompleter
			mConfirmChanges = true;
			mSchrittweite = 10;
			mAbfragebereich = -1;
			mAbfrageZufällig = false;
			mLerngeschwindigkeit = 1;


			return;
					}
		//Public Sub New()
		//	MyBase.New()
		//	Init()
		// End Sub
		public Vokabel(Activity Container, TextView txtStatus) throws Exception
		{
			
			this.Container = Container;
			Init(txtStatus);
			mWortFont = new clsFont(Container);
			//lokale Kopie
			mBedFont = new clsFont(Container);
			//lokale Kopie
			mKomFont = new clsFont(Container);
			//lokale Kopie
			Init();
		}
		public Context getContext()
		{
			if (Container != null)
			{
				return Container;
			}
			else
			{
				return null;
			}
		}
		public void OpenURL(String strLocation) throws Exception
		{
			URL toDownload = new URL(strLocation);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		    try {
		        byte[] chunk = new byte[4096];
		        int bytesRead;
		        InputStream stream = toDownload.openStream();

		        while ((bytesRead = stream.read(chunk)) > 0) {
		            outputStream.write(chunk, 0, bytesRead);
		        }
		        stream.close();

		    } catch (IOException e) {
		        e.printStackTrace();
		        
		    }
			String b = new String(outputStream.toByteArray());
			if (libString.Len(b) < 50) {
				libLearn.gStatus = "Vok.OpenURL Line 39";
				// Inserted by CodeCompleter
				throw new RuntimeException("OpenUrl: Could not load URL " + strLocation);
			}
			if (libString.InStr(1, b, "<html>") > 0) {
				throw new RuntimeException("OpenUrl:" +  b);
			}
		}		
	public Vokabel() {
		// TODO Auto-generated finalructor stub
	}

}
