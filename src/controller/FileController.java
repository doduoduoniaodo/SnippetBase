package controller;
/*
 * Author: Qianyi Ma
 * 
 * This class manages file I/O operations for the program
 * It loads and saves repository data and exports code to files
 */

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.Language;
import model.Repository;
import model.Snippet;

public class FileController {
	
	// fields
	private Repository repository;
	private static final String FILEPATH = "file/snippetBaseData.txt";
	
	// constructor
	public FileController() {
		this.repository = new Repository();
		loadData();
	}
	
	// saes the current repository state to the txt data file
	public void saveData() {
		
		// open a writer to the file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILEPATH))) {
			
			// write the number of tags
			writer.write(repository.getTagList().size() + "\n");
			
			// write each tag
			for (String tag : repository.getTagList()) {
				writer.write(tag + "\n");
			}
			
			// write the number of snippets
			writer.write(repository.getSnippetList().size() + "\n");
			
			// loop through the snippet and write the data
			for (Snippet s : repository.getSnippetList()) {
				
				writer.write(s.getTitle() + "\n");	// write its title
				writer.write(s.getLanguage().getDisplayName() + "\n");	// write its language
				for (String tag : s.getTags()) {
					writer.write(tag + ",");	// write the its tags, separated by a comma
				}
				writer.write("\n" + s.getDate() + "\n");	// write its date
				writer.write(s.getCode() + "\n");	// write its code body
				writer.write("[SPLIT]\n");	// write a snippet separator that indicates this snippet's data is ended
				
			}
			
			// write a marker that indicates that the file is ended
			writer.write("[END]");
			
		// handle file saving errors
		} catch (IOException e) {
			System.out.println("Failed to save the repository");
		}
	}
	
	// loads repository data from the txt data file
	public void loadData(){
		
		// open a reader to the file
		try (BufferedReader reader = new BufferedReader(new FileReader(FILEPATH))) {
			
			// get the number of tags
			int numTag = Integer.parseInt(reader.readLine());
			// add tags to the tag list
			for (int i = 0; i < numTag; i++) {
				repository.getTagList().add(reader.readLine());
			}
			
			// get the number of snippets
			int numSnippet = Integer.parseInt(reader.readLine());
			// set the max ID to the number of the snippets
			repository.setMaxID(numSnippet);
			// set the ID counter to 1
			int currentID = 1;
			
			// iterate to load each snippet
			for (int i = 0; i < numSnippet; i++) {
				
				int ID = currentID;	// set its ID to the current ID
				currentID++;	// increment the ID counter
				String title = reader.readLine();	// read the title
				Language language = Language.fromDisplayName(reader.readLine());	// read the language
				
				// read the tags and split it by the comma
				String tagLine = reader.readLine().trim();
				String[] tags = tagLine.split(",");
				ArrayList<String> tagList = new ArrayList<>(Arrays.asList(tags));
				
				String date = reader.readLine();	// read the date
				
				// read the code until this line is the separator
				String code = "";
				String codeLine;
				while(! (codeLine = reader.readLine()).equals("[SPLIT]")) {
					if (codeLine.isEmpty())
						code += "\n";
					else
						code += codeLine + "\n";
				}
				// remove the trailing newline (\n)
				if (code.length() > 2) {
					code = code.substring(0, code.length() - 1);
				}
				
				// create and add snippet to repository
				repository.getSnippetList().add(new Snippet(ID, title, tagList, language, code, date));
				
			}
			
			// check for the end marker
			if (reader.readLine().equals("[END]"))
				System.out.println("Successfully loaded the repository");
			else
				System.out.println("Failed to load the repository ([END] issue)");
		
		// handle file loading errors
        } catch (IOException e) {
            System.err.println("Failed to load the repository");
        }
	}
	
	// exports code content to a code file
	public void saveCodeAs(String code, String extension) {
		
		// open a file dialog
		JFileChooser fileChooser = new JFileChooser(); 
		fileChooser.setDialogTitle("Save As");
		
		// show the dialog
		int userSelection = fileChooser.showSaveDialog(null);
		
		// if user approved the selection
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			// get the selected file
			File fileToSave = fileChooser.getSelectedFile();
			
			// ensure file has correct extension
			String filePath = fileToSave.getAbsolutePath();
			if (!filePath.toLowerCase().endsWith(extension)) {
				fileToSave = new File(filePath + extension);
			}
			
			// write code content to the file
			try (FileWriter writer = new FileWriter(fileToSave)) {
				writer.write(code);
				
				// show success message
				JLabel message = new JLabel("Code file saved successfully: " + fileToSave.getAbsolutePath());
				message.setFont(new Font("Consolas", Font.BOLD, 18));
				JOptionPane.showMessageDialog(null, message, 
						"Success", JOptionPane.INFORMATION_MESSAGE);
			
			// handle write errors 
			} catch (IOException e) {
				System.out.println("Failed to save code file: " + e.getMessage());
			}
		}
	}
	
	// getters and setters
	
	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	
}
