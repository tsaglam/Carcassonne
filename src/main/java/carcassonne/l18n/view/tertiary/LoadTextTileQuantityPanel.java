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
public class LoadTextTileQuantityPanel {
    private HashMap<String, String> hmResult;

	public LoadTextTileQuantityPanel() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("CLICK_TO_ROTATE",rb.getString("TQP_CLICK_TO_ROTATE.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

