package view;
/*
 * Author: Qianyi Ma
 * 
 * This class represents the listing panel on the main frame
 * It is in the middle of the screen that displays a table of code snippets under the current filter
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import model.Repository;

public class ListingPanel extends JPanel {
	
	// fields
	
	private JTable table;
	private DefaultTableModel tableModel;
	private JScrollPane scrollPane;
	
	private static final int DEFAULT_ROW_NUM = 13;
	private static final String[] COLUMNNAMES = {"Title", "Language", "Tags", "Date Modified"};
	
	// constructor
	public ListingPanel(Repository repository) {
		
		// set the configurations for this panel
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(700, 990));
		setBorder(new MatteBorder(5, 0, 0, 0, new Color(204, 204, 204)));
		setBackground(new Color(245, 245, 245));
		
		// define column names and initial row data
		String[][] rowData = new String[Math.max(repository.getSnippetList().size(), DEFAULT_ROW_NUM)][4];
		for(int i = 0; i < rowData.length; i++) {
			
			// fill the row with the snippet data if it exists
			if (i < repository.getSnippetList().size()) {
				
				rowData[i][0] = repository.getSnippetList().get(i).getTitle();
				rowData[i][1] = repository.getSnippetList().get(i).getLanguage().getDisplayName();
				rowData[i][2] = String.join(",", repository.getSnippetList().get(i).getTags());
				rowData[i][3] = repository.getSnippetList().get(i).getDate().toString();
				
			}
			// otherwise, create empty rows
			else
				rowData[i] = new String[4];
			
		}
		
		// initialize the table model and table component
		// https://docs.oracle.com/javase/8/docs/api/javax/swing/JTable.html
		// https://tips4java.wordpress.com/2010/01/24/table-row-rendering/
		tableModel = new DefaultTableModel(rowData, COLUMNNAMES);
		table = new JTable(tableModel) {
			public Component prepareRenderer(
					TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);
				
				// set the font for the text on the table
				c.setFont(new Font("Consolas", Font.PLAIN, 20));
				
				// align text in cells
				if (c instanceof JLabel) {
			        JLabel label = (JLabel) c;
			        if (column == 0) {
			            label.setHorizontalAlignment(JLabel.LEFT);
			        } else if (column == 1) {
			            label.setHorizontalAlignment(JLabel.CENTER);
			        }
			    }
				
				// showing alternating row colours
				if (!isRowSelected(row)) {
					if (row % 2 == 0)
						c.setBackground(new Color(208, 216, 232));
					else
						c.setBackground(new Color(233, 237, 244));
					c.setForeground(Color.BLACK);
				} 
				else {
					c.setBackground(new Color(38, 117, 191));
					c.setForeground(Color.WHITE);;
				}
				
				// set the customized cell border
				if (c instanceof JComponent) {
					((JComponent) c).setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
				}
				
				return c;
			}
			
			// disable direct editing
			@Override
			public boolean isCellEditable(int row, int column) {
			    return false;
			}
		};
		
		// configure customized header renderer
		// https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableCellRenderer.html
		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value,
		            boolean isSelected, boolean hasFocus, int row, int column) {
		        
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        // set header's style and alignment
				this.setBackground(new Color(79, 129, 189));
				this.setForeground(Color.WHITE);
				this.setHorizontalAlignment(JLabel.CENTER);
		        
		        // apply the font to the header
		        this.setFont(new Font("Consolas", Font.BOLD, 20));
		        // set the header's border
		        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
		        return this;
		    }
		};
		
		// assign the custom renderer to the table header
		table.getTableHeader().setDefaultRenderer(headerRenderer);
		table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 75));
		
		// prevent the user from reordering the table columns
		// https://stackoverflow.com/questions/28917880/make-jtable-column-header-not-movable-resizable
		table.getTableHeader().setReorderingAllowed(false);
		
		// set the row height
		table.setRowHeight(67);
		
		// add the table to a scroll pane
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		
	}
	
	// update table data with new values
	public void updateTable(Object[][] newRowData) {
		
		// update row data
		tableModel.setDataVector(newRowData, COLUMNNAMES);
		
		// notify table to refresh display
		tableModel.fireTableDataChanged();
		
	}
	
	// getters and setters
	
	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	
}
