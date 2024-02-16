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
public class LoadTextTileDistributionView {
    private HashMap<String, String> hmResult;

	public LoadTextTileDistributionView() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("MULTIPLIER",	rb.getString("TDV_MULTIPLIER.text"));
		hmResult.put("BRACKET",		rb.getString("TDV_BRACKET.text"));
		hmResult.put("STACK_SIZE",	rb.getString("TDV_STACK_SIZE.text"));
		hmResult.put("SHUFFLE",		rb.getString("TDV_SHUFFLE.text"));
		hmResult.put("RESET",		rb.getString("TDV_RESET.text"));
		hmResult.put("ACCEPT",		rb.getString("TDV_ACCEPT.text"));
		hmResult.put("TITLE",		rb.getString("TDV_TITLE.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

