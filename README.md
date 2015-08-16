# Frutilla
Frutilla lets software development teams describe the tests in plain text, and link them to the specifications.

I like the [Cucumber](https://cucumber.io/) way to describe tests using GIVEN + WHEN + THEN sentences, and I think JUnit needs something to help UT to be more descriptive.

I will not enter in the discussion of UT must be self descriptive, etc. I really appreciate a javadoc in top of a UT describing what is being tested, you know exactly the use case in seconds.
But the disadvantage of a javadoc is it cant be included in the .class file, so the descriptions are missing in test reports.

I created 2 ways or flavors of adding descriptions: with annotations or with JUnit rules.

Using annotations a test looks like the following:

    @Frutilla(
            Given = "a test with Frutilla annotations",
            When = "it fails due to an error",
            Then = {
                    "it shows the test description in the stacktrace",
                    "and in the logs"
            }
    )
    @Test
    public void testError() {
        throw new RuntimeException("forced error");
    }

In case annotations is not your cup of tea I included a way to do it using the powerful JUnit rules

    public class FrutillaExamplesWithRuleTest {

      @Rule
      public FrutillaRule mScenario = new FrutillaRule();
      
      @Test
      public void testError() throws Exception {
        mScenario.given("a test with Frutilla rule")
                .when("it fails due to and error")
                .then("it shows the test description in the stacktrace").and("in the logs").end();

        throw new RuntimeException("forced exception");
      }

I see very invasive to include the description inside the test, but the alternative is there for you if you like it.

What I added to test reports is the description in top of the stacktrace errors. I dont care the tests that passed, I care about those that failed, and I want to know fast what is the problem. 
The stacktrace looks like this:

    java.lang.RuntimeException:
    [
    GIVEN a test with Frutilla annotations
    WHEN it fails due to an error
    THEN it shows the test description in the stacktrace
    AND in the logs
    ]
    [
    Message: forced error
    ]
    at org.frutilla.android.FrutillaExamplesWithAnnotationTest.testError(FrutillaExamplesWithAnnotationTest.java:38)
    at java.lang.reflect.Method.invokeNative(Native Method)
    at java.lang.reflect.Method.invoke(Method.java:511)
    ...
  
It is helpful to see those descriptions in CI servers like jenkins. I really hate to see a failed UT in jenkins and start searching for it in the code to understand what is doing due to the name is something like "testParsingDataValidWhenNoUser". WTF, that method name can be hundred of things.
With the proper description I know exactly what is failing, and the descriptions can be linked to specifications.

It can be used also in android using the JUnit4 instrumentation runner android.support.test.runner.AndroidJUnitRunner
Add it in gradle using:

    androidTestCompile 'com.android.support.test:runner:0.3'
    
Frutilla is still in development, but functional. I appreciate any kind of feedback.

Why Frutilla? In my land when something good was added to another good thing we say "es la frutilla del postre", similar to "the icing in the cake".

JUnit is good, just needs some flavor on it ;)


