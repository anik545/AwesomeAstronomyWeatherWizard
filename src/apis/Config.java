package apis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
			configFile.load(ClassLoader.getSystemResourceAsStream("config.properties"));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public String getProperty(String key){
		String value = this.configFile.getProperty(key);
		return value;
   }
}