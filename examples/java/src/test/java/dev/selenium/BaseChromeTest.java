package dev.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

public class BaseChromeTest extends BaseTest {
  String className = null;
  String methodName = null;
  @BeforeEach
  public void setup(TestInfo testInfo) throws MalformedURLException {
    className = testInfo.getTestClass().map(Class::getName).orElse("unknown");
    methodName = testInfo.getTestMethod().map(Method::getName).orElse("unknown");
    System.out.println("Running test: " + className + " - " + methodName);
    lambdatest(className, methodName);
  }
}
