package com.lg.sentiment;

import android.database.Cursor;

import com.lg.database.DatabaseAdapter;
import com.lg.sentimentalanalysis.Method;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/*Important observations:
 * Can't use string.toLowerCase() before this.partOfSpeech(string), if do, some 
 * words does not make sense for tagger, an example is I.
 */
public class SentiWordNet extends Method {
	// private MaxentTagger tagger;
	private MaxentTagger tagger;
	private DatabaseAdapter db = DatabaseAdapter.getInstance();
	private String tableName;
	private String modelFilePath;

	public SentiWordNet(String tableName, String modelFilePath) {
		this.tableName = tableName;
		this.db.open();
		this.modelFilePath = modelFilePath;
	}

	private String partOfSpeech(String text) {
		try {
			this.tagger = new MaxentTagger(this.modelFilePath);
			String tag = tagger.tagString(text);
			tag = tag.replace("_", "#");
			return tag;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Transform the POS out of the Stanford Library in original SentiWordNet
	 * standard and adds #POS in each word.
	 */
	private String formatString(final String text) {
		String tag = this.partOfSpeech(text);
		String[] words = tag.split(" ");
		String mSentence = "";

		for (String word : words) {
			String[] s = word.split("#");
			// When the "s" is many "#", the s[1] don't exist and there is an
			// exception.
			if (s.length > 1) {
				switch (s[1]) {
				case "RB": // Checking adverbs:
				case "RBR":
				case "RBS":
					mSentence += s[0] + "#r ";
					break;
				case "JJ": // Checking adjectives
				case "JJR":
				case "JJS":
					mSentence += s[0] + "#a ";
					break;
				case "VB": // Checking verbs
				case "VBD":
				case "VBG":
				case "VBN":
				case "VBP":
				case "VBZ":
					mSentence += s[0] + "#v ";
					break;
				case "NNP": // Checking nouns
				case "NN":
				case "NNPS":
				case "NNS":
					mSentence += s[0] + "#n ";
					break;
				default:
					// The tag not is important. Do nothing.
				}
			}
		}
		mSentence = mSentence.toLowerCase();
		return mSentence;
	}

	private Double calculatePolarity(String word) {
		String[] keys = word.replaceAll("#+", "#").split("#");
		Cursor c;

		String sql = "SELECT * FROM " + this.tableName + "WHERE word = "
				+ keys[0] + "AND type = " + keys[1];
		c = this.db.getData(sql);
		double sum = 0;
		double score = 0;
		while (c.moveToNext()) {
			int index = c.getInt(1);
			float pos = c.getFloat(2);
			float neg = c.getFloat(3);
			score += 1 / (index * (pos - neg));
			sum += 1 / index;
		}
		c.close();

		return sum == 0 ? 0 : score / sum;
	}

	@Override
	public int analyseText(final String text) {
		String sentence = this.formatString(text);
		Double finalScore = 0.;
		String[] words = sentence.split(" ");
		for (String word : words) {
			finalScore += calculatePolarity(word);
		}

		if (finalScore < 0.) {
			return NEGATIVE;
		} else if (finalScore > 0.) {
			return POSITIVE;
		} else {
			return NEUTRAL;
		}
	}

	@Override
	public void loadDictionaries() {
		// TODO Auto-generated method stub

	}
}
