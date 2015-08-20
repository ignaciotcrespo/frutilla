package org.frutilla;

import org.frutilla.annotations.Frutilla;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.frutilla.FrutillaParser.given;
import static org.frutilla.FrutillaParser.reset;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(value = FrutillaTestRunner.class)
public class FrutillaParserAnnotationsTest {

    private static final String GIVEN = "given";
    private static final String WHEN = "when";
    private static final String THEN = "then";
    private static final String ONE = "one";
    private static final String TWO = "two";
    public static final String ONETWO = "\n AND " + ONE + "\n AND " + TWO;
    public static final String ONEBUTTWO = "\n AND " + ONE + "\n BUT " + TWO;

    @Mock
    Frutilla mMockFrutilla;

    @Before
    public void setUp() throws Exception {
        reset();
        MockitoAnnotations.initMocks(this);
    }

    @Frutilla(
            Given = {
                    "a 'given' annotation",
                    "AND has null sentence"
            },
            When = "process it",
            Then = "sentence is empty text"
    )
    @Test
    public void testGivenFromAnnotationNull() throws Exception {
        when(mMockFrutilla.Given()).thenReturn(null);
        String sentence = given(mMockFrutilla);

        assertEquals("", sentence);
    }

    @Frutilla(
            Given = {
                    "a 'given' annotation",
                    "AND has empty array sentence"
            },
            When = "process it",
            Then = "sentence is empty text"
    )
    @Test
    public void testGivenFromAnnotationEmpty() throws Exception {
        when(mMockFrutilla.Given()).thenReturn(new String[]{});
        String sentence = given(mMockFrutilla);

        assertEquals("", sentence);
    }

    @Frutilla(
            Given = {
                    "a 'given' annotation",
                    "AND has one empty string sentence"
            },
            When = "process it",
            Then = "sentence is empty text"
    )
    @Test
    public void testGivenFromAnnotationEmptyString() throws Exception {
        mockGivenAnnotation("");
        String sentence = given(mMockFrutilla);

        assertEquals("", sentence);
    }

    @Frutilla(
            Given = {
                    "a 'given' annotation",
                    "AND has a sentence"
            },
            When = "process it",
            Then = "sentence has the correct text"
    )
    @Test
    public void testGivenFromAnnotation() throws Exception {
        mockGivenAnnotation(GIVEN);
        String sentence = given(mMockFrutilla);

        assertEquals("GIVEN " + GIVEN, sentence);
    }

    @Frutilla(
            Given = {
                    "a 'given' annotation",
                    "AND has two sentences"
            },
            When = "process it",
            Then = {
                    "sentence has GIVEN",
                    "AND the two sentences"
            }
    )
    @Test
    public void testGivenMultipleFromAnnotation() throws Exception {
        mockGivenAnnotation(GIVEN, ONE, TWO);
        String sentence = given(mMockFrutilla);

        assertEquals("GIVEN " + GIVEN + ONETWO, sentence);
    }

    @Frutilla(
            Given = {
                    "a 'given' annotation",
                    "AND a 'when' annotation",
                    "AND both have two sentences"
            },
            When = "process them",
            Then = {
                    "sentence has GIVEN",
                    "AND sentence has WHEN",
                    "AND both GIVEN and WHEN have the two sentences"
            }
    )
    @Test
    public void testGivenWhenMultipleFromAnnotation() throws Exception {
        mockGivenAnnotation(GIVEN, ONE, TWO);
        mockWhenAnnotation(WHEN, ONE, TWO);
        String sentence = given(mMockFrutilla);

        assertEquals("GIVEN " + GIVEN + ONETWO
                + "\nWHEN " + WHEN + ONETWO
                , sentence);
    }

    @Frutilla(
            Given = {
                    "a 'given' annotation",
                    "AND a 'when' annotation",
                    "AND a 'then' annotation",
                    "AND all have two sentences"
            },
            When = "process them",
            Then = {
                    "sentence has GIVEN",
                    "AND sentence has WHEN",
                    "AND sentence has THEN",
                    "AND all GIVEN, WHEN and THEN have the two sentences"
            }
    )
    @Test
    public void testGivenWhenThenMultipleFromAnnotation() throws Exception {
        mockGivenAnnotation(GIVEN, ONE, TWO);
        mockWhenAnnotation(WHEN, ONE, TWO);
        mockThenAnnotation(THEN, ONE, TWO);
        String sentence = given(mMockFrutilla);

        assertEquals("GIVEN " + GIVEN + ONETWO
                + "\nWHEN " + WHEN + ONETWO
                + "\nTHEN " + THEN + ONETWO
                , sentence);
    }

    @Frutilla(
            Given = {
                    "a 'given' annotation",
                    "AND a 'when' annotation",
                    "AND a 'then' annotation",
                    "AND all have a sentence starting with 'and ' (ignoring case and spaces at left)",
                    "AND all have a sentence starting with 'but ' (ignoring case and spaces at left)"
            },
            When = "process them",
            Then = {
                    "sentence has GIVEN",
                    "AND sentence has WHEN",
                    "AND sentence has THEN",
                    "AND all GIVEN, WHEN and THEN have the two sentences",
                    "AND the sentences starting with 'and ' is reformatted starting with 'AND ' ",
                    "AND the sentences starting with 'but ' is reformatted starting with 'BUT ' ",
            }
    )
    @Test
    public void testGivenWhenThenMultipleFromAnnotationHardcodedAndBut() throws Exception {
        mockGivenAnnotation(GIVEN, "and one", " BuT two");
        mockWhenAnnotation(WHEN, " And one", "but two");
        mockThenAnnotation(THEN, "   AND one", "BUT two ");
        String sentence = given(mMockFrutilla);

        assertEquals("GIVEN " + GIVEN + ONEBUTTWO
                + "\nWHEN " + WHEN + ONEBUTTWO
                + "\nTHEN " + THEN + ONEBUTTWO
                , sentence);
    }

    @Frutilla(
            Given = {
                    "a 'given' annotation",
                    "AND a 'when' annotation",
                    "AND a 'then' annotation",
                    "AND all have a null sentences"
            },
            When = "process them",
            Then = "sentence is empty"
    )
    @Test
    public void testGivenWhenThenMultipleFromAnnotationAllNull() throws Exception {
        mockGivenAnnotation(null, null, null);
        mockWhenAnnotation(null, null, null);
        mockThenAnnotation(null, null, null);
        String sentence = given(mMockFrutilla);

        assertEquals("", sentence);
    }

    @Frutilla(
            Given = {
                    "a 'given' annotation",
                    "AND a 'when' annotation",
                    "AND a 'then' annotation",
                    "AND all have a empty text sentences (ignoring spaces)"
            },
            When = "process them",
            Then = "sentence is empty"
    )
    @Test
    public void testGivenWhenThenMultipleFromAnnotationAllEmpty() throws Exception {
        mockGivenAnnotation("", "   ", "");
        mockWhenAnnotation("   ", " ", "  ");
        mockThenAnnotation(" ", "", " ");
        String sentence = given(mMockFrutilla);

        assertEquals("", sentence);
    }

    private void mockGivenAnnotation(String... text) {
        when(mMockFrutilla.Given()).thenReturn(text);
    }

    private void mockWhenAnnotation(String... text) {
        when(mMockFrutilla.When()).thenReturn(text);
    }

    private void mockThenAnnotation(String... text) {
        when(mMockFrutilla.Then()).thenReturn(text);
    }

}
