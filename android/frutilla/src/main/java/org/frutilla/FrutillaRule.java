package org.frutilla;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * JUnit rule to write Frutilla sentences at the beginning of tests.
 */
public class FrutillaRule extends TestWatcher {

    private FrutillaParser.AbstractSentence mRoot;

    /**
     * Starts writing sentences. This is the description of the scenario of the use case. <br/>
     * To add more sentences to the scenario use <br/>
     * {@link org.frutilla.FrutillaParser.Scenario#and(String)} <br/>
     * {@link org.frutilla.FrutillaParser.Scenario#but(String)}
     * @param text the sentence.
     * @return a {@link org.frutilla.FrutillaParser.Given} to continue writing sentences.
     */
    public FrutillaParser.Scenario scenario(String text) {
        mRoot = FrutillaParser.scenario(text);
        return (FrutillaParser.Scenario) mRoot;
    }

    /**
     * Starts writing sentences. This is the entry point of the use case. <br/>
     * To add more sentences to the entry point use <br/>
     * {@link org.frutilla.FrutillaParser.Given#and(String)} <br/>
     * {@link org.frutilla.FrutillaParser.Given#but(String)}
     * @param text the sentence.
     * @return a {@link org.frutilla.FrutillaParser.Given} to continue writing sentences.
     */
    public FrutillaParser.Given given(String text) {
        mRoot = FrutillaParser.given(text);
        return (FrutillaParser.Given) mRoot;
    }

    @Override
    protected void failed(Throwable e, Description description) {
        if (mRoot != null && !mRoot.isEmpty()) {
            ExceptionUtils.insertMessage(e, mRoot.popSentence());
        }
        super.failed(e, description);
    }
}
