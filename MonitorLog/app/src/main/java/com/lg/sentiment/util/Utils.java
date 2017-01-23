package com.lg.sentiment.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lg.sentimentalanalysis.MethodCreator;

import android.util.Log;

/**
 * @author johnnatan, matheus, elias, jpaulo
 */
public class Utils {

	private static String TAG = Utils.class.getSimpleName();
	/**
	 * @author matheus for Happiness Index and VADER regex:
	 *         [!\"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~]
	 */
	private static final String PUNCTUATION_REGEX = "[!\"#$%&'()\\*\\+,-\\./:;<=>?@\\[\\]\\^_`{|}~]";

	/**
	 * @param puncRegEx
	 * @param text
	 * @return
	 */
	public static String removePunctuation(String puncRegEx, String text) {

		return text.replaceAll(puncRegEx, "");
	}

	public static String removePunctuation(String text) {

		return removePunctuation(PUNCTUATION_REGEX, text);
	}

	/**
	 * @return whether str doesn't contain lowercase letter
	 */
	public static boolean isUpperString(String str) {
		// v1 - jp - PENSO SER A MAIS CORRETA
		// if (str.matches("^[A-Z]+$")) {
		// return true;
		// }

		// v2 - jp
		// if (!str.matches("^.*[a-z].*$")) {
		// return true;
		// }
		// v3 - matheus c++
		for (int i = 0; i < str.length(); ++i) {
			if (Character.isLowerCase(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param list
	 * @param value
	 * @return how many elements <code>value</code> the <code>list</code>
	 *         contains.
	 */
	public static int countOccurrences(List<String> list, String value) {

		int count = 0;
		for (String element : list) {

			if (element.equals(value)) {
				++count;
			}
		}

		return count;
	}

	/**
	 * @param text
	 * @param c
	 * @return how many times <code>c</code> occours in <code>text</code>.
	 */
	public static int countChars(String text, char c) {

		int count = 0;
		for (int i = 0; i < text.length(); ++i) {
			if (c == text.charAt(i)) {
				++count;
			}
		}

		return count;
	}

	/**
	 * @param d
	 *            number to apply precision
	 * @param precision
	 *            max size of decimal
	 * @return <code>d</code> with <code>precision</code> especified
	 */
	public static double setPrecision(double d, int precision) {

		BigDecimal bd = new BigDecimal(d).setScale(precision,
				RoundingMode.HALF_EVEN);
		return bd.doubleValue();
	}

	/**
	 * 
	 * @param fileName
	 * @return Set containing all lexicon word read
	 */
	public static Set<String> readFileLinesToSet(final String fileName) {

		Set<String> set = new HashSet<String>();

		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(
					MethodCreator.assets.open(fileName)));

			String line = br.readLine();
			while (line != null) {
				set.add(line.trim());
				line = br.readLine();
			}
			br.close();

		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		return set;
	}

	/**
	 * @param x
	 * @return log of x at base 2
	 */
	public static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}

	//
	// /**
	// * Same results of String.tokenize(), using a pattern besides a char.
	// * @param text
	// * @param regex
	// * @return list of words tokenized according to pattern regex on text's
	// */
	// public static List<String> tokenizeWithRegex(String text, String regex) {
	//
	//
	// Pattern pattern = Pattern.compile(regex);
	// Matcher matcher = pattern.matcher(text);
	// text.split
	// List<String> allMatches = new ArrayList<String>();
	// while (matcher.find()) {
	// allMatches.add(matcher.group());
	// }
	//
	// return allMatches;
	// }
}
