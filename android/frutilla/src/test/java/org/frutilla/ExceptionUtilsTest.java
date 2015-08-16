package org.frutilla;

import org.frutilla.annotations.Frutilla;
import org.junit.Test;

import static org.frutilla.ExceptionUtils.insertMessage;
import static org.junit.Assert.assertTrue;

public class ExceptionUtilsTest {

    public static final String MESSAGE = "original message";
    public static final String NEW_MESSAGE = "new inserted message";

    @Frutilla(
            Given = "an exception",
            When = "I insert a text in the exception message",
            Then = {"the new text must exist in the exception message",
                    "and the original message must not be deleted"
            }
    )
    @Test
    public void testInsertMessage() throws Exception {
        final RuntimeException exception = new RuntimeException(MESSAGE);

        insertMessage(exception, NEW_MESSAGE);

        assertTrue(exception.getMessage().contains(MESSAGE));
        assertTrue(exception.getMessage().contains(NEW_MESSAGE));
    }
}
