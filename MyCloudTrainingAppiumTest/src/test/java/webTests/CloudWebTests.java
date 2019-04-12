package webTests;

import io.appium.java_client.AppiumDriver;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testSuiteSetup.Hooks;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.className;

/**
 * This class is for simple iana.org testing. The title, label and about link are under test.
**/

public class CloudWebTests extends Hooks {

    final private By ianaLabel = By.cssSelector("h1 > span");
    final private By aboutLink = By.cssSelector("a[class='avoid-break']");
    private AppiumDriver driver = null;
    @BeforeClass
    /**
     * Prepare driverSingle to run test(s)
     */
    public void setUp() throws Exception {

        System.out.println("Setting Up Cloud Web tests....");

        driver = driver();
        // For devices with low performance
        driver.manage().timeouts()
                .pageLoadTimeout(5, TimeUnit.MINUTES)
                .implicitlyWait(90, TimeUnit.SECONDS);
    }

    @Test
    public void demoTest() throws Exception {
        System.out.println("Demo Test Started");

        final String epamUrl = "https://www.epam.com/";

        String expectedBrowser = driver.getCapabilities().getBrowserName();
        AssertJUnit.assertTrue(format("Focus is not on '%s'", expectedBrowser), driver.isBrowser());

        driver.get(epamUrl);

        new FluentWait<>(driver).withMessage("Page was not loaded")
                .pollingEvery(ofSeconds(1))
                .withTimeout(ofMinutes(5))
                .until(driver -> driver.findElements(className("header__logo")).size() > 0);

        AssertJUnit.assertEquals("Current url is incorrect", epamUrl, driver.getCurrentUrl());
        AssertJUnit.assertEquals("Page title is incorrect", "EPAM | Enterprise Software Development, Design & Consulting", driver.getTitle());

        System.out.println("Demo Test Passed");
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
        System.out.println("Tearing down Simple Web tests..., not quitting driver");
    }
}
