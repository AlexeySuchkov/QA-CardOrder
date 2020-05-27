import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderTest {

    private static WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach

    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }


    @Test
    void submitRequestNoCheckUp() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("input[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("input[type='tel']")).sendKeys("+79650000000");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actual);
    }

    @Test
    void shouldNotSubmitRequest() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("input[type='text']")).sendKeys("Ivan Ivanov");
        driver.findElement(By.cssSelector("input[type='tel']")).sendKeys("+79650000000");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span + span +span")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actual);
    }

    @Test
    void submitEmptyName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("input[type='tel']")).sendKeys("+79650000000");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span + span +span")).getText();
        assertEquals("Поле обязательно для заполнения", actual);
    }

    @Test
    void submitEmptyPhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("input[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span + span +span")).getText();
        assertEquals("Поле обязательно для заполнения", actual);
    }

    @Test
    void submitFalsePhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("input[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("input[type='tel']")).sendKeys("+79650");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span + span +span")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actual);
    }
}