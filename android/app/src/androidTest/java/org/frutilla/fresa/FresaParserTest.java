package org.frutilla.fresa;

import org.frutilla.annotations.FrutillaCase;

import org.frutilla.annotations.Frutilla;

import junit.framework.TestCase;

import static org.frutilla.fresa.FresaParser.given;
import static org.frutilla.fresa.FresaParser.has;
import static org.frutilla.fresa.FresaParser.reset;

@FrutillaCase(
        ClassUnderTest = FresaParser.class,
        Specs = "not yet"
)
public class FresaParserTest extends TestCase {

    private static final String GIVEN = "given";
    private static final String WHEN = "when";
    private static final String THEN = "then";
    private static final String ONE = "one";
    private static final String TWO = "two";
    private static final String THREE = "three";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FresaParser.reset();
        FresaParser.setListener(null);
    }

    @Frutilla(
            Given = "nothing declared",
            When = "checking if something exists",
            Then = "nothing exists"
    )
    public void testEmptyByDefault() throws Exception {
        assertTrue(FresaParser.isEmpty());
    }

    @Frutilla(
            Given = "a 'given' sentence",
            When = "is declared",
            Then = "the sentence exists"
    )
    public void testGiven() throws Exception {
        given(GIVEN).end();

        assertTrue(has(GIVEN));
    }

    @Frutilla(
            Given = "multiple 'given' sentences",
            When = "are added",
            Then = "all sentences exist"
    )
    public void testGivenMultiple() throws Exception {
        given(GIVEN).and(ONE).and(TWO).end();

        assertTrue(has(ONE));
        assertTrue(has(TWO));
    }

    @Frutilla(
            Given = "all kind of sentences",
            When = "are added",
            Then = "all exist"
    )
    public void testAllTogether() throws Exception {
        given(GIVEN).and(ONE).when(WHEN).and(TWO).then(THEN).and(THREE).end();

        assertTrue(has(GIVEN));
        assertTrue(has(WHEN));
        assertTrue(has(THEN));
        assertTrue(has(ONE));
        assertTrue(has(TWO));
        assertTrue(has(THREE));
    }

    @Frutilla(
            Given = "some sentences added",
            When = "clear all sentences",
            Then = "there are no sentences"
    )
    public void testReset() throws Exception {
        given(GIVEN).and(ONE).when(WHEN).and(TWO).then(THEN).and(THREE).end();

        reset();

        assertFalse(has(GIVEN));
        assertFalse(has(WHEN));
        assertFalse(has(THEN));
        assertFalse(has(ONE));
        assertFalse(has(TWO));
        assertFalse(has(THREE));
    }

    @Frutilla(
            Given = "some sentences are added",
            When = "getting complete sentence",
            Then = {
                    "the sentence is correct",
                    "and is empty after reading sentence"
            }
    )
    public void testPopSentence() throws Exception {
        given(GIVEN).and(ONE).when(WHEN).and(TWO).then(THEN).and(THREE).end();

        final String sentence = String.format("GIVEN %s\n AND %s\nWHEN %s\n AND %s\nTHEN %s\n AND %s\n", GIVEN, ONE, WHEN, TWO, THEN, THREE);
        assertEquals(sentence, FresaParser.popSentence());
        assertTrue(FresaParser.isEmpty());
    }

    @Frutilla(
            Given = "a 'when' sentence",
            When = "is declared",
            Then = "the sentence exists"
    )
    public void testWhen() throws Exception {
        given(GIVEN).when(WHEN).end();

        assertTrue(has(WHEN));
    }

    @Frutilla(
            Given = "multiple 'when' sentences",
            When = "are added",
            Then = "all sentences exist"
    )
    public void testWhenMultiple() throws Exception {
        given(GIVEN).when(WHEN).and(ONE).and(TWO).end();

        assertTrue(has(ONE));
        assertTrue(has(TWO));
    }


    @Frutilla(
            Given = "a 'then' sentence",
            When = "is declared",
            Then = "the sentence exists"
    )
    public void testThen() throws Exception {
        given(GIVEN).when(WHEN).then(THEN).end();

        assertTrue(has(THEN));
    }

    @Frutilla(
            Given = "multiple 'then' sentences",
            When = "are added",
            Then = "all sentences exist"
    )
    public void testThenMultiple() throws Exception {
        given(GIVEN).when(WHEN).then(THEN).and(ONE).and(TWO).end();

        assertTrue(has(ONE));
        assertTrue(has(TWO));
    }

    @Override
    protected void tearDown() throws Exception {
        FresaParser.reset();
        super.tearDown();
    }
}
