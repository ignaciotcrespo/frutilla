package org.frutilla.examples;

import org.frutilla.FrutillaRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Examples using Frutilla with JUnit rules.
 */
public class FrutillaExamplesWithRuleTest {

    @Rule
    public FrutillaRule mScenario = new FrutillaRule();

    @Test
    public void testPassed() throws Exception {
        mScenario.given("there is a test").and("uses frutilla rule")
                .when("result is passed")
                .then("must be passed and displayed");

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
                .then("must be failed and displayed");

        assertTrue(false);
    }

    @Test
    public void testError() throws Exception {
        mScenario.given("there is a test").and("uses frutilla rule")
                .when("result is error")
                .then("must be error and displayed");

        throw new RuntimeException("forced exception");
    }

}
