package Setup;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.io.*;
import java.util.stream.Stream;

public class TestProperties {

    protected static String propertiesFileName = "test.properties";
    public static void SetPropertiesFile(String fileName){
        propertiesFileName = fileName;
    }

    Properties currentProps = new Properties();

    Properties getCurrentProps() throws IOException {

        String userDir = System.getProperty("user.dir");
        currentProps.setProperty("userDir", userDir);
        try {
            System.out.println("Loading properties from: " + propertiesFileName);
            FileInputStream in = new FileInputStream(userDir + "/src/main/resources/" + propertiesFileName);
            currentProps.load(in);
            in.close();
        }
        catch(IOException ex) {
            String msg = ex.getCause().getMessage();
            System.out.println(msg);
            throw ex;
        }
        return currentProps;
    }

    protected String getProp(String propKey) throws IOException {
        if (!currentProps.containsKey(propKey)) currentProps = getCurrentProps();

        // "default" form used to handle the absence of parameter
        return currentProps.getProperty(propKey, null);

    }
}

