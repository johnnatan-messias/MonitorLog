package com.lg.sentiment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.util.Log;

import com.lg.sentiment.util.NLPUtil;
import com.lg.sentimentalanalysis.Method;
import com.lg.sentimentalanalysis.MethodCreator;

/**
 * @author Johnnatan Messias, jpaulo
 */
public class SenticNet extends Method {

	private static String TAG = SenticNet.class.getSimpleName();
	private static final String REGEX_TOKENIZER = "([a-zA-Z]+-[a-zA-Z]+)|([a-zA-Z]+)"; // "(\\w+-\\w+)|(\\w+)")
	public static final int PLEASANTNESS = 0;
	public static final int ATTENTION = 1;
	public static final int SENSITIVITY = 2;
	public static final int APTITUDE = 3;
	public static final int POLARITY = 4; // focus

	private Map<String, Vector<Double>> senticnetData; // lexicon dictionary
	private String filenameIn;

	public SenticNet(String filenameIn) {

		this.filenameIn = filenameIn;
		this.loadDictionaries();
	}

	@Override
	public void loadDictionaries() {
		this.senticnetData = new HashMap<String, Vector<Double>>();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					MethodCreator.assets.open(this.filenameIn)));
			String line = br.readLine();

			while (line != null) {
				Vector<Double> parameters = new Vector<>();

				String[] parametersAux = line.split("\t");
				String word = parametersAux[0].split("=")[1];

				parameters.add(Double.valueOf(parametersAux[1].split("=")[1])); // Pleasantness
																				// score
				parameters.add(Double.valueOf(parametersAux[2].split("=")[1])); // Attention
																				// score
				parameters.add(Double.valueOf(parametersAux[3].split("=")[1])); // Sensitivity
																				// score
				parameters.add(Double.valueOf(parametersAux[4].split("=")[1])); // Aptitude
																				// score
				parameters.add(Double.valueOf(parametersAux[5].split("=")[1])); // Polarity
																				// score
				senticnetData.put(word, parameters);

				line = br.readLine();
			}

			br.close();
		} catch (NumberFormatException e) {
			Log.e(TAG, e.getMessage());
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int analyseText(String text) {

		double result = this.checkText(text.toLowerCase());

		if (result > 0d) {
			return POSITIVE;
		} else if (result < 0d) {
			return NEGATIVE;
		}

		return NEUTRAL;
	}

	/**
	 * @param text
	 * @param stopwords
	 * @return text tokenized removing stopWords and whatever doesn't fit regex
	 */
	public static Vector<String> rexp(String text, Set<String> stopwords) {
		Vector<String> tokens = new Vector<String>();

		Pattern p = Pattern.compile(REGEX_TOKENIZER);
		Matcher m = p.matcher(text);

		while (m.find()) {
			if (!stopwords.contains(m.group())) {
				tokens.add(m.group());
			}
		}

		return tokens;
	}

	/**
	 * @param text
	 *            to be analysed
	 * @return polarity/sentiment intensity
	 */
	@SuppressWarnings("unused")
	public Double checkText(String text) {
		double sumPleasantness = 0d;
		double sumAttention = 0d;
		double sumSensitivity = 0d;
		double sumAptitude = 0d;
		double sumPolarity = 0d;
		double result;

		Vector<String> tokens;

		tokens = rexp(text, NLPUtil.stopWordsSenticNet);

		if (tokens.size() == 0) {
			return 0d;
		}

		// check for occurrences of 4-, 3-, 2- and 1-grams
		String token;
		int index = 3;
		int ngram = 0;
		int length = tokens.size();

		while (index - 3 < length) {
			if ((length > index)
					&& (this.senticnetData.containsKey(tokens.get(index - 3)
							+ " " + tokens.get(index - 2) + " "
							+ tokens.get(index - 1) + " " + tokens.get(index)))) {
				// 4-gram
				token = tokens.get(index - 3) + " " + tokens.get(index - 2)
						+ " " + tokens.get(index - 1) + " " + tokens.get(index);
				index += 3;
				ngram += 3;
			} else if ((length > index - 1)
					&& (this.senticnetData.containsKey(tokens.get(index - 3)
							+ " " + tokens.get(index - 2) + " "
							+ tokens.get(index - 1)))) {
				// 3-gram
				token = tokens.get(index - 3) + " " + tokens.get(index - 2)
						+ " " + tokens.get(index - 1);
				index += 2;
				ngram += 2;
			} else if ((length > index - 2)
					&& (this.senticnetData.containsKey(tokens.get(index - 3)
							+ " " + tokens.get(index - 2)))) {
				// 2-gram
				token = tokens.get(index - 3) + " " + tokens.get(index - 2);
				index += 1;
				ngram += 1;
			} else if (this.senticnetData.containsKey(tokens.get(index - 3))) {
				// 1-gram
				token = tokens.get(index - 3);
			} else {
				index += 1;
				continue;
			}

			index += 1;
			sumPleasantness += this.senticnetData.get(token).get(PLEASANTNESS);
			sumAttention += this.senticnetData.get(token).get(ATTENTION);
			sumSensitivity += this.senticnetData.get(token).get(SENSITIVITY);
			sumAptitude += this.senticnetData.get(token).get(APTITUDE);
			sumPolarity += this.senticnetData.get(token).get(POLARITY);
		}

		result = sumPolarity / (length - ngram);

		return result;
	}
}
