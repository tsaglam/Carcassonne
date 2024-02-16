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
public class LoadTextAbstractGameState {
    private HashMap<String, String> hmResult;

	public LoadTextAbstractGameState() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBControl");
		hmResult = new HashMap<String, String>();

		hmResult.put("NO_MOVE",rb.getString("AGS_NO_MOVE.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

