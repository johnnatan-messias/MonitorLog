package com.lg.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.lg.sentimentalanalysis.Method;
import com.lg.sentimentalanalysis.MethodCreator;

import android.util.Log;

/**
 * @author jpaulo Afinn-111
 */
public class Afinn extends Method {

	/**
	 * key: token; value: score
	 */
	private static String TAG = Afinn.class.getSimpleName();
	private Map<String, Integer> lexiconDictionary;
	private String dictionaryFilePath;

	public Afinn(String dictionaryFilePath) {

		this.dictionaryFilePath = dictionaryFilePath;
		this.loadDictionaries();
	}

	/**
	 * Afinn-111 has one dictionary
	 */
	@Override
	public void loadDictionaries() {

		this.lexiconDictionary = new HashMap<String, Integer>();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					MethodCreator.assets.open(this.dictionaryFilePath)));
			String line = br.readLine();
			while (line != null) {

				String[] data = line.split("\t");

				this.lexiconDictionary.put(data[0], Integer.valueOf(data[1]));

				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	@Override
	public int analyseText(String text) {

		Map<String, Integer> wordsInfo = this.parseText(text);
		int wordsValue = 0;
		int totalFrequency = 0;

		// find text's words present in lexicon
		for (String word : wordsInfo.keySet()) {

			if (this.lexiconDictionary.containsKey(word)) {

				wordsValue += (wordsInfo.get(word) * this.lexiconDictionary
						.get(word));
				totalFrequency += wordsInfo.get(word);
			}
		}

		double sentimentScore = (double) wordsValue / totalFrequency;

		if (sentimentScore > 0d) {
			return 1;
		} else if (sentimentScore < 0d) {
			return -1;
		}
		// TODO threshold: see iFeel code

		return 0; // neutral
	}

	/**
	 * @param text
	 * @return
	 */
	private Map<String, Integer> parseText(String text) {

		Map<String, Integer> wordsInfo = new HashMap<String, Integer>();

		// TODO remove punctuation before tokenize

		String[] tokenized = text.toLowerCase().split(" ");

		for (String token : tokenized) {

			if (wordsInfo.containsKey(token)) {
				wordsInfo.put(token, wordsInfo.get(token) + 1);
			} else {
				wordsInfo.put(token, 1);
			}
		}

		return wordsInfo;
	}

	/**
	 * just to verify
	 */
	@SuppressWarnings("unused")
	private void printDictionary() {

		int count = 0;
		for (String key : this.lexiconDictionary.keySet()) {
			Log.i(TAG,
					++count + ") [" + key + "] = "
							+ this.lexiconDictionary.get(key));
		}
	}
}
