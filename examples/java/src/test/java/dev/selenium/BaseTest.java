package dev.selenium;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.grid.Main;
//import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BaseTest {
  protected WebDriver driver;
  protected WebDriverWait wait;
  protected File driverPath;
  protected File browserPath;

  public WebElement getLocatedElement(WebDriver driver, By by) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    return wait.until(d -> driver.findElement(by));
  }

  protected FirefoxDriver startFirefoxDriver() {
    return startFirefoxDriver(new FirefoxOptions());
  }

  protected FirefoxDriver startFirefoxDriver(FirefoxOptions options) {
    options.setImplicitWaitTimeout(Duration.ofSeconds(1));
    driver = new FirefoxDriver(options);
    return (FirefoxDriver) driver;
  }

  protected ChromeDriver startChromeDriver() {
    ChromeOptions options = new ChromeOptions();
    options.setImplicitWaitTimeout(Duration.ofSeconds(1));
    return startChromeDriver(options);
  }

  protected ChromeDriver startChromeDriver(ChromeOptions options) {
    driver = new ChromeDriver(options);
    return (ChromeDriver) driver;
  }

  protected File getTempDirectory(String prefix) {
    File tempDirectory = null;
    try {
      tempDirectory = Files.createTempDirectory(prefix).toFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    tempDirectory.deleteOnExit();

    return tempDirectory;
  }

  protected File getTempFile(String prefix, String suffix) {
    File logLocation = null;
    try {
      logLocation = File.createTempFile(prefix, suffix);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    logLocation.deleteOnExit();

    return logLocation;
  }

//  protected URL startStandaloneGrid() {
//    int port = PortProber.findFreePort();
//    try {
//      Main.main(new String[] {
//        "standalone",
//        "--port", String.valueOf(port),
//        "--enable-managed-downloads", "true",
//        "--log-level", "WARNING"
//      });
//      return new URL("http://localhost:" + port);
//    } catch (MalformedURLException e) {
//      throw new RuntimeException("Failed to create URL for the Selenium Grid", e);
//    }
//  }

  protected void lambdatest(String className, String methodName) throws MalformedURLException {
    URL remoteAddress = new URL("https://:@hub.lambdatest.com/wd/hub");
    DesiredCapabilities capabilities = new DesiredCapabilities();
    HashMap<String, Object> ltOptions = getLtOptions(className, methodName);
    capabilities.setCapability("LT:OPTIONS", ltOptions);
    driver = new RemoteWebDriver(remoteAddress, capabilities);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
  }

  private static HashMap<String, Object> getLtOptions(String className, String methodName) {
    HashMap<String, Object> ltOptions = new HashMap<>();
    ltOptions.put("console", true);
    ltOptions.put("commandLog", true);
    ltOptions.put("build", "seleniumAllCommands - " + className);
    ltOptions.put("idleTimeout", 420);
    ltOptions.put("browserName", "Chrome");
    ltOptions.put("name", methodName);
    ltOptions.put("visual", true);
    ltOptions.put("systemLog", true);
    ltOptions.put("isRealMobile", false);
    ltOptions.put("version", "latest");
    ltOptions.put("platform", "Windows 10");
    ltOptions.put("queueTimeout", 300);
    ltOptions.put("network", true);
    return ltOptions;
  }

  @AfterEach
  public void quit() {
    if (driver != null) {
      driver.quit();
    }
  }
}
