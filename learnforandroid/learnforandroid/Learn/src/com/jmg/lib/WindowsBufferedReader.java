package com.jmg.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class WindowsBufferedReader extends BufferedReader {

	public WindowsBufferedReader(Reader in) {
		super(in);
		// TODO Auto-generated constructor stub
	}

	public WindowsBufferedReader(Reader in, int size) {
		super(in, size);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String readLine() throws IOException
	{
		
		String s = super.readLine();
		int length = s.length();
		char c = s.charAt(0);
		int ic = c;
		if (ic == 65279 && length > 1)
		{
			s = s.substring(1);
		}
		return s;
	}

}
