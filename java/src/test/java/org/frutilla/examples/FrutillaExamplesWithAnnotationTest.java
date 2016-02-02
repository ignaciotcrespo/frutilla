package org.frutilla.examples;

import org.frutilla.annotations.Frutilla;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Examples using Frutilla with annotations.
 */
@RunWith(value = org.frutilla.FrutillaTestRunner.class)
public class FrutillaExamplesWithAnnotationTest {

    @Frutilla(
            Given = "a test with Frutilla annotations",
            When = "it fails",
            Then = {
                    "it shows the test description in the stacktrace",
                    "and in the logs"
            }
    )
    @Test
    public void testFailed() {
//        assertTrue(false);
    }

    @Frutilla(
            Given = "a test with Frutilla annotations",
            When = "it fails due to error",
            Then = {
                    "it shows the test description in the stacktrace",
                    "and in the logs"
            }
    )
    @Test
    public void testError() {
//        throw new RuntimeException("forced error");
    }

    @Frutilla(
            Given = "a test with Frutilla annotations",
            When = "it passes",
            Then = "it shows the test description in the logs"
    )
    @Test
    public void testPassed() {
        assertTrue(true);
    }

}
