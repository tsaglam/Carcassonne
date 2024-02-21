package carcassonne.l18n.view.util;

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
 * Convert text for L18N.
 * @author Mitsugu Oyama
 */
public class ConvStringL18n {
    private HashMap<String, String> hmResult;

	/**
	*
	* @param basename - the base name of the resource bundle, a fully qualified class name.
	* @param key - the key for the desired string
	*/
	public ConvStringL18n(String baseName, String key) {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle(baseName);
		hmResult = new HashMap<String, String>();

		hmResult.put(key,rb.getString(key));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

