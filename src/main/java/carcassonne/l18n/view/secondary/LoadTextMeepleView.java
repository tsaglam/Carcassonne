package carcassonne.view.secondary;

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
 * Load text for MeepleView.java
 * @author Mitsugu Oyama
 */
public class LoadTextMeepleView {
    private HashMap<String, String> hmResult;

	public LoadTextMeepleView() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("PLACE_MEEPEL", rb.getString("MV_PLACE_MEEPEL.text"));
		hmResult.put("BRACKET", rb.getString("MV_BRACKET.text"));
		hmResult.put("DONT_PLACE_MEEPLE", rb.getString("MV_DONT_PLACE_MEEPLE.text"));
		hmResult.put("CENTER", rb.getString("MV_CENTER.text"));
		hmResult.put("EAST", rb.getString("MV_EAST.text"));
		hmResult.put("WEST", rb.getString("MV_WEST.text"));
		hmResult.put("NORTH", rb.getString("MV_NORTH.text"));
		hmResult.put("SOUTH", rb.getString("MV_SOUTH.text"));
		hmResult.put("NORTH_EAST", rb.getString("MV_NORTH_EAST.text"));
		hmResult.put("NORTH_WEST", rb.getString("MV_NORTH_WEST.text"));
		hmResult.put("SOUTH_EAST", rb.getString("MV_SOUTH_EAST.text"));
		hmResult.put("SOUTH_WEST", rb.getString("MV_SOUTH_WEST.text"));
	}

	public String get(String key) {
		return " " + hmResult.get(key.toUpperCase().replaceAll(" ","_"));
	}
}

