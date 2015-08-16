package org.frutilla;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * JUnit rule to write Frutilla sentences at the beginning of tests.
 */
public class FrutillaRule extends TestWatcher {

    private FrutillaParser.Given mGiven;

    /**
     * Starts writing sentences. This is the entry point of the use case. <br/>
     * To add more sentences to the entry point use {@link org.frutilla.FrutillaParser.Given#and(String)}
     * @param text the sentence.
     * @return a {@link org.frutilla.FrutillaParser.Given} to continue writing sentences.
     */
    public FrutillaParser.Given given(String text) {
        mGiven = FrutillaParser.given(text);
        return mGiven;
    }

    @Override
    protected void failed(Throwable e, Description description) {
        if (mGiven != null && !mGiven.isEmpty()) {
            ExceptionUtils.insertMessage(e, mGiven.popSentence());
        }
        super.failed(e, description);
    }
}
