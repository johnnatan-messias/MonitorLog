package com.lg.sentimentalanalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

public abstract class Method {

	private static String TAG = Method.class.getSimpleName();
	protected final int POSITIVE = 1;
	protected final int NEGATIVE = -1;
	protected final int NEUTRAL = 0;

	public abstract void loadDictionaries();

	public abstract int analyseText(String text);

	public void analyseFile(String filePath) {
		long i = 0;
		Log.i(TAG, filePath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					MethodCreator.assets.open(filePath)));
			String line = br.readLine();
			while (line != null) {
				this.analyseText(line);
				//Log.i(TAG, String.valueOf(i++));
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
	}
}
