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
 * Load text for zoom slider.
 * @author Mitsugu Oyama
 */
public class LoadTextTileView {
    private HashMap<String, String> hmResult;

	public LoadTextTileView() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("NO_TILE_TO_SELECT",rb.getString("TV_NO_TILE_TO_SELECT.text"));
		hmResult.put("TOOL_TIP",rb.getString("TV_TOOL_TIP.text"));
		hmResult.put("SKIP_TURN",rb.getString("TV_SKIP_TURN.text"));
		hmResult.put("ROTATE_LEFT",rb.getString("TV_ROTATE_LEFT.text"));
		hmResult.put("ROTATE_RIGHT",rb.getString("TV_ROTATE_RIGHT.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

