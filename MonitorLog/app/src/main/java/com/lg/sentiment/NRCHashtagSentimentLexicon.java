package com.lg.sentiment;

/**
 * @author jpaulo
 * By Saif Mohammad
 */
public class NRCHashtagSentimentLexicon extends MohammadSentimentLexiconMethod {

	/*
	 * score's range of each dictionary:
	 * unigrams: from -6.925 to 7.526
	 * bigrams:  from -8.639 to 8.888 
	 * pairs:    from -4.999 to 5.0
	 */
	
	public NRCHashtagSentimentLexicon(String unigrams_NRC, 
			String bigrams_NRC, String pairs_NRC) {

		super(unigrams_NRC, bigrams_NRC, pairs_NRC);
		//LOCAL IDEAL PARA THRESHOLD
	}
}
