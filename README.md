![Frutilla Logo](https://raw.githubusercontent.com/ignaciotcrespo/frutilla/master/design/frutilla.jpg)

![build status](https://img.shields.io/badge/build-info =>-yellow.svg) [![Build Status](https://travis-ci.org/ignaciotcrespo/frutilla.svg?branch=master)](https://travis-ci.org/ignaciotcrespo/frutilla) [![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ignaciotcrespo/frutilla/blob/master/LICENSE)


# Frutilla 0.7.0
Frutilla lets java software development teams describe the tests in plain text, and link them to the specifications.

I like the **[Cucumber](https://cucumber.io/)** way to describe tests using **GIVEN + WHEN + THEN** sentences, and I think JUnit needs something to help UT to be more descriptive. Cucumber has a java API but I think it is very complex to use, linking sentences to java methods. Creating a UT should be a simple task.

Tests must be readable and less lines of code is better. I really appreciate a self descriptive test, you know exactly the use case in seconds. It doesnt matter if it is a small unit test or an integration test, a proper description is always welcome. The problem I have found is **the readable code can not be read in the test reports**, and sometimes the reports are opened by people without access to the code, or you as developer received a report but temporary dont have access to the code.

Also sometimes I really appreciate a javadoc in top of a UT describing what is being tested, some tests are hard to write in a readable way, could be due to the poor architecture of the current code. The same disadvantage than before, a javadoc cant be included in the .class file.

I created 2 ways or flavors of adding descriptions that will be included in the compiled classes: with annotations and with JUnit rules.

# Flavor 1: Annotations

Using annotations needs a specific runner and looks like the following:

```java
    @RunWith(value = org.frutilla.FrutillaTestRunner.class)
    public class FrutillaExamplesWithAnnotationTest {
    
        @Frutilla(
            Given = "a test with Frutilla annotations",
            When = "it fails due to an error",
            Then = "it shows the test description in the stacktrace"
        )
        @Test
        public void testError() {
            throw new RuntimeException("forced error");
        }
        
    }
```

It supports also adding **AND** sentences on every block GIVEN, WHEN or THEN.

One advantage of using annotations is they can be collapsed by default, and the test in your IDE will be

```java
    @{...}
    public void testError() {
        throw new RuntimeException("forced error");
    }
```

# Flavor 2: JUnit rules

In case annotations is not your cup of tea I included a way to do it using the powerful JUnit rules. In this case there is no need to run with FrutillaTestRunner, but the rule needs to be declared.

```java
    public class FrutillaExamplesWithRuleTest {

      @Rule
      public FrutillaRule mScenario = new FrutillaRule();
      
      @Test
      public void testError() throws Exception {
        mScenario.given("a test with Frutilla rule")
                .when("it fails due to and error")
                .then("it shows the test description in the stacktrace").end();

        throw new RuntimeException("forced exception");
      }
```

I see pretty invasive to include the description inside the test, but the alternative is there for you if you like it. Another disadvantage is you can not collapse the descriptions block.

# The stacktrace in the reports

What I added to test reports is the description in top of the stacktrace errors. I dont care the tests that passed, I care about those that failed, and I want to know fast what is the problem. 
Using Frutilla the stacktrace looks like this:

    java.lang.RuntimeException:
    [
    GIVEN a test with Frutilla annotations
    WHEN it fails due to an error
    THEN it shows the test description in the stacktrace
    ]
    [
    Message: forced error
    ]
    at org.frutilla.android.FrutillaExamplesWithAnnotationTest.testError(FrutillaExamplesWithAnnotationTest.java:38)
    at java.lang.reflect.Method.invokeNative(Native Method)
    at java.lang.reflect.Method.invoke(Method.java:511)
    ...
  
It is helpful to see those descriptions in CI servers like jenkins. I really hate to see a failed UT in jenkins and start searching for it in the code to understand what is doing due to the name is something like "testParsingDataValidWhenNoUser". WTF, that method name can be a hundred of things. What is the data? Why is not valid? And more.
With the proper description I know exactly what is failing, and if the descriptions are linked to the specifications then we are 1 click of knowing the complete scenario to understand the problem.

# Android

Frutilla can be used also in **Android** using the excellent JUnit4 instrumentation runner **[AndroidJUnitRunner](http://developer.android.com/reference/android/support/test/runner/AndroidJUnitRunner.html)**

Add it in gradle using:

    androidTestCompile 'com.android.support.test:runner:0.3'
    
and add the instrumentation in your manifest

        <instrumentation
        android:name="android.support.test.runner.AndroidJUnitRunner"
        android:targetPackage="org.frutilla"/>
    
# Installation

Use the group id for maven and gradle, an example of how to add it in an android project

```
    androidTestCompile 'com.github.ignaciotcrespo:frutilla:0.7.0'
```

***

Frutilla is still in development, but functional. I appreciate your feedback to itcrespo@gmail.com

Pending:
- exporting xml/html reports with the descriptions
- linking reports to official specifications
 
***

# Why Frutilla? 

"Frutilla" means strawberry in spanish, in my land when something good was added to another good thing we say "es la frutilla del postre", similar to "the icing on the cake".

JUnit is good, just needs some flavor on it ;)


