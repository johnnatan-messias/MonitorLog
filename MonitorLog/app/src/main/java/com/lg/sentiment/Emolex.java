package com.lg.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lg.sentimentalanalysis.Method;
import com.lg.sentimentalanalysis.MethodCreator;

import android.util.Log;

/**
 * @author jpaulo
 */
public class Emolex extends Method {

	/**
	 * 
	 */
	private Map<String, Map<String, Integer>> dictionary;
	private String dictionaryFilePath;
	private static String TAG = Emolex.class.getSimpleName();

	public Emolex(String dictionaryFilePath) {

		this.dictionaryFilePath = dictionaryFilePath;
		this.loadDictionaries();
	}

	/**
	 * @author jpaulo
	 */
	@Override
	public void loadDictionaries() {

		final int TOKEN_IDX = 0;
		final int SENTIMENT_IDX = 1;
		final int VALUE_IDX = 2;
		this.dictionary = new HashMap<String, Map<String, Integer>>();

		BufferedReader br;

		try {
			br = new BufferedReader(new InputStreamReader(
					MethodCreator.assets.open(this.dictionaryFilePath)));
			String line = br.readLine();
			while (line != null) {

				String[] data = line.split("\t");

				if (data[VALUE_IDX].equals("1")) {

					if (!this.dictionary.containsKey(data[TOKEN_IDX])) {

						Map<String, Integer> wordValueBySentiment = new HashMap<String, Integer>();
						wordValueBySentiment.put(data[SENTIMENT_IDX], 1);
						this.dictionary.put(data[TOKEN_IDX],
								wordValueBySentiment);
					} else {

						this.dictionary.get(data[TOKEN_IDX]).put(
								data[SENTIMENT_IDX], 1);
					}
				}

				line = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	@Override
	public int analyseText(String text) {

		String[] tokens = text.toLowerCase().split(" ");
		// TODO tratar pontuacao?

		// for each sentiment
		Map<String, Integer> sumSentimentValues = new HashMap<String, Integer>();

		for (String token : tokens) {

			if (this.dictionary.containsKey(token)) {

				for (String sentiment : this.dictionary.get(token).keySet()) {

					int value = this.dictionary.get(token).get(sentiment);

					if (sumSentimentValues.containsKey(sentiment)) {
						int curr = sumSentimentValues.get(sentiment);
						sumSentimentValues.put(sentiment, value + curr);
					} else {
						sumSentimentValues.put(sentiment, value);
					}
				}
			}
		}

		/*
		 * 
		 */

		int posTotal = (sumSentimentValues.get("positive") != null) ? sumSentimentValues
				.get("positive") : 0;
		int negTotal = (sumSentimentValues.get("negative") != null) ? sumSentimentValues
				.get("negative") : 0;

		// sentiments not considered as positive: joy, trust
		// sentiments not considered as negative: anger, disgust, fear, sadness
		// sentiments not considered as neutral: surprise, anticipation
		// System.out.println("pos: " + posTotal + "; neg: " + negTotal);

		if (posTotal > negTotal) {
			return POSITIVE;
		} else if (negTotal > posTotal) {
			return NEGATIVE;
		}

		return NEUTRAL;
	}

	/**
	 * just to verify
	 */
	@SuppressWarnings("unused")
	private void printDictionary() {

		List<String> keys = new ArrayList<String>();
		keys.addAll(this.dictionary.keySet());
		Collections.sort(keys);
		int count = 0;
		for (String k : keys) {
			for (String j : this.dictionary.get(k).keySet()) {
				Log.i(TAG, ++count + ") [" + k + "][" + j + "] = "
						+ this.dictionary.get(k).get(j));
			}
		}
	}
}
