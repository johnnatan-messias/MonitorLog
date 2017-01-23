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
public class PanasT extends Method {

	private static String TAG = PanasT.class.getSimpleName();
	private Set<String> positiveWords;
	private Set<String> negativeWords;
	private Set<String> neutralWords;
	private String positiveWordsFile;
	private String negativeWordsFile;
	private String neutralWordsFile;

	public PanasT(String posWordsFile, String negWordsFile, String neuWordsFile) {

		this.positiveWordsFile = posWordsFile;
		this.negativeWordsFile = negWordsFile;
		this.neutralWordsFile = neuWordsFile;
		this.loadDictionaries();
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

		this.positiveWords = this.loadDictionary(this.positiveWordsFile); 											
		this.negativeWords = this.loadDictionary(this.negativeWordsFile); 																	
		this.neutralWords = this.loadDictionary(this.neutralWordsFile); 																
																	
	}

	@Override
	public int analyseText(String text) {

		int positive = 0;
		int negative = 0;
		int neutral = 0;
		Set<String> stopWords = NLPUtil.loadDefaultStopWords();

		String[] words = text.split(" ");
		int lengthWords = words.length;

		for (int i = 0; i < lengthWords; i++) {

			String aux = words[i].toLowerCase();
			aux = aux.replaceAll("[^a-zA-Z]*", ""); // Remove tokens TODO[JP]

			if (stopWords.contains(aux) == false) {
				if (positiveWords.contains(aux) == true) {
					positive++;
				} else if (negativeWords.contains(aux) == true) {
					negative++;
				} else if (neutralWords.contains(aux)) {
					neutral++;
				}
			}
		}

		if ((positive > negative) && (positive > neutral)) {
			return POSITIVE;
		} else if ((negative > positive) && (negative > neutral)) {
			return NEGATIVE;
		}
		return NEUTRAL;
	}
}