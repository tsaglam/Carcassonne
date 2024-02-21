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
public class LoadTextPlayerEstheticsView {
    private HashMap<String, String> hmResult;

	public LoadTextPlayerEstheticsView() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("CHANGE_COLOR",	rb.getString("PSV_CHANGE_COLOR.text"));
		hmResult.put("EMPTY_NAME",		rb.getString("PSV_EMPTY_NAME.text"));
		hmResult.put("ACCEPT_CHANGES",	rb.getString("PSV_ACCEPT_CHANGES.text"));
		hmResult.put("CHANGE_NAME",		rb.getString("PSV_CHANGE_NAME.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

