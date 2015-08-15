package org.frutilla.fresa;

import android.os.SystemClock;

import junit.framework.TestCase;

import org.frutilla.annotations.Fresa;

import static org.frutilla.fresa.FresaParser.given;

public class FresaExamplesTest extends TestCase {

    public void testPassedNormal() throws Exception {
        assertTrue(true);
    }

    public void testFailedNormal() throws Exception {
        assertTrue(false);
    }

    public void testErrorNormal() throws Exception {
        throw new RuntimeException();
    }

    @Fresa
    public void testPassed() throws Exception {
        given("there is a test").and("has pseudocode annotation").when("result is passed").then("must be passed and displayed").end();

        SystemClock.sleep(50);

        assertTrue(true);

    }

    @Fresa
    public void testPassedWithoutEnd() throws Exception {
        given("there is a test").and("has pseudocode annotation").when("result is passed").and("pseudocode has no end").then("result must be marked as passed");

        SystemClock.sleep(50);

        assertTrue(true);

    }

    @Fresa
    public void testFailedWithoutEnd() throws Exception {
        given("there is a test").and("has pseudocode annotation").when("result is failed").then("must be marked as failed");

        assertTrue(false);

    }

    @Fresa
    public void testErrorWithoutEnd() throws Exception {
        given("there is a test").and("has pseudocode annotation").when("result is error").then("must be marked as error");

        throw new RuntimeException();

    }

    @Fresa
    public void testPassedNoPseudocode() throws Exception {

        assertTrue(true);

    }

    @Fresa
    public void testFailedNoPseudocode() throws Exception {

        assertTrue(false);

    }

    @Fresa
    public void testErrorNoPseudocode() throws Exception {
        throw new RuntimeException();
    }

    public void testPassedNoAnnotation() throws Exception {
        try {
            given("there is a test").and("has no pseudocode annotation").when("executing pseudocode").then("must fail due to it needs the annotation").end();
            fail();
        } catch (Exception exc) {
            //expected
            assertEquals("Test must be annotated with TestPseudocode", exc.getMessage());
        }

    }

    @Fresa
    public void testFailed() throws Exception {
        given("there is a test").and("has pseudocode annotation").when("result is failed").then("must be failed and displayed").end();

        SystemClock.sleep(75);

        assertTrue(false);

    }

    @Fresa
    public void testError() throws Exception {
        given("there is a test").and("has pseudocode annotation").when("result is error").then("must be error and displayed").end();

        SystemClock.sleep(100);

        throw new RuntimeException("forced exception");

    }

//    @Frutilla(
//            Given = {"there is a test", "has pseudocode and Frutilla annotation"},
//            When = "result is passed",
//            Then = "must be marked as passed and displayed"
//    )
    @Fresa
    public void testPassedWithPseudocodeAndTestX() throws Exception {
        given("there is a test").and("has pseudocode and Frutilla annotation").when("result is passed").then("must be passed and displayed").end();

        SystemClock.sleep(50);

        assertTrue(true);

    }


}
