package org.frutilla.tests;

import org.frutilla.FrutillaRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FrutillaExamplesWithRuleTest {

    @Rule
    public FrutillaRule mScenario = new FrutillaRule();

    @Test
    public void testPassedNormal() throws Exception {
        assertTrue(true);
    }

    @Test
    public void testFailedNormal() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testErrorNormal() throws Exception {
        throw new RuntimeException();
    }

    @Test
    public void testPassed() throws Exception {
        mScenario.given("there is a test").and("uses frutilla rule")
                .when("result is passed")
                .then("must be passed and displayed").end();

        assertTrue(true);
    }

    @Test
    public void testPassedWithoutEnd() throws Exception {
        mScenario.given("there is a test").and("uses frutilla rule")
                .when("result is passed").and("has no end")
                .then("result must be marked as passed");

        assertTrue(true);
    }

    @Test
    public void testFailedWithoutEnd() throws Exception {
        mScenario.given("there is a test").and("uses frutilla rule")
                .when("result is failed")
                .then("must be marked as failed");

        assertTrue(false);
    }

    @Test
    public void testErrorWithoutEnd() throws Exception {
        mScenario.given("there is a test").and("uses frutilla rule")
                .when("result is error").then("must be marked as error");

        throw new RuntimeException();
    }

    @Test
    public void testFailed() throws Exception {
        mScenario.given("there is a test").and("uses frutilla rule")
                .when("result is failed")
                .then("must be failed and displayed").end();

        assertTrue(false);
    }

    @Test
    public void testError() throws Exception {
        mScenario.given("there is a test").and("uses frutilla rule")
                .when("result is error")
                .then("must be error and displayed").end();

        throw new RuntimeException("forced exception");
    }

    //    @Frutilla(
//            Given = {"there is a test", "has pseudocode and Frutilla annotation"},
//            When = "result is passed",
//            Then = "must be marked as passed and displayed"
//    )

//    public void testPassedWithPseudocodeAndTestX() throws Exception {
//        given("there is a test").and("has pseudocode and Frutilla annotation").when("result is passed").then("must be passed and displayed").end();
//
//        assertTrue(true);
//
//    }


}
