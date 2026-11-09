package model;
/*
 * Author: Qianyi Ma
 * 
 * This class is a data model that represents a code snippet with its fields (id, title, tags, language, code, date)
 */

import java.time.LocalDate;
import java.util.ArrayList;

public class Snippet {
	
	// fields
	private int id;
	private String title;
	private ArrayList<String> tags;
	private Language language;
	private String code;
	private String date;
	
	// constructors
	
	public Snippet(int id, String title, ArrayList<String> tags, Language language, String code, LocalDate date) {
		super();
		this.id = id;
		this.title = title;
		this.tags = tags;
		this.language = language;
		this.code = code;
		this.date = date.toString();
	}
	
	public Snippet(int id, String title, ArrayList<String> tags, Language language, String code, String date) {
		super();
		this.id = id;
		this.title = title;
		this.tags = tags;
		this.language = language;
		this.code = code;
		this.date = date;
	}

	// getters and setters
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	// string representation
	@Override
	public String toString() {
		return "Snippet [id=" + id + ", title=" + title + ", tags=" + tags + ", language=" + language + ", code=" + code
				+ ", date=" + date + "]";
	}
	
}
