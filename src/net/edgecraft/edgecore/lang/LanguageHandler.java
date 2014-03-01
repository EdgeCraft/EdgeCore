package net.edgecraft.edgecore.lang;

import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.ChatColor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LanguageHandler {
	
	private static String defaultLanguage;
	
	protected static final LanguageHandler instance = new LanguageHandler();
	
	protected LanguageHandler() { /* ... */ }
	
	public static final LanguageHandler getInstance() {
		return instance;
	}
	
	
	/**
	 * Returns the default language.
	 * 
	 * @return String
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
	 * @return String
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
	 * @return String
	 */
	public String getRawMessage(String lang, String element, String messageKey) {
		
		try {
	    	
	      DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
	      DocumentBuilder builder = fac.newDocumentBuilder();
	      fac.setNamespaceAware(false);
	      
	      InputStream stream = getClass().getClassLoader().getResourceAsStream("languages/lang_" + lang + ".xml");
	      
	      Document doc = builder.parse(stream);
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
		try {
			
			return new JarEntry(new JarFile("EdgeCore.jar").getJarEntry("languages/lang_" + language + ".xml")) == null;
			
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
