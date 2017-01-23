package com.lg.sentimentalanalysis;

import java.io.File;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.lg.sentiment.Afinn;
import com.lg.sentiment.Emolex;
import com.lg.sentiment.EmoticonDS;
import com.lg.sentiment.Emoticons;
import com.lg.sentiment.HappinessIndex;
import com.lg.sentiment.MPQAAdapter;
import com.lg.sentiment.NRCHashtagSentimentLexicon;
import com.lg.sentiment.OpinionLexicon;
import com.lg.sentiment.PanasT;
import com.lg.sentiment.Sann;
import com.lg.sentiment.Sasa;
import com.lg.sentiment.SentiStrengthAdapter;
import com.lg.sentiment.SentiWordNet;
import com.lg.sentiment.SenticNet;
import com.lg.sentiment.Sentiment140Lexicon;
import com.lg.sentiment.SoCal;
import com.lg.sentiment.StanfordAdapter;
import com.lg.sentiment.UmigonAdapter;
import com.lg.sentiment.Vader;

/**
 * @author jpaulo Design Patterns: Factory (Creator), Singleton
 */
public class MethodCreator {

	public static String TAG = MethodCreator.class.getSimpleName();
	public static AssetManager assets;
	public static Context context;
	private static MethodCreator instance; // singleton
	private String modelFile = "models" + File.separator+"mpqa_opinionfinder"+ File.separator + "english-left3words-distsim.tagger";

	public synchronized static MethodCreator getInstance() {

		if (instance == null) {
			instance = new MethodCreator();
		}

		return instance;
	}

	public Method createMethod(int methodId) {

		String lexDir = "dictionaries" + File.separator;
        String modDir = "models" + File.separator;

		switch (methodId) {
			case 1:
				Log.i(TAG, "Afinn");
				return new Afinn(lexDir + "afinn" + File.separator
						+ "AFINN-111.txt");
			case 2:
				Log.i(TAG, "Emolex");
				return new Emolex(lexDir + "emolex" + File.separator
						+ "NRC-emotion-lexicon-wordlevel-alphabetized-v0.92.txt");
			case 3:
				Log.i(TAG, "Emoticons");
				return new Emoticons(lexDir + "emoticons" + File.separator
						+ "positive.txt", lexDir + "emoticons" + File.separator
						+ "negative.txt", lexDir + "emoticons" + File.separator
						+ "neutral.txt");
			case 4:
				Log.i(TAG, "EmoticonsDS");
				return new EmoticonDS("emoticonDS");
			case 5:
				Log.i(TAG, "HappinessIndex");
				return new HappinessIndex(lexDir + "happinessindex"
						+ File.separator + "anew.csv");
			case 6:
				Log.i(TAG, "MPQA");
				return new MPQAAdapter(lexDir + "mpqa_opinionfinder", modDir + "mpqa_opinionfinder");
			case 7:
				Log.i(TAG, "NRCHashtagSentimentLexicon");
				return new NRCHashtagSentimentLexicon(lexDir
						+ "nrchashtagsentiment" + File.separator
						+ "unigrams-pmilexicon.txt", lexDir + "nrchashtagsentiment"
						+ File.separator + "bigrams-pmilexicon.txt", lexDir
						+ "nrchashtagsentiment" + File.separator
						+ "pairs-pmilexicon.txt");
			case 8:
				Log.i(TAG, "OpinionLexicon");
				return new OpinionLexicon(lexDir + "opinionlexicon"
						+ File.separator + "positive-words.txt", lexDir
						+ "opinionlexicon" + File.separator + "negative-words.txt");
			case 9:
				Log.i(TAG, "PanasT");
				return new PanasT(lexDir + "panas" + File.separator
						+ "positive.txt", lexDir + "panas" + File.separator
						+ "negative.txt", lexDir + "panas" + File.separator
						+ "neutral.txt");
			case 10:
				Log.i(TAG, "SANN");
				return new Sann(modelFile, lexDir + "sann" + File.separator + "emoticons.txt",
					lexDir + "sann" + File.separator + "subjclust.txt",
					lexDir + "sann" + File.separator + "wordNetDictList.txt");
			case 11:
				Log.i(TAG, "Sasa");
				return new Sasa(lexDir + "sasa" + File.separator
						+ "trainedset4LG.txt");
			case 12:
				Log.i(TAG, "SenticNet");
				return new SenticNet(lexDir + "senticnet" + File.separator
						+ "senticnet_v3_dataset.tsv");
			case 13:
				Log.i(TAG, "Sentiment140Lexicon");
				return new Sentiment140Lexicon(lexDir + "sentiment140"
						+ File.separator + "unigrams-pmilexicon.txt", lexDir
						+ "sentiment140" + File.separator
						+ "bigrams-pmilexicon.txt", lexDir + "sentiment140"
						+ File.separator + "pairs-pmilexicon.txt");
			case 14:
				Log.i(TAG, "SentiStrengthAdapter");
				return new SentiStrengthAdapter(lexDir + "sentistrength"
						+ File.separator);
			case 15:
			/* Test it */
				Log.i(TAG, "SentiWordNet");
				return new SentiWordNet(lexDir + "sentiwordnet" + File.separator
						+ "SentiWordNet_3.0.0_20130122.txt", lexDir
						+ "sentiwordnet" + File.separator
						+ "english-bidirectional-distsim.tagger");
			case 16:
				Log.i(TAG, "SoCal");
				return new SoCal(lexDir + "socal" + File.separator
						+ "adj_dictionary1.11.txt", lexDir + "socal"
						+ File.separator + "adv_dictionary1.11.txt", lexDir
						+ "socal" + File.separator + "int_dictionary1.11.txt",
						lexDir + "socal" + File.separator
								+ "noun_dictionary1.11.txt", lexDir + "socal"
						+ File.separator + "verb_dictionary1.11.txt",
						lexDir + "socal" + File.separator + "google_dict.txt",
						modelFile);
			case 17:
				Log.i(TAG, "StanfordAdapter");
				return new StanfordAdapter();
			case 18:
				Log.i(TAG, "Umigon");
				return new UmigonAdapter(lexDir + "umigon");
			case 19:
				Log.i(TAG, "Vader");
				return new Vader(lexDir + "vader" + File.separator
						+ "vader_sentiment_lexicon.txt");
			default:
				return null;
		}
	}
}
