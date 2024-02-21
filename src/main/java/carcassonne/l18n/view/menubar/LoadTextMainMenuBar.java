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
 * Load text for the menu bar for the main view.
 * @author Mitsugu Oyama
 */
public class LoadTextMainMenuBar {
	private HashMap<String, String> hmResult;

	public LoadTextMainMenuBar() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("DISTRIBUTION",	rb.getString("MI_DISTRIBUTION.text"));
		hmResult.put("GRID_SIZE",		rb.getString("MI_GRID_SIZE.text"));
		hmResult.put("ABORT",			rb.getString("MI_ABORT.text"));
		hmResult.put("GAME",			rb.getString("MI_GAME.text"));
		hmResult.put("NEW_ROUND",		rb.getString("MI_NEW_ROUND.text"));
		hmResult.put("OPTIONS",			rb.getString("MI_OPTIONS.text"));
		hmResult.put("PLAYER_SETTINGS",	rb.getString("MI_PLAYER_SETTINGS.text"));
		hmResult.put("VIEW",			rb.getString("MI_VIEW.text"));
		hmResult.put("ABOUT",			rb.getString("MI_ABOUT.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}
