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
public class LoadTextGridSizeDialog {
    private HashMap<String, String> hmResult;

	public LoadTextGridSizeDialog() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("BRACKET",rb.getString("GSD_BRACKET.text"));
		hmResult.put("TOTAL_TILES",rb.getString("GSD_TOTAL_TILES.text"));
		hmResult.put("INVALID_SIZE",rb.getString("GSD_INVALID_SIZE.text"));
		hmResult.put("CROSS",rb.getString("GSD_CROSS.text"));
		hmResult.put("TITLE",rb.getString("GSD_TITLE.text"));
		hmResult.put("WIDTH",rb.getString("GSD_WIDTH.text"));
		hmResult.put("HEIGHT",rb.getString("GSD_HEIGHT.text"));
		hmResult.put("NOT_CORRECT",rb.getString("GSD_NOT_CORRECT.text"));
		hmResult.put("MESSAGE",rb.getString("GSD_MESSAGE.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}


