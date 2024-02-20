package carcassonne.view.util;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Locale;
import java.io.File;
import java.nio.file.Paths;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;

/**
 * Load text for Game Message (about dialogbox).
 * @author Mitsugu Oyama
 */
public class LoadTextGameMessage {
    private HashMap<String, String> hmResult;

	public LoadTextGameMessage() {
			ResourceBundle rb;
			rb = ResourceBundle.getBundle("RBView");
			hmResult = new HashMap<String, String>();

			hmResult.put("ABOUT", rb.getString("GML_ABOUT.text"));
			hmResult.put("TITLE", rb.getString("GML_TITLE.text"));
	}
	public String get(String s) {
		return(hmResult.get(s));
	}
}
