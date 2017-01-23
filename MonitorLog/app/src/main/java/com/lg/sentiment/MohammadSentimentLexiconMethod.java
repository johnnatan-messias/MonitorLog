package com.lg.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lg.sentiment.util.NLPUtil;
import com.lg.sentimentalanalysis.Method;
import com.lg.sentimentalanalysis.MethodCreator;

import android.database.Cursor;

/**
 * @author jpaulo Lexicons developed by Saif Mohammad.
 *         http://saifmohammad.com/WebPages/lexicons.html Must be an abstract
 *         class to force concrete methods to extend it, despite all functions
 *         were already been implemented here.
 *
 * Adaption for Android: elias
 */

public abstract class MohammadSentimentLexiconMethod extends Method {

	private static String TAG = MohammadSentimentLexiconMethod.class
			.getSimpleName();

	protected Map<String, Double> unigramsDictionary;
	protected Map<String, Double> bigramsDictionary;
	protected Map<String, Double> pairsDictionary;

	private String unigramsFileName;
	private String bigramsFileName;
	private String pairsFileName;

	public MohammadSentimentLexiconMethod(String unigramsFileName,
										  String bigramsFileName, String pairsFileName) {

		this.unigramsFileName = unigramsFileName;
		this.bigramsFileName = bigramsFileName;
		this.pairsFileName = pairsFileName;
		this.loadDictionaries();
	}

	@Override
	public void loadDictionaries() {
		this.unigramsDictionary = new HashMap<>();
		this.bigramsDictionary = new HashMap<>();
		this.pairsDictionary = new HashMap<>();

		loadDictionary(this.unigramsDictionary, this.unigramsFileName);
		loadDictionary(this.bigramsDictionary, this.bigramsFileName);
		loadDictionary(this.pairsDictionary, this.pairsFileName);
	}

	private void loadDictionary(Map<String, Double> dictionary, String dictionaryFilePath) {

		try {

			double maxPos = 0d, minNeg = 0d;
			//BufferedReader br = new BufferedReader(new FileReader(dictionaryFilePath));
			BufferedReader br = new BufferedReader(new InputStreamReader(
					MethodCreator.assets.open(dictionaryFilePath)));
			String line = br.readLine();

			while (line != null) {

				String[] data = line.split("\t"); //[0]: sentiment ngram; [1]: sentiment score

				if (!dictionary.containsKey(data[0])) {
					dictionary.put(data[0], Double.valueOf(data[1]));//
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int analyseText(String text) {

		double scoreTotal = 0d;
		String[] tokens = text.toLowerCase().split(" ");
		List<String> unigrams = Arrays.asList(tokens);
		List<String> bigrams = NLPUtil.bigrams(unigrams);
		Cursor c = null;

		int qtUnigrams = unigrams.size();
		int qtBigrams = bigrams.size();

		Set<Integer> unigramsIndexesToRemove = new HashSet<Integer>();
		Set<Integer> bigramsIndexesToRemove = new HashSet<Integer>();

		// pairs' score (paper text: "480,010 non-contiguous pairs.").
		// pairs unigram-unigramCursor()
		scoreTotal += this.pairsAux(unigrams, 0, qtUnigrams - 2, unigramsIndexesToRemove,
				unigrams, 2, qtUnigrams, unigramsIndexesToRemove);

		// pairs unigram-bigram
		scoreTotal += this.pairsAux(unigrams, 0, qtUnigrams - 2, unigramsIndexesToRemove,
				bigrams, 2, qtBigrams, bigramsIndexesToRemove);

		// pairs bigram-unigram
		scoreTotal += this.pairsAux(bigrams, 0, qtBigrams - 2, bigramsIndexesToRemove,
				unigrams, 3, qtUnigrams, unigramsIndexesToRemove);

		// pairs bigram-bigram
		scoreTotal += this.pairsAux(bigrams, 0, qtBigrams - 2, bigramsIndexesToRemove,
				bigrams, 3, qtBigrams, bigramsIndexesToRemove);

		// remove bigrams found in pairs from bigrams list
		this.removeElementsByIndexes(bigrams, bigramsIndexesToRemove);

		// bigrams' score
		int i = 0;
		for (String bigram : bigrams) {

			if (bigram != null && this.bigramsDictionary.containsKey(bigram)) {
				//System.out.println("bigram found: [" + i + "]" + bigram + " = " + this.bigramsDictionary.get(bigram));
				scoreTotal += this.bigramsDictionary.get(bigram);

				//add indexes to remove respective elements in unigrams list
				unigramsIndexesToRemove.add(i);
				unigramsIndexesToRemove.add(i + 1);
			}

			++i;
		}
		this.removeElementsByIndexes(unigrams, unigramsIndexesToRemove);

		// unigrams' score
		i = 0;
		for (String unigram : unigrams) {
			if (unigram != null && this.unigramsDictionary.containsKey(unigram)) {
				scoreTotal += this.unigramsDictionary.get(unigram);
			}
			++i;
		}

		// Ad-hoc for this method: TODO define trashold < 0.5 should be neutral?
		// What about threshold 1.0 value?
		double threshold = 0.5; // 1d, 0.75 ??

		if (scoreTotal > threshold) {
			return POSITIVE;
		} else if (scoreTotal < -threshold) {
			return NEGATIVE;
		}

		if (c != null) c.close();
		return NEUTRAL; // neutral OR N/A
	}

	private void removeElementsByIndexes(List<String> ngramsList,
										 Set<Integer> ngramsIndexesToRemove) {

		for (int index : ngramsIndexesToRemove) {

			ngramsList.set(index, null);
			// System.out.print(index + " ");
		}
	}

	private double pairsAux(List<String> list1, int index1I, int index1F, Set<Integer> list1IndexesToRemove,
							List<String> list2, int index2I, int index2F, Set<Integer> list2IndexesToRemove) {

		double scoreTotal = 0d;
		for (int i1 = index1I; i1 < index1F; ++i1) {

			for (int i2 = index2I + i1; i2 < index2F; ++i2) {
				//pair is represented as ngram---ngram in dictionary
				String pair = new String(list1.get(i1) + "---" + list2.get(i2));

				if (this.pairsDictionary.containsKey(pair)) {
					//System.out.println("Pair found: [" + i1 + "]" + pair + "[" + i2 + "] = " + this.pairsDictionary.get(pair));
					scoreTotal += this.pairsDictionary.get(pair);

					//set indexes to remove elements from bigrams and unigrams lists
					list1IndexesToRemove.add(i1);
					list2IndexesToRemove.add(i2);
				}
			}
		}

		return scoreTotal;
	}
}