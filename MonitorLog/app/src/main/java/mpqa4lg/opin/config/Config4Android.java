package mpqa4lg.opin.config;

import android.content.res.AssetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * [4Android]
 * 1 AssetManager
 * File types replaced by String
 */
public class Config4Android
{
    private String intensifierLexicon;
    private String valenceshifterLexicon;
    private String polarityLexicon;
    private String subjLexicon;
    private String stanfordModel;
    private String subjModel;
    private String polarityModel;
    private String polarityModelHeader;
    private String swsdModelDir;
    private Charset charset;
    private boolean runPreprocessor;
    private boolean runClueFinder;
    private boolean runSubjClassifier;
    private boolean runRulebasedClassifier;
    private boolean runPolarityClassifier;
    private boolean runSGMLOutput;
    private boolean runSWSD;
    private boolean useDatabaseStructure;
    private ArrayList<String> documents;
    private static String USAGE;

    private AssetManager assetManager;

    public Config4Android() {
        this.charset = Charset.forName("UTF-8");
        this.runPreprocessor = true;
        this.runClueFinder = true;
        this.runSubjClassifier = true;
        this.runRulebasedClassifier = true;
        this.runPolarityClassifier = true;
        this.runSGMLOutput = true;
        this.runSWSD = false;
        this.useDatabaseStructure = false;
        this.documents = new ArrayList<String>();
    }

    public Config4Android(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
    }

    public boolean parseCommandLineOptions(final String[] args) {
        if (args.length < 1) {
            System.out.println(Config4Android.USAGE);
            return false;
        }
        final String f = args[0];
        boolean useAsDoclist = false;
        for (int i = 1; i < args.length; ++i) {
            if (args[i].equals("-d")) {
                useAsDoclist = true;
            }
            else if (args[i].equals("-m")) {
                if (i == args.length - 1) {
                    System.out.println(Config4Android.USAGE);
                    return false;
                }
                final String modelPath = args[++i];
                this.stanfordModel = modelPath + File.separator + "english-left3words-distsim.tagger";
                this.subjModel = modelPath + File.separator + "sent_subj.model";
                this.polarityModel = modelPath + File.separator + "exp_polarity.model";
                this.polarityModelHeader = modelPath + File.separator + "exp_polarity_header.arff";
                this.swsdModelDir = modelPath + File.separator + "swsd";
            }
            else if (args[i].equals("-l")) {
                if (i == args.length - 1) {
                    System.out.println(Config4Android.USAGE);
                    return false;
                }
                final String lexiconPath = args[++i];
                this.subjLexicon = lexiconPath + File.separator + "subjcluesSentenceClassifiersOpinionFinderJune06.tff";
                this.polarityLexicon = lexiconPath + File.separator + "subjclueslen1polar.tff";
                this.intensifierLexicon = lexiconPath + File.separator + "intensifiers.tff";
                this.valenceshifterLexicon = lexiconPath + File.separator + "valenceshifters.tff";
            }
            else if (args[i].equals("-r")) {
                if (i == args.length - 1) {
                    System.out.println(Config4Android.USAGE);
                    return false;
                }
                final String[] vs = args[++i].split(",");
                this.runPreprocessor = false;
                this.runClueFinder = false;
                this.runSubjClassifier = false;
                this.runRulebasedClassifier = false;
                this.runPolarityClassifier = false;
                this.runSGMLOutput = false;
                for (int j = 0; j < vs.length; ++j) {
                    if (vs[j].equals("preprocessor")) {
                        this.runPreprocessor = true;
                    }
                    else if (vs[j].equals("cluefinder")) {
                        this.runClueFinder = true;
                    }
                    else if (vs[j].equals("subjclass")) {
                        this.runSubjClassifier = true;
                    }
                    else if (vs[j].equals("rulebasedclass")) {
                        this.runRulebasedClassifier = true;
                    }
                    else if (vs[j].equals("polarityclass")) {
                        this.runPolarityClassifier = true;
                    }
                    else {
                        if (!vs[j].equals("sgml")) {
                            System.out.println("!! Not recognized module name: " + vs[j]);
                            return false;
                        }
                        this.runSGMLOutput = true;
                    }
                }
            }
            else if (args[i].equals("-s")) {
                this.useDatabaseStructure = true;
            }
            else if (args[i].equals("-e")) {
                if (!Charset.isSupported(args[i + 1])) {
                    System.out.println("!! Not recognized character set: " + args[i + 1]);
                    return false;
                }
                this.charset = Charset.forName(args[++i]);
            }
            else if (args[i].equals("-w")) {
                this.runSWSD = true;
            }
            else {
                System.out.println(Config4Android.USAGE);
            }
        }

        return true;
    }

    public String getIntensifierLexicon() {
        return this.intensifierLexicon;
    }

    public String getValenceshifterLexicon() {
        return this.valenceshifterLexicon;
    }

    public String getPolarityLexicon() {
        return this.polarityLexicon;
    }

    public String getSubjLexicon() {
        return this.subjLexicon;
    }

    public String getStanfordModel() {
        return this.stanfordModel;
    }

    public String getSubjModel() {
        return this.subjModel;
    }

    public String getPolarityModel() {
        return this.polarityModel;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public boolean isRunPreprocessor() {
        return this.runPreprocessor;
    }

    public boolean isRunClueFinder() {
        return this.runClueFinder;
    }

    public boolean isRunSubjClassifier() {
        return this.runSubjClassifier;
    }

    public boolean isRunRulebasedClassifier() {
        return this.runRulebasedClassifier;
    }

    public boolean isRunPolarityClassifier() {
        return this.runPolarityClassifier;
    }

    public boolean isUseDatabaseStructure() {
        return this.useDatabaseStructure;
    }

    public boolean isRunSGMLOutput() {
        return this.runSGMLOutput;
    }

    public ArrayList<String> getDocuments() {
        return this.documents;
    }

    public String getPolarityModelHeader() {
        return this.polarityModelHeader;
    }

    public boolean isRunSWSD() {
        return this.runSWSD;
    }

    public String getSwsdModelDir() {
        return this.swsdModelDir;
    }

    public InputStream valenceshifterLexiconIS() {
        try {
            return this.assetManager.open(this.valenceshifterLexicon);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream intensifierLexiconIS() {
        try {
            return this.assetManager.open(this.intensifierLexicon);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream polarityLexiconIS() {
        try {
            return this.assetManager.open(this.polarityLexicon);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream subjLexiconIS() {
        try {
            return this.assetManager.open(this.subjLexicon);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream stanfordModelIS() {
        try {
            return this.assetManager.open(this.stanfordModel);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream subjModelIS() {
        try {
            return this.assetManager.open(this.subjModel);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream polarityModelIS() {
        try {
            return this.assetManager.open(this.polarityModel);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream polarityModelHeaderIS() {
        try {
            return this.assetManager.open(this.polarityModelHeader);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
