package dev.selenium.elements;

import dev.selenium.BaseChromeTest;
import java.io.File;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class FileUploadTest extends BaseChromeTest {

  @Test
  public void fileUploadTest() {
    driver.get("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-zip-file.zip");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException ignored) {
    }

    driver.get("https://the-internet.herokuapp.com/upload");
    File uploadFile = new File("src/test/resources/selenium-snapshot.png");

    WebElement fileInput = driver.findElement(By.cssSelector("input[type=file]"));
    fileInput.sendKeys(uploadFile.getAbsolutePath());
    driver.findElement(By.id("file-submit")).click();

    WebElement fileName = driver.findElement(By.id("uploaded-files"));
    Assertions.assertEquals("selenium-snapshot.png", fileName.getText());
  }
}
