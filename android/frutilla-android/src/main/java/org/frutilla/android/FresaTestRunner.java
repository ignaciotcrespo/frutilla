package org.frutilla.android;

import android.os.Bundle;
import android.test.AndroidTestRunner;
import android.test.InstrumentationTestRunner;

/**
 * Created by crespo on 13/08/15.
 */
public class FresaTestRunner extends InstrumentationTestRunner {

    final FresaTestListener mTestXRunnerListenerPseudocode = new FresaTestListener(this);

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
    }

    @Override
    protected AndroidTestRunner getAndroidTestRunner() {
        final AndroidTestRunner runner = super.getAndroidTestRunner();
        runner.addTestListener(mTestXRunnerListenerPseudocode);
        return runner;
    }

    @Override
    public void sendStatus(int resultCode, Bundle results) {
        final boolean pesudocodeEnqueuedStatus = mTestXRunnerListenerPseudocode.onSendStatusEnqueued(resultCode, results);
        if (!pesudocodeEnqueuedStatus) {
            super.sendStatus(resultCode, results);
        }
    }


    public void sendStatusDelayed(int resultCode, Bundle results) {
        super.sendStatus(resultCode, results);
    }

}
