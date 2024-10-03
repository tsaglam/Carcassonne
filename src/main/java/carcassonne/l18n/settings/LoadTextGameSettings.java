package carcassonne.settings;

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
 * Load text for the menu bar for the main view.
 * @author Mitsugu Oyama
 */
public class LoadTextGameSettings {
	private HashMap<String, String> hmResult;

	public LoadTextGameSettings() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("YOU",		rb.getString("YOU.text"));
		hmResult.put("ALICE",	rb.getString("ALICE.text"));
		hmResult.put("BOB",		rb.getString("BOB.text"));
		hmResult.put("CAROL",	rb.getString("CAROL.text"));
		hmResult.put("DAN",		rb.getString("DAN.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}
