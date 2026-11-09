package model;
/*
 * Author: Qianyi Ma
 * 
 * This enum defines supported programming languages with their display names, 
 * associated syntax highlighting style constants for RSyntaxTextArea, and file extension names
 */

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public enum Language {
	
	// all language constants
	JAVA("Java", SyntaxConstants.SYNTAX_STYLE_JAVA, ".java"),
    PYTHON("Python", SyntaxConstants.SYNTAX_STYLE_PYTHON, ".py"),
    CPP("C++", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS, ".cpp"),
    C("C", SyntaxConstants.SYNTAX_STYLE_C, ".c"),
    JAVASCRIPT("JavaScript", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT, ".js"),
    CSHARP("C#", SyntaxConstants.SYNTAX_STYLE_CSHARP, ".cs"), 
    HTML("HTML", SyntaxConstants.SYNTAX_STYLE_HTML, ".html"),
    CSS("CSS", SyntaxConstants.SYNTAX_STYLE_CSS, ".css"),
    XML("XML", SyntaxConstants.SYNTAX_STYLE_XML, ".xml"),
    JSON("JSON", SyntaxConstants.SYNTAX_STYLE_JSON, ".json"),
    SQL("SQL", SyntaxConstants.SYNTAX_STYLE_SQL, ".sql"),
    PLAIN_TEXT("Plain Text", SyntaxConstants.SYNTAX_STYLE_NONE, ".txt");
	
	// fields
	private final String displayName;
	private final String syntaxStyle;
	private final String extension;
	
	// constructor
	Language(String displayName, String syntaxStyle, String extension){
		this.displayName = displayName;
		this.syntaxStyle = syntaxStyle;
		this.extension = extension;
	}

	// getters
	
	public String getDisplayName() {
		return displayName;
	}

	public String getSyntaxStyle() {
		return syntaxStyle;
	}
	
	public String getExtension() {
	    return extension;
	}
	
	// returns an array of all language's display names
	public static String[] getNames() {
		
		// get all enum values
		Language[] languages = Language.values();
		// create an array that stores for display names of all languages
		String[] names = new String[languages.length];
		
		// loop through every language constant and get their display names
		for (int i = 0; i < languages.length; i++) {
			names[i] = languages[i].getDisplayName();
		}
		
		return names;
		
	}
	
	// finds a language enum constant based on the display name
	public static Language fromDisplayName(String displayName) {
		
		// loop through all language enum constants and 
		// see if this language's display name is the same as the parameter
		for (Language language : Language.values()) {
			if (language.getDisplayName().equals(displayName)) {
				return language;
			}
		}
		
		return null;
		
	}
	
}
