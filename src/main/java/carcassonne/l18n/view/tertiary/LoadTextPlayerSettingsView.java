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
public class LoadTextPlayerSettingsView {
    private HashMap<String, String> hmResult;

	public  LoadTextPlayerSettingsView() {
		ResourceBundle rb;
		rb = ResourceBundle.getBundle("RBView");
		hmResult = new HashMap<String, String>();

		hmResult.put("AESTHETIC",					rb.getString("PSV_AESTHETIC.text"));
		hmResult.put("AESTHETIC_TOOL_TIP",			rb.getString("PSV_AESTHETIC_TOOL_TIP.text"));
		hmResult.put("HAND",						rb.getString("PSV_HAND.text"));
		hmResult.put("HAND_TOOL_TIP",				rb.getString("PSV_HAND_TOOL_TIP.text"));
		hmResult.put("MEEPLE_RULES",				rb.getString("PSV_MEEPLE_RULES.text"));
		hmResult.put("MEEPLE_RULES_TOOL_TIP",		rb.getString("PSV_MEEPLE_RULES_TOOL_TIP.text"));
		hmResult.put("FORTIFYING",					rb.getString("PSV_FORTIFYING.text"));
		hmResult.put("FORTIFYING_TOOL_TIP",			rb.getString("PSV_FORTIFYING_TOOL_TIP.text"));
		hmResult.put("ENCLAVE",						rb.getString("PSV_ENCLAVE.text"));
		hmResult.put("ENCLAVE_TOOL_TIP",			rb.getString("PSV_ENCLAVE_TOOL_TIP.text"));
		hmResult.put("SCORE_SPLITTING",				rb.getString("PSV_SCORE_SPLITTING.text"));
		hmResult.put("SCORE_SPLITTING_TOOL_TIP",	rb.getString("PSV_SCORE_SPLITTING_TOOL_TIP.text"));
		hmResult.put("MULTI_TILE",					rb.getString("PSV_MULTI_TILE.text"));
		hmResult.put("CLASSIC",						rb.getString("PSV_CLASSIC.text"));
		hmResult.put("CUSTOMIZE",					rb.getString("PSV_CUSTOMIZE.text"));
		hmResult.put("AI_PLAYER",					rb.getString("PSV_AI_PLAYER.text"));
		hmResult.put("PLAYERS",						rb.getString("PSV_PLAYERS.text"));
		hmResult.put("CLOSE",						rb.getString("PSV_CLOSE.text"));
		hmResult.put("TITLE",						rb.getString("PSV_TITLE.text"));
		hmResult.put("COLON",						rb.getString("PSV_COLON.text"));
		hmResult.put("PLAYER",						rb.getString("PSV_PLAYER.text"));
		hmResult.put("SPACE",						rb.getString("PSV_SPACE.text"));
	}

	public String get(String s) {
		return(hmResult.get(s));
	}
}

