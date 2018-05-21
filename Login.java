/* 
 * asuhail_FinalProject
 * Coded by: Abdullah Suhail
 * Starter Code provided by: Prof. Papademas
 * 5-2-2017
 * Login.java
 */

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Login extends ticketsGUI //inherits database connection method
{
    
	//declare GUI compnents
    public JFrame mainFrame;
    public JLabel headerLabel;
    public JLabel statusLabel;
    public JPanel controlPanel;
	final static String username = "";
	
    public Login() throws Exception
    {
    	super(username); //invoke super constructor
    	//declare all methods from this file
    	createTable();
    	deleteusertableRows();
    	populateUsers();
        prepareGUI();
    }
    
    public void createTable(){
    	
		// vars. for SQL Query create
		  final String createTable = "CREATE TABLE IF NOT EXISTS a_suha_users "
		  		+ "(user_id INT AUTO_INCREMENT PRIMARY KEY, "
		  		+ "user_name VARCHAR(30), "
		  		+ "user_password VARCHAR(256))";
		
		  //use a prepared statement to prevent SQL Injection
		  PreparedStatement preparedStmt = null;
			try {
				  // This will connect to DB using the predefined function
				  System.out.println("Connecting to a selected database to create users table...");
				  Connection dbConnection = null;
				  dbConnection = getDBConnection();
				  System.out.println("Connected database successfully...");
		 	      //create table
				  preparedStmt = dbConnection.prepareStatement(createTable);		      
				  preparedStmt.executeUpdate();

				//end create table
			    //close connection/statement object  
			     preparedStmt.close();
			     dbConnection.close();
			    } catch (Exception e) {
			    	System.out.println(e.getMessage());  
			    }  
    }
    
    //method to delete all rows from the specified table
    public static void deleteusertableRows()
    {
    	try
    	{
    		Connection dbConnection = null;
    		dbConnection = getDBConnection();
    		//prepare statement to prevent SQL Injection
    		PreparedStatement preparedStmt = null;
    		String sql = "DELETE FROM a_suha_users";
    		preparedStmt = dbConnection.prepareStatement(sql); 
    		System.out.println("Deleting the table contents...");		
    		preparedStmt.executeUpdate();
    		preparedStmt.close(); //table dropped, hopefully
    		System.out.println("Table Contents Deleted...");    		
    	}
    	catch (SQLException e)
    	{
    		System.out.println(e.getMessage());
    	}
    }
    
    public void populateUsers(){
    	
    	      // vars. for SQL Query inserts
	      BufferedReader br;
	    	  List<List<String>> array = new ArrayList<>();  //arraylist to hold spreadsheet rows & columns	
	    	  
	    	  //read data from file
	    	  try {
				   br = new BufferedReader(new FileReader(new File("userlist.csv")));

			       String line;
						while ((line = br.readLine()) != null) {
							array.add(Arrays.asList(line.split(",")));
						}
					} catch (Exception e) {
						System.out.println("There was a problem loading the file");
		   }
	    	  
		   try {
			     // This will connect to DB using the predefined function
				 Connection dbConnection = null;
				//use a prepared statement to prevent SQL Injection
				 PreparedStatement preparedStmt = null;
				 dbConnection = getDBConnection();
			  //create loop to grab each array index containing a list of values
			  //and PASS that data into your record objects' setters 
			    for (List<String> rowData: array)
			    {
				   //perform inserts into users table
			       //grab values one column at a time from array
				   preparedStmt = dbConnection.prepareStatement("insert into a_suha_users(user_name, user_password) VALUES (?,?)");
				   preparedStmt.setString(1, rowData.get(0));
				   preparedStmt.setString(2, rowData.get(1));
	               preparedStmt.executeUpdate();
			    }
		  	    System.out.println("Inserts completed in the given database...");

		    //close connection/statement object  
		     preparedStmt.close();
		     dbConnection.close();
		    } catch (Exception e) {
		    System.out.println(e.getMessage());
		    }  
    }
    

    public void prepareGUI(){
    	//set specifications for mainFrame
        mainFrame = new JFrame("Login");
        mainFrame.setSize(400,150);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new GridLayout(3, 1));
        
        mainFrame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent wE){   //define a close operation
                System.exit(0);
        }
        });
        
        headerLabel = new JLabel("",JLabel.CENTER);
        statusLabel = new JLabel("",JLabel.CENTER);
        
        statusLabel.setSize(350,100);
        
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        //add GUI components to mainFramae
        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        //mainFrame.setVisible(true);
    }
    
    public void showTextFields(){
        headerLabel.setText("Account Access");
        
        JLabel  namelabel= new JLabel("User ID: ", JLabel.RIGHT);
        JLabel  passwordLabel = new JLabel("Password: ", JLabel.CENTER);
        final JTextField userText = new JTextField(6);
        final JPasswordField passwordText = new JPasswordField(6);
        
        JButton loginButton = new JButton("Login");
        loginButton.setMnemonic('L');
        loginButton.addActionListener(new ActionListener() {
        	
            @SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
                
               /*verify user with a matching password/username from users table
                 *give popup message if either username or password is incorrect
               */
            	//use a prepared statement to prevent SQL Injection
       		  PreparedStatement preparedStmt = null;
       		  
       		  try {
       		      // This will load the MySQL driver, each DB has its own driver
				 Connection dbConnection = null;
				 dbConnection = getDBConnection();
       	 	      
       		      //verify user from users table
       		      String username = userText.getText();
       		      String password = passwordText.getText();
    			  boolean blnFound=false; //flag if user's credentials are valid
       		      preparedStmt = dbConnection.prepareStatement("SELECT user_name, user_password FROM a_suha_users WHERE user_name = ? AND BINARY user_password = ?");
       		      preparedStmt.setString(1, username);
       		      preparedStmt.setString(2, password);
       	          ResultSet rs = preparedStmt.executeQuery();
       	          blnFound = rs.first();  //grabs first record match!
                 
                  if(blnFound) 
                  {
              	  mainFrame.dispose();
              	  //open up ticketsGUI file upon successful login
              	  new ticketsGUI(username);
              	  }
              	  else
              	  {
              		JOptionPane.showMessageDialog(null, "You entered an incorrect username or password");
              	  }
  
        		     //close connection/statement object
                  preparedStmt.close();
       		      dbConnection.close();
       		    } catch (Exception e1) {
       		    	System.out.println(e1.getMessage());  
       		    }  
               }
        });
        
        //add components for login in frame to controlPanel
        controlPanel.add(namelabel);
        controlPanel.add(userText);
        controlPanel.add(passwordLabel);       
        controlPanel.add(passwordText);
        controlPanel.add(loginButton);
        mainFrame.setVisible(true);  
    }
    
    //main method to create new Login object
    public static void main(String[] args) throws Exception{
        Login login = new Login();
        login.showTextFields();
    }    
}