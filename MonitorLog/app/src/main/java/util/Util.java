package util;

import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Johnnatan Messias
 */
public class Util {

	public static int PLEASANTNESS = 0;
	public static int ATTENTION = 1;
	public static int SENSITIVITY = 2;
	public static int APTITUDE = 3;
	public static int POLARITY = 4;

	public static Vector<String> regex(String line,
			HashMap<String, Boolean> stopwords, String re) {
		Vector<String> tokens = new Vector<String>();

		Pattern p = Pattern.compile(re);
		Matcher m = p.matcher(line);

		while (m.find()) {
			if (!stopwords.containsKey(m.group()))
				tokens.add(m.group());
		}

		return tokens;
	}

}
