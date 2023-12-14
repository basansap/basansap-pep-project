package DAO;

import java.sql.*;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    /**
     * #1. process new User registrations.
     * @param account contain username and password but not the account_id
     * @return sucessfully creatred account with account_id, username, and password
     */
    public Account register(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try {
            //  Write SQL logic here. You should only be register account with the username and password column, so that the database may
            //  automatically generate a primary key.
            String sql = "INSERT INTO account(username,password) values(?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString method here.
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * #2. Process User logins.
     * @param account have username and password
     * @return logined accounder holder if username is available in database
     */
    public Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            //SQL query to check if account with username and password is in the database
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString method here.
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Find an account using username
     */
    public boolean checkAccountByUserName(String username){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * from account where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) return true;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Find the account user with id
     * @param id, account id
     * @return Account realted to account_id
     */
    public boolean checkAccountById(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * from account where account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) return true;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
