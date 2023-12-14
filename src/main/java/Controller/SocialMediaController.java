package Controller;

import java.util.List;

import org.eclipse.jetty.server.RequestLog.Collection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.MessageService;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be.
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postCreateMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageByAccountIdHandler);
        return app;
    }

    /**
     * #1. Register Account Handler: POST method
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegisterHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(),Account.class);

        Account registerdAccount = accountService.register(account);
        if(registerdAccount != null){
            ctx.json(registerdAccount).status(200);
        }else{
            //Client error
            ctx.status(400);
        }
    }

    /**
     * #2. Login Account Handler: POST method
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(),Account.class);

        Account loginAccount = accountService.login(account);
        if(loginAccount!= null){
            ctx.json(loginAccount).status(200);
        }else{
            // unauthorized response status
            ctx.status(401); 
        }
    }

    /**
     * #3. Create new Message Handler: POST method
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postCreateMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message= mapper.readValue(ctx.body(),Message.class);

        System.out.println("postCreateMessageHandler:"+ message.toString());
        Message createdMessage = messageService.creatMessage(message);

        if(createdMessage!= null){
            ctx.json(createdMessage).status(200);
        }else{
            // client error
            ctx.status(400);
        }
    }

    /**
     * #4. return all Messages Handler: endpoint GET localhost:8080/messages.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context ctx){
        List<Message> messagesList = messageService.getAllMessages();
        ctx.json(messagesList).status(200);
    }

    /**
     * #5. retrieve message by message_id handler: endpoint GET localhost:8080/messages/{message_id}.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByIdHandler(Context ctx){
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if(message != null){
            ctx.json(message).status(200);
        }else{
            ctx.status(200);
        }
        
    }

    /**
     * #6. delete message by message_id handler: endpoint DELETE localhost:8080/messages/{message_id}.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageByIdHandler(Context ctx){
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageById(message_id);

        if(message != null){
            ctx.json(message).status(200);
        }else{
            ctx.status(200);
        }
    }

    /**
     * #7. Update a message text identified by a message ID: endpoint PATCH localhost:8080/messages/{message_id}.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void patchMessageByIdHandler(Context ctx){
        Message newMessage = ctx.bodyAsClass(Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        
        Message message = messageService.patchMessage(message_id,newMessage.getMessage_text());
        if(message != null){
            ctx.json(message).status(200);
        }else{
            //Client error
            ctx.status(400);
        } 
    }

    /**
     * #8.Retrieve all messages written by a particular user: endpoint GET localhost:8080/accounts/{account_id}/messages.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByAccountIdHandler(Context ctx){
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messagesList = messageService.getAllMessagesByAccountId(account_id);

        ctx.json(messagesList).status(200);
    }

    










}