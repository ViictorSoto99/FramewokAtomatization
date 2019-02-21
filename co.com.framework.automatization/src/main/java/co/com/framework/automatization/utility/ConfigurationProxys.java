package co.com.framework.automatization.utility;

import java.util.Properties;

public class ConfigurationProxys {
	
	//Constructor
	private ConfigurationProxys() {
		
	}
	
	//Configuraci�n de proxy para consumo de servicio
	public static void configProxyPragma() {
		String proxy = "hermes.pragma.com.co";
		String port = "8080";
		Properties systemProperties = System.getProperties();
		systemProperties.setProperty("https.proxyHost", proxy);
		systemProperties.setProperty("https.proxyPort", port);
	}
}

