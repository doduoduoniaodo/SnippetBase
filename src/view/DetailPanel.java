package view;
/*
 * Author: Qianyi Ma
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import model.Language;

public class DetailPanel extends JPanel {
	
	// fields
	
	private JButton saveChangesButton;
	private JButton saveAsButton;
	private JButton copyCodeButton;
	private JButton deleteButton;
	
	private JTextField titleInputField;
	private JTextField tagsInputField;
	
	private JComboBox<String> languageSelectionDropdown;
	
	private RSyntaxTextArea codeTextArea;
	
	// constructor
	public DetailPanel() {
		
		// set the configurations for this panel
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(820, 990));
		setBorder(new MatteBorder(5, 5, 0, 0, new Color(204, 204, 204)));
		setBackground(new Color(245, 245, 245));
		
		// initializes the top section
		initializeTopPanel();
		
		// initializes the middle section
		initializeMiddlePanel();
		
		// initializes the bottom section
		initializeBottomPanel();
		
	}
	
	// initializes the top section with title, tags, and language inputs
	private void initializeTopPanel() {
		
		// create a panel and use GridBag for alignment
		JPanel topPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;	// fill horizontally
		
		gbc.gridx = 0;	// col 0
		gbc.gridy = 0;	// row 0
		// create the title label
		JLabel titleLabel = new JLabel("Title: ");
		titleLabel.setFont(new Font("Consolas", Font.BOLD, 20));
		topPanel.add(titleLabel, gbc);
		
		gbc.gridx = 1;	// col 1
		gbc.weightx = 1.0;	// take available space
		// create the title input text field
		titleInputField = new JTextField();
		titleInputField.setFont(new Font("Consolas", Font.PLAIN, 20));
		topPanel.add(titleInputField, gbc);
		
		gbc.gridx = 0;	// col 0
		gbc.gridy = 1;	// row 1
		gbc.weightx = 0.0;	// reset weight
		// create the tags label
		JLabel tagLabel = new JLabel("Descriptive Tags: ");
		tagLabel.setFont(new Font("Consolas", Font.BOLD, 20));
		topPanel.add(tagLabel, gbc);
		
		gbc.gridx = 1;	// col 1
		gbc.weightx = 1.0;	// take avaliable space
		// create the tags input text field
		tagsInputField = new JTextField();
		tagsInputField.setText("Separate each tag by a comma (,)");
		tagsInputField.setFont(new Font("Arial", Font.ITALIC, 20));
		tagsInputField.setForeground(new Color(168, 168, 168));
		// handle focus events for placeholder text behaviour
		// https://docs.oracle.com/javase/8/docs/api/java/awt/event/FocusListener.html
		tagsInputField.addFocusListener(new FocusListener() {
			// if the text area is focused and the placeholder is shown,
			// then remove the placeholder and get ready for the user input
			@Override
			public void focusGained(FocusEvent e) {
				if (tagsInputField.getText().equals("Separate each tag by a comma (,)")) {
					tagsInputField.setText("");
					tagsInputField.setForeground(Color.BLACK);
					tagsInputField.setFont(new Font("Consolas", Font.PLAIN, 20));
				}
			}
			
			// if the text area is no longer focused and there is nothing in it,
			// then show the placeholder
			@Override
			public void focusLost(FocusEvent e) {
				if (tagsInputField.getText().trim().isEmpty()) {
					tagsInputField.setText("Separate each tag by a comma (,)");
					tagsInputField.setFont(new Font("Arial", Font.ITALIC, 20));
					tagsInputField.setForeground(new Color(168, 168, 168));
				}
			}
		});
		topPanel.add(tagsInputField, gbc);
		
		gbc.gridx = 0;	// col 0
		gbc.gridy = 2;	// row 2
		gbc.weightx = 0.0;	// reset weight
		// create the language label
		JLabel languageLabel = new JLabel("Language: ");
		languageLabel.setFont(new Font("Consolas", Font.BOLD, 20));
		topPanel.add(languageLabel, gbc);
		
		gbc.gridx = 1;	// col 1
		gbc.weightx = 1.0;	// take available space
		// create the dropdown combo box for language selection
		languageSelectionDropdown = new JComboBox<>(Language.getNames());
		languageSelectionDropdown.setFont(new Font("Consolas", Font.PLAIN, 20));
		topPanel.add(languageSelectionDropdown, gbc);
		
		// add the top section to this detail panel
		add(topPanel, BorderLayout.NORTH);
		
	}
	
	
	// initializes the middle section with the code text area
	private void initializeMiddlePanel() {
		
		JPanel middlePanel = new JPanel(new BorderLayout());
		
		// External Library: RSyntaxTextArea
		// https://github.com/bobbylight/RSyntaxTextArea/wiki
		// https://javadoc.io/doc/com.fifesoft/rsyntaxtextarea/latest/org/fife/ui/rsyntaxtextarea/RSyntaxTextArea.html
		// create an RSyntaxTextArea or syntax highlighting and formatting code editor
		codeTextArea = new RSyntaxTextArea();
		codeTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);	// default set to Java
		codeTextArea.setFont(codeTextArea.getFont().deriveFont(20f));
        codeTextArea.setCodeFoldingEnabled(true);	// enable folding and expand blocks of code
        codeTextArea.setAnimateBracketMatching(true);	// enable the little effect on the matching brackets
        codeTextArea.setAutoIndentEnabled(true);	// enable auto indent so that new lines will match the current indentation level
		
        // put the text area into a scroll pane
        RTextScrollPane codeScrollPane = new RTextScrollPane(codeTextArea);
        codeScrollPane.setLineNumbersEnabled(true);	// show line numbers
        codeScrollPane.setFoldIndicatorEnabled(true);	// show the folding and expanding indicator
        middlePanel.add(codeScrollPane, BorderLayout.CENTER);
		
        // add the middle section to this detail panel
		add(middlePanel, BorderLayout.CENTER);
		
	}
	
	// initializes the bottom section with action buttons
	private void initializeBottomPanel() {
		
		// create a panel with FlowLayout for alignment
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		
		// create the "Save Changes" button
		saveChangesButton = new JButton("Save Changes");
		saveChangesButton.setFont(new Font("Consolas", Font.BOLD, 20));
		saveChangesButton.setBackground(new Color(224, 224, 224));
		bottomPanel.add(saveChangesButton);
		
		// create the "Save As" button
		saveAsButton = new JButton("Save As");
		saveAsButton.setFont(new Font("Consolas", Font.BOLD, 20));
		saveAsButton.setBackground(new Color(224, 224, 224));
		bottomPanel.add(saveAsButton);
		
		// create the "Copy Code" button
		copyCodeButton = new JButton("Copy Code");
		copyCodeButton.setFont(new Font("Consolas", Font.BOLD, 20));
		copyCodeButton.setBackground(new Color(224, 224, 224));
		bottomPanel.add(copyCodeButton);
		
		// create the "Delete" button
		deleteButton = new JButton("Delete");
		deleteButton.setFont(new Font("Consolas", Font.BOLD, 20));
		deleteButton.setBackground(new Color(224, 224, 224));
		bottomPanel.add(deleteButton);
		
		// add the bottom section to this detail panel
		add(bottomPanel, BorderLayout.SOUTH);
		
	}
	
	// resets all input fields to their initial states
	public void resetInputComponents() {
		
		// clear the text in the title input text field
		titleInputField.setText("");
		
		// reset the placeholder text in the tags input text field
		tagsInputField.setText("Separate each tag by a comma (,)");
		tagsInputField.setFont(new Font("Arial", Font.ITALIC, 20));
		tagsInputField.setForeground(new Color(168, 168, 168));
		
		// reset the dropdown selection to 0 (Default - Java)
		languageSelectionDropdown.setSelectedIndex(0);
		
		// clear the code in the code text area
		codeTextArea.setText("");
		
	}
	
	// getters and setters

	public JButton getSaveChangesButton() {
		return saveChangesButton;
	}

	public void setSaveChangesButton(JButton saveChangesButton) {
		this.saveChangesButton = saveChangesButton;
	}

	public JButton getSaveAsButton() {
		return saveAsButton;
	}

	public void setSaveAsButton(JButton saveAsButton) {
		this.saveAsButton = saveAsButton;
	}

	public JButton getCopyCodeButton() {
		return copyCodeButton;
	}

	public void setCopyCodeButton(JButton copyCodeButton) {
		this.copyCodeButton = copyCodeButton;
	}

	public JTextField getTitleInputField() {
		return titleInputField;
	}

	public void setTitleInputField(JTextField titleInputField) {
		this.titleInputField = titleInputField;
	}

	public JTextField getTagsInputField() {
		return tagsInputField;
	}

	public void setTagsInputField(JTextField tagsInputField) {
		this.tagsInputField = tagsInputField;
	}

	public JComboBox<String> getLanguageSelectionDropdown() {
		return languageSelectionDropdown;
	}

	public void setLanguageSelectionDropdown(JComboBox<String> languageSelectionDropdown) {
		this.languageSelectionDropdown = languageSelectionDropdown;
	}

	public JButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(JButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	public RSyntaxTextArea getCodeTextArea() {
		return codeTextArea;
	}

	public void setCodeTextArea(RSyntaxTextArea codeTextArea) {
		this.codeTextArea = codeTextArea;
	}
	
}
