/* 
 * asuhail_FinalProject
 * Coded by: Abdullah Suhail
 * Starter Code provided by: Prof. Papademas
 * 5-2-2017
 * ticketsGUI.java
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/*proj implements one listener and can trigger action off menu item clicked
 * with one action performed event handler!
 */
@SuppressWarnings("serial")
public class ticketsGUI extends JFrame implements ActionListener {

	//needed class level member vars
	public JFrame mainFrame;
	
	//incl main menu objects 
	public	JMenu file = new JMenu("File");
	public	JMenu tickets = new JMenu("Tickets");
	JScrollPane sp = null;
	
	//incl sub menu item objects and global variables
	JMenuItem ItemNew;
    JMenuItem ItemExit;
    JMenuItem ItemOpenTicket;
    JMenuItem ItemEditTicket;
    JMenuItem ItemCloseTicket;
    JMenuItem ItemDeleteTicket;
    JMenuItem ItemViewTicket;
    JMenuItem ItemViewTickets;
    JMenuItem ItemViewMetrics;
	final String username;
	final Boolean admin;
	int redo;
	String open = "Open";
	String closed = "Closed";
	
	//method that calls all methods created in this file based on admin privileges
	public ticketsGUI(String username) throws Exception
	{
		this.username=username;
		if (username.equalsIgnoreCase("admin"))//checks if username equals the text shown
		{
			//you are an admin
		    admin= true;
		    createTable();
		    createmetricsTable();
			//deletetableRows();
			createMenu();
			prepareGUI();
		}
		else
		{
			//you are not an admin
			admin= false;
	    	createTable();
	    	createmetricsTable();
			//deletetableRows();
			createMenu();
		    prepareGUI();
		}
	}
	

	public void createTable()
	{	
		// vars. for SQL Query create
		  final String createTable = "CREATE TABLE IF NOT EXISTS a_suha_ticket"
		  		+ "(id INT AUTO_INCREMENT PRIMARY KEY, "
		  		+ "username VARCHAR(30),"
		  		+ "tname VARCHAR(30),"
		  		+ "openTime datetime,"
		  		+ "closeTime datetime,"
		  		+ "updateTime datetime,"
		  		+ "tstatus VARCHAR(30),"
		  		+ "tdescription VARCHAR(200),"
		  		+ "tpriority VARCHAR(30))";
		  //use a prepared statement to prevent SQL Injection
		  PreparedStatement preparedStmt = null;
				  
		try {
			  // This will connect to DB using the predefined function
			  System.out.println("Connecting to a selected database to create ticket table...");
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
	
	public void createmetricsTable()
	{	
		// vars. for SQL Query create
		  final String createTable = "CREATE TABLE IF NOT EXISTS a_suha_metrics"
		  		+ "(id INT(10) PRIMARY KEY,"
		  		+ "tduration VARCHAR(30),"
		  		+ "tdescription VARCHAR(200))";
		  
		  //use a prepared statement to prevent SQL Injection
		  PreparedStatement preparedStmt = null;
				  
		try {
		      // This will connect to DB using the predefined function
			  System.out.println("Connecting to a selected database to create metrics table...");
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
	
	private void createMenu()
	{
		//set up mnemonics for main menu items (triggered by alt keys)
		file.setMnemonic('F');
		tickets.setMnemonic('T');
		
		//initialize up sub menu items
		ItemNew = new JMenuItem("Switch User");
		ItemNew.setMnemonic('S');
		file.add(ItemNew);
		
		ItemExit = new JMenuItem("Exit Application");
		ItemExit.setMnemonic('x');
		file.add(ItemExit);
		
		ItemOpenTicket = new JMenuItem("Open New Ticket");
		ItemOpenTicket.setMnemonic('O');
		tickets.add(ItemOpenTicket);
		
		ItemEditTicket = new JMenuItem("Edit Existing Ticket (Must enter Ticket ID)");
		ItemEditTicket.setMnemonic('E');
		tickets.add(ItemEditTicket);
		
		ItemCloseTicket = new JMenuItem("Close Ticket (Must enter Ticket ID)");
		ItemCloseTicket.setMnemonic('C');
		tickets.add(ItemCloseTicket);
		
		ItemDeleteTicket = new JMenuItem("Delete Existing Ticket (Must enter Ticket ID)");
		ItemDeleteTicket.setMnemonic('D');
		tickets.add(ItemDeleteTicket);		
		
		ItemViewTicket = new JMenuItem("View Specific Ticket (Must enter Ticket ID)");
		ItemViewTicket.setMnemonic('V');
		tickets.add(ItemViewTicket);
		
		ItemViewTickets = new JMenuItem("View All Tickets");
		ItemViewTickets.setMnemonic('A');
		tickets.add(ItemViewTickets);
		
		ItemViewMetrics = new JMenuItem("View Metrics Report");
		ItemViewMetrics.setMnemonic('M');
		tickets.add(ItemViewMetrics);
				
		//users w/out admin privileges get less stuff
		if (admin == false) 
	 	{
			ItemEditTicket.setEnabled(false);
			ItemCloseTicket.setEnabled(false);
			ItemDeleteTicket.setEnabled(false);
			ItemViewTicket.setEnabled(false);
			ItemViewTickets.setEnabled(false);
			ItemViewMetrics.setEnabled(false);
		}
		
		//add listeners for each desired menu item 
		ItemNew.addActionListener(this);
		ItemExit.addActionListener(this);
		ItemOpenTicket.addActionListener(this);
		ItemEditTicket.addActionListener(this);
		ItemCloseTicket.addActionListener(this);
		ItemDeleteTicket.addActionListener(this);
		ItemViewTicket.addActionListener(this);
		ItemViewTickets.addActionListener(this);
		ItemViewMetrics.addActionListener(this);
	}
 
public void prepareGUI()
{ 
	//initialize frame object
	mainFrame = new JFrame("Main");
    //create jmenu bar
	JMenuBar bar = new JMenuBar();
	bar.add(file);  //set menu orders
	bar.add(tickets);
	 //add menu bar component to frame and set size
    mainFrame.setJMenuBar(bar); 
    mainFrame.setSize(1200,400);
    mainFrame.setLocationRelativeTo(null); //center the application
	mainFrame.setVisible(true);
}

//this methods deletes rows from the ticket and metrics tables
public static void deletetableRows() throws Exception
{
	try
	{
		Connection dbConnection = null;
		dbConnection = getDBConnection();
		//use a prepared statements to prevent SQL Injection
		PreparedStatement preparedStmt = null;
		PreparedStatement preparedStmt2 = null;
		String sql = "DELETE FROM a_suha_ticket";
		String sql2 = "DELETE FROM a_suha_metrics";
		preparedStmt = dbConnection.prepareStatement(sql);
		preparedStmt2 = dbConnection.prepareStatement(sql2);
		System.out.println("Deleting the table contents...");
		preparedStmt.executeUpdate();
		preparedStmt2.executeUpdate();
		preparedStmt.close();
		preparedStmt2.close();
		System.out.println("Table Contents Deleted...");
	}
	catch (SQLException e)
	{
		System.out.println(e.getMessage());
	}
}

@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	
	//add desired coding to trigger each sub menu action as examples shown below
	if(e.getSource() == ItemNew) { 
		System.out.println("Switch User Selected.");
		mainFrame.dispose(); //close old mainFrame and log in as different user
		Login login;
		try {
			login = new Login();
			login.showTextFields();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.getMessage();
		}
        
	}
	
	 //this coice will exit the application
	 else if(e.getSource() == ItemExit){
         System.exit(0);
     }
	 
	 //this choice will allow you to open a new ticket
	 else if(e.getSource() == ItemOpenTicket){
		 
		 try {
			//add in ticket information
			 String username=
				 JOptionPane.showInputDialog(null,"Enter your Username", "Username", JOptionPane.QUESTION_MESSAGE);
			 String tname=
				 JOptionPane.showInputDialog(null,"Enter the name of the ticket", "Ticket Name", JOptionPane.QUESTION_MESSAGE);
			 String tdescription=
				 JOptionPane.showInputDialog(null,"Enter a ticket description", "Ticket Description", JOptionPane.QUESTION_MESSAGE);
			 Object[] priority = {"High", "Medium", "'Tis but a scratch!"};
			 String tpriority=
				 (String)JOptionPane.showInputDialog(null, "Enter the priority of the ticket", "Ticket Priority", JOptionPane.QUESTION_MESSAGE, null, priority, "High");
				Connection dbConnection = null;
				dbConnection = getDBConnection();
				//use a prepared statement to prevent SQL Injection
	            PreparedStatement preparedStmt = null;
	 		    String sql = "INSERT INTO a_suha_ticket (username, tname, openTime, closeTime, tstatus, updateTime, tdescription, tpriority) VALUES (?,?,?,?,?,?,?,?)";
	            //Execute queries
			      preparedStmt = dbConnection.prepareStatement(sql);	      
			      System.out.println("Inserting records into the table...");
			  	  preparedStmt.setString(1, username);
			      preparedStmt.setString(2, tname);
			      preparedStmt.setTimestamp(3, getCurrentTimeStamp());
			      preparedStmt.setString(4, null);
			      preparedStmt.setString(5, open);
			      preparedStmt.setString(6, null);
				  preparedStmt.setString(7, tdescription);
				  preparedStmt.setString(8, tpriority);
			      int result = preparedStmt.executeUpdate();
			      System.out.println("Inserted records into the table...");
			      preparedStmt.close(); //Records inserted, hopefully
			      
			      
	            if (result != 0) {
					System.out.println("Ticket Created Successfully!!!");
				} else {
					System.out.println("Ticket cannot be Created!!!");
				}
			    		        
		    } 
		       catch (SQLException ex) {
				// TODO Auto-generated catch block
				ex.getMessage();
		       }
		 
		 try {
			    //will display the id of the ticket that was created
			 	Connection dbConnection = null;
				dbConnection = getDBConnection();
				String sql = "SELECT MAX(id) FROM a_suha_ticket";
				PreparedStatement preparedStmt = dbConnection.prepareStatement(sql);
		        ResultSet rs = preparedStmt.executeQuery();
		        if(rs.next())
		        {
		        String idCreated = rs.getString(1);
		        JOptionPane.showMessageDialog(null,"Ticket id #" + idCreated + " has been created", "Conformation", JOptionPane.INFORMATION_MESSAGE);
		        }
		        rs.close();
		        dbConnection.close();
		    }
		 catch (SQLException ex) 
		 {
				// TODO Auto-generated catch block
				ex.getMessage();
		 }
     }
	
		//this choice will allow user to edit a ticket
		else if (e.getSource() == ItemEditTicket)//for updating ticket
		{
			 try
			 {
				 //get id information
				 String edit=
						 JOptionPane.showInputDialog(null,"Which ticket would you like to edit?", "Edit Ticket", JOptionPane.QUESTION_MESSAGE);
				 int selectId = Integer.parseInt(edit);
					Connection dbConnection = null;
					dbConnection = getDBConnection();
					//use a prepared statement to prevent SQL Injection
					 PreparedStatement preparedStmt2 = dbConnection.prepareStatement("Select id FROM a_suha_ticket WHERE id = ?");
					 preparedStmt2.setInt(1, selectId);
					 ResultSet rs = preparedStmt2.executeQuery();//resultset allows us to use the values from the database
				if (rs.next())//allow the cursor to navigate through the rows
				{
				//get new information for ticket update
				String username=
				JOptionPane.showInputDialog(null,"Enter new Username", "Edit Username", JOptionPane.QUESTION_MESSAGE);
				
				String tname=
						 JOptionPane.showInputDialog(null,"Enter the new name of the ticket", "Edit Ticket Name", JOptionPane.QUESTION_MESSAGE);
				
				String tdescription=
						 JOptionPane.showInputDialog(null,"Enter a new ticket description", "Edit Ticket Description", JOptionPane.QUESTION_MESSAGE);
				
				Object[] tPriority = {"High", "Medium", "'Tis but a scratch!"};
				String tpriority=
				(String)JOptionPane.showInputDialog(null, "Enter the new priority of the ticket", "Edit Ticket Priority", JOptionPane.QUESTION_MESSAGE, null, tPriority, "High");
						 
				
						
				String sqlUpdate = "UPDATE a_suha_ticket SET username = ?, tname = ?, tdescription = ?, tpriority = ?, updateTime = ? WHERE id = ?";
				PreparedStatement preparedStmt= dbConnection.prepareStatement(sqlUpdate);
				preparedStmt.setString(1, username);
			    preparedStmt.setString(2, tname);
			    preparedStmt.setString(3, tdescription);
			    preparedStmt.setString(4, tpriority);
			    preparedStmt.setTimestamp(5, getCurrentTimeStamp());
			    preparedStmt.setInt(6, selectId);
			    
				preparedStmt.executeUpdate();// update statement allows the SQL command to be executed
				preparedStmt.close();
				dbConnection.close();
				JOptionPane.showMessageDialog(null,"Ticket id #" + selectId + " has been updated", "Conformation", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"This ticket does not exist!");//will print if the inputted ticket id number doesn't exist
				}
		 } catch (SQLException e1) {
			 // TODO Auto-generated catch block
			 e1.getMessage();
		} catch (NumberFormatException e1) {
			System.out.println("You entered an inappropriate value");
			JOptionPane.showMessageDialog(null,"There have been no changes made");
		} 
	}
		//This choice will allow user to close a ticket
		else if (e.getSource() == ItemCloseTicket)//for closing a ticket
		{
			 try
			 {
				 //get id information
				 String close=
						 JOptionPane.showInputDialog(null,"Which ticket would you like to close?", "Close Ticket", JOptionPane.QUESTION_MESSAGE);
				 int selectId = Integer.parseInt(close);
					Connection dbConnection = null;
					dbConnection = getDBConnection();
					//use a prepared statement to prevent SQL Injection
					 PreparedStatement preparedStmt2 = dbConnection.prepareStatement("Select id FROM a_suha_ticket WHERE id = ?");
					 preparedStmt2.setInt(1, selectId);
					 ResultSet rs = preparedStmt2.executeQuery();
				if (rs.next())//allows the cursor to navigae through the rows
				{										
				String sqlUpdate = "UPDATE a_suha_ticket SET tstatus = ?, closeTime= ? WHERE id = ?";
				PreparedStatement preparedStmt= dbConnection.prepareStatement(sqlUpdate);
				preparedStmt.setString(1, closed);
			    preparedStmt.setTimestamp(2, getCurrentTimeStamp());
			    preparedStmt.setInt(3, selectId);
			    
				preparedStmt.executeUpdate();
				preparedStmt.close();
				dbConnection.close();
				JOptionPane.showMessageDialog(null,"Ticket id #" + selectId + " has been closed", "Conformation", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"This ticket does not exist!");//will display if ticket id number doesn't exist
				}
		 } catch (SQLException e1) {
			 // TODO Auto-generated catch block
			 e1.getMessage();
		} catch (NumberFormatException e1) {
			System.out.println("You entered an inappropriate value");
			JOptionPane.showMessageDialog(null,"There have been no changes made");
		} 
	}
	
	
		//this choice will allow user to delete ticket
		else if(e.getSource() == ItemDeleteTicket){
			
			do//surround with do while block so user can delete multiple tickets
			{
			 try {
				 //get id information
				 String delete=
						 JOptionPane.showInputDialog(null,"Which ticket would you like to delete?", "Close a specific Ticket", JOptionPane.QUESTION_MESSAGE);
				 int selectId = Integer.parseInt(delete);
				 Connection dbConnection = null;
				 dbConnection = getDBConnection();
				 //use a prepared statement to prevent SQL Injection
				 PreparedStatement preparedStmt = dbConnection.prepareStatement("DELETE FROM a_suha_ticket WHERE id = ?");
				 preparedStmt.setInt(1, selectId);
				 PreparedStatement preparedStmt2 = dbConnection.prepareStatement("Select id FROM a_suha_ticket WHERE id = ?");
				 preparedStmt2.setInt(1, selectId);
				 ResultSet rs = preparedStmt2.executeQuery();
				 if (rs.next())//allow the cursor to navigate through the rows
				 {
				 //cpnfirm that user wants to delete ticket
				 int tdelete = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ticket #" + selectId + "?", "Delete Ticket?", JOptionPane.YES_NO_OPTION);
				 
					if (tdelete == JOptionPane.YES_OPTION)// if they select yes, this will run
					{
						preparedStmt.executeUpdate();
						JOptionPane.showMessageDialog(null,  "You deleted ticket #" +selectId, "Deletion Complete", JOptionPane.INFORMATION_MESSAGE);
	            
	            ResultSet results = preparedStmt.executeQuery("SELECT * FROM a_suha_ticket");
	            
	            // Use JTable built in functionality to build a table model and display the table model in a JFrame
	            // off a resultset!!!
	            JTable jt = new JTable(ticketsJTable.buildTableModel(results));
	            jt.setBounds(30,40,200,300);
	            sp=new JScrollPane(jt);
	            getContentPane().add(sp);
	            JPanel buttonPanel = new JPanel();
	            getContentPane().add( buttonPanel, BorderLayout.SOUTH );
	            JFrame frame = new JFrame("Table after ticket deletion");// display the table to show ticket deletion
	            frame.setSize(1200,400);
	            frame.setLocationRelativeTo(null); //center the application
	    		frame.add(new JScrollPane(jt));
	    		frame.setVisible(true);
	    		preparedStmt.close();
	    		preparedStmt2.close();
		        dbConnection.close();//close connections!!!
				}
				else
				{
					JOptionPane.showMessageDialog(null,"No ticket will be deleted");
				}
				 }
				 else
				 {
					 JOptionPane.showMessageDialog(null,"This ticket does not exist!");// will print if the ticket id isn't found
				 }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.getMessage();
			}
			 catch (NumberFormatException e1) {
					System.out.println("You entered an inappropriate value");
					JOptionPane.showMessageDialog(null,"No ticket will be deleted");
				} 
			 
			 redo = JOptionPane.showConfirmDialog(null, "Would you like to delete another ticket?", "Delete Another Ticket?", JOptionPane.YES_NO_OPTION);
			}
			while (redo == 0);
			 
	     }
	
		//this coice allows for the viewing of a particular ticket
		else if(e.getSource() == ItemViewTicket){
			 //retrieve ticket information
			do//surround with do while block so user can view multiple tickets
			{
			 try {
				 //get id information
				 String view=
						 JOptionPane.showInputDialog(null,"Which ticket would you like to View?", "View a specific Ticket", JOptionPane.QUESTION_MESSAGE);
				 int selectId = Integer.parseInt(view);
				 Connection dbConnection = null;
				 dbConnection = getDBConnection();
				//use a prepared statement to prevent SQL Injection
				PreparedStatement preparedStmt = dbConnection.prepareStatement("Select id FROM a_suha_ticket WHERE id = ?");
				preparedStmt.setInt(1, selectId);
				ResultSet rs = preparedStmt.executeQuery();
				if (rs.next())//allow the cursor to navigate through the rows
				{
				PreparedStatement preparedStmt2 = dbConnection.prepareStatement("Select * FROM a_suha_ticket WHERE id = ?");
				preparedStmt2.setInt(1, selectId);
				ResultSet rs2 = preparedStmt2.executeQuery();
	            // Use JTable built in functionality to build a table model and display the table model in a JFrame
	            // off a resultset!!!
	            JTable jt = new JTable(ticketsJTable.buildTableModel(rs2));
	            jt.setBounds(30,40,200,300);
	            sp=new JScrollPane(jt);
	            getContentPane().add(sp);
	            JPanel buttonPanel = new JPanel();
	            getContentPane().add( buttonPanel, BorderLayout.SOUTH );
	            JFrame frame = new JFrame("Viewing Ticket #" + selectId);
	            frame.setSize(1200,400);
	            frame.setLocationRelativeTo(null); //center the application
	    		frame.add(new JScrollPane(jt));
	    		frame.setVisible(true);
	            preparedStmt.close();
		        dbConnection.close();//close connections!!!
			   }
			   else
			   {
				   JOptionPane.showMessageDialog(null,"This ticket does not exist!");
			   }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.getMessage();
			}
			 catch (NumberFormatException e1) {
					System.out.println("You entered an inappropriate value");
					JOptionPane.showMessageDialog(null,"No ticket will be displayed");
				} 
			 redo = JOptionPane.showConfirmDialog(null, "Would you like to view another ticket?", "View Another Ticket?", JOptionPane.YES_NO_OPTION);
			}
			while (redo == 0);
	     }
	
	 //this choice allows user to view all tickets 
	 else if(e.getSource() == ItemViewTickets){
			 //retrieve ticket information
			
			 try {
				 Connection dbConnection = null;
				 dbConnection = getDBConnection();
	            
				PreparedStatement preparedStmt = dbConnection.prepareStatement("SELECT * FROM a_suha_ticket");
	            ResultSet results = preparedStmt.executeQuery();
	            
	            // Use JTable built in functionality to build a table model and display the table model
	            // off a resultset!!!
	            JTable jt = new JTable(ticketsJTable.buildTableModel(results));
	            jt.setBounds(30,40,200,300);
	            sp=new JScrollPane(jt);
	            getContentPane().add(sp);

	            JPanel buttonPanel = new JPanel();
	            getContentPane().add( buttonPanel, BorderLayout.SOUTH );
	            JFrame frame = new JFrame("Viewing All Tickets");
	            frame.setSize(1200,400);
	            frame.setLocationRelativeTo(null); //center the application
	    		frame.add(new JScrollPane(jt));
	    		frame.setVisible(true);
	    		preparedStmt.close();
		        dbConnection.close();//close connections!!!
	            
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.getMessage();
			}
		 
	     }
	
	 //this choice will print certain metrics to the console	
	 else if(e.getSource() == ItemViewMetrics)
	 {
		 
		try 
		{
			Connection dbConnection = getDBConnection();
			String sql = "SELECT id, openTime, tstatus, tdescription FROM a_suha_ticket WHERE tstatus = ?";
			//use a prepared statement to prevent SQL Injection
			PreparedStatement preparedStmt3 = null;
			PreparedStatement preparedStmt = dbConnection.prepareStatement(sql);
			preparedStmt.setString(1, open);
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next())
			{
				//take objects from database and convert them to usable variables
				int selectId = rs.getInt("id");
				String description = rs.getString("tdescription");
				Timestamp openTime = rs.getTimestamp("openTime");
				Timestamp currentTime = getCurrentTimeStamp();
				
				//method of determining time difference between two timestamps
				long millisecondsTime = currentTime.getTime() - openTime.getTime();
				int secondsTime = (int) (millisecondsTime/1000);
				int minutesTime = (secondsTime % 3600)/60;
				int hoursTime = secondsTime/3600;
				secondsTime = (secondsTime % 3600)%60;
				String selectDuration = hoursTime+" Hrs "+minutesTime+" Mins "+secondsTime+" Secs";
				//display ticket information
			    System.out.println("\nTicket #" + selectId + " was opened: " + openTime);
			    System.out.println("The time now is: " + currentTime);
			    
				System.out.println("Time since ticket #" + selectId + " was created: ");
			    System.out.println("Hour(s): " + hoursTime);
			    System.out.println("Minute(s): " + minutesTime);
			    System.out.println("Second(s): " + secondsTime);
			    String sqlIns = "Insert into a_suha_metrics(id, tduration, tdescription) VALUES(?,?,?)";
			    preparedStmt3 = dbConnection.prepareStatement(sqlIns);
			    //pass the values to the prepared statement to insert data into table
			    preparedStmt3.setInt(1, selectId);
			    preparedStmt3.setString(2, selectDuration);
			    preparedStmt3.setString(3, description);
			}
			String sql1 = "SELECT closeTime FROM a_suha_ticket";
			PreparedStatement preparedStmt1 = dbConnection.prepareStatement(sql1);
			ResultSet rs1 = preparedStmt1.executeQuery();
			System.out.println("\nRatio of closed tickets to open tickets");
			int openCounter = 0;
			int closeCounter = 0;
			while (rs1.next())
			{
				//increment counter based on number of closed and open tickets by checking value of closeTime
				Timestamp closeTime = rs1.getTimestamp("closeTime");
				if (closeTime == null)
				{
					openCounter++;
				}
				else
				{
					closeCounter++;
				}
			}
			System.out.println(openCounter + "-" + closeCounter);//display close/open ratio
			
			//display all open tickets of high priority
			String sql2 = "SELECT id, tstatus, tpriority FROM a_suha_ticket WHERE tpriority = ?";
			PreparedStatement preparedStmt2 = dbConnection.prepareStatement(sql2);
			preparedStmt2.setString(1, "High");
			ResultSet rs2 = preparedStmt2.executeQuery();
			System.out.println("\nHigh Priority Tickets:");
			while (rs2.next())
			{
				int selectId = rs2.getInt("id");
				String priority = rs2.getString("tpriority");
				
			    System.out.println("Ticket #" + selectId + " is open with priority " + priority);
			}
			preparedStmt3.executeUpdate();
						
			//close all statements and connections
			preparedStmt.close();
			preparedStmt1.close();
			preparedStmt2.close();		
			preparedStmt3.close();	
			dbConnection.close();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.getMessage();
		}
		 
	 }
}

//This method is used to connect to the database used in this project
public static Connection getDBConnection() 
{

	Connection dbConnection = null;

	try {

		Class.forName("com.mysql.jdbc.Driver");

	} catch (ClassNotFoundException e) {

		System.out.println(e.getMessage());

	}

	try {

		dbConnection = (Connection) DriverManager.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
				+ "&user=fp411&password=411");
		return dbConnection;

	} catch (SQLException e) {

		System.out.println(e.getMessage());

	}

	return dbConnection;

}

//This method is used to return the current time
private static Timestamp getCurrentTimeStamp() 
{
	java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime());
}
}