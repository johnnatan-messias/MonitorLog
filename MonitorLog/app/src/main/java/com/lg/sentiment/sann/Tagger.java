package com.lg.sentiment.sann;
import android.util.Log;

import com.lg.sentiment.custom.StanfordMaxentTagger4Android;
import com.lg.sentimentalanalysis.MethodCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Tagger {
    private MaxentTagger tagger;
    public static String TAG = Tagger.class.getSimpleName();
    public Tagger(String models){
        try {
            this.tagger = new StanfordMaxentTagger4Android(MethodCreator.assets.open(models));
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public String partOfSpeech(String text) {
        String sentence = text;
        String tag = this.tagger.tagString(sentence);
        tag = tag.replace("_", "#");
        return tag;
    }

    public List<String> formatingString(String text) {
        String tag = this.partOfSpeech(text);
        String[] words = tag.split(" ");
        String mSentence = "";
        for(String word : words) {
            String[] s = word.split("#");
            boolean check = s.length > 1;
            if(check){
                if((s[1].equals("RB")) || (s[1].equals("RBR")) || (s[1].equals("RBS"))) {
                    mSentence += s[0] + "#r ";
                } else if((s[1].equals("JJ")) || (s[1].equals("JJR")) 
                        || (s[1].equals("JJS"))) {
                    mSentence += s[0] + "#a ";
                } else if((s[1].equals("VB")) || (s[1].equals("VBD")) 
                        || (s[1].equals("VBG")) || (s[1].equals("VBN"))
                        || (s[1].equals("VBP")) || (s[1].equals("VBZ"))) {
                    mSentence += s[0] + "#v ";
                } else if((s[1].equals("NNP")) || (s[1].equals("NN"))
                        || s[1].equals("NNPS") || (s[1].equals("NNS"))
                        || (s[1].equals("FW"))) {
                    mSentence += s[0] + "#n ";
                }
            }
        }
        mSentence = mSentence.toLowerCase();
        String[] temp = mSentence.split(" ");
        List<String> r = new ArrayList<String>();
        for(int k = 0; k < temp.length; k++){
            if(!temp[k].equals("")){
                r.add(temp[k]);
            }
        }
        return r;
    }
}
