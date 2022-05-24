//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package utility;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

public class PropertiesLoader {
    public static final String DEFAULT_DIRECTORY_PATH = "properties/";

    public static Properties loadPropertiesFile(String propertiesFile) {
        FileInputStream input;
        Properties prop;
        if (propertiesFile == null) {
            try {
                input = new FileInputStream("properties/runmode.properties");

                try {
                    prop = new Properties();
                    prop.load(input);
                    propertiesFile = "properties/" + prop.getProperty("current_mode");
                    System.out.println(propertiesFile);
                } catch (Throwable var8) {
                    try {
                        input.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }

                    throw var8;
                }

                input.close();
            } catch (IOException var9) {
                var9.printStackTrace();
            }
        }

        try {
            input = new FileInputStream(propertiesFile);

            Properties var3;
            try {
                prop = new Properties();
                prop.load(input);
                var3 = prop;
            } catch (Throwable var5) {
                try {
                    input.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            input.close();
            return var3;
        } catch (IOException var6) {
            var6.printStackTrace();
            return null;
        }
    }
}
