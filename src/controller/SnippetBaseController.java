package controller;
/*
 * Author: Qianyi Ma
 * 
 * This class is the main controller that handles the program logic, and 
 * links the models and views by managing user action events
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Language;
import model.Repository;
import model.Snippet;
import view.MainFrame;

public class SnippetBaseController implements ActionListener, TreeSelectionListener, WindowListener, ListSelectionListener {
	
	// fields
	private MainFrame frame;
	private FileController fileController;
	private Repository repository;
	private int currentSnippetID;
	private ArrayList<Snippet> currentlyDisplayedSnippets;
	private DefaultMutableTreeNode currentSelectedNode;
	
	// constructor
	public SnippetBaseController(FileController fileController, Repository repository) {
		
		// initialize the fields
		this.fileController = fileController;
		this.repository = repository;
		this.currentSnippetID = -1;
		this.currentlyDisplayedSnippets = new ArrayList<>(repository.getSnippetList());
		
		// initialize the frame / application window
		frame = new MainFrame(repository);
		// display the frame
		frame.setVisible(true);
		// add window listener for the frame
		frame.addWindowListener(this);
		
		// add action listener to all the buttons in the view classes
		frame.getNewSnippetButton().addActionListener(this);
		frame.getNavigationPanel().getSearchButton().addActionListener(this);
		frame.getDetailPanel().getSaveChangesButton().addActionListener(this);
		frame.getDetailPanel().getSaveAsButton().addActionListener(this);
		frame.getDetailPanel().getCopyCodeButton().addActionListener(this);
		frame.getDetailPanel().getDeleteButton().addActionListener(this);
		
		// add the action listener to the dropdown combo box
		frame.getDetailPanel().getLanguageSelectionDropdown().addActionListener(this);
		
		// add the tree seelction listener to the navigation tree
		frame.getNavigationPanel().getTree().addTreeSelectionListener(this);
		
		// add the list selection listener to the table
		//https://stackoverflow.com/questions/15906001/jtable-selection-listener
		frame.getListingPanel().getTable().getSelectionModel().addListSelectionListener(this);
		
		// set the default tree selection to the "All Snippets" node
		JTree tree = frame.getNavigationPanel().getTree();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		DefaultMutableTreeNode allSnippetsNode = (DefaultMutableTreeNode) root.getChildAt(0);
		this.currentSelectedNode = allSnippetsNode;
		
	}
	
	// copies the code string to the system clipboard
	// https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
	public void copyCodeToClipboard(String code) {
		StringSelection stringSelection = new StringSelection(code);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
	
	// handles action events from buttons and the combo box
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == frame.getNewSnippetButton()) {
			newSnippetButtonHandler();
		}
		 
		else if (e.getSource() == frame.getNavigationPanel().getSearchButton()) {
			searchButtonHandler();
		}
		
		else if (e.getSource() == frame.getDetailPanel().getSaveChangesButton()) {
			saveChangesButtonHandler();
		}
		
		else if (e.getSource() == frame.getDetailPanel().getSaveAsButton()) {
			saveAsButtonHandler();
		}
		
		else if (e.getSource() == frame.getDetailPanel().getCopyCodeButton()) {
			copyCodeButtonHandler();
		}
		
		else if (e.getSource() == frame.getDetailPanel().getDeleteButton()) {
			deleteButtonHandler();
		}
		
		else if (e.getSource() == frame.getDetailPanel().getLanguageSelectionDropdown()) {
			languageSelectionDropdownHandler();
		}
	}
	
	// handles the "Create a New Snippet" button
	private void newSnippetButtonHandler() {
		
		// reset all the input components in the detail panel
		frame.getDetailPanel().resetInputComponents();
		
		// deselect the table
		frame.getListingPanel().getTable().clearSelection();
		
		// reset the current snippet ID
		this.currentSnippetID = -1;
		
	}
	
	// handles the "Search" button
	private void searchButtonHandler() {
		
		// get the user's input
		String searchKeyword = frame.getNavigationPanel().getSearchBar().getText().trim();
		// get the list of snippets' ID that contains the keyword in their title
		ArrayList<Integer> searchResult = repository.searchSnippet(searchKeyword);
		
		// create a list for the new table data
		ArrayList<String[]> newRowData = new ArrayList<>();
		
		// clear the current displayed snippets list
		currentlyDisplayedSnippets.clear();
		
		// filter based on the search, iterate the snippets
		for (Snippet s : repository.getSnippetList()) {
			
			// if the current snippet's ID is in the search result list, 
			// then add its data to the new row data list and the currently displayed snippets list
			if (searchResult.contains(s.getId())) {
				newRowData.add(new String[] {s.getTitle(), s.getLanguage().getDisplayName(), 
						String.join(",", s.getTags()), s.getDate().toString()});
				currentlyDisplayedSnippets.add(s);
			}
		}
		
		// update the table in the listing panel
		frame.getListingPanel().updateTable(newRowData.toArray(new String[0][]));
		
	}
	
	// handles the "Save Changes" button
	private void saveChangesButtonHandler() {
		
		// get the title string from the input text field
		String title = frame.getDetailPanel().getTitleInputField().getText().trim();
		// if it is empty, then show an error message and end the process
		if (title.isEmpty()) {
			JLabel message = new JLabel("Title cannot be empty. ");
			message.setFont(new Font("Consolas", Font.BOLD, 18));
			JOptionPane.showMessageDialog(null, message, 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// check for duplicate title
		boolean isDuplicate = false;
		// loop through each snippets in the repository
		for (Snippet s : repository.getSnippetList()) {
			
			// if there is a existing snippet with the title that the user input, 
			// then set the boolean flag to true and then end the loop
			if (s.getTitle().equalsIgnoreCase(title) && s.getId() != currentSnippetID) {
				isDuplicate = true;
				break;
			}
		}
		
		// if the duplicate boolean flag is true (there is a duplicate title), 
		// then show the error message
		if (isDuplicate) {
			JLabel message = new JLabel("A snippet with this title already exists. ");
			message.setFont(new Font("Consolas", Font.BOLD, 18));
			JOptionPane.showMessageDialog(null, message, 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// get the tag string from the input field
		String tagsStr = frame.getDetailPanel().getTagsInputField().getText().trim();
		// if it is empty, then show an error message and end the process
		if (tagsStr.isEmpty()) {
			JLabel message = new JLabel("Tags cannot be empty. ");
			message.setFont(new Font("Consolas", Font.BOLD, 18));
			JOptionPane.showMessageDialog(null, message, 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// split the tag string by comma
		String[] tags = tagsStr.split(",");
		// convert the array to an ArrayList
		ArrayList<String> tagList = new ArrayList<>(Arrays.asList(tags));
		
		// get the language that the user selected in the combo box
		Language language = Language.fromDisplayName((String) frame.getDetailPanel().getLanguageSelectionDropdown().getSelectedItem());
		
		// get the code in the code text area
		String code = frame.getDetailPanel().getCodeTextArea().getText();
		
		// if this is a new snippet (the user is creating a new snippet, not modifying an old snippet)
		if (currentSnippetID == -1) {
			
			// create the new Snippet object and add it to the repository
			Snippet newSnippet = new Snippet(repository.getMaxID() + 1, title, tagList, language, code, LocalDate.now());
			repository.getSnippetList().add(newSnippet);
			// set its ID
			repository.setMaxID(repository.getMaxID() + 1);
			this.currentSnippetID = newSnippet.getId();
			
			// update the table if the new snippet belongs to the current selected table view 
			// https://stackoverflow.com/questions/4240074/jtree-how-to-get-the-text-of-selected-item?noredirect=1&lq=1
			
			// get the current selected tree node
			DefaultMutableTreeNode selectedNode = this.currentSelectedNode;
			
			if (selectedNode != null) {
				
				// get the text of the selected tree node
				String nodeValue = selectedNode.getUserObject().toString();
				// create a boolean flag
				boolean shouldUpdate = false;
				
				// if this new snippet belongs to the current selected tree node category, 
				// then set the boolean flag to true
				if (newSnippet.getLanguage().getDisplayName().equals(nodeValue) || 
						newSnippet.getTags().contains(nodeValue) || nodeValue.equals("All Snippets")) {
					shouldUpdate = true;
				}
				
				// if the boolean flag is true, then we should update the table
				if (shouldUpdate) {
					
					// add this snippet to the currently displayed snippet list
					currentlyDisplayedSnippets.add(newSnippet);
					
					// create a list for the new row data for the table
					ArrayList<String[]> newRowData = new ArrayList<>();
					// loop through every snippet in the currently displayed snippet list, 
					// add their data to the new row data list
					for (Snippet s : currentlyDisplayedSnippets) {
						newRowData.add(new String[] {s.getTitle(), s.getLanguage().getDisplayName(), 
		                        String.join(",", s.getTags()), s.getDate().toString()});
					}
					
					// update the table in the listing panel
					frame.getListingPanel().updateTable(newRowData.toArray(new String[0][]));
					
					// make this new row selected in the table
					int newRowIndex = currentlyDisplayedSnippets.size() - 1;
					if (newRowIndex >= 0) {
						frame.getListingPanel().getTable().setRowSelectionInterval(newRowIndex, newRowIndex);
					}
					
					// scroll the table to the bottom
					// https://stackoverflow.com/questions/5147768/scroll-jscrollpane-to-bottom
					JScrollBar scrollBar = frame.getListingPanel().getScrollPane().getVerticalScrollBar();
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							scrollBar.setValue(scrollBar.getMaximum());
						}
					});
					
					// repaint the GUI
					frame.repaint();
					
				}
			}
		}
		
		// otherwise, update the existing snippet
		else {
			
			// loop through the snippet list
			for (Snippet s : repository.getSnippetList()) {
				
				// if this is the snippet that the user modified
				if (s.getId() == currentSnippetID) {
					
					// update its fields
					s.setTitle(title);
					s.setTags(tagList);
					s.setLanguage(language);
					s.setCode(code);
					s.setDate(LocalDate.now().toString());
					break;
					
				}	
			}
		}
		
		// get the currently selected row number
		int selectedRow = frame.getListingPanel().getTable().getSelectedRow();
		
		// create a list for the new row data for the table
		ArrayList<String []> newRowData = new ArrayList<>();
		
		// loop through the currently displayed snippet list
		for (Snippet s : currentlyDisplayedSnippets) {
			// add this snippet's data to the new row data list
			newRowData.add(new String[] {s.getTitle(), s.getLanguage().getDisplayName(), 
					String.join(",", s.getTags()), s.getDate().toString()});
		}
		
		// update the table in the listing panel
		frame.getListingPanel().updateTable(newRowData.toArray(new String[0][]));
		
		// restore the row selection
		if (selectedRow != -1 && selectedRow < frame.getListingPanel().getTable().getRowCount())
			frame.getListingPanel().getTable().setRowSelectionInterval(selectedRow, selectedRow);
		
		// loop through the tag list
		for (String tag : tagList) {
			
			// if this tag is not in the repository's tag list, 
			// then add it to the list
			if (! repository.getTagList().contains(tag)) {
				repository.getTagList().add(tag);
			}
		}
		
		// refresh the tree in the navigation panel
		frame.getNavigationPanel().refreshTree(repository);
		
	}
	
	// Handles the "Save As" button
	private void saveAsButtonHandler() {
		
		// get the code from the text area
		String code = frame.getDetailPanel().getCodeTextArea().getText();
		// get the language from the combo box
		String languageStr = (String) frame.getDetailPanel().getLanguageSelectionDropdown().getSelectedItem();
		Language language = Language.fromDisplayName(languageStr);
		// get the corresponding extension
		String extension;
		if (language == null) {
			extension = ".txt";
		}
		else {
			extension = language.getExtension();
		}
		
		// call the method in the file controller to export the code file
		fileController.saveCodeAs(code, extension);
		
	}
	
	// Handles the "Copy Code" button
	private void copyCodeButtonHandler() {
		
		// get the code from the text area
		String code = frame.getDetailPanel().getCodeTextArea().getText();
		
		// call the method to copy it to the system clipboard
		copyCodeToClipboard(code);
		
	}
	
	// Handles the "Delete" button
	private void deleteButtonHandler() {
		
		// show an confirm dialog
		JLabel message = new JLabel("Are you sure you want to delete this snippet?");
		message.setFont(new Font("Consolas", Font.BOLD, 18));
		int confirm = JOptionPane.showConfirmDialog(null, message, "Confirm Delete", JOptionPane.YES_NO_OPTION);
		
		// if the user clicks "Yes", then delete this snippet
		if (confirm == JOptionPane.YES_OPTION) {
			
			// if the current snippet ID is -1 (no existing snippet is selected), 
			// then reset all the input components in the detail panel
			if (currentSnippetID == -1) {
				
				frame.getDetailPanel().resetInputComponents();
				
			}
			// otherwise, remove the snippet
			else {
				
				// loop through the repository and find the snippet that needs to be removed
				// store its tag list and its title
				ArrayList<String> tagsToCheck = new ArrayList<>();
				String titleOfRemovedSnippet = "";
				for (Snippet s : repository.getSnippetList()) {
					
					if (s.getId() == currentSnippetID) {
						
						tagsToCheck = s.getTags();
						titleOfRemovedSnippet = s.getTitle();
						// remove the snippet and end the loop
						repository.getSnippetList().remove(s);
						break;
						
					}
				}
				
				// loop through each tag in the tag list of that snippet
				for (String tag : tagsToCheck) {
					
					// create a boolean flag to track whether this tag is still used
					boolean stillUsed = false;
					
					// loop through each snippet in the repository 
					for (Snippet s : repository.getSnippetList()) {
						
						// if there is an other snippet contains this tag, 
						// then set the boolean flag to true and end the loop
						if (s.getTags().contains(tag)) {	
							stillUsed = true;
							break;
						}
					}
					
					// if none of the snippets contain this tag, then remove it from the repository tag list
					if (! stillUsed)
						repository.getTagList().remove(tag);
					
				}
				
				// reset all the input components in the detail panel
				frame.getDetailPanel().resetInputComponents();
				
				// remove this snippet from the currenly displayed snippets list
				for (Snippet s : currentlyDisplayedSnippets) {
					if (s.getTitle().equals(titleOfRemovedSnippet)) {
						currentlyDisplayedSnippets.remove(s);
						break;
					}
				}
				
				// create a list for the new row data for the table
				ArrayList<String []> newRowData = new ArrayList<>();
				
				// loop through the currently displayed snippet list
				for (Snippet s : currentlyDisplayedSnippets) {
					// add this snippet's data to the new row data list
					newRowData.add(new String[] {s.getTitle(), s.getLanguage().getDisplayName(), 
							String.join(",", s.getTags()), s.getDate().toString()});
				}
				
				// update the table in the listing panel
				frame.getListingPanel().updateTable(newRowData.toArray(new String[0][]));
				
				// reset the current snippet ID to -1
				currentSnippetID = -1;
				
				// refresh the tree in the navigation panel
				frame.getNavigationPanel().refreshTree(repository);
				
				// repaint the GUI
				frame.repaint();
				
			}	
		}
	}
	
	// handles the language selection dropdown combo box
	private void languageSelectionDropdownHandler() {
		
		// get the text of the selected language
        String selectedItem = (String) frame.getDetailPanel().getLanguageSelectionDropdown().getSelectedItem();
        
        // get the language's syntax style constant for the RSyntaxTextArea
        String language = Language.fromDisplayName(selectedItem).getSyntaxStyle();
        
        // set the syntax style of the RSyntaxTextArea to this language
        frame.getDetailPanel().getCodeTextArea().setSyntaxEditingStyle(language);
		
	}
	
	// handles tree node selection event
	// if a tree node is selected, then update the table in the listing panel
	// https://stackoverflow.com/questions/4240074/jtree-how-to-get-the-text-of-selected-item?noredirect=1&lq=1
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		
		// get the selected node
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) 
				frame.getNavigationPanel().getTree().getLastSelectedPathComponent();
		
		if (selectedNode == null) {
            return; 
        }
		
		// update the current selected node
		this.currentSelectedNode = selectedNode;
		
		// get the text of this selected node
		String nodeValue = selectedNode.getUserObject().toString();
		
		// if the node is "All Snippets", then show every snippets in the table
		if (nodeValue.equals("All Snippets")) {
			
			// set the currently displayed snippet to the entire repository snippet list
			currentlyDisplayedSnippets = new ArrayList<>(repository.getSnippetList());
			
			// add the snippets' data to the new row data list for table update
			ArrayList<String[]> newRowData = new ArrayList<>();
			for (Snippet s : currentlyDisplayedSnippets) {
				newRowData.add(new String[] {s.getTitle(), s.getLanguage().getDisplayName(), 
                        String.join(",", s.getTags()), s.getDate().toString()});
			}
			
			// update the table in the listing panel
			frame.getListingPanel().updateTable(newRowData.toArray(new String[0][]));
			
		}
		
		// if the selected node is a language, then filter the table by that language
		for (String language : Language.getNames()) {
			if (language.equals(nodeValue)) {
				
				// create a new row data list for table update
				ArrayList<String []> newRowData = new ArrayList<>();
				// clear the currently displayed snippets list
				currentlyDisplayedSnippets.clear();
				
				// loop through every snippet in the repository
				for (Snippet s : repository.getSnippetList()) {
					
					// if the current snippet's language is the same as the selected one, 
					// then add it to the new row data list and the currently displayed snippets list
					if (s.getLanguage().getDisplayName().equals(nodeValue)) {
						newRowData.add(new String[] {s.getTitle(), s.getLanguage().getDisplayName(), 
								String.join(",", s.getTags()), s.getDate().toString()});
						currentlyDisplayedSnippets.add(s);
					}
				}
				
				// update the table in the listing panel
				frame.getListingPanel().updateTable(newRowData.toArray(new String[0][]));
				
			}
		}
		
		// if the selected node is a tag, then filter the table by that tag
		for (String tag : repository.getTagList()) {
			if (tag.equals(nodeValue)) {
				
				// create a new row data list for table update
				ArrayList<String []> newRowData = new ArrayList<>();
				// clear the currently displayed snippets list
				currentlyDisplayedSnippets.clear();
				
				// loop through every snippet in the repository
				for (Snippet s : repository.getSnippetList()) {
					// if the current snippet contains the selected tag, 
					// then add it to the new row data list and the currently displayed snippets list
					if (s.getTags().contains(nodeValue)) {
						newRowData.add(new String[] {s.getTitle(), s.getLanguage().getDisplayName(), 
								String.join(",", s.getTags()), s.getDate().toString()});
						currentlyDisplayedSnippets.add(s);
					}
				}
				
				// update the table in the listing panel
				frame.getListingPanel().updateTable(newRowData.toArray(new String[0][]));
				
			}
		}
	}
	
	// Handles the table selection event
	// if a row in the table is selected, then update the components in the detail panel
	// https://stackoverflow.com/questions/15906001/jtable-selection-listener
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if (! e.getValueIsAdjusting()) {
			
			// get the selected row number
			int selectedRow = frame.getListingPanel().getTable().getSelectedRow();
			if (selectedRow != -1 && selectedRow < currentlyDisplayedSnippets.size()) {
				
				// get the corresponding snippet
				Snippet s = currentlyDisplayedSnippets.get(selectedRow);
				
				// set the components in the detail panel to this snippet's data
				frame.getDetailPanel().getTitleInputField().setText(s.getTitle());
				frame.getDetailPanel().getTagsInputField().setText(String.join(",", s.getTags()));
				frame.getDetailPanel().getLanguageSelectionDropdown().setSelectedItem(s.getLanguage().getDisplayName());
				frame.getDetailPanel().getCodeTextArea().setText(s.getCode());
				frame.getDetailPanel().getCodeTextArea().setCaretPosition(0);		// scroll the code text area to the top
				
				// set the tag input field's style back to user input instead of placeholder
				frame.getDetailPanel().getTagsInputField().setForeground(Color.BLACK);
				frame.getDetailPanel().getTagsInputField().setFont(new Font("Consolas", Font.PLAIN, 20));
				
				// set the current snippet ID
				this.currentSnippetID = s.getId();
				
			}
		}
	}
	
	// handles the window events
	
	@Override
	public void windowOpened(WindowEvent e) {}
	
	// If the window is closing, save the data and then terminate the program
	@Override
	public void windowClosing(WindowEvent e) {
	    System.out.println("Window is closing, saving data...");
	    fileController.saveData();
	    System.exit(0);
	}
	
	@Override
	public void windowClosed(WindowEvent e) {}
	
	@Override
	public void windowIconified(WindowEvent e) {}
	
	@Override
	public void windowDeiconified(WindowEvent e) {}
	
	@Override
	public void windowActivated(WindowEvent e) {}
	
	@Override
	public void windowDeactivated(WindowEvent e) {}
	
	// getters and setters
	
	public MainFrame getFrame() {
		return frame;
	}

	public void setFrame(MainFrame frame) {
		this.frame = frame;
	}

	public FileController getFileController() {
		return fileController;
	}

	public void setFileController(FileController fileController) {
		this.fileController = fileController;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public int getCurrentSnippetID() {
		return currentSnippetID;
	}

	public void setCurrentSnippetID(int currentSnippetID) {
		this.currentSnippetID = currentSnippetID;
	}

	public ArrayList<Snippet> getCurrentlyDisplayedSnippets() {
		return currentlyDisplayedSnippets;
	}

	public void setCurrentlyDisplayedSnippets(ArrayList<Snippet> currentlyDisplayedSnippets) {
		this.currentlyDisplayedSnippets = currentlyDisplayedSnippets;
	}
	
}
