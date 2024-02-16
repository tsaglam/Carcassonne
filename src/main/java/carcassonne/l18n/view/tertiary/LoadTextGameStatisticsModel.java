package carcassonne.view.tertiary;

import java.lang.System;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Locale;
import java.io.File;
import java.nio.file.Paths;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;

/**
 * Load text for zoom slider.
 * @author Mitsugu Oyama
 */
public class LoadTextGameStatisticsModel {
    private HashMap<String, String> hmResult;

	public LoadTextGameStatisticsModel() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("PLAYER",		rb.getString("GSM_PLAYER.text"));
		hmResult.put("CASTLE",		rb.getString("GSM_CASTLE.text"));
		hmResult.put("ROAD",		rb.getString("GSM_ROAD.text"));
		hmResult.put("MONASTERY",	rb.getString("GSM_MONASTERY.text"));
		hmResult.put("FIELD",		rb.getString("GSM_FIELD.text"));
		hmResult.put("SCORE",		rb.getString("GSM_SCORE.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}


