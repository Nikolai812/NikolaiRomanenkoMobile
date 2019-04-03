package testSuiteSetup;

import Setup.DriverWrapper;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import io.appium.java_client.AppiumDriver;

import javax.annotation.ParametersAreNullableByDefault;

public class Hooks {

    //protected AppiumDriver driver;
    //protected WebDriverWait driverWait;

    protected AppiumDriver driver() throws Exception{
        return DriverWrapper.driver();
    }
    protected WebDriverWait driverWait() throws Exception{
        return DriverWrapper.driverWait();
    }

    @Parameters({"propertiesFile"})
    @BeforeSuite(description = "Prepare driver to run test(s)")
    public void setUp(@Optional String fileName) throws Exception {
        if(fileName !=null && fileName.length() > 0) {
            DriverWrapper.SetPropertiesFile(fileName);
        }
        DriverWrapper.driver();
        DriverWrapper.driverWait();
        System.out.println("Hooks: Initializing test suite: Appium driver should have already started. propertiesFile(fileName): " + fileName);
    }

    @AfterSuite(description = "Close driver on all tests completion")
    public void tearDown() throws Exception {
        DriverWrapper.driver().quit();
        System.out.println("Hooks: Test suite completed. Appium driver is quit");
    }

}
