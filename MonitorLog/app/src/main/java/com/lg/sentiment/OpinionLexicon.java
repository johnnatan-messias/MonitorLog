package com.lg.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

import com.lg.sentiment.util.NLPUtil;
import com.lg.sentimentalanalysis.Method;
import com.lg.sentimentalanalysis.MethodCreator;

/**
 * @author miller
 */
public class OpinionLexicon extends Method {

	private static String TAG = OpinionLexicon.class.getSimpleName();
	private Set<String> positiveWords; // lexicon dictionary
	private Set<String> negativeWords; // lexicon dictionary
	private String positiveWordsFile;
	private String negativeWordsFile;

	public OpinionLexicon(String posWordsFile, String negWordsFile) {
		this.positiveWordsFile = posWordsFile;
		this.negativeWordsFile = negWordsFile;
		loadDictionaries();
	}

	private Set<String> loadDictionary(String fileName) {

		Set<String> set = new HashSet<String>();

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(
					MethodCreator.assets.open(fileName)));

			String aux = br.readLine();

			while (aux != null) {
				set.add(aux);
				aux = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}

		return set;
	}

	@Override
	public void loadDictionaries() {

		this.positiveWords = this.loadDictionary(this.positiveWordsFile); // Positive
																			// words
																			// file
		this.negativeWords = this.loadDictionary(this.negativeWordsFile); // Negative
																			// words
																			// file
	}

	@Override
	public int analyseText(String text) { // throws IOException

		int positive = 0;
		int negative = 0;
		Set<String> stopWords = NLPUtil.loadDefaultStopWords();
		String[] words = text.split(" ");
		int lengthWords = words.length;

		for (int i = 0; i < lengthWords; i++) {
			String aux = words[i].toLowerCase();
			aux = aux.replaceAll("[^a-zA-Z]*", ""); // Remove tokens

			if (stopWords.contains(aux) == false) {
				if (positiveWords.contains(aux) == true) {
					positive++;
				} else if (negativeWords.contains(aux) == true) {
					negative++;
				}
			}
		}

		if (positive > negative) {
			return POSITIVE;
		} else if (negative > positive) {
			return NEGATIVE;
		} else {
			return NEUTRAL;
		}
	}
}