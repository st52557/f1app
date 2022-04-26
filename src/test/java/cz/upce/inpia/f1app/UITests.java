package cz.upce.inpia.f1app;

import cz.upce.inpia.f1app.repository.RaceRepository;
import cz.upce.inpia.f1app.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;


@SpringBootTest(classes = F1appApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UITests {


    private String address = "https://semf1-front.herokuapp.com/";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private WebDriver driver;

    private WebDriverWait wait;

    @BeforeAll
    static void setupWebdriverChromeDriver() throws IOException {
        Resource resource = new ClassPathResource("chromedriver.exe");
        String filePath = resource.getFile().getPath();

        System.setProperty("webdriver.chrome.driver", filePath);

        //System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

    }

    @BeforeEach
    void setup() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);

        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, 10);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void performLogin(String username, String password) {
        driver.get(address+"login");

        System.out.println(driver.getCurrentUrl());

        //wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/login']"))).click();
        //WebElement element1 = driver.findElement(By.xpath("//a[text()='Login']"));

        //System.out.println(element1);

        //element1.click();

        //driver.findElement(By.xpath("//a[@href='/login']")).click();

        //WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Login']")));
        //System.out.println(element);


        //wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Login"))).click();
        //driver.findElement(By.linkText("Login")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='formPassword']")));
        driver.findElement(By.xpath("//input[@id='formUsername']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@id='formPassword']")).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @Test
    void loginTest() {

        //driver.get("https://www.github.com");
        //Assertions.assertTrue(driver.getTitle().contains("GitHub"));

        performLogin("lukas", "123");
        wait.until(ExpectedConditions.urlToBe(address));
        Assertions.assertEquals(driver.getCurrentUrl(), address);
    }


}
