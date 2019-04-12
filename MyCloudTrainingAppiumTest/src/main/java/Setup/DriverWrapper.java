package Setup;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.*;
import io.appium.java_client.AppiumDriver;

import java.io.*;
import java.net.URL;

import static java.lang.String.format;

/**
 * Appium driver wrapper. Is implemented as a singletone
 * **/
public class DriverWrapper extends TestProperties {
    protected static AppiumDriver driverSingle;
    protected static WebDriverWait waitSingle;

    protected DesiredCapabilities capabilities;

    // Properties to be read
    protected String USER_DIR;
    protected String AUT; // (mobile) app under testing
    protected String APP_PACKAGE; // cloud app under testing
    protected String APP_ACTIVITY; // activity of cloud app
    protected String SUT; // site under testing
    protected String PLATFORM_NAME;
    protected String PLATFORM_VERSION;
    protected String DRIVER;
    protected String DEVICE;

    private static DriverWrapper  wrapperInstance;
    private static DriverWrapper getWrapperInstance() throws IOException{
        if (wrapperInstance == null) {
            wrapperInstance = new DriverWrapper();
        }

        return wrapperInstance;
    };

    /**
     * Drivers capabilities preparation and driver creation
     * **/
    private static void prepareDriver() throws Exception {
        DriverWrapper wrapper = getWrapperInstance();
        wrapper.capabilities = new DesiredCapabilities();


        String browserName;       // Setup test platform: Android or iOS. Browser also depends on a platform.
        switch(wrapper.PLATFORM_NAME) {
              case "Android":
                  browserName = "Chrome";
                  break;
              case "iOS":
                  browserName = "Safari";
                  break;
              default: throw new Exception("Unknown mobile platform");
        }

        wrapper.capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME.toString(), wrapper.PLATFORM_NAME);
        wrapper.capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION.toString(), wrapper.PLATFORM_VERSION);
        wrapper.capabilities.setCapability(MobileCapabilityType.DEVICE_NAME.toString(), wrapper.DEVICE);

        // Setup type of application:local native, cloud native or web
        if(wrapper.APP_PACKAGE != null && wrapper.APP_ACTIVITY != null && wrapper.SUT == null && wrapper.AUT == null && wrapper.DRIVER == null){
            // these are properties for native app at mobile farm, sut, driver and aut
            wrapper.capabilities.setCapability("appPackage", wrapper.APP_PACKAGE);
            wrapper.capabilities.setCapability("appActivity", wrapper.APP_ACTIVITY);
        } else if(wrapper.APP_PACKAGE == null && wrapper.APP_ACTIVITY == null && wrapper.SUT == null && wrapper.AUT != null && wrapper.DRIVER != null) {
            // Local Native
            // path to application under test (ContactManager)
            File app = new File(wrapper.USER_DIR + wrapper.AUT);
            wrapper.capabilities.setCapability(MobileCapabilityType.APP.toString(), app.getAbsolutePath());

        } else if(wrapper.SUT != null && wrapper.AUT == null && wrapper.APP_PACKAGE == null && wrapper.APP_ACTIVITY == null){
            // properties for Web, both for cloud and local runs
            wrapper.capabilities.setCapability(MobileCapabilityType.BROWSER_NAME.toString(), browserName);        }
          else{
              String msg = String.format("Unclear type of cloud mobile app: AUT=s%, APP_PACKAGE=%s, APP_ACTIVITY=%s, SUT=%s, DRIVER=%s",
                      wrapper.AUT, wrapper.APP_PACKAGE, wrapper.APP_ACTIVITY, wrapper.SUT, wrapper.DRIVER);
              throw new Exception(msg);
         }


        String dPath = wrapper.GetDriverPath();
        switch(wrapper.PLATFORM_NAME) {
            case "Android":
                driverSingle = new AndroidDriver(
                          new URL(dPath), wrapper.capabilities);
                break;
            case "iOS":
                driverSingle = new IOSDriver(
                        new URL(dPath), wrapper.capabilities);
                break;
        }
    }

    /**
     * Public getter for singleton instance
     * **/
    public static AppiumDriver driver() throws Exception
    {
        if(driverSingle == null) {
            System.out.println("DriverWrapper: creating driver from the scratch");
            prepareDriver();
        }
        else{
            System.out.println("DriverWrapper: getting already existing driver");
        }

        return driverSingle;
    }

    /**
     * Public getter for driver wait
     * **/
    public static WebDriverWait driverWait() throws Exception {
        if(waitSingle == null) waitSingle = new WebDriverWait(driver(), 10);

        return waitSingle;
    }

    //Begin:  Non-static stuff
    /**
     * Constructor, is expected to be called once due to singleton pattern
     * **/
    private DriverWrapper() throws IOException {
        getCurrentProps();
        AUT = getProp("aut");
        APP_PACKAGE = getProp("appPackage");
        APP_ACTIVITY = getProp("appActivity");
        String t_sut = getProp("sut");
        SUT = t_sut == null ? null : "http://" + t_sut;

        PLATFORM_NAME = getProp("platform");
        PLATFORM_VERSION = getProp("platformVersion");
        DRIVER = getProp("driver");
        USER_DIR = getProp("userDir");
        DEVICE = getProp("deviceName");
    }

    /**
     * Resolving driver path for local or cloud driver, depending on properties
     * **/
    private String GetDriverPath() throws IOException {
        String result = getProp("driver");
        if(result != null && result.length() > 0) {
            System.out.println("DriverPath is: " + result);
            return result;
        }
        else {
            result = format("http://%s:%s@%s/wd/hub", getProp("projectName"), getProp("apiKey"), getProp("appiumHub"));
            System.out.println("DriverPath is: "+ result);
            return result;
        }
    }
    // End: Non-static stuff
}
