package com.example.restapp.GestorFinanciero;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Mispruebas {

    private WebDriver driver;

    @BeforeAll
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe");
        driver = new ChromeDriver(options);
    }
    @Test
    public void prueba1() {
        driver.get("http://localhost:5173/");
        System.out.println(driver.getTitle());
    }

    @AfterAll
    public void teardown() {
        if (driver != null) {
            driver.quit(); 
        }
    }
}
