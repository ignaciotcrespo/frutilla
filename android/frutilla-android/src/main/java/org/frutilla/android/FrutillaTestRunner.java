package org.frutilla.android;

import android.os.Bundle;
import android.test.AndroidTestRunner;
import android.test.InstrumentationTestRunner;

public class FrutillaTestRunner extends InstrumentationTestRunner {

    final FrutillaTestListener mListener = new FrutillaTestListener(this);

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
    }

    @Override
    protected AndroidTestRunner getAndroidTestRunner() {
        final AndroidTestRunner runner = super.getAndroidTestRunner();
        runner.addTestListener(mListener);
        return runner;
    }

    @Override
    public void sendStatus(int resultCode, Bundle results) {
        mListener.onSendStatusChangedName(resultCode, results);
        super.sendStatus(resultCode, results);
    }
}
