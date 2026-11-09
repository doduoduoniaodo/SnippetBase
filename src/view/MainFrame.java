package view;
/*
 * Author: Qianyi Ma
 * 
 * This class is the main application window for the program
 * It assembles the header, navigation, listing, and detail panels to provide a GUI window
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Repository;

public class MainFrame extends JFrame {
	
	// fields
	
	private NavigationPanel navigationPanel;
	private ListingPanel listingPanel;
	private DetailPanel detailPanel;
	private JPanel headerPanel;
	
	private JButton newSnippetButton;
	
	// constructor
	public MainFrame(Repository repository) {
		
		setTitle("SnippetBase"); // set the title of the window
		setSize(1920, 1080);	 // set the size of the window
		setLayout(new BorderLayout());	// set the layout of the frame
		setResizable(false);	 // set the window not resizable
		setLocationRelativeTo(null); // centre the frame on the screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// exit the program when the frame/window is closed
		
		///////////////////////// Header Panel /////////////////////////
		
		// create the header panel and set its configurations
		headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBorder(new EmptyBorder(new Insets(15, 30, 15, 50)));
		headerPanel.setPreferredSize(new Dimension(1920, 90));
		headerPanel.setBackground(new Color(75, 115, 163));
		
		// create a title label that will be put inside the header panel
		JLabel titleLabel = new JLabel("SnippetBase: Code Snippet Manager & Repository");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Consolas", Font.BOLD, 30));
		headerPanel.add(titleLabel, BorderLayout.WEST);
		
		// create a "Create a New Snippet" button
		newSnippetButton = new JButton("Create a New Snippet");
		newSnippetButton.setFont(new Font("Consolas", Font.BOLD, 20));
		newSnippetButton.setBackground(new Color(224, 224, 224));
		headerPanel.add(newSnippetButton, BorderLayout.EAST);
		
		// add the header panel to the frame
		add(headerPanel, BorderLayout.NORTH);
		
		///////////////////////// Navigation Panel /////////////////////////
		
		// initialize the navigation panel and add it to the frame
		navigationPanel = new NavigationPanel(repository);
		add(navigationPanel, BorderLayout.WEST);
		
		///////////////////////// Listing Panel /////////////////////////
		
		// initialize the listing panel and add it to the frame
		listingPanel = new ListingPanel(repository);
		add(listingPanel, BorderLayout.CENTER);
		
		///////////////////////// Detail Panel /////////////////////////
		
		// initialize the detail panel and add it to the frame
		detailPanel = new DetailPanel();
		add(detailPanel, BorderLayout.EAST);
		
	}
	
	// getters and setters
	
	public NavigationPanel getNavigationPanel() {
		return navigationPanel;
	}

	public void setNavigationPanel(NavigationPanel navigationPanel) {
		this.navigationPanel = navigationPanel;
	}

	public ListingPanel getListingPanel() {
		return listingPanel;
	}

	public void setListingPanel(ListingPanel listingPanel) {
		this.listingPanel = listingPanel;
	}

	public DetailPanel getDetailPanel() {
		return detailPanel;
	}

	public void setDetailPanel(DetailPanel detailPanel) {
		this.detailPanel = detailPanel;
	}

	public JPanel getHeaderPanel() {
		return headerPanel;
	}

	public void setHeaderPanel(JPanel headerPanel) {
		this.headerPanel = headerPanel;
	}

	public JButton getNewSnippetButton() {
		return newSnippetButton;
	}

	public void setNewSnippetButton(JButton newSnippetButton) {
		this.newSnippetButton = newSnippetButton;
	}
	
}
