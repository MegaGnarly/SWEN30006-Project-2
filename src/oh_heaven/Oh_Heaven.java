package oh_heaven;

// Oh_Heaven.java
import utility.PropertiesLoader;

import java.util.Properties;

@SuppressWarnings("serial")
public class Oh_Heaven {

  public static void main(String[] args)
  {
	// System.out.println("Working Directory = " + System.getProperty("user.dir"));
	final Properties properties;
	if (args == null || args.length == 0) {
		properties = PropertiesLoader.loadPropertiesFile(null);
	} else {
		properties = PropertiesLoader.loadPropertiesFile(args[0]);
	}
    new Game(properties);
  }

}
