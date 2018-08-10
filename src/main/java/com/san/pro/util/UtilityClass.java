package com.san.pro.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class UtilityClass {
	
	public static PropertiesConfiguration getConfigProperties() {

		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration("application.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return config;
	}
	
	public String getDateInFormat(long inMillis) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(inMillis);
		return formatter.format(date);
	}

	public void updateProperties(long startDate) {

		try {
			PropertiesConfiguration config = new PropertiesConfiguration("application.properties");
			config.setProperty("date", getDateInFormat(startDate));
			config.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
}
