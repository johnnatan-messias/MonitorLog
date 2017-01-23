package mpqa4lg.opin.main;

import mpqa4lg.opin.config.Config;
import mpqa4lg.opin.entity.Corpus;
import mpqa4lg.opin.featurefinder.ClueFind;
import mpqa4lg.opin.logic.AnnotationHandler;
import mpqa4lg.opin.output.SGMLOutput;
import mpqa4lg.opin.preprocessor.PreProcess;
import mpqa4lg.opin.rulebased.RuleBasedClassifier;
import mpqa4lg.opin.supervised.ExpressionPolarityClassifier;
import mpqa4lg.opin.supervised.SentenceSubjectivityClassifier;

public class RunOpinionFinder
{
    public static void main(final String[] args) {
        final Config conf = new Config();
        if (!conf.parseCommandLineOptions(args)) {
            System.exit(-1);
        }
        final Corpus corpus = new Corpus(conf);
        if (conf.isRunPreprocessor()) {
            final PreProcess preprocessor = new PreProcess(conf);
            preprocessor.process(corpus);
        }
        if (conf.isRunClueFinder()) {
            final ClueFind clueFinder = new ClueFind(conf);
            clueFinder.process(corpus);
        }
        final AnnotationHandler annHandler = new AnnotationHandler(conf);
        if (conf.isRunRulebasedClassifier() || conf.isRunSubjClassifier() || conf.isRunPolarityClassifier()) {
            annHandler.buildSentencesFromGateDefault(corpus);
        }
        if (conf.isRunRulebasedClassifier()) {
            annHandler.readInRequiredAnnotationsForRuleBased(corpus);
            final RuleBasedClassifier rulebased = new RuleBasedClassifier();
            rulebased.process(corpus);
        }
        if (conf.isRunSubjClassifier()) {
            annHandler.readInRequiredAnnotationsForSubjClassifier(corpus);
            final SentenceSubjectivityClassifier subjClassifier = new SentenceSubjectivityClassifier(conf);
            subjClassifier.process(corpus);
        }
        if (conf.isRunPolarityClassifier()) {
            annHandler.readInRequiredAnnotationsForPolarityClassifier(corpus);
            final ExpressionPolarityClassifier polarityClassifier = new ExpressionPolarityClassifier(conf);
            polarityClassifier.process(corpus);
        }
        if (conf.isRunSGMLOutput()) {
            final SGMLOutput output = new SGMLOutput(conf.isRunRulebasedClassifier(), conf.isRunSubjClassifier(), conf.isRunPolarityClassifier());
            output.process(corpus);
        }
    }
}
