package Setup;

/**
 * Mobile capabilities enum. Does not include remote capabilities for mobile farm
 * **/
public enum MobileCapabilityType {
    DEVICE_NAME("deviceName"),
    BROWSER_NAME("browserName") ,
    PLATFORM_NAME("platformName"),
    PLATFORM_VERSION("platformVersion"),
    APP("app");

    private final String name;

    MobileCapabilityType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
