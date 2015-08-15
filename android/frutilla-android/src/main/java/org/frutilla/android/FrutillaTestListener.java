package org.frutilla.android;

import android.app.Instrumentation;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;

import org.frutilla.FrutillaAnnotationHelper;
import org.frutilla.FrutillaParser;
import org.frutilla.annotations.Frutilla;
import org.frutilla.utils.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.Hashtable;

public class FrutillaTestListener implements TestListener {

    private final Instrumentation mInstrumentation;
    Hashtable<String, String> mCacheDescriptions = new Hashtable<>();

    public FrutillaTestListener(Instrumentation instrumentation) {
        mInstrumentation = instrumentation;
    }

    @Override
    public void addError(Test test, Throwable throwable) {
        addMessage(test, throwable);
    }

    private void addMessage(Test test, Throwable throwable) {
        if (test instanceof TestCase) {
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
    }

    @Override
    public void endTest(Test test) {
    }

    @Override
    public void startTest(Test test) {
        if (test instanceof TestCase) {
            TestCase tc = (TestCase) test;
            processAnnotations(tc);
        }
    }

    private void processAnnotations(TestCase tc) {
        try {
            Method mtd = tc.getClass().getMethod(tc.getName());
            processAnnotationTestx(tc, mtd);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void processAnnotationTestx(TestCase tc, Method mtd) {
        Frutilla ann = mtd.getAnnotation(Frutilla.class);
        String value = FrutillaAnnotationHelper.getText(ann);
        mCacheDescriptions.put(tc.getName(), value);
    }

    public void onSendStatusChangedName(int resultCode, Bundle results) {
        final String name = results.getString(InstrumentationTestRunner.REPORT_KEY_NAME_TEST);
        System.out.println("xxx result " + resultCode + " " + name);
        if (resultCode == InstrumentationTestRunner.REPORT_VALUE_RESULT_START && name != null && mCacheDescriptions.containsKey(name)) {
            final String value = name + ":  " + mCacheDescriptions.get(name);
            System.out.println("xxx new name " + value);
            //results.putString(InstrumentationTestRunner.REPORT_KEY_NAME_TEST, value);
        } else {
            if (resultCode != InstrumentationTestRunner.REPORT_VALUE_RESULT_START) {
                if (!FrutillaParser.isEmpty()) {
                    System.out.println("xxx --------- ");
                    System.out.println("xxx " + name + ": " + FrutillaParser.popSentence());
                    System.out.println("xxx ---------");
                } else {
                    System.out.println("xxx --------- " + resultCode);
                    System.out.println("xxx " + name);
                    System.out.println("xxx ---------");
                }
            }
        }
    }

}
