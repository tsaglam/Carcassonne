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
public class LoadTextStateGameOver {
    private HashMap<String, String> hmResult;

	public LoadTextStateGameOver() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBControl");
		hmResult = new HashMap<String, String>();

		hmResult.put("GAME_OVER_MESSAGE",rb.getString("SGO_GAME_OVER_MESSAGE.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

