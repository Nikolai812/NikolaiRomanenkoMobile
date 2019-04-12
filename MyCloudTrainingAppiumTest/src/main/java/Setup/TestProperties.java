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

    /**
     * Reads properties from property file
     * @return
     * @throws IOException
     */
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

    /**
     * Getter for a property value. Does not reread properties again from the source if the requested value is absent
     * @param propKey
     * @return
     * @throws IOException
     */
    protected String getProp(String propKey) throws IOException {

        // "default" form used to handle the absence of parameter
        return currentProps.getProperty(propKey, null);

    }
}

