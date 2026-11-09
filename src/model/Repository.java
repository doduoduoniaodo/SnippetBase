package model;
/*
 * Author: Qianyi Ma
 * 
 * This class manages the collection of Snippets and tags and provides a searching method for the Search button
 */

import java.util.ArrayList;

public class Repository {
	
	// fields
	private ArrayList<Snippet> snippetList;
	private ArrayList<String> tagList;
	private int maxID;

	// constructor
	public Repository() {
		super();
		this.snippetList = new ArrayList<>();
		this.tagList = new ArrayList<>();
		this.maxID = 0;
	}
	
	// getter and setter 
	
	public ArrayList<Snippet> getSnippetList() {
		return snippetList;
	}

	public void setSnippetList(ArrayList<Snippet> snippetList) {
		this.snippetList = snippetList;
	}
	
	public ArrayList<String> getTagList() {
		return tagList;
	}

	public void setTagList(ArrayList<String> tagList) {
		this.tagList = tagList;
	}
	
	public int getMaxID() {
		return maxID;
	}

	public void setMaxID(int maxID) {
		this.maxID = maxID;
	}
	
	
	// utility method
	
	// Searches for Snippets based on a keyword in the title and returns a list of matching IDs
	public ArrayList<Integer> searchSnippet(String searchKeyword) {
		
		// create a new list to store the IDs of matched snippets
		ArrayList<Integer> result = new ArrayList<>();
		
		// loop through each snippet currently in the repository list
		for (Snippet s : snippetList) {
			
			// if the current snippet's title contains the provided search keyword, 
			// add the ID of this snippet to the result list
			if (s.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())) {
				result.add(s.getId());
			}
			
		}
		
		return result;
		
	}

	
}
