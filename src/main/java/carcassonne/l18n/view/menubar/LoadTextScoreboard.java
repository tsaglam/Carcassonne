package carcassonne.view.menubar;

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
public class LoadTextScoreboard {
    private HashMap<String, String> hmResult;

	public LoadTextScoreboard() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("SETTING_1",	rb.getString("SB_SETTING_1.text"));
		hmResult.put("SETTING_2",	rb.getString("SB_SETTING_2.text"));
		hmResult.put("POINTS",		rb.getString("SB_POINTS.text"));
		hmResult.put("MEEPLES",		rb.getString("SB_MEEPLES.text"));
		hmResult.put("STACK",		rb.getString("SB_STACK.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

