package carcassonne.control.state;

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
public class LoadTextStateManning {
    private HashMap<String, String> hmResult;

	public LoadTextStateManning() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBControl");
		hmResult = new HashMap<String, String>();

		hmResult.put("ABORT",rb.getString("SM_ABORT.text"));
		hmResult.put("CANT_PLACE",rb.getString("SM_CANT_PLACE.text"));
		hmResult.put("NO_MEEPLE",rb.getString("SM_NO_MEEPLE.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

