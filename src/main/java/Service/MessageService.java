package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;
import java.util.*;

public class MessageService {
    private MessageDAO messageDAO;

    // default construtor
    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    // construction with MessageDAO object
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    // #3. creation of new message
    public Message creatMessage(Message message){
        String message_text = message.getMessage_text();
        int posted_by = message.getPosted_by();
        boolean exesting_user = new AccountDAO().checkAccountById(posted_by);
        //edge cases 
        if(message_text == null || message_text.equals("") || message_text.length() > 255 || !exesting_user) return null;
        return messageDAO.creatMessage(message);
    }

    // #4. retrieve all messages
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    //#5. retrieve a message by its ID.
    public Message getMessageById(int message_id){
        return messageDAO.getMessageById(message_id);
    }

    //#6. delete a message identified by a message ID
    public Message deleteMessageById(int message_id){
        return messageDAO.deleteMessageById(message_id);
    }

    //#7. update a message text identified by a message ID.
    public Message patchMessage(int message_id, String new_message){
        // check edge cases
        if(new_message == null || new_message.equals("") || new_message.length() > 255) return null;

        return messageDAO.patchMessage(message_id, new_message);
    }

    //#8. retrieve all messages written by a particular user
    public List<Message> getAllMessagesByAccountId(int account_id){
        return messageDAO.getAllMessagesByAccountId(account_id);
    }
}
