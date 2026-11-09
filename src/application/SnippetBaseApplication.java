package application;
/*
 * Author: Qianyi Ma
 * 
 * Date: June 12, 2026
 * Course Code: ICS4U1-02
 * 
 * Title: SnippetBase
 * 
 * Description: 
 * 	SnippetBase is a localized Java desktop application designed to help programmers efficiently manage, 
 * 	organize, and retrieve reusable and helpful code snippets. It creates a graphical user interface based 
 *  on MVC architecture. It allows users to categorize, store, and quickly search through the snippets. 
 *  It saves the data locally and loads them every time the program starts. Users can copy the code into their
 *  system clipboard or export the code file. 
 * 
 * Features: 
 *  - Snippet Management - Creating, viewing, updating, and deleting
 *  	- Snippet data - Title, tags, language, code, and date modified
 *  - Categorization - A navigation tree organizes snippets by their language or tags 
 * 					 - categories: "All Snippets", "Languages", "Tags"
 *  - Search - A search bar that allows the users to search keyword to filter snippets by title
 *  - Code formatting/editing - An integrated code editor with syntax highlighting (by RSyntaxTextArea)
 *  - A table that shows all the snippets under the current filter. 
 *  	- Clicking a node in the tree navigation will update the table
 *  	- Cliking a row in the table will update the snippet view (right side of the screen)
 *  - A "Copy Code" button that copies the code content to the system clipboard
 *  - A "Save As" button that allows exporting snippet code to an external code file based on the selected language's file extension
 *  - Automatically saves and loads snippet data to and from a local txt file (file/snippetBaseData.txt)
 *    on program startup and shutdown
 *  - Using an external API, FlatJaf, to show a modern look for the components
 *  
 * Major Skills: 
 * - Model-View-Controller framework
 * - GUI (Javax.swing and java.awt)
 * - Object Oriented Programming (OOP)
 * - Various data structures implementations
 * - User interaction
 * - File saving and loading
 * - External APIs (FlatLaf and RSyntaxTextArea)
 * - Time management and Organization
 * 
 * Area of Concerns: 
 *  - The application relies on the txt data file (file/snippetBaseData.txt). 
 *    Do not manually edit this file.
 *    Its structure is very fragile. 
 *    If the markers, numbers, or format is modified or removed, the application cannot correctly load the snippets or crash on startup. 
 *  - The project uses the local JAR files located in the "lib/" directory. 
 *    If these files are moved or deleted, the program will crash.
 * 
 *  - RSyntaxTextArea supports a lot of language, I just chose some of them in my Language enum. 
 *  
 *  - I put some code snippets into the repository already. 
 * 
 */

import com.formdev.flatlaf.FlatLightLaf;

import controller.FileController;
import controller.SnippetBaseController;

public class SnippetBaseApplication {
	
	public static void main(String[] args) {
		
		FlatLightLaf.setup();
		FileController fileController = new FileController();
		new SnippetBaseController(fileController, fileController.getRepository());
		
	}
	
}
