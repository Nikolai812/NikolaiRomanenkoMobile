package Setup;

public enum MobileCapabilityType {
    DEVICE_NAME("deviceName"),
    BROWSER_NAME("browserName") ,
    PLATFORM_NAME("platformName"),
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
