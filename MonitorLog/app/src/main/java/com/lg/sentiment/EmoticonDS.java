package com.lg.sentiment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lg.database.DatabaseAdapter;
import com.lg.sentiment.util.NLPUtil;
import com.lg.sentimentalanalysis.Method;

import android.database.Cursor;
import android.util.Log;

/**
 * @author elias
 */
public class EmoticonDS extends Method {
	private static String TAG = "EmoticonDS";
	private String tableName;
	private DatabaseAdapter db = DatabaseAdapter.getInstance();

	public EmoticonDS(String tableName) {
		this.tableName = tableName;
		db.open();
	}

	public int analyseText(final String text) {

		int posTotal = 0;
		int negTotal = 0;
		int neuTotal = 0;

		String lowerc = text.toLowerCase();
		List<String> words = new ArrayList<String>(Arrays.asList(lowerc
				.split(" ")));

		NLPUtil.removeStopWords(words);

		for (int i = 0; i < words.size(); i++) {
			Cursor cursor = db.getData(this.tableName, words.get(i));
			
			if (cursor.moveToFirst()) {
				int qntPos = cursor.getInt(1);
				int qntNeg = cursor.getInt(2);
				if (qntPos > qntNeg) {
					posTotal++;
				} else if (qntNeg > qntPos) {
					negTotal++;
				} else {
					neuTotal++;
				}
			}
		}
		if ((posTotal > negTotal) && (posTotal > neuTotal)) {
			return POSITIVE;
		} else if ((negTotal > posTotal) && (negTotal > neuTotal)) {
			return NEGATIVE;
		}
		return NEUTRAL;
	}

	@Override
	public void loadDictionaries() {
		
	}
}