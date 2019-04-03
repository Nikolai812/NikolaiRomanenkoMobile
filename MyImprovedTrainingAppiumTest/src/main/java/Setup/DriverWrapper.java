package Setup;

import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.*;
import io.appium.java_client.AppiumDriver;

import java.io.*;
import java.net.URL;


public class DriverWrapper extends TestProperties {
    protected static AppiumDriver driverSingle;
    protected static WebDriverWait waitSingle;

    protected DesiredCapabilities capabilities;

    // Properties to be read
    protected String USER_DIR;
    protected String AUT; // (mobile) app under testing
    protected String SUT; // site under testing
    protected String TEST_PLATFORM;
    protected String DRIVER;
    protected String DEVICE;

    private static DriverWrapper  wrapperInstance;
    private static DriverWrapper getWrapperInstance() throws IOException{
        if (wrapperInstance == null) {
            wrapperInstance = new DriverWrapper();
        }

        return wrapperInstance;
    };

    private DriverWrapper() throws IOException {
        AUT = getProp("aut");
        String t_sut = getProp("sut");
        SUT = t_sut == null ? null : "http://" + t_sut;

        TEST_PLATFORM = getProp("platform");
        DRIVER = getProp("driver");
        USER_DIR = getProp("userDir");
        DEVICE = getProp("deviceName");
    }

    private static void prepareDriver() throws Exception {
        DriverWrapper wrapper = getWrapperInstance();
        wrapper.capabilities = new DesiredCapabilities();

        String browserName;       // Setup test platform: Android or iOS. Browser also depends on a platform.
        switch(wrapper.TEST_PLATFORM) {
              case "Android":
                  wrapper.capabilities.setCapability(MobileCapabilityType.DEVICE_NAME.toString(), wrapper.DEVICE);
                  browserName = "Chrome";
                   break;
              case "iOS":
                  browserName = "Safari";
                  break;
              default: throw new Exception("Unknown mobile platform");

               }
        wrapper.capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME.toString(), wrapper.TEST_PLATFORM);

        // Setup type of application: mobile, web (or hybrid)
        if(wrapper.AUT != null && wrapper.SUT == null){
            // Native
            // path to application under test (ContactManager)
            File app = new File(wrapper.USER_DIR + wrapper.AUT);
            wrapper.capabilities.setCapability(MobileCapabilityType.APP.toString(), app.getAbsolutePath());
        } else if(wrapper.SUT != null && wrapper.AUT == null){
            // Web
            wrapper.capabilities.setCapability(MobileCapabilityType.BROWSER_NAME.toString(), browserName);        }
          else{
              throw new Exception("Unclear type of mobile app");
         }


        // Init driverSingle for local Appium server with capabilities have been set
         driverSingle = new AppiumDriver(new URL(wrapper.DRIVER), wrapper.capabilities);
    }

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

    public static WebDriverWait driverWait() throws Exception {
        if(waitSingle == null) waitSingle = new WebDriverWait(driver(), 10);

        return waitSingle;
    }
}
