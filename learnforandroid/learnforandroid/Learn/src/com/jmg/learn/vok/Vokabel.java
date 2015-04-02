package com.jmg.learn.vok;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.CharUtils;

import CS2JNet.JavaSupport.Collections.Generic.LCC.CollectionSupport;
import CS2JNet.JavaSupport.language.RefSupport;

import android.widget.TextView;

import com.jmg.learn.*;
import com.jmg.lib.lib;
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

		final String className = "Vokabel";
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
			ähnlich,
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
		public android.view.View Container;
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
		private typVok[] mVok;
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
		private clsFont mWortFont = new clsFont(Container);
			//lokale Kopie
		private clsFont mBedFont = new clsFont(Container);
			//lokale Kopie
		private clsFont mKomFont = new clsFont(Container);
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
		
		public class typVok
		{
			//Aufbau einer Vokabel
			public String Wort;
			public String Bed1;
			public String Bed2;
			public String Bed3;
			public String[] getBedeutungen() {
				return new String[] {Bed1,Bed2,Bed3};
			}

			public int getAnzBed() {
				int functionReturnValue = 0;
				for  (String Bed : getBedeutungen()) 
				{
					if (!libString.IsNullOrEmpty(Bed))
						functionReturnValue += 1;
				}
				return functionReturnValue;
			}
			//Kommentar
			public String Kom;
				//Zähler wie oft gewußt
			public short z;
		}
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
		public typVok[] getVokabeln() {
			return mVok; 
		}

		public String getProperties() throws Exception {
				String txt = null;
				txt = R.string.TotalNumber +": " + this.getGesamtzahl();
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
	        libLearn.gStatus = "Vokabel.Status Start";
	        //
	        //
	        functionReturnValue = mSTatus;
	        return functionReturnValue;
	    }

	    public void setStatus(String value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Status Start";
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
	        functionReturnValue = mVok[mIndex].z;
	        return functionReturnValue;
	    }

	    public void setZaehler(short value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.ZÃƒÂ¤hler Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.ZÃƒÂ¤hler = 5
	        mVok[mIndex].z = value;
	        aend = true;
	    }

	    public String getKommentar() throws Exception {
	        String functionReturnValue = null;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Kommentar Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.Kommentar
	        functionReturnValue = mVok[mIndex].Kom;
	        return functionReturnValue;
	    }

	    public void setKommentar(String value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Kommentar Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.Kommentar = 5
	        mVok[mIndex].Kom = value;
	        aend = true;
	    }

	    public String getBedeutung3() throws Exception {
	        String functionReturnValue = null;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung3 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.Bedeutung3
	        functionReturnValue = mVok[mIndex].Bed3.trim();
	        return functionReturnValue;
	    }

	    public void setBedeutung3(String value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung3 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.Bedeutung3 = 5
	        mVok[mIndex].Bed3 = value;
	        aend = true;
	    }

	    public String getBedeutung2() throws Exception {
	        String functionReturnValue = null;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung2 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.Bedeutung2
	        functionReturnValue = (mVok[mIndex].Bed2).trim();
	        return functionReturnValue;
	    }

	    public void setBedeutung2(String value) throws Exception {
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung2 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Zuweisen eines Werts in eine Eigenschaft auf der linken Seite der Gleichung, verwendet.
	        //Syntax: X.Bedeutung2 = 5
	        mVok[mIndex].Bed2 = value;
	        aend = true;
	    }

	    public String getBedeutung1() throws Exception {
	        String functionReturnValue = null;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.Bedeutung1 Start";
	        // ERROR: Not supported in C#: OnErrorStatement
	        //wird beim Ermitteln einer Eignschaft auf der rechten Seite der Gleichung verwendet.
	        //Syntax: Debug.Print X.Bedeutung1
	        functionReturnValue = (mVok[mIndex].Bed1).trim();
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
	            mVok[mIndex].Bed1 = value;
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
	        functionReturnValue = mVok[mIndex].Wort;
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
	            mVok[mIndex].Wort = value;
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



		public void SkipVokabel()
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
				short Lösungen = 0;
				// Anzahl der richtigen Antworten
				short anzAntworten = 0;
				// Anzahl der eingegebenen Antworten
				short anzBedeutungen = 0;

				short Enthalten = 0;
				// Anzahl der Antworten die nur einen TeilString enthalten
				short ähnlich = 0;
				// Anzahl der ähnlichen Antworten
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

									Lösungen  += 1;

									break; // TODO: might not be correct. Was : Exit For
								// Falls eine Antwort mehrere Teilantworten enthält
								} else {
									TeilErgebnis = TeileÜberprüfen(Antwort, 
											EnthältTrennzeichen(RemoveKomment(Bedeutungen[ii])), ii);
									if (TeilErgebnis == Bewertung.AllesRichtig) {
										libLearn.gStatus = "Vokabel.CheckAnwort Line 288";
										// Inserted by CodeCompleter
										Lösungen = Lösungen + 1;
										mOldBed[ii] = Bedeutungen[ii];
										Bedeutungen[ii] = "";
										Antwort = "";
										break; // TODO: might not be correct. Was : Exit For
									} else if (TeilErgebnis == Bewertung.TeilweiseRichtig) {
										TeilweiseRichtig = TeilweiseRichtig + 1;

										//Bedeutungen(ii) = ""
										//Antwort = ""
										//Exit For
										libLearn.gStatus = "Vokabel.CheckAnwort Line 298";
										// Inserted by CodeCompleter
									} else {
										if (Aehnlichkeit(Bedeutungen[ii], Antwort, ref ii) > 0.5) {
											ähnlich = ähnlich + 1;
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

				if (Lösungen < anzBedeutungen) {
					if (Lösungen > 0)
						functionReturnValue = Bewertung.TeilweiseRichtig;

				} else {
					//MsgBox "Alles richtig!"
					functionReturnValue = Bewertung.AllesRichtig;
					//AntwortRichtig()
					goto EndCheck;

				}


				if (Lösungen + TeilweiseRichtig < anzBedeutungen) {


				} else {
					//MsgBox Lösungen & " richtig " & Enthalten & " teilweise richtig."
					functionReturnValue = TeilweiseRichtig;
					goto EndCheck;
				}

				if (Lösungen + Enthalten + TeilweiseRichtig + ähnlich == 0) {
					//MsgBox "AllesFalsch"
					functionReturnValue = Bewertung.AllesFalsch;
					AntwortFalsch();
				} else if (functionReturnValue != TeilweiseRichtig) {
					//MsgBox Lösungen & " richtig, " & Enthalten & " teilweise richtig, " _
					//& Ähnlich & " ähnlich."
					functionReturnValue = Bewertung.ähnlich;
				}
				EndCheck:

			} catch (Exception ex) {
				clsErrorHandling.HandleError(ex, CodeLoc);
			}
			return functionReturnValue;

		}

	    private float aehnlichkeit(String Bedeutung, String Antwort, RefSupport<short[]> BedNR) throws Exception 
	    {
	        
	        final String CodeLoc = className + ".Aehnlichkeit";
	        libLearn.gStatus = CodeLoc + " Start";
	        short Size1 = 0;
	        short iMin = 0;
	        Bedeutung = Bedeutung.toLowerCase();
	        Antwort = Antwort.toLowerCase();
	        Size1 = (short) RemoveKomment(Bedeutung).length();
	        libLearn.gStatus = CodeLoc + " RemoveKomment";
	        //Antwort = RemoveKomment(Antwort)
	        libLearn.gStatus = CodeLoc + " Levenshtein";
	        int levenshtein = LevenshteinDistance(Bedeutung, Antwort);
	        iMin = (short) (Size1 - 1);
	        boolean blnOldBed = !libString.IsNullOrEmpty(mOldBed[BedNR.getValue()[0]]);
	        //TODO: Dim locs(Size1) As Integer
	        int LastPos = 0;
	        String Test = null;
	        //Bedeutung = Me.Bedeutungen(BedNR)
	        //mOldBed(BedNR) = ""
	        //Antwort = mAntworten(BedNR)
	        Test = new String(new char[Bedeutung.length()]).replace('\0', '*'); //new String('*', Bedeutung.length());
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

			char[] s = Bedeutung.ToCharArray();
			char[] t = Antwort.ToCharArray();

			// for all i and j, d[i,j] will hold the Levenshtein distance between
			// the first i characters of s and the first j characters of t;
			// note that d has (m+1)x(n+1) values
			int[,] d = new int[s.GetUpperBound(0) + 2, t.GetUpperBound(0) + 2];
			int m = s.GetUpperBound(0) + 1;
			int n = t.GetUpperBound(0) + 1;
			for (int i = 0; i <= m; i++) {
				d[i, 0] = i;
				//the distance of any first String to an empty second String
			}
			for (int j = 0; j <= n; j++) {
				d[0, j] = j;
				// the distance of any second String to an empty first String
			}
			for (int j = 1; j <= n; j++) {
				for (int i = 1; i <= m; i++) {
					if (s[i - 1] == t[j - 1]) {
						d[i, j] = d[i - 1, j - 1];
						//// no operation required(()
					} else {
						d[i, j] = Math.Min(Math.Min(d[i - 1, j] + 1, d[i, j - 1] + 1), d[i - 1, j - 1] + 1);
					}
				}
			}


			return d[m, n];
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
				f1 = Bed.indexOf("(", 0) ;//Strings.InStr(1, Bed, "(");
				libLearn.gStatus = CodeLoc + " Klammern verarbeiten";
				while (f1>-1) {
					f2 = Bed.indexOf(")",f1+1); //Strings.InStr(f1 + 1, Bed, ")");
					if (f2>-1) {
						Bed = Bed.substring(0,f1-1) + "*" + Bed.substring(f2+1, Bed.length()-f2-1); //Strings.Left(Bed, f1 - 1) + "*" + Strings.Mid(Bed, f2 + 1, Strings.Len(Bed) - f2);
						f1 = Bed.indexOf("(",f2+1); //Strings.InStr(f2 + 1, Bed, "(");
					} else {
						f1 = f2;
					}
				}
				libLearn.gStatus = CodeLoc + "Kommentare herausfiltern";
				Bed = RemoveKomment(Bed);
				libLearn.gStatus = CodeLoc + " Ungültige Zeichen ersetzen";
				if (Bed.length() > 0) {
					Bed = (Bed).toUpperCase();

					for (i = 1; i <= (Bed).length(); i++) {
						try {
							intAsc = Bed.charAt(i-1);//Strings.Asc(Strings.Mid(Bed, i, 1));
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
			f1 = Strings.InStr(1, Bed, "[");
			while (f1) {
				f2 = Strings.InStr(f1 + 1, Bed, "]");
				libLearn.gStatus = "Vokabel.MakeVergl Line 392";
				// Inserted by CodeCompleter
				if (f2) {
					Bed = Strings.Left(Bed, f1 - 1) + "" + Strings.Mid(Bed, f2 + 1, Strings.Len(Bed) - f2);
					f1 = Strings.InStr(f2 + 1, Bed, "[");
				} else {
					f1 = f2;
				}
			}
			return Bed;
		}

	    private Bewertung TeileÜberprüfen(RefSupport<String> Antwort, RefSupport<String[]> teile, RefSupport<short[]>BedNR) throws Exception 
	    {
	        Bewertung functionReturnValue = Bewertung.undefiniert;
	        // ERROR: Not supported in C#: OnErrorStatement
	        libLearn.gStatus = "Vokabel.TeileÜberprüfen Start";
	        short i = 0;
	        short ii = 0;
	        short richtig = 0;
	        short Bedeutungen = 0;
	        short ähnlich = 0;
	        String Antworten[] = null;
	        if ((teile.getValue() == null))
	        {
	            functionReturnValue = Bewertung.AllesFalsch;
	            return functionReturnValue;
	        }
	         
	        Antworten = EnthältTrennzeichen(Antwort.getValue());
	        for (i = 0;i <= (teile.getValue()).length -1;i++)
	        {
	            // Richtige Teilantworten finden
	            libLearn.gStatus = "Vokabel.TeileÜberprüfen Line 420";
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
	                                libLearn.gStatus = "Vokabel.TeileÜberprüfen Line 430";
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
	        // Erst in zweitem Schritt Ähnlichkeiten feststellen!
	        boolean Aehn = false;
	        float lAehnlichkeit = 0;
	        for (i = 0;i <= (teile.getValue()).length-1;i++)
	        {
	            libLearn.gStatus = "Vokabel.TeileÜberprüfen Line 420";
	            // Inserted by CodeCompleter
	            Aehn = false;
	            if (!String.IsNullOrEmpty(teile.getValue()[i]))
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
	                            ähnlich = ähnlich + 1;
	                            break;
	                            // TODO: might not be correct. Was : Exit For
	                            libLearn.gStatus = "Vokabel.TeileÜberprüfen Line 440";
	                        }
	                         
	                    }
	                     
	                }
	                // Inserted by CodeCompleter
	                if (!Aehn)
	                {
	                    if (String.IsNullOrEmpty(mOldBed[BedNR.getValue()]))
	                    {
	                        mOldBed[BedNR.getValue()] = ClsGlobal.MakeMask(teile.getValue()[i]);
	                    }
	                    else
	                    {
	                        mOldBed[BedNR.getValue()] = mOldBed[BedNR.getValue()] + "," + ClsGlobal.MakeMask(teile.getValue()[i]);
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
	            libLearn.gStatus = "Vokabel.TeileÜberprüfen Line 450";
	            // Inserted by CodeCompleter
	            if (richtig == 0)
	            {
	                functionReturnValue = Bewertung.AllesFalsch;
	                if (ähnlich > 0)
	                    functionReturnValue = ähnlich;
	                 
	            }
	             
	        }
	          
	        return functionReturnValue;
	        	        
	    }
	    

	


		private String[] EnthältTrennzeichen(String Antwort)
		{
			String[] functionReturnValue = null;
			// Rückgabewert ist Anzahl der Teilbedeutungen
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.EnthältTrennzeichen Start";
			String Trenn = null;
			String[] teile = new String[-1 + 1];
			short i = 0;
			short lastTrenn = 0;
			short Trennz = 0;
			Trenn = "/,.;:-\\";
			Antwort = RemoveKomment(Antwort);
			Antwort = Antwort + ",";

			for (i = 1; i <= Strings.Len(Antwort); i++) {
				if (Strings.InStr(1, Trenn, Strings.Mid(Antwort, i, 1)) > 0) {
					libLearn.gStatus = "Vokabel.EnthältTrennzeichen Line 464";
					// Inserted by CodeCompleter
					Array.Resize(ref teile, Trennz + 1);
					teile[Trennz] = Strings.Mid(Antwort, lastTrenn + 1, i - lastTrenn - 1);
					lastTrenn = i;
					Trennz = Trennz + 1;
				}
			}

			if (Trennz > 0) {
				functionReturnValue = teile;
			} else {
				return null;
			}
			return functionReturnValue;
					}
		public void AntwortRichtig()
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.AntwortRichtig Start";
			if (mVok1[mIndex].z < 0)
				mVok1[mIndex].z = 0;
			mVok1[mIndex].z = mVok1[mIndex].z + 1;
			aend = true;
			if (mVok1[mIndex].z > 0) {
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
			if (mVok1[mIndex].z <= 0) {
				mVok1[mIndex].z = mVok1[mIndex].z - 1;
			} else {
				mVok1[mIndex].z = 0;
			}
			aend = true;
			AnzFalsch += 1;
			//InitAbfrage
			return;
					}

		public void InitAbfrage()
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.InitAbfrage Start";
			 // ERROR: Not supported in C#: OnErrorStatement

			short i = 0;
			int voknr = 0;
			boolean blnDurch = false;
			boolean blnDurch2 = false;

			//mLastIndex = 0
			voknr = mLastIndex;

			Status = "Init Abfrage";
			if (mGesamtzahl > 1) {
				libLearn.gStatus = "Vokabel.InitAbfrage Line 499";
				// Inserted by CodeCompleter
				if (mSchrittweite < 5)
					mSchrittweite = 5;
				if (mSchrittweite >= mGesamtzahl) {
					if (mGesamtzahl >= 5) {
						mSchrittweite = mGesamtzahl - 1;
					} else {
						mSchrittweite = mGesamtzahl;
					}
				}
				Array.Resize(mLernVokabeln, mSchrittweite + 1);
				// Überprüfen ob Cache mit Lernvokabeln gefüllt ist
				for (i = 1; i <= mSchrittweite; i++) {
					if (mLernVokabeln[i] == 0) {
						// falls Lernvokabel gelöscht ist neue holen
						if (mAbfragebereich == -1 | blnDurch2 == true) {
							VokabelVonAllenHolen(voknr, i);
						} else {
							libLearn.gStatus = "Vokabel.InitAbfrage Line 509";
							// Inserted by CodeCompleter
							Get_Vok(voknr, i, blnDurch, blnDurch2);
						}
					}
				}
				// Nächste Vokabel im Puffer einstellen
				mLernindex = mLernindex + 1;
				// falls wir am Ende sind wieder an den Anfang gehen
				if (mLernindex > mSchrittweite)
					mLernindex = 1;
				// Vokabelnummer aus dem Puffer holen
				libLearn.gStatus = "Vokabel.InitAbfrage Line 519";
				// Inserted by CodeCompleter
				mIndex = mLernVokabeln[mLernindex];
				mOldBed[0] = ClsGlobal.MakeMask(Bedeutung1);
				mOldBed[1] = ClsGlobal.MakeMask(Bedeutung2);
				mOldBed[2] = ClsGlobal.MakeMask(Bedeutung3);

			} else {
				//If mLernindex < mGesamtzahl Then mLernindex += 1 Else mLernindex = 1
			}
			if (mLernVokabeln[mLernindex] > 0) {
				mblnLernInit = true;
				mLastIndex = voknr;
				libLearn.gStatus = "Vokabel.InitAbfrage Line 529";
				// Inserted by CodeCompleter
			} else {
				mblnLernInit = false;
			}
			goto _finally;
			return;
			// ErrHandler:

			Interaction.MsgBox(Err().Description);
			_finally:

			Status = "";
			return;
					}
		public ArrayList<typVok> Select(String Wort, String Bedeutung)
		{
			int Zaehler = -100;
			return Select(Wort, Bedeutung, Zaehler);
		}
		
		public ArrayList<typVok> Select(String Wort, String Bedeutung, int Zaehler)
		{
			ArrayList<typVok> Sel = new ArrayList<Vokabel.typVok>();
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


	    public void get_Vok(RefSupport<int[]> vokNr, RefSupport<int[]> i, RefSupport<boolean[]> blnDurch, RefSupport<boolean[]> blnDurch2) throws Exception {
	        Get_Vok:blnDurch.setValue(false);
	        do
	        {
	            if (vokNr.getValue() < mGesamtzahl)
	            {
	                vokNr.setValue(vokNr.getValue() + 1);
	            }
	            else
	            {
	                vokNr.setValue(1);
	                if (blnDurch.getValue() == true)
	                {
	                    blnDurch2.setValue(true);
	                    //UPGRADE_ISSUE: Die Anweisung GoSub wird nicht unterstützt. Klicken Sie hier für weitere Informationen: 'ms-help://MS.VSExpressCC.v80/dv_commoner/local/redirect.htm?keyword="C5A1A479-AB8B-4D40-AAF4-DB19A2E5E77F"'
	                    RefSupport<int> refVar___0 = new RefSupport<int>(vokNr.getValue());
	                    RefSupport<int> refVar___1 = new RefSupport<int>(i.getValue());
	                    vokabelVonAllenHolen(refVar___0,refVar___1);
	                    vokNr.setValue(refVar___0.getValue());
	                    i.setValue(refVar___1.getValue());
	                    break;
	                }
	                 
	                // TODO: might not be correct. Was : Exit Do
	                blnDurch.setValue(true);
	            } 
	            if (mVok(vokNr.getValue()).z == mAbfragebereich | mAbfragebereich >= 6 & mVok(vokNr.getValue()).z >= 6 | mAbfragebereich == 0 & mVok(vokNr.getValue()).z <= 0)
	            {
	                mLernVokabeln(i.getValue()) = vokNr.getValue();
	                break;
	            }
	             
	        }
	        while (true);
	        // TODO: might not be correct. Was : Exit Do
	        RefSupport<int> refVar___2 = new RefSupport<int>(vokNr.getValue());
	        RefSupport<int> refVar___3 = new RefSupport<int>(i.getValue());
	        vokabelVonAllenHolen(refVar___2,refVar___3);
	        vokNr.setValue(refVar___2.getValue());
	        i.setValue(refVar___3.getValue());
	    }

	    public void vokabelVonAllenHolen(RefSupport<int[]> vokNr, RefSupport<int[]> i) throws Exception {
	        VokabelVonAllenHolen:
	        do
	        {
	            if (vokNr.getValue() < Gesamtzahl)
	            {
	                vokNr.setValue(vokNr.getValue() + 1);
	            }
	            else
	            {
	                vokNr.setValue(1);
	            } 
	            if (mAbfrageZufällig)
	                vokNr.setValue(Conversion.Int(VBMath.Rnd(1) * mGesamtzahl) + 1);
	             
	            if (mVok(vokNr.getValue()).z <= 1)
	            {
	                mLernVokabeln(i.getValue()) = vokNr.getValue();
	                break;
	            }
	            else
	            {
	                // TODO: might not be correct. Was : Exit Do
	                if (VBMath.Rnd(1) < 1 / mVok(vokNr.getValue()).z)
	                {
	                    mLernVokabeln(i.getValue()) = vokNr.getValue();
	                    break;
	                }
	                 
	            } 
	        }
	        while (true);
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
			for (int i = index; i <= mGesamtzahl - 1; i++) {
				mVok[i] = mVok[i + 1];
			}
			mGesamtzahl = mGesamtzahl - 1;
			Array.Resize(mVok, mGesamtzahl + 1);
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
			mGesamtzahl = mGesamtzahl + 1;
			mIndex = mGesamtzahl;
			Array.Resize(ref mVok, mIndex + 1);
			return;
					}

		public void SaveFile(String strFileName, boolean blnUniCode)
		{
			if (String.IsNullOrEmpty(strFileName))
				return;
			
			java.io.OutputStreamWriter sWriter = null;
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
			

			this.vok_Path = fname.getParent();
			try {
				Charset enc = Charset.defaultCharset();
				if ((fname).exists()) {
					using (java.io.InputStream in = new java.io.FileInputStream(fname)) {
						CharUtils.		
						using (java.io.InputStreamReader r = new java.io.InputStreamReader(in,enc))) {
							enc = Charset.forName(r.getEncoding());
							r.Close();
						}
					}
				}
				if (blnUniCode) {
					enc = System.Text.Encoding.Unicode;
				} else {
					if (object.ReferenceEquals(enc, System.Text.Encoding.Unicode) || object.ReferenceEquals(enc, System.Text.Encoding.UTF8)) {
						if (Interaction.MsgBox(ClsGlobal.GetLang("SaveAsUniCode", "Möchten Sie diese Datei als Unicode abspeichern?"), MsgBoxStyle.YesNo) == MsgBoxResult.No) {
							enc = System.Text.Encoding.GetEncoding(1252);
						}
					} else {
						enc = System.Text.Encoding.GetEncoding(1252);
					}
				}
				sWriter = new System.IO.StreamWriter(fname, false, enc);

				System.Windows.Forms.Cursor.Current = System.Windows.Forms.Cursors.WaitCursor;
				spr = spr | -(tasta) * 32;
				spr = spr | -(varbed) * 64;
				spr = spr | -(varHebr) * 16;
				spr = spr | this.Sprache;

				if (Strings.Len(fontfil) < 15) {
					fontfil = Conversion.Str(this.Sprache + 1) + "," + System.Enum.GetName(typeof(libLearn.ClsGlobal.EnumSprachen), this.Sprache);
					fontfil += "," + Conversion.Str(FontWort.Size);
					fontfil += "," + FontWort.Name;
					fontfil += "," + Conversion.Str(FontBed.Size);
					fontfil += "," + FontBed.Name;
					fontfil += "," + Conversion.Str(FontKom.Size);
					fontfil += "," + FontKom.Name;

				}


				if (!String.IsNullOrEmpty(tastbel) | !String.IsNullOrEmpty(fontfil)) {
					sWriter.WriteLine(spr | 128 | einst);
					sWriter.WriteLine(tastbel);
					sWriter.WriteLine(fontfil);
				} else {
					sWriter.WriteLine(spr | einst);
				}
				for (h = 1; h <= mVok1.GetUpperBound(0); h++) {
					if (!String.IsNullOrEmpty(mVok1[h].Wort)) {
						LWort = mVok1[h].Wort;
						if (!String.IsNullOrEmpty(mVok1[h].Kom))
							LWort += Strings.Chr(8) + mVok1[h].Kom;
						if (!String.IsNullOrEmpty(LWort))
							LWort = LWort.Replace(finalants.vbCr, "{CR}").Replace(finalants.vbLf, "{LF}");
						sWriter.WriteLine(LWort);
						LWort = mVok1[h].Bed1;
						if (!String.IsNullOrEmpty(LWort))
							LWort = LWort.Replace(finalants.vbCr, "{CR}").Replace(finalants.vbLf, "{LF}");
						qf = 0;
						sWriter.WriteLine(LWort);
						LWort = mVok1[h].Bed2;
						if (!String.IsNullOrEmpty(LWort))
							LWort = LWort.Replace(finalants.vbCr, "{CR}").Replace(finalants.vbLf, "{LF}");
						qf = 0;
						sWriter.WriteLine(LWort);
						LWort = mVok1[h].Bed3;
						if (!String.IsNullOrEmpty(LWort))
							LWort = LWort.Replace(finalants.vbCr, "{CR}").Replace(finalants.vbLf, "{LF}");
						qf = 0;
						sWriter.WriteLine(LWort);
						sWriter.WriteLine(mVok1[h].z);
					}

				}
			} catch (Exception ex) {
				throw ex;
			} finally {
				sWriter.Close();
				sWriter.Dispose();
				sWriter = null;
			}
			aend = false;

			spr = spr & 7;
			System.Windows.Forms.Cursor.Current = System.Windows.Forms.Cursors.Default;
		}
		public void revert()
		{

			for (int h = mVok1.GetLowerBound(0); h <= mVok1.GetUpperBound(0); h++) {
				String vok = mVok1[h].Wort;
				mVok1[h].Wort = mVok1[h].Bed1;
				if (!String.IsNullOrEmpty(mVok1[h].Bed2)) {
					mVok1[h].Wort += "/" + mVok1[h].Bed2;
					mVok1[h].Bed2 = "";
					if (!String.IsNullOrEmpty(mVok1[h].Bed3)) {
						mVok1[h].Wort += "/" + mVok1[h].Bed3;
						mVok1[h].Bed3 = "";
					}
				}
				mVok1[h].Bed1 = vok;
				mVok1[h].z = 0;
			}
		}

		public void reset()
		{
			for (int h = mVok1.GetLowerBound(0); h <= mVok1.GetUpperBound(0); h++) {
				mVok1[h].z = 0;
			}
		}

		int static_GetNextLineFromString_startLine;
		 private boolean getNextLineFromString(RefSupport<String> strContent, RefSupport<String> strRef, int FirstLine) throws Exception {
		        boolean functionReturnValue = false;
		        // ERROR: Not supported in C#: OnErrorStatement
		        libLearn.gStatus = "Vokabel.GetNextLineFromString Start";
		        int crFound = 0;
		        if (String.IsNullOrEmpty(strContent.getValue()))
		        {
		            Err().Raise(finalants.vbObjectError, "GetNextLineFromString", "String ist empty!");
		        }
		         
		        if (StringSupport.equals(strRef.getValue(), "nihxyz"))
		        {
		            functionReturnValue = !(static_GetNextLineFromString_startLine > Strings.Len(strContent.getValue()));
		            return functionReturnValue;
		            libLearn.gStatus = "Vokabel.GetNextLineFromString Line 648";
		        }
		         
		        // Inserted by CodeCompleter
		        if (FirstLine.getValue())
		            static_GetNextLineFromString_startLine = 1;
		         
		        crFound = Strings.InStr(static_GetNextLineFromString_startLine, strContent.getValue(), finalants.vbCrLf);
		        if (crFound == 0)
		            crFound = Strings.Len(strContent.getValue());
		         
		        strRef.setValue(Strings.Mid(strContent.getValue(), static_GetNextLineFromString_startLine, crFound - static_GetNextLineFromString_startLine));
		        static_GetNextLineFromString_startLine = crFound + 2;
		        return functionReturnValue;
		    }

		
		 
		 public void LoadFromString(String strContent)
		 {
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.LoadFromString Start";

			short Varhebr = 0;
			short qf = 0;
			short hh = 0;
			short h = 0;
			short sp = 0;
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
			mLernVokabeln = new int[mSchrittweite + 1];
			mLastIndex = 0;
			 // ERROR: Not supported in C#: OnErrorStatement

			mFileName = "";
			fontfil = "";
			Sprache = "";
			tastbel = "";
			strTmp = "";

			libLearn.gStatus = "Vokabel.LoadFromString Line 669";
			// Inserted by CodeCompleter


			GetNextLineFromString(strContent, strTmp, true);
			//SPRACHE LADEN
			sp = Conversion.Val(strTmp);
			einst = sp & ((Math.Pow(2, 16)) - 256);
			Varhebr = (sp & 16) != 0;
			varbed = (sp & 64) != 0;
			tasta = (sp & 32) != 0;
			indexlang = sp & 7;
			libLearn.gStatus = "Vokabel.LoadFromString Line 679";
			// Inserted by CodeCompleter
			mSprache = indexlang;
			if (sp & 128) {
				GetNextLineFromString(strContent, tastbel);
				GetNextLineFromString(strContent, fontfil);
				//UPGRADE_ISSUE: Die Anweisung GoSub wird nicht unterstützt. Klicken Sie hier für weitere Informationen: 'ms-help://MS.VSExpressCC.v80/dv_commoner/local/redirect.htm?keyword="C5A1A479-AB8B-4D40-AAF4-DB19A2E5E77F"'
				getfonts(fontfil, hh, h, indexlang, qf, lad);
				//Windows Fonts extrahieren
			} else {
				lad = false;
			}
			while ((GetNextLineFromString(strContent))) {
				libLearn.gStatus = "Vokabel.LoadFromString Line 689";
				// Inserted by CodeCompleter
				System.Windows.Forms.Application.DoEvents();
				n = n + 1;
				Array.Resize(ref mVok, n + 1);
				GetNextLineFromString(strContent, mVok[n].Wort);

				qf = Strings.InStr(mVok[n].Wort, Strings.Chr(0));
				if (qf == 0)
					qf = Strings.InStr(mVok[n].Wort, Strings.Chr(8));
				if (qf != 0) {
					mVok[n].Kom = Strings.Right(mVok[n].Wort, Strings.Len(mVok[n].Wort) - qf);
					libLearn.gStatus = "Vokabel.LoadFromString Line 699";
					// Inserted by CodeCompleter
					mVok[n].Wort = Strings.Left(mVok[n].Wort, qf - 1);
				}

				GetNextLineFromString(strContent, mVok[n].Bed1);
				GetNextLineFromString(strContent, mVok[n].Bed2);
				GetNextLineFromString(strContent, mVok[n].Bed3);
				GetNextLineFromString(strContent, strTmp);
				mVok[n].z = Conversion.Val(strTmp);
				if (String.IsNullOrEmpty(mVok[n].Wort)) {
					libLearn.gStatus = "Vokabel.LoadFromString Line 709";
					// Inserted by CodeCompleter
					n = n - 1;
					Array.Resize(ref mVok, n + 1);
				}

			}
			mGesamtzahl = n;
			mIndex = 1;
			closefile:

			// ******** Hier gehts hin wenn ein Fehler auftrit oder wenn _
			//' ******** Schluß ist.....

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
				mFileName = "String";
			} else {
				mblnLernInit = false;
			}
			aend = false;
			return;

			// FErr:
			if (Err().Number == 59)
			{
				Interaction.MsgBox("Wort zu lang!"); // ERROR: Not supported in C#: ResumeStatement
			}

			if (Interaction.MsgBox("Error in LoadFromString " + finalants.vbCrLf + Err().Description, MsgBoxStyle.RetryCancel, Err().Source) == MsgBoxResult.Retry) {
				 // ERROR: Not supported in C#: ResumeStatement

			}
			 // ERROR: Not supported in C#: OnErrorStatement

			goto closefile;
			return;
			}

		    public void getfonts(RefSupport<String> fontfil, int hh, int h, int indexLang, int qf, boolean lad) throws Exception 
		    {
		        getfonts:// ********** Hier werden die Fonts 'extrahiert'
		        hh.setValue(1);
		        if (Strings.InStr(fontfil.getValue(), ",") != 0)
		        {
		            fontfil.setValue(fontfil.getValue() + ",");
		            h.setValue(Strings.InStr(hh.getValue(), fontfil.getValue(), ","));
		            if (h.getValue() != 0 & h.getValue() - hh.getValue() > 0)
		                indexLang.setValue(Conversion.Val(Strings.Mid(fontfil.getValue(), hh.getValue(), h.getValue() - hh.getValue())) - 1);
		             
		            hh.setValue(h.getValue() + 1);
		            h.setValue(Strings.InStr(hh.getValue(), fontfil.getValue(), ","));
		            try
		            {
		                if (h.getValue() != 0 & h.getValue() - hh.getValue() > 0)
		                    Sprache = System.Enum.Parse(EnumSprachen.class, Strings.Mid(fontfil.getValue(), hh.getValue(), h.getValue() - hh.getValue()));
		                 
		            }
		            catch (Exception __dummyCatchVar0)
		            {
		                if (StringSupport.equals(Strings.Mid(fontfil.getValue(), hh.getValue(), h.getValue() - hh.getValue()), "Hebr竳ch"))
		                {
		                    Sprache = EnumSprachen.Hebrew;
		                }
		                else
		                {
		                    Sprache = EnumSprachen.Normal;
		                } 
		            }

		            hh.setValue(h.getValue() + 1);
		            for (qf.setValue(1);qf.getValue() <= 3;qf.setValue(qf.getValue() + 1, ReturnPreOrPostValue.POST))
		            {
		                h.setValue(Strings.InStr(hh.getValue(), fontfil.getValue(), ","));
		                if (h.getValue() != 0 & h.getValue() - hh.getValue() > 0)
		                {
		                    switch(qf.getValue())
		                    {
		                        case 1: 
		                            mWortFont.Size = Convert.ToByte(Strings.Mid(fontfil.getValue(), hh.getValue(), h.getValue() - hh.getValue()));
		                            break;
		                        case 2: 
		                            mBedFont.Size = Convert.ToByte(Strings.Mid(fontfil.getValue(), hh.getValue(), h.getValue() - hh.getValue()));
		                            break;
		                        case 3: 
		                            mKomFont.Size = Convert.ToByte(Strings.Mid(fontfil.getValue(), hh.getValue(), h.getValue() - hh.getValue()));
		                            break;
		                    
		                    }
		                }
		                 
		                hh.setValue(h.getValue() + 1);
		                h.setValue(Strings.InStr(hh.getValue(), fontfil.getValue(), ","));
		                if (h.getValue() != 0 & h.getValue() - hh.getValue() > 0)
		                {
		                    switch(qf.getValue())
		                    {
		                        case 1: 
		                            mWortFont.Name = (Strings.Mid(fontfil.getValue(), hh.getValue(), h.getValue() - hh.getValue()));
		                            break;
		                        case 2: 
		                            mBedFont.Name = (Strings.Mid(fontfil.getValue(), hh.getValue(), h.getValue() - hh.getValue()));
		                            break;
		                        case 3: 
		                            mKomFont.Name = (Strings.Mid(fontfil.getValue(), hh.getValue(), h.getValue() - hh.getValue()));
		                            break;
		                    
		                    }
		                }
		                 
		                hh.setValue(h.getValue() + 1);
		            }
		            lad.setValue(true);
		        }
		         
		    }
	    

		


		public void NewFile()
		{
			mLernVokabeln = new int[mSchrittweite + 1];
			mVok1 = new typVok[1];
			mLastIndex = 0;
			mGesamtzahl = 0;
			mIndex = 0;
		}
		
		public void LoadFile(String strFileName)
		{
			LoadFile(String strFileName, false, false, false)
		}
		
		public void LoadFile(String strFileName, boolean blnSingleLine, boolean blnAppend, boolean blnUnicode)
		{
			try
			{
				final String CodeLoc = "Vokabel.LoadFile";
				libLearn.gStatus = CodeLoc + " Start";
	
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
				String tmp = null;
				fontfil = "";
				Sprache = "";
				tastbel = "";
				strTmp = "";
				mLernVokabeln = new int[mSchrittweite + 1];
				mLastIndex = 0;
				 // ERROR: Not supported in C#: OnErrorStatement
	
				Status = "Load File: " + strFileName;
				libLearn.gStatus = "Load File: " + strFileName;
				mFileName = "";
	
				libLearn.gStatus = CodeLoc + " Open Stream";
				// Inserted by CodeCompleter
	
				if (!String.IsNullOrEmpty(FileSystem.Dir(strFileName))) {
					sr = new System.IO.StreamReader(strFileName, (System.Text.Encoding)(blnUnicode ? System.Text.Encoding.Unicode : System.Text.Encoding.GetEncoding(1252)));
				} else {
					Interaction.MsgBox(ClsGlobal.GetLang("FileDoesNotExist", "Dateiname existiert nicht!", ));
					//Call Err.Raise(vbObjectError + ErrWrongfilename, CodeLoc & "", "Dateiname_ungültig", "", "")
					return;
				}
				_UniCode = (sr.CurrentEncoding.Equals(System.Text.Encoding.Unicode) || object.ReferenceEquals(sr.CurrentEncoding, System.Text.Encoding.UTF8));
				if (System.IO.Path.GetExtension(strFileName).IndexOf(".k", System.StringComparison.CurrentCultureIgnoreCase) != -1)
					_cardmode = true;
				else
					_cardmode = false;
				libLearn.gStatus = CodeLoc + " ReadLine1";
				tmp = sr.ReadLine();
				sp = Convert.ToInt32(tmp);
				einst = sp & ((Math.Pow(2, 16)) - 256);
				varHebr = (sp & 16) != 0;
				varbed = (sp & 64) != 0;
				tasta = (sp & 32) != 0;
				libLearn.gStatus = CodeLoc + " Line 819";
				// Inserted by CodeCompleter
				indexlang = sp & 7;
				if (!blnAppend)
					mSprache = indexlang;
				if (sp & 128) {
					tastbel = sr.ReadLine();
					fontfil = sr.ReadLine();
					if (!blnAppend)
						getfonts(fontfil, hh, h, indexlang, qf, lad);
					//Windows Fonts extrahieren
				} else {
					lad = false;
				}
				libLearn.gStatus = CodeLoc + " Line 829";
				// Inserted by CodeCompleter
				if (blnAppend)
					n = mGesamtzahl;
				while (!sr.EndOfStream) {
					n = n + 1;
					Array.Resize(ref mVok, n + 1);
					libLearn.gStatus = CodeLoc + " ReadLine2";
					mVok[n].Wort = sr.ReadLine().Replace("{CR}", finalants.vbCr).Replace("{LF}", finalants.vbLf);
					qf = Strings.InStr(mVok[n].Wort, Strings.Chr(0));
					if (qf == 0)
						qf = Strings.InStr(mVok[n].Wort, Strings.Chr(8));
					if (qf != 0) {
						mVok[n].Kom = Strings.Right(mVok[n].Wort, Strings.Len(mVok[n].Wort) - qf);
						libLearn.gStatus = CodeLoc + " Line 839";
						// Inserted by CodeCompleter
						mVok[n].Wort = Strings.Left(mVok[n].Wort, qf - 1);
					} else {
						mVok[n].Kom = "";
					}
					libLearn.gStatus = CodeLoc + " ReadLine3";
					if (!sr.EndOfStream) {
						mVok[n].Bed1 = sr.ReadLine().Replace("{CR}", finalants.vbCr).Replace("{LF}", finalants.vbLf);
					}
					if (!blnSingleLine) {
						if (!sr.EndOfStream) {
							libLearn.gStatus = CodeLoc + " ReadLine4";
							mVok[n].Bed2 = sr.ReadLine().Replace("{CR}", finalants.vbCr).Replace("{LF}", finalants.vbLf);
						}
						libLearn.gStatus = CodeLoc + " Line 849";
						// Inserted by CodeCompleter
						if (!sr.EndOfStream) {
							libLearn.gStatus = CodeLoc + " ReadLine5";
							mVok[n].Bed3 = sr.ReadLine().Replace("{CR}", finalants.vbCr).Replace("{LF}", finalants.vbLf);
						}
					} else {
						mVok[n].Bed2 = "";
						mVok[n].Bed3 = "";
					}
					if (!sr.EndOfStream) {
						libLearn.gStatus = CodeLoc + " ReadLine6";
						strTmp = sr.ReadLine();
						mVok[n].z = Conversion.Val(strTmp);
					}
					if (String.IsNullOrEmpty(mVok[n].Wort)) {
						n = n - 1;
						libLearn.gStatus = CodeLoc + " Line 859";
						// Inserted by CodeCompleter
						Array.Resize(ref mVok, n + 1);
					} else {
						mVok[n].Wort = mVok[n].Wort.Replace("ùú", finalants.vbCrLf);
						mVok[n].Kom = mVok[n].Kom.Replace("ùú", finalants.vbCrLf);
						mVok[n].Bed1 = mVok[n].Bed1.Replace("ùú", finalants.vbCrLf);
						mVok[n].Bed2 = mVok[n].Bed2.Replace("ùú", finalants.vbCrLf);
						mVok[n].Bed3 = mVok[n].Bed3.Replace("ùú", finalants.vbCrLf);
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
					libLearn.gStatus = CodeLoc + " Line 889";
					// Inserted by CodeCompleter
					mblnLernInit = false;
				}
				aend = false;
				return;
			}
			catch (Exception ex)
			{
			// FErr:
				if (Err().Number == 59){Interaction.MsgBox("Wort zu lang!");}
	
				if (Interaction.MsgBox("Fileerror " + finalants.vbCrLf + Err().Description, MsgBoxStyle.RetryCancel) 
						== MsgBoxResult.Retry) {
				
				}
				 // ERROR: Not supported in C#: OnErrorStatement
	
			}	
			return;
		}
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

			Status = "Load File: " + strFileName;
			mFileName = "";

			libLearn.gStatus = "Vokabel.LoadFile Line 799";
			// Inserted by CodeCompleter

			if (!String.IsNullOrEmpty(FileSystem.Dir(strFileName))) {
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

			sp = sr.ReadLine();
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
				tastbel = sr.ReadLine();
				fontfil = sr.ReadLine();
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
				mVok[n].Wort = sr.ReadLine().Replace("{CR}", finalants.vbCr).Replace("{LF}", finalants.vbLf);
				qf = Strings.InStr(mVok[n].Wort, Strings.Chr(0));
				if (qf == 0)
					qf = Strings.InStr(mVok[n].Wort, Strings.Chr(8));
				if (qf != 0) {
					mVok[n].Kom = Strings.Right(mVok[n].Wort, Strings.Len(mVok[n].Wort) - qf);
					libLearn.gStatus = "Vokabel.LoadFile Line 839";
					// Inserted by CodeCompleter
					mVok[n].Wort = Strings.Left(mVok[n].Wort, qf - 1);
				} else {
					mVok[n].Kom = "";
				}
				String WordConvert = "";
				char cTest = '\0';
				foreach (char c in mVok[n].Wort) {
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

				mVok[n].Wort = WordConvert;
				if (!sr.EndOfStream) {
					mVok[n].Bed1 = sr.ReadLine().Replace("{CR}", finalants.vbCr).Replace("{LF}", finalants.vbLf);
				}
				if (!blnSingleLine) {
					if (!sr.EndOfStream) {
						mVok[n].Bed2 = sr.ReadLine().Replace("{CR}", finalants.vbCr).Replace("{LF}", finalants.vbLf);
					}
					libLearn.gStatus = "Vokabel.LoadFile Line 849";
					// Inserted by CodeCompleter
					if (!sr.EndOfStream) {
						mVok[n].Bed3 = sr.ReadLine().Replace("{CR}", finalants.vbCr).Replace("{LF}", finalants.vbLf);
					}
				} else {
					mVok[n].Bed2 = "";
					mVok[n].Bed3 = "";
				}
				if (!sr.EndOfStream) {
					strTmp = sr.ReadLine();
					mVok[n].z = Conversion.Val(strTmp);
				}
				if (String.IsNullOrEmpty(mVok[n].Wort)) {
					n = n - 1;
					libLearn.gStatus = "Vokabel.LoadFile Line 859";
					// Inserted by CodeCompleter
					Array.Resize(ref mVok, n + 1);
				} else {
					mVok[n].Wort = mVok[n].Wort.Replace("ùú", finalants.vbCrLf);
					mVok[n].Kom = mVok[n].Kom.Replace("ùú", finalants.vbCrLf);
					mVok[n].Bed1 = mVok[n].Bed1.Replace("ùú", finalants.vbCrLf);
					mVok[n].Bed2 = mVok[n].Bed2.Replace("ùú", finalants.vbCrLf);
					mVok[n].Bed3 = mVok[n].Bed3.Replace("ùú", finalants.vbCrLf);
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

			if (Interaction.MsgBox("Fileerror " + finalants.vbCrLf + Err().Description, MsgBoxStyle.RetryCancel) == MsgBoxResult.Retry) {
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
			switch (Strings.Asc(cTest)) {
				case 0x61:
				case 0x62:
					cConv = Strings.ChrW(Strings.Asc(c) - 0x61 + 0x5d0);
					break;
				case 0x63:
					cConv = Strings.ChrW(0x5e1);
					break;
				case 0x64:
					cConv = Strings.ChrW(0x5d3);
					break;
				case 0x65:
					cConv = Strings.ChrW(0x5b6);
					break;
				case 0x66:
					cConv = Strings.ChrW(0x5b8);
					break;
				case 0x67:
					cConv = Strings.ChrW(0x5d2);
					break;
				case 0x68:
					cConv = Strings.ChrW(0x5d4);
					break;
				case 0x69:
					cConv = Strings.ChrW(0x5e2);
					break;
				case 0x6a:
					cConv = Strings.ChrW(0x5e6);
					break;
				case 0x6b:
					cConv = Strings.ChrW(0x5db);
					break;
				case 0x6c:
					cConv = Strings.ChrW(0x5dc);
					break;
				case 0x6d:
					cConv = Strings.ChrW(0x5de);
					break;
				case 0x6e:
					cConv = Strings.ChrW(0x5e0);
					break;
				case 0x6f:
					//o
					cConv = Strings.ChrW(0x5b9);
					break;
				case Strings.Asc('p'):
					cConv = Strings.ChrW(0x5e4);
					break;
				case Strings.Asc('q'):
					cConv = Strings.ChrW(0x5e7);
					break;
				case Strings.Asc('r'):
					cConv = Strings.ChrW(0x5e8);
					break;
				case Strings.Asc('s'):
					cConv = Strings.ChrW(0x5e9) + Strings.ChrW(0x5c2);
					break;
				case Strings.Asc('t'):
					cConv = Strings.ChrW(0x5ea);
					break;
				case Strings.Asc('u'):
					cConv = Strings.ChrW(0x5d8);
					break;
				case Strings.Asc('v'):
					cConv = Strings.ChrW(0x5d5);
					break;
				case Strings.Asc('w'):
					cConv = Strings.ChrW(0x5e9) + Strings.ChrW(0x5c1);
					break;
				case Strings.Asc('x'):
					cConv = Strings.ChrW(0x5d7);
					break;
				case Strings.Asc('y'):
					cConv = Strings.ChrW(0x5d9);
					break;
				case Strings.Asc('z'):
					cConv = Strings.ChrW(0x5d6);
					break;
				case Strings.Asc('('):
					cConv = Strings.ChrW(0x5e2);
					break;
				case Strings.Asc(')'):
					cConv = Strings.ChrW(0x5d0);
					break;
				case Strings.Asc('+'):
					cConv = Strings.ChrW(0x5b7);
					break;
				case Strings.Asc('-'):
					cConv = Strings.ChrW(0x5b7);
					break;
				case Strings.Asc('0'):
					cConv = Strings.ChrW(0x5c2);
					break;
				case Strings.Asc('1'):
					cConv = Strings.ChrW(0x5c5);
					break;
				case Strings.Asc('2'):
					cConv = Strings.ChrW(0x5b0);
					break;
				case Strings.Asc('3'):
					cConv = Strings.ChrW(0x5a6);
					break;
				case Strings.Asc('4'):
					cConv = "";
					break;
				case Strings.Asc('5'):
					cConv = "";
					break;
				case Strings.Asc('6'):
					cConv = "";
					break;
				case Strings.Asc('7'):
					cConv = "";
					break;
				case Strings.Asc('9'):
					cConv = Strings.ChrW(0x5bf);
					break;
				case Strings.Asc('"'):
					cConv = Strings.ChrW(0x5b5);
					break;
				case Strings.Asc('I'):
					cConv = Strings.ChrW(0x5b4);
					break;
				case Strings.Asc('f'):
					cConv = Strings.ChrW(0x5b8);
					break;
				case Strings.Asc('o'):
					cConv = Strings.ChrW(0x5c1);
					break;
				case Strings.Asc('e'):
					cConv = Strings.ChrW(0x5b6);
					break;
				case Strings.Asc(':'):
					cConv = Strings.ChrW(0x5b0);
					break;
				case Strings.Asc('_'):
					cConv = Strings.ChrW(0x5b2);
					break;
				case Strings.Asc('.'):
					cConv = Strings.ChrW(0x5c3);
					break;
				case Strings.Asc('^'):
					cConv = Strings.ChrW(0x5ab);
					break;
				case Strings.Asc(']'):
					cConv = Strings.ChrW(0x5df);
					break;
				case Strings.Asc('%'):
					cConv = Strings.ChrW(0x5da) + Strings.ChrW(0x5bc) + Strings.ChrW(0x5b3);

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
			switch (Strings.Asc(cTest)) {
				case Strings.Asc('A'):
					cConv = Strings.ChrW(0x391);
					break;
				case Strings.Asc('B'):
					cConv = Strings.ChrW(0x392);
					break;
				case Strings.Asc('C'):
					cConv = Strings.ChrW(0x3a7);
					break;
				case Strings.Asc('D'):
					cConv = Strings.ChrW(0x394);
					break;
				case Strings.Asc('E'):
					cConv = Strings.ChrW(0x395);
					break;
				case Strings.Asc('F'):
					cConv = Strings.ChrW(0x3a6);
					break;
				case Strings.Asc('G'):
					cConv = Strings.ChrW(0x393);
					break;
				case Strings.Asc('H'):
					cConv = Strings.ChrW(0x397);
					break;
				case Strings.Asc('I'):
					cConv = Strings.ChrW(0x399);
					break;
				case Strings.Asc('J'):
					cConv = "ῳͅ";
					break;
				case Strings.Asc('K'):
					cConv = Strings.ChrW(0x39a);
					break;
				case Strings.Asc('L'):
					cConv = Strings.ChrW(0x39b);
					break;
				case Strings.Asc('M'):
					cConv = Strings.ChrW(0x39c);
					break;
				case Strings.Asc('N'):
					cConv = Strings.ChrW(0x39d);
					break;
				case Strings.Asc('O'):
					cConv = Strings.ChrW(0x39f);
					break;
				case Strings.Asc('P'):
					cConv = Strings.ChrW(0x3a0);
					break;
				case Strings.Asc('Q'):
					cConv = Strings.ChrW(0x398);
					break;
				case Strings.Asc('R'):
					cConv = Strings.ChrW(0x3a1);
					break;
				case Strings.Asc('S'):
					cConv = Strings.ChrW(0x3a3);
					break;
				case Strings.Asc('T'):
					cConv = Strings.ChrW(0x3a4);
					break;
				case Strings.Asc('U'):
					cConv = Strings.ChrW(0x3a5);
					break;
				case Strings.Asc('V'):
					cConv = "ῃ";
					break;
				case Strings.Asc('W'):
					cConv = Strings.ChrW(0x3a9);
					break;
				case Strings.Asc('X'):
					cConv = Strings.ChrW(0x39e);
					break;
				case Strings.Asc('Y'):
					cConv = Strings.ChrW(0x3a8);
					break;
				case Strings.Asc('Z'):
					cConv = Strings.ChrW(0x396);
					break;
				case Strings.Asc('…'):
					cConv = "ί";
					break;
				case Strings.Asc('š'):
					cConv = "έ";
					break;
				case Strings.Asc('Œ'):
					cConv = "ἷ";
					break;
				case Strings.Asc('ƒ'):
					cConv = "ἱ";
					break;
				case Strings.Asc('†'):
					cConv = "ἵ";
					break;
				case Strings.Asc('„'):
					cConv = "ἰ";
					break;
				case Strings.Asc('ˆ'):
					cConv = "ὶ";
					break;
				case Strings.Asc('$'):
					cConv = "ϛ";
					break;
				case Strings.Asc('%'):
					cConv = "ϙ";
					break;
				case Strings.Asc('#'):
					cConv = "ϝ";
					break;
				case 0x61: // TODO: to 0x7a
					cConv = "αβχδεφγηιςκλμνοπθρστυᾳωξψζ".SubString("abcdefghijklmnopqrstuvwxyz".IndexOf(cTest), 1);
					break;
				case Strings.Asc('·'): // TODO: to Strings.Asc('Ï')
					cConv = "ῥῤἡἠήἥἤὴἣἢῆἧἦͺᾑᾐῄᾕᾔῂᾓᾒῇᾗᾖ".SubString("·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏ".IndexOf(cTest), 1);
					break;
				case Strings.Asc('\u008d'): // TODO: to Strings.Asc('¶')
					int Index = "\u008d\u008e\u008f\u0090\u009d\u009e¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶".IndexOf(cTest);
					if (Index > -1) {
						cConv = "ἶῑΐῒὲἓἁἀάἅἄὰἃἂᾶἇἆᾁᾴᾅᾄᾲᾃᾄᾷᾇᾆ".SubString(Index, 1);
					}
					break;
				case Strings.Asc('Ð'): // TODO: to Strings.Asc('å')
					int Index = "ÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäå".IndexOf(cTest);
					if (Index > -1) {
						cConv = "ὁὀόὅὄὸὃὂὑὐύὕὔὺὓὔῦὗὖϋΰῢ".SubString(Index, 1);
					}
					break;
				case Strings.Asc('æ'): // TODO: to Strings.Asc('û')
					int Index = "æçèéêëìíîïðñòóôõö÷øùúû".IndexOf(cTest);
					if (Index > -1) {
						cConv = "ὡὠώὥὤὼὣὢῶὧὦᾡᾠῴᾥᾤῲᾣᾢῷᾧᾦ".SubString(Index, 1);
					}
					break;
				case Strings.Asc('ü'): // TODO: to Strings.Asc('∙')
					int Index = "üýŒœŠšŸƒˆ˜–—‘’\"„†‡•…‰‹›™∙".IndexOf(cTest);
					if (Index > -1) {
						cConv = "εοἷἔἒἲέἒἱὶἑ῍΅῟῏῞῎ἰἵἴ῝ίἳῖἕἐῥ".SubString(Index, 1);
					}

					break;

			}
			return cConv;
		}
		//UPGRADE_NOTE: Class_Initialize wurde aktualisiert auf Init. Klicken Sie hier für weitere Informationen: 'ms-help://MS.VSExpressCC.v80/dv_commoner/local/redirect.htm?keyword="A9E4979A-37FA-4718-9994-97DD76ED70A7"'
		private void Init()
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vokabel.Class_Initialize Start";
			mVok1 = new typVok[2];


			mVok1[0].Wort = "empty";
			mVok1[0].Bed1 = "empty";
			mVok1[0].Bed2 = "empty";
			mVok1[0].Bed3 = "empty";
			mVok1[0].Kom = "empty";
			mVok1[0].z = 0;
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
		public Vokabel(View Container)
		{
			Init();
			this.Container = Container;
		}
		public void OpenURL(String strLocation)
		{
			 // ERROR: Not supported in C#: OnErrorStatement

			libLearn.gStatus = "Vok.OpenURL Start";
			String b = null;
			String strURL = null;
			dynamic tempFile = null;
			 // ERROR: Not supported in C#: OnErrorStatement

			using (System.Net.WebClient iNet1 = new System.Net.WebClient()) {

				strURL = strLocation;
				b = iNet1.DownloadString(strURL);
				if (Strings.Len(b) < 50) {
					libLearn.gStatus = "Vok.OpenURL Line 39";
					// Inserted by CodeCompleter
					Err().Raise(finalants.vbObjectError, "OpenUrl", "Could not load URL " + strURL);
				}
				if (Strings.InStr(1, b, "<html>", finalants.vbTextCompare) > 0) {
					Err().Raise(finalants.vbObjectError, "OpenUrl", b);
				}
				this.LoadFromString(b);
				return;
				// errH:
				Interaction.MsgBox(Err().Description);
			}
			return;
		}		
	public Vokabel() {
		// TODO Auto-generated finalructor stub
	}

}
