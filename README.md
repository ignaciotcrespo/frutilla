# frutilla
Frutilla lets software development teams describe the tests in plain text, and link them to the specifications.

I like the cucumber way to describe tests, and I think JUnit needs something to help UT to be more descriptive.
I will not enter in the discussion of UT must be self descriptive. I really appreciate a javadoc in top of a UT describing what is being tested.
But the disadvantage of a javadoc is cant be included in the .class file, so the descriptions are missing in test reports.

I created 2 ways or flavors of adding descriptions: with annotations or with JUnit rules.

Using annotations a test looks like the following:

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
        throw new RuntimeException("forced error");
    }

and the stacktrace of the error will show

    java.lang.RuntimeException:
    [
    GIVEN a test with Frutilla annotations
    WHEN it fails due to error
    THEN it shows the test description in the stacktrace
    and in the logs
    ]
    [
    Message: forced error
    ]
    at org.frutilla.android.FrutillaExamplesWithAnnotationTest.testError(FrutillaExamplesWithAnnotationTest.java:38)
    at java.lang.reflect.Method.invokeNative(Native Method)
    at java.lang.reflect.Method.invoke(Method.java:511)
    ...
  
It is helpful to see those descriptions in CI servers like jenkins. I really hate to see a failed UT in jenkins and start searching for it in the code to understand what is doint due to the name is something like "testParsingDataValidWhenNoUser".
With the proper description I know exactly what is failing, and the descriptions can be linked to specifications.

In case annotations is not your cup of tea I included a way to do it using the powerful JUnit rules

    public class FrutillaExamplesWithRuleTest {

      @Rule
      public FrutillaRule mScenario = new FrutillaRule();
      
      @Test
      public void testError() throws Exception {
        mScenario.given("there is a test").and("uses frutilla rule")
                .when("result is error")
                .then("must be error and displayed").end();

        throw new RuntimeException("forced exception");
      }

I see very invasive to include the description inside the test, but the alternative is there for you if you like it.

It can be used also in android using the JUnit4 instrumentation runner android.support.test.runner.AndroidJUnitRunner
Including in gradle the following is enough

    androidTestCompile 'com.android.support.test:runner:0.3'
    
The code is still in development but functional.
