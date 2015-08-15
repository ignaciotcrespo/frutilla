package org.frutilla;

import org.frutilla.FrutillaParser;
import org.frutilla.utils.ExceptionUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Created by crespo on 15/08/15.
 */
public class FrutillaRule extends TestWatcher {

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
