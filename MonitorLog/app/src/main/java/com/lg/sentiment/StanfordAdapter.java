package com.lg.sentiment;

import java.util.Properties;

import com.lg.sentimentalanalysis.Method;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;

/**
 * @author jpaulo Adapter class to run sentiment analysis from
 *         stanford-corenlp-3.3.1.jar (and others) and convert its output to
 *         standard implementation.
 */
public class StanfordAdapter extends Method {

	/**
	 * from stanford-corenlp-3.3.1.jar
	 */
	private StanfordCoreNLP stanfordNLP;

	/**
	 *
	 */
	public StanfordAdapter() {

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment"); // from
		// SentimentPipeline.main

		this.stanfordNLP = new StanfordCoreNLP(props);
	}

	@Override
	public int analyseText(String text) {

		// from SentimentPipeline.main: Map<Class, List<Map<Class, Object>. List
		// has size = 1 and Object in this key is String.
		Annotation ann = this.stanfordNLP.process(text);
		String out = ann.get(CoreAnnotations.SentencesAnnotation.class).get(0)
				.get(SentimentCoreAnnotations.ClassName.class); //Stanford 3.4.1
		//		.get(SentimentCoreAnnotations.SentimentClass.class); //Stanford 3.5.2

		// System.out.println("Out: " + out);
		if (out.contains("ositive")) {
			return POSITIVE;
		}
		else if (out.contains("egative")) {
			return NEGATIVE;
		}
		else if (out.contains("eutral")) {
			return NEUTRAL;
		}

		return NEUTRAL;
	}

	/**
	 * do nothing, it's an Adapter class
	 */
	@Override
	public void loadDictionaries() {
	}

	/**
	 *
	 @Override public void analyseFile(String filePath) {
	  *
	  *           //super.analyseFile(filePath);
	  *
	  *           //output in xml file, taking so much time java.util.List<File>
	  *           fileList = new java.util.ArrayList<>(); fileList.add(new
	  *           File(filePath));
	  *
	  *           try { this.stanfordNLP.processFiles(fileList); } catch
	  *           (IOException e) { e.printStackTrace(); } }
	 */
}
