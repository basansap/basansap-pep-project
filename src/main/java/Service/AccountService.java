package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account register(Account account){
        String username = account.getUsername();
        String password = account.getPassword();

        // check if username is blank, password is less then 4 character long
        // return null if both or one condition are true
        if(username == null || username.equals("") || 
            password.length() < 4 || accountDAO.checkAccountByUserName(username)){
            return null;
        }
                
        return accountDAO.register(account);
    }
    public Account login(Account account){
        return accountDAO.login(account);
    }

    


}
