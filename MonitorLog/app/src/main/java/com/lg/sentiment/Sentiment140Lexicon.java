package com.lg.sentiment;

/**
 * @author jpaulo
 * Lexicon developed by Saif Mohammad
 */
public class Sentiment140Lexicon extends MohammadSentimentLexiconMethod {

	/*
	 * score's range of each dictionary:
	 * unigrams: -4.999 to 5.0
	 * bigrams:  -5.606 to 7.352
	 * pairs:    -4.999 to 5.0
	 */

	public Sentiment140Lexicon(String unigrams_140, 
			String bigrams_140, String pairs_140) {

		super(unigrams_140, bigrams_140, pairs_140);

		//absolute score's value threshold to classify as NEUTRAL
		Double thresholdNeutral = null; 
	}
	
	public static void main(String[] args) {
		
	}
}
