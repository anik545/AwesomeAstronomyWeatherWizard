package apis;

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
			configFile.load(this.getClass().getClassLoader().getResourceAsStream("config.cfg"));
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