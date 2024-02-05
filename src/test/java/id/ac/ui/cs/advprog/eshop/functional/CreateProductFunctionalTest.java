package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {
    /**
     * The port number assigned to the running application during test execution.
     * Set automatically during each test run by Spring Framework's test context.
     */
    @LocalServerPort
    private int serverPort;

    /**
     * The base URL for testing. Default to {@code http://localhost}
     */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;
    private String listEndpoint;
    private String createEndpoint;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
        listEndpoint = baseUrl + "/product/list";
        createEndpoint = baseUrl + "/product/create";
    }

    @Test
    void createButtonGoToCreatePage(ChromeDriver driver) throws Exception {
        driver.get(listEndpoint);
        WebElement createPageButton = driver.findElement(By.linkText("Create Product"));
        createPageButton.click();
        String currentUrl = driver.getCurrentUrl();

        assertEquals(createEndpoint, currentUrl);
    }

    @Test
    void createProduct_isCorrect(ChromeDriver driver) throws Exception {
        driver.get(createEndpoint);
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        String productName = "Test Product";
        nameInput.clear();
        nameInput.sendKeys(productName);

        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        String productQuantity = "5";
        quantityInput.clear();
        quantityInput.sendKeys(productQuantity);

        WebElement submitButton = driver.findElement(By.tagName("button"));
        submitButton.click();

        String currentUrl = driver.getCurrentUrl();
        assertEquals(listEndpoint, currentUrl);

        List<WebElement> tdTags = driver.findElements(By.tagName("td"));

        // productName, productQuantity, editButton, deleteButton
        assertEquals(4, tdTags.size());

        String savedProductName = tdTags.get(0).getText();
        assertEquals(productName, savedProductName);

        String savedProductQuantity = tdTags.get(1).getText();
        assertEquals(productQuantity, savedProductQuantity);
    }
}
