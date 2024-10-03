package carcassonne;

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
 * Load text for Carcassonne.java
 * @author Mitsugu Oyama
 */
public class LoadTextCarcassonne {
	private HashMap<String, String> hmResult;

	public LoadTextCarcassonne() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBCarcassonne");
		hmResult = new HashMap<String, String>();

		hmResult.put("LOOK_AND_FEEL_ERROR",rb.getString("C_LOOK_AND_FEEL_ERROR.text"));
		hmResult.put("CLOSING_BRACKET",rb.getString("C_CLOSING_BRACKET.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}
