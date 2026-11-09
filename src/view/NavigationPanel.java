package view;
/*
 * Author: Qianyi Ma
 * 
 * This class represents the navigation panel on the main frame
 * It is on the left side of the screen, providing a search bar and 
 * a tree navigation structure for filtering the JTable in the listing panel
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import model.Language;
import model.Repository;

public class NavigationPanel extends JPanel {
	
	// fields
	private JButton searchButton;
	private JTextField searchBar;
	private JTree tree;
	
	// constructor
	public NavigationPanel(Repository repository) {
		
		// set configurations for this panel
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 990));
		setBorder(new MatteBorder(5, 0, 0, 5, new Color(204, 204, 204)));
		setBackground(new Color(245, 245, 245));
		
		// initialize the search panel
		initializeSearchPanel();
		
		// initialize the navigation tree
		initializeTree(repository);
		
	}
	
	// initializes the search bar and the search button
	private void initializeSearchPanel() {
		
		// search input area setup
		JPanel searchPanel = new JPanel(new BorderLayout(30, 0));
		searchPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

		// create search text field for user input
		searchBar = new JTextField();
		searchBar.setMargin(new Insets(0, 10, 0, 0));
		searchBar.setText("Enter a term");
		searchBar.setFont(new Font("Arial", Font.ITALIC, 20));
		searchBar.setForeground(new Color(168, 168, 168));
		// handle focus events for placeholder text behaviour
		// https://docs.oracle.com/javase/8/docs/api/java/awt/event/FocusListener.html
		searchBar.addFocusListener(new FocusListener() {
			// if the text area is focused and the placeholder is shown,
			// then remove the placeholder and get ready for the user input
			@Override
			public void focusGained(FocusEvent e) {
				if (searchBar.getText().equals("Enter a term")) {
					searchBar.setText("");
					searchBar.setForeground(Color.BLACK);
					searchBar.setFont(new Font("Consolas", Font.PLAIN, 20));
					searchButton.setEnabled(true);
				}
			}
			
			// if the text area is no longer focused and there is nothing in it,
			// then show the placeholder
			@Override
			public void focusLost(FocusEvent e) {
				if (searchBar.getText().trim().isEmpty()) {
					searchBar.setText("Enter a term");
					searchBar.setFont(new Font("Arial", Font.ITALIC, 20));
					searchBar.setForeground(new Color(168, 168, 168));
					searchButton.setEnabled(false);
				}
			}
		});
		
		// group the search text area and the search button together
		JPanel searchGroupPanel = new JPanel(new BorderLayout(0, 0));
		searchGroupPanel.add(searchBar, BorderLayout.CENTER);

		// create "Search" button
		searchButton = new JButton("Search");
		searchButton.setEnabled(false);
		searchButton.setBackground(new Color(180, 200, 220));
		searchButton.setFont(new Font("Consolas", Font.BOLD, 20));	
		searchGroupPanel.add(searchButton, BorderLayout.EAST);
		
		// add the group panel to the search panel
		searchPanel.add(searchGroupPanel, BorderLayout.CENTER);
		
		// add the search panel to this navigation panel
		add(searchPanel, BorderLayout.NORTH);
		
	}
	
	// initializes the tree view with categories (languages and tags)
	private void initializeTree(Repository repository) {
		
		// create the invisible root (don't need to show this root)
		DefaultMutableTreeNode hiddenRoot = new DefaultMutableTreeNode();
		
		// create a node for showing all the snippets
		DefaultMutableTreeNode allSnippetsCategory = new DefaultMutableTreeNode("All Snippets"); 
		
		// create a parent node for the languages category
		DefaultMutableTreeNode languagesCategory = new DefaultMutableTreeNode("Languages");
		// create a parent node for the tags category
		DefaultMutableTreeNode tagsCategory = new DefaultMutableTreeNode("Tags");
		
		// loop through every language in that enum and add them as nodes to the languages category
		for (String language : Language.getNames()) {
			languagesCategory.add(new DefaultMutableTreeNode(language));
		}
		
		// loop through every tag in the repository and add them as nodes to the tags category
		for (String tag : repository.getTagList()) {
			tagsCategory.add(new DefaultMutableTreeNode(tag));
		}
		
		// add those parent nodes to the root
		hiddenRoot.add(allSnippetsCategory);
		hiddenRoot.add(languagesCategory);
		hiddenRoot.add(tagsCategory);
		
		// create the tree
		// https://stackoverflow.com/questions/10111849/how-to-change-style-color-font-of-a-single-jtree-node
		tree = new JTree(hiddenRoot);
		// set the different fonts for the parent and the child nodes
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
		    public Component getTreeCellRendererComponent(JTree tree, Object value, 
		            boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				
				super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
				
				Font currentFont = tree.getFont();
		        
		        if (leaf) {
		            // set plain font for leaf nodes
		            setFont(currentFont.deriveFont(Font.PLAIN));
		        } else {
		            // set bold font for parent nodes
		            setFont(currentFont.deriveFont(Font.BOLD));
		        }
		        
		        return this;
				
			}
		});
		
		tree.setRootVisible(false);	// hide the root node
		tree.setShowsRootHandles(true);	// show expand/collapse
		tree.setFont(new Font("Consolas", Font.PLAIN, 20)); 
		tree.setRowHeight(35);
		tree.setSelectionPath(new TreePath(allSnippetsCategory.getPath()));	// initially, set the "All Snippets" node selected
		
		// put the tree into a scroll pane
		JScrollPane treeScrollPane = new JScrollPane(tree);
		// add the scroll pane to this navigation panel
		add(treeScrollPane, BorderLayout.CENTER);
		
	}
	
	// refreshes the tree structure when repositroy data changes
	public void refreshTree(Repository repository) {
		
		// create the invisible root (don't need to show this root)
		DefaultMutableTreeNode hiddenRoot = new DefaultMutableTreeNode();
		
		// create a node for showing all the snippets
		DefaultMutableTreeNode allSnippetsCategory = new DefaultMutableTreeNode("All Snippets"); 
		
		// create a parent node for the languages category
		DefaultMutableTreeNode languagesCategory = new DefaultMutableTreeNode("Languages");
		// create a parent node for the tags category
		DefaultMutableTreeNode tagsCategory = new DefaultMutableTreeNode("Tags");
		
		// loop through every language in that enum and add them as nodes to the languages category
		for (String language : Language.getNames()) {
			languagesCategory.add(new DefaultMutableTreeNode(language));
		}
		
		// loop through every tag in the repository and add them as nodes to the tags category
		for (String tag : repository.getTagList()) {
			tagsCategory.add(new DefaultMutableTreeNode(tag));
		}
		
		// add those parent nodes to the root
		hiddenRoot.add(allSnippetsCategory);
		hiddenRoot.add(languagesCategory);
		hiddenRoot.add(tagsCategory);
		
		// set a new model for the existing tree
	    tree.setModel(new DefaultTreeModel(hiddenRoot));
		
	}
	
	// getters and setters
	
	public JButton getSearchButton() {
		return searchButton;
	}

	public void setSearchButton(JButton searchButton) {
		this.searchButton = searchButton;
	}

	public JTextField getSearchBar() {
		return searchBar;
	}

	public void setSearchBar(JTextField searchBar) {
		this.searchBar = searchBar;
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}
	
}
