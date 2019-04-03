package webTests;
import Setup.DriverWrapper;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.appium.java_client.AppiumDriver;
import testSuiteSetup.Hooks;
import java.util.List;

/**
 * This class is for simple iana.org testing. The title, label and about link are under test.
**/

public class SimpleWebTests extends Hooks {

    final private By ianaLabel = By.cssSelector("h1 > span");
    final private By aboutLink = By.cssSelector("a[class='avoid-break']");
    @BeforeClass
    /**
     * Prepare driverSingle to run test(s)
     */
    public void setUp() throws Exception {

        System.out.println("Setting Up Simple Web tests....");
    }

    @Test(description = "Open and slightly test website iana.org")
    public void webTest() throws Exception {//InterruptedException {
        AppiumDriver driver = driver();

        // opening expected page
        driver.get("http://iana.org");
        System.out.println("Loading iana.org page");

        // waiting for the page to load
        driverWait().until(new ExpectedCondition<Boolean>() {
            @NullableDecl
            @Override
            public Boolean apply(@NullableDecl WebDriver webDriver) {
                return driver.getTitle().endsWith("Authority");
            }
        });

        // performing verifications
        VerifyTitleAndLabel(driver);
        VerifyThatAboutLinkExists(driver);

        // clicking about link and changing the page
        System.out.println("Clicking the about link");
        driver.findElement(aboutLink).click();

        // waiting for new page to load
        driverWait().until(new ExpectedCondition<Boolean>() {
            @NullableDecl
            @Override
            public Boolean apply(@NullableDecl WebDriver webDriver) {
                return driver.findElements(aboutLink).size() == 0;
            }
        });

        // performing verifications
        VerifyThatAboutLinkNotExists(driver);
    }

    private void VerifyTitleAndLabel(AppiumDriver driver) {
        Assert.assertEquals(driver.getTitle(), "Internet Assigned Numbers Authority");
        List<WebElement> elements =  driver.findElements(ianaLabel);
        Assert.assertEquals(elements.size(),1);
        System.out.println("Verification: Title and label found");
    }
    private void VerifyThatAboutLinkExists(AppiumDriver driver) {

        List<WebElement> linkElements =  driver.findElements(aboutLink);
        Assert.assertEquals(linkElements.size(),1);
        Assert.assertEquals(linkElements.get(0).getText(), "Learn more.");
        System.out.println("Verification: About link found");
    }

    private void VerifyThatAboutLinkNotExists(AppiumDriver driver) {
        Assert.assertEquals(driver.findElements(aboutLink).size(), 0);
        System.out.println("Verification: No about link found");
    }

    @AfterClass
    /**
     * Close driverSingle on all tests completion
     */
    public void tearDown() throws Exception {
        System.out.println("Tearing down Simple Web tests..., quitting driver");
        driver().quit();
    }


}
