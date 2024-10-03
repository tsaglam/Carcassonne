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
public class LoadTextZoomSlider {
    private HashMap<String, String> hmResult;

	public LoadTextZoomSlider() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("ZOOM_OUT",	rb.getString("ZOOM_OUT.text"));
		hmResult.put("ZOOM_IN",		rb.getString("ZOOM_IN.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}
