package org.frutilla;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * JUnit rule to write Frutilla sentences at the beginning of tests.
 */
public class FrutillaRule extends TestWatcher {

    /**
     * Starts writing sentences. This is the entry point of the use case. <br/>
     * To add more sentences to the entry point use {@link org.frutilla.FrutillaParser.Given#and(String)}
     * @param text the sentence.
     * @return a {@link org.frutilla.FrutillaParser.Given} to continue writing sentences.
     */
    public FrutillaParser.Given given(String text) {
        return FrutillaParser.given(text);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        if (!FrutillaParser.isEmpty()) {
            ExceptionUtils.insertMessage(e, FrutillaParser.popSentence());
        }
        super.failed(e, description);
    }
}
