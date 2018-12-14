package dk.aau.cs.ds308e18.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GetProperties {
    public static Properties getProperties(String fileName) throws IOException {
        Properties properties = new Properties();

        String path = "resources/" + fileName + ".properties";

        FileInputStream in = new FileInputStream(path);
        properties.load(in);
        in.close();

        return properties;
    }
}