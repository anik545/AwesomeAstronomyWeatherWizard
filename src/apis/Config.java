package apis;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Config {
	Properties configFile;

	public static void main(String[] args) {
		Config conf = new Config();
		String key = conf.getProperty("WEATHER_KEY");
	}


	public Config() {
		configFile = new java.util.Properties();
		try {
            System.out.println(new File("./res/config.properties").getCanonicalPath());
            File file = new File("./res/config.properties");
            FileInputStream f  = new FileInputStream(file);
			configFile.load(f);
			System.out.println(configFile);
		} catch (Exception eta){
			eta.printStackTrace();
		}
	}

	public String getProperty(String key){
		String value = this.configFile.getProperty(key);
		return value;
   }
}