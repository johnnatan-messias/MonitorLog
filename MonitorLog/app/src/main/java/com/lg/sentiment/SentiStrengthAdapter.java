package com.lg.sentiment;

import com.lg.sentimentalanalysis.Method;

import uk.ac.wlv.sentistrength.SentiStrength;

/**
 * @author jpaulo
 * Adapter class to run sentiment analysis from SentiStrength.jar
 * and convert its output to standard implementation.
 */
public class SentiStrengthAdapter extends Method {

	/**
	 * from SentiStrength.jar
	 */
	private SentiStrength sentiStrength;	

	/**
	 * @param path to folder that contains required data and dictionaries of .jar
	 */
	public SentiStrengthAdapter(String dictionariesFolderPath) {

		this.sentiStrength = new SentiStrength();

		/*
		 * Output options (3 discret values):
		 * trinary: Positive rate; Negative rate; Polarity.  Ranges 1 ~ 5; -1 ~ -5; -1,0,1
		 * scale:   Positive rate; Negative rate; Intensity. Ranges 1 ~ 5; -1 ~ -5; -4 ~ 4 (pos + neg scores, 0 neutral)
		 */
		this.sentiStrength.initialise(new String[] { "scale", "sentidata", dictionariesFolderPath });
	}
	
	@Override
	public int analyseText(String text) {

		String out = this.sentiStrength.computeSentimentScores(text);

		//polarity (or strength) is the 3rd value of SentiStrength's output
		int polarity = Integer.parseInt(out.split(" ")[2]);

		/*
		 * This polarity value verification works for both output
		 * options, trinary or scale, explained in constructor.
		 */
		if (polarity > 0) {
			return POSITIVE;
		}
		else if (polarity < 0) {
			return NEGATIVE;
		}

		return NEUTRAL;
	}

	/**
	 * do nothing, it's an Adapter class
	 */
	@Override
	public void loadDictionaries() {
	}
}
