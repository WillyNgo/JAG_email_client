package com.williamngo.database;


import java.sql.*;

/**
 *
 * @author 1435707
 */
public class jagemail_database {
    public void populateEmailTable()
    {
        String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
        String user = "cs1435707";
        String password = "tripermu";
        
        String query = "INSERT INTO emails (receiver, sender, cc, message, html, typeFlag, receive_date, folder)";
        try(Connection conn = DriverManager.getConnection(url, user, password)){
            //PreparedStatement stmt = conn.prepareStatement();
        }catch(Exception e)
        {
            
        }
    }
}
