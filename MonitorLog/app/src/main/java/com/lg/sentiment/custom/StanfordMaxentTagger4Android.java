package com.lg.sentiment.custom;

import java.io.DataInputStream;
import java.io.InputStream;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.StringUtils;

/**
 * @author jpaulo
 */
public class StanfordMaxentTagger4Android extends MaxentTagger {

	/**
	 * para evitar warning
	private static final long serialVersionUID = 8078894931922574881L;
	 */

	public StanfordMaxentTagger4Android(InputStream isFile) {

        super();
        DataInputStream isData = new DataInputStream(isFile);
//       	super.readModelAndInit(null, isData, false);
        super.readModelAndInit(StringUtils.argsToProperties("-model", "english-left3words-distsim.tagger"), isData, false);
    }
}
