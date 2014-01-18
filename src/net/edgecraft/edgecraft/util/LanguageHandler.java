package net.edgecraft.edgecraft.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.ChatColor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LanguageHandler {
	
	private static String defaultLanguage;
	

	/**
	 * Returns the default language.
	 * 
	 * @return
	 */
	public static String getDefaultLanguage() {
		return LanguageHandler.defaultLanguage;
	}

	/**
	 * Sets the default language.
	 * 
	 * @param defaultLanguage
	 */
	public static void setDefaultLanguage( String defaultLanguage ) {
		if( defaultLanguage != null )	
			LanguageHandler.defaultLanguage = defaultLanguage;
	}

	/**
	 * Returns a colored message in the specified language (if available) with the specified message-key.
	 * 
	 * @param lang
	 * @param messageKey
	 * @return
	 */
	public String getColoredMessage(String lang, String messageKey) {
		return ChatColor.translateAlternateColorCodes('@', getRawMessage(lang, "message", messageKey));
	}
	
	/**
	 * Returns a raw message in the specified language (if available) with the given message-key.
	 * 
	 * @param lang
	 * @param element
	 * @param messageKey
	 * @return
	 */
	public String getRawMessage(String lang, String element, String messageKey) {
		
		try {
	    	
	      DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
	      DocumentBuilder builder = fac.newDocumentBuilder();
	      fac.setNamespaceAware(false);

	      Document doc = builder.parse("plugins/EdgeCraft/languages/lang_" + lang + ".xml");
	      NodeList messages = doc.getElementsByTagName(element);

	      for (int i = 0; i < messages.getLength(); i++) {
	    	  
	        Node current = messages.item(i);
	        Node key = current.getAttributes().getNamedItem("key");

	        if ((key != null) && (messageKey.equals(key.getNodeValue()))) {
	          
	          return current.getAttributes().getNamedItem("value").getNodeValue().replace("[nl]", "\n");	     
	          
	        }
	      }
	    }
	    
	    catch (Exception e) {
	      e.printStackTrace();
	    }

	    return "@cCouldn't find phrase for @6" + messageKey + " @cin @6" + lang + "@c!";		
	}
	
	/**
	 * Checks whether the given language is available or not.
	 * 
	 * @param language
	 * @return true/false
	 */
	public boolean exists(String language) {
	    return new File("plugins/EdgeCraft/languages/lang_" + language + ".xml").exists();		
	}
}
