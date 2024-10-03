package carcassonne.model.ai;

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
public class LoadTextRuleBasedAI {
    private HashMap<String, String> hmResult;

	public LoadTextRuleBasedAI() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBModel");
		hmResult = new HashMap<String, String>();

		hmResult.put("EMPTY_COLLECTION",rb.getString("RBA_EMPTY_COLLECTION.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

