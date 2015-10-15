Cucumber-JVM + Groovy + Rest Integration Test Starter
=================================================

To run:
```java
gradle --quiet cucumber
```
What is supported:

1. Any endpoint within https://website.cloudhub.io (obviously you have to change these URLs). E.g. /api/test1, /api/test1/subtest1, etc.
2. GET requests are supported under the following pattern: "I access the resource url..."
3. POST requests are supported under the following pattern: "I post to the resource url..."
4. Query parameters ("I provide parameter..." pattern)
5. Request headers ("I provide header..." pattern)
6. Response status code ("the status code should be..." pattern)
7. Json field result validation ("it should have the field 'nnnn' containing the value 'mmmm'... pattern)
8. Body support through an external file (under /test/resources/requestSamples). The pattern is: "I provide a body request file from..."

Installation:
---
As long as you have gradle (and maven) installed, there is nothing else you need. Gradle (https://docs.gradle.org/current/userguide/installation.html) takes care of everything for you :)

What is missing:
  * I'm missing a lot of patterns and extras. E.g. Better "it should have the field" conditions.
  * If you can extend it, please do so and send me a pull request. Thanks!
