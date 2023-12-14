package DAO;

import java.sql.*;
import java.util.*;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    /**
     * # 3. Process the creation of new messages.
     * @param message, provided Message object which contain all component except for the message_id
     * @return Message after inserting new message.
     */
    public Message creatMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) values(?,?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString method here.
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return  null;
    }

    /**
     * # 4. Retrieve all messages in message database table
     * @return List of all the messages in the message database table
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                                            rs.getInt("posted_by"), 
                                            rs.getString("message_text"), 
                                            rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * # 5. Retrieve a message by its ID.
     * @param id message_id as int
     * @return Message which belong to the message_id provided
     */
    public Message getMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                return new Message(rs.getInt("message_id"), 
                                    rs.getInt("posted_by"), 
                                    rs.getString("message_text"), 
                                    rs.getLong("time_posted_epoch"));
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * #6. delete a message identified by a message ID.
     * @param message_id
     * @return true if record deleted based on message_id, false otherwise
     */
    public Message deleteMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        Message deletMessage = getMessageById(message_id);
        try{
            
            String sql = "delete FROM message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            int  res = preparedStatement.executeUpdate();

            if(res == 1) return deletMessage;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * #7. Patch the message with new message_text for a given message_id
     * @param message_id, message_id to be updated
     * @param new_message new message as String to be updated in message database which belong to message_id
     * @return
     */
    public Message patchMessage(int message_id, String new_message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, new_message);
            preparedStatement.setInt(2, message_id);
            int res = preparedStatement.executeUpdate();

            // return the updated message by calling the getMessageById method 
            // with the message_id we used to patch the database
            if(res == 1) return getMessageById(message_id);

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * # 8. retrieve all the message written by a perticular user
     * @param account_id, user_id for perticular user
     * @return List of Messages posted_by perticular account_id
     */
    public List<Message> getAllMessagesByAccountId(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                                            rs.getInt("posted_by"), 
                                            rs.getString("message_text"), 
                                            rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
}
