package com.jmg.lib;

import java.io.BufferedReader;
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
	public String readLine()
	{
		return this.readLine().replace("\r", "");
	}

}
