package org.frutilla.android;

import android.app.Instrumentation;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;

import org.frutilla.FrutillaParser;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;

import org.frutilla.annotations.Fresa;
import org.frutilla.utils.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by crespo on 13/08/15.
 */
public class FresaTestListener implements TestListener, FrutillaParser.PseudocodeListener {

    private final Instrumentation mInstrumentation;
    Hashtable<String, String> mCacheDescriptions = new Hashtable<>();
    private boolean mHasError;
    private boolean mHasFailure;

    public FresaTestListener(Instrumentation instrumentation) {
        mInstrumentation = instrumentation;
        FrutillaParser.setListener(this);
    }

    @Override
    public void addError(Test test, Throwable throwable) {
        addMessage(test, throwable);
        mHasError = true;
    }

    private void addMessage(Test test, Throwable throwable) {
        if (isPseudocodeAnnotation() && test instanceof TestCase) {
            TestCase tc = (TestCase) test;
            final String detailMessage = mCacheDescriptions.get(tc.getName());
            if (detailMessage != null) {
                ExceptionUtils.insertMessage(throwable, detailMessage);
            }
        }
    }

    @Override
    public void addFailure(Test test, AssertionFailedError assertionFailedError) {
        addMessage(test, assertionFailedError);
        mHasFailure = true;
    }

    @Override
    public void endTest(Test test) {
        //no end called in pseudocode?
        if (test instanceof TestCase) {
            if (isPseudocodeAnnotation() && FrutillaParser.isEndPending()) {
                TestCase tc = (TestCase) test;
                CachedStatus status;
                while ((status = mQueueStarts.poll()) != null) {
                    if (mHasError) {
//                        ((FrutillaTestRunner) mInstrumentation).sendStatusDelayed(InstrumentationTestRunner.REPORT_VALUE_RESULT_ERROR, status.results);
                    } else if (mHasFailure) {
//                        ((FrutillaTestRunner) mInstrumentation).sendStatusDelayed(InstrumentationTestRunner.REPORT_VALUE_RESULT_FAILURE, status.results);
                    } else {
                        ((FresaTestRunner) mInstrumentation).sendStatusDelayed(status.resultCode, status.results);
                        ((FresaTestRunner) mInstrumentation).sendStatusDelayed(InstrumentationTestRunner.REPORT_VALUE_RESULT_OK, status.results);
                    }
                }
            }
        }
        currentTest = null;
    }

    Test currentTest;

    @Override
    public void startTest(Test test) {
        mHasError = false;
        mHasFailure = false;
        currentTest = test;
    }

    public boolean onSendStatusEnqueued(int resultCode, Bundle results) {
        final String name = results.getString(InstrumentationTestRunner.REPORT_KEY_NAME_TEST);
        System.out.println("xxx-pseudo result " + resultCode + " " + name);
        if (isPseudocodeAnnotation()) {
            if (resultCode == InstrumentationTestRunner.REPORT_VALUE_RESULT_START) {
                enqueueStatus(resultCode, results);
                return true;
            }
        } else {
            if (resultCode != InstrumentationTestRunner.REPORT_VALUE_RESULT_START) {
                CachedStatus status;
                while ((status = mQueueStarts.poll()) != null) {
                    ((FresaTestRunner) mInstrumentation).sendStatusDelayed(status.resultCode, status.results);
                }
            }
        }

        checknamecached(results, resultCode, name);
        return false;
    }

    private void enqueueStatus(int resultCode, Bundle results) {
        CachedStatus status = new CachedStatus();
        status.resultCode = resultCode;
        status.results = new Bundle(results);
        mQueueStarts.add(status);
    }

    Queue<CachedStatus> mQueueStarts = new LinkedBlockingQueue<>();

    static class CachedStatus {
        int resultCode;
        Bundle results;
    }

    public boolean isPseudocodeAnnotation() {
        if (currentTest != null && currentTest instanceof TestCase) {
            TestCase tc = (TestCase) currentTest;
            try {
                Method mtd = tc.getClass().getMethod(tc.getName());
                final Fresa annotation = mtd.getAnnotation(Fresa.class);
                return annotation != null;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public void sendStatusStartDelayed() {
        if (!isPseudocodeAnnotation()) {
            CachedStatus status;
            while ((status = mQueueStarts.poll()) != null) {
                ((FresaTestRunner) mInstrumentation).sendStatusDelayed(status.resultCode, status.results);
            }
            throw new RuntimeException("Test must be annotated with TestPseudocode");
        }
        if (currentTest != null && currentTest instanceof TestCase) {
            TestCase tc = (TestCase) currentTest;
            System.out.println("xxx-pseudo send status delayed: " + tc.getName());
            if (!FrutillaParser.isEmpty()) {
                mCacheDescriptions.put(tc.getName(), FrutillaParser.popSentence());
            }
        }

        CachedStatus status;
        while ((status = mQueueStarts.poll()) != null) {
            Bundle results = status.results;
            int resultCode = status.resultCode;
            final String name = results.getString(InstrumentationTestRunner.REPORT_KEY_NAME_TEST);
            checknamecached(results, resultCode, name);
            ((FresaTestRunner) mInstrumentation).sendStatusDelayed(status.resultCode, status.results);
        }

    }

    private void checknamecached(Bundle results, int resultCode, String name) {
        if (name != null && mCacheDescriptions.containsKey(name)) {
            final String value = name; //+ ":  " + mCacheDescriptions.get(name);
            System.out.println("xxx-pseudo new name " + value);
            results.putString(InstrumentationTestRunner.REPORT_KEY_NAME_TEST, value);
            if (resultCode != InstrumentationTestRunner.REPORT_VALUE_RESULT_START) {
                if (!FrutillaParser.isEmpty()) {
                    System.out.println("xxx-pseudo --------- ");
                    System.out.println("xxx-pseudo " + name + ": " + FrutillaParser.popSentence());
                    System.out.println("xxx-pseudo ---------");
                } else {
                    System.out.println("xxx-pseudo --------- " + resultCode);
                    System.out.println("xxx-pseudo " + name);
                    System.out.println("xxx-pseudo ---------");
                }
            }
        }
    }

}
