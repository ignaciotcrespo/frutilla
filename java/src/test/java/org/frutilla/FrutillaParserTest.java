package org.frutilla;

import org.frutilla.annotations.Frutilla;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.frutilla.FrutillaParser.given;
import static org.frutilla.FrutillaParser.has;
import static org.frutilla.FrutillaParser.reset;
import static org.frutilla.FrutillaParser.scenario;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(value = FrutillaTestRunner.class)
public class FrutillaParserTest {

    private static final String GIVEN = "given";
    private static final String WHEN = "when";
    private static final String THEN = "then";
    private static final String ONE = "one";
    private static final String TWO = "two";
    private static final String THREE = "three";
    public static final String BUT = "pero";
    public static final String SCENARIO = "my scenario desc";

    @Before
    public void setUp() throws Exception {
        reset();
    }

    // --- TESTS BEGIN -----------------------------------------------------------------------------

    @Frutilla(
            Scenario = "FrutillaParser not used",
            Given = "nothing declared",
            When = "checking if something exists",
            Then = "nothing exists"
    )
    @Test
    public void testEmptyByDefault() throws Exception {
        assertTrue(FrutillaParser.isEmpty());
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = "a 'given' sentence",
            When = "is declared",
            Then = "the sentence exists"
    )
    @Test
    public void testGiven() throws Exception {
        given(GIVEN);

        assertHas(GIVEN);
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = "multiple 'given' sentences",
            When = "are added",
            Then = "all sentences exist"
    )
    @Test
    public void testGivenMultiple() throws Exception {
        given(GIVEN).and(ONE).and(TWO);

        assertHas(ONE);
        assertHas(TWO);
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = "multiple 'given' sentences",
            When = "are added to the same 'given' instance variable",
            Then = "all sentences exist"
    )
    @Test
    public void testGivenMultipleMultiline() throws Exception {
        FrutillaParser.Given given = given(GIVEN);
        given.and(ONE);
        given.and(TWO);

        assertHas(ONE);
        assertHas(TWO);
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = {
                    "all kind of sentences",
                    "BUT starting with 'given'"
            },
            When = "are added",
            Then = "all exist"
    )
    @Test
    public void testAllTogetherStartingWithGiven() throws Exception {
        given(GIVEN).and(ONE).when(WHEN).and(TWO).then(THEN).and(THREE);

        assertHas(GIVEN);
        assertHas(WHEN);
        assertHas(THEN);
        assertHas(ONE);
        assertHas(TWO);
        assertHas(THREE);
    }

    @Frutilla(
            Given = "all kind of sentences",
            When = "are added",
            Then = "all exist"
    )
    @Test
    public void testAllTogetherStartingWithScenario() throws Exception {
        scenario(SCENARIO).given(GIVEN).and(ONE).when(WHEN).and(TWO).then(THEN).and(THREE);

        assertHas(SCENARIO);
        assertHas(GIVEN);
        assertHas(WHEN);
        assertHas(THEN);
        assertHas(ONE);
        assertHas(TWO);
        assertHas(THREE);
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = "some sentences added",
            When = "clear all sentences",
            Then = "there are no sentences"
    )
    @Test
    public void testReset() throws Exception {
        given(GIVEN).and(ONE).when(WHEN).and(TWO).then(THEN).and(THREE);

        reset();

        assertFalse(has(GIVEN));
        assertFalse(has(WHEN));
        assertFalse(has(THEN));
        assertFalse(has(ONE));
        assertFalse(has(TWO));
        assertFalse(has(THREE));
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = "some sentences are added",
            When = "getting complete sentence",
            Then = {
                    "the sentence is correct",
                    "and is empty after reading sentence"
            }
    )
    @Test
    public void testPopSentence() throws Exception {
        given(GIVEN).and(ONE).but(BUT).when(WHEN).and(TWO).then(THEN).and(THREE);

        final String sentence = String.format("GIVEN %s\n AND %s\n BUT %s\nWHEN %s\n AND %s\nTHEN %s\n AND %s",
                GIVEN, ONE, BUT, WHEN, TWO, THEN, THREE);
        assertEquals(sentence, FrutillaParser.popSentence());
        assertTrue(FrutillaParser.isEmpty());
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = "a 'when' sentence",
            When = "is declared",
            Then = "the sentence exists"
    )
    @Test
    public void testWhen() throws Exception {
        given(GIVEN).when(WHEN);

        assertHas(WHEN);
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = "multiple 'when' sentences",
            When = "are added",
            Then = "all sentences exist"
    )
    @Test
    public void testWhenMultiple() throws Exception {
        given(GIVEN).when(WHEN).and(ONE).and(TWO);

        assertHas(ONE);
        assertHas(TWO);
    }


    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = "a 'then' sentence",
            When = "is declared",
            Then = "the sentence exists"
    )
    @Test
    public void testThen() throws Exception {
        given(GIVEN).when(WHEN).then(THEN);

        assertHas(THEN);
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = "multiple 'then' sentences",
            When = "are added",
            Then = "all sentences exist"
    )
    @Test
    public void testThenMultiple() throws Exception {
        given(GIVEN).when(WHEN).then(THEN).and(ONE).and(TWO);

        assertHas(ONE);
        assertHas(TWO);
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = "a 'scenario' sentence",
            When = "is added",
            Then = "the sentence exist"
    )
    @Test
    public void testScenario() throws Exception {
        scenario(SCENARIO);

        assertHas(SCENARIO);
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = {
                    "a 'scenario' sentence",
                    "AND a 'given' sentence"
            },
            When = "are added",
            Then = "the sentences exist"
    )
    @Test
    public void testScenarioGiven() throws Exception {
        scenario(SCENARIO).given(GIVEN);

        assertHas(SCENARIO);
        assertHas(GIVEN);
    }

    // ---------------------------------------------------------------------------------------------

    @Frutilla(
            Given = {
                    "a 'scenario' sentence",
                    "AND an 'and' sentence",
                    "AND a 'but' sentence"
            },
            When = "are added",
            Then = "the sentences exist"
    )
    @Test
    public void testScenarioAndBut() throws Exception {
        scenario(SCENARIO).and(ONE).but(TWO);

        assertHas(SCENARIO);
        assertHas(ONE);
        assertHas(TWO);
    }

    // --- TESTS END -------------------------------------------------------------------------------

    @After
    public void tearDown() throws Exception {
        reset();
    }

    private void assertHas(String sentence) {
        assertTrue(has(sentence));
    }

}
