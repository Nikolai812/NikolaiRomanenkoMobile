package nativeTests;

import Setup.DriverWrapper;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import testSuiteSetup.Hooks;
import java.util.List;


public class SimpleNativeTests extends Hooks {

    final private String app_package_name = "com.example.android.contactmanager:id/";
    final private By add_btn = By.id(app_package_name + "addContactButton");

    @BeforeClass
    /**
     * Prepare driverSingle to run test(s)
     */
    public void setUp() throws Exception {

        System.out.println("Setting Up Simple Native tests....");
    }

    @Test
    /**
     * This simple test checks the existsnce of  button 'Add contact'
     * then clicks on it and verifies that the screen has changed; no more
     */
    public void SimpleNativeTest() throws Exception{

        AppiumDriver driver = driver();

        // verifying that the "Add Contact" button exists.
        VerifyAddContctButtonExists(driver);

        // Since the button exists we can click it
        System.out.println("Clicking Add Contact button to change the screen");
        driver.findElement(add_btn).click();

        VerifyAddContctButtonNotExists(driver);
        System.out.println("Simplest Appium test done");
    }

    private void VerifyAddContctButtonExists(AppiumDriver driver) {
        List<WebElement> elements = driver.findElements(add_btn);
        Assert.assertNotNull(elements);
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(elements.get(0).getText(), "Add Contact");
        System.out.println("Verified: Add Contact button does exist and only one");
    }

    private void VerifyAddContctButtonNotExists(AppiumDriver driver) {
        Assert.assertEquals(driver.findElements(add_btn).size(), 0);
        System.out.println("Verified: Add Contact button does not exist");
    }

    @AfterClass
    /**
     * Close driverSingle on all tests completion
     */
    public void tearDown() throws Exception {
        System.out.println("Tearing down Simple Native tests..., quitting driver");
        driver().quit();
    }
}
