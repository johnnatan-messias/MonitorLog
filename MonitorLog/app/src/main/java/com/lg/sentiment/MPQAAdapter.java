package com.lg.sentiment;

import android.util.Log;

import com.lg.sentimentalanalysis.Method;
import com.lg.sentimentalanalysis.MethodCreator;

import java.util.List;
import java.util.Map;

import mpqa4lg.opin.config.Config4Android;
import mpqa4lg.opin.entity.Annotation;
import mpqa4lg.opin.entity.Sentence;
import mpqa4lg.opin.featurefinder.ClueFind4Android;
import mpqa4lg.opin.io.ReaderUtils4Android;
import mpqa4lg.opin.logic.AnnotationHandler4Android;
import mpqa4lg.opin.preprocessor.PreProcess;
import mpqa4lg.opin.supervised.ExpressionPolarityClassifier4Android;

/**
 * @author jpaulo
 * Adapter class to run sentiment analysis from Opinion Finder/MPQA
 * and convert its output to standard implementation.
 * [4Android]
 */
public class MPQAAdapter extends Method {

	private final Config4Android conf;
	private final PreProcess preProcessor;
	private final ClueFind4Android clueFinder;
	private final AnnotationHandler4Android annHandler;
	private final ExpressionPolarityClassifier4Android polarityClassifier;

	/**
	 * [4Android]
     * TODO improve the AssetManager usage
	 */
	public MPQAAdapter(String lexiconsFolderPath, String modelsFolderPath) {

		ReaderUtils4Android.assetManager = MethodCreator.assets;
		this.conf = new Config4Android(MethodCreator.assets);
		boolean b = this.conf.parseCommandLineOptions(new String[]{"", //file with sentences not used
				"-l", lexiconsFolderPath, "-m", modelsFolderPath});

		this.preProcessor = new PreProcess(this.conf);
		this.clueFinder = new ClueFind4Android(this.conf);
		this.annHandler = new AnnotationHandler4Android(this.conf);
		this.polarityClassifier = new ExpressionPolarityClassifier4Android(this.conf);
	}

	/**
	 * based on original source code
	 * @param text sentence
	 * @return result achieved by code adapted by us, where map's values are polarities
	 */
	private Map<String, String> achievePolarityPredictions(String text) {

		List<Annotation> gateDefaultAnnotations = this.preProcessor.process(text);

		Map<String, List<Annotation>> mapClueAnnotations = this.clueFinder.process(gateDefaultAnnotations);

		List<Sentence> sentences = this.annHandler.buildSentencesFromGateDefault(gateDefaultAnnotations);
		this.annHandler.readInRequiredAnnotationsForPolarityClassifier(sentences, mapClueAnnotations);

		Map<String, String> polarityResult = this.polarityClassifier.process(sentences);
		return polarityResult;
	}

	@Override
	public int analyseText(String text) {

		Map<String, String> polarityResult = this.achievePolarityPredictions(text);

		/*
		 * MPQA/Opinion Finder original code achieves polarity for many separated parts of the text, not whole text. 
		 * Approach used on our research: text is classified with the polarity that occurs more times. 
		 */
		int finalResult = 0;
		for (String p : polarityResult.values()) {
			if (p.equals("positive")) {
				++finalResult;
			}
			else if (p.equals("negative")) {
				--finalResult;
			}
		}

		if (finalResult > 0) {
			return POSITIVE;
		}
		else if (finalResult < 0) {
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
