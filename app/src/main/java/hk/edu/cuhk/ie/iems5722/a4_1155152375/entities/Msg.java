package hk.edu.cuhk.ie.iems5722.a4_1155152375.entities;

public class Msg {
    private String message_only;
    private int type;//TYPE_RECEIVED=1; TYPE_SEND=0;
    public static final  int TYPE_RECEIVED=0;
    public static final  int TYPE_SEND=1;

    private int chatroom_id;
    private int user_id;
    private String username;
    private String message;
    private String contenttime;
    private int page;


    public Msg(int type, int chatroom_id, int user_id, String username, String message, String contenttime){
        this.username=username;
        this.type=type;
        this.chatroom_id=chatroom_id;
        this.user_id=user_id;
        this.message_only = message;
        this.message="User:"+username+"\r\n"+message+"\r\n"+contenttime;
        this.contenttime=contenttime;
    }
    public String getContent(){
        return message;
    }

    public String getMessage_only(){
        return message_only;
    }

    public String getUsername(){
        return username;
    }

    public int getType(){ return type;}

    public int getChatroom_id(){return chatroom_id;}

    public int getUser_id(){return user_id;}

    public int getPage(){return page;}

    public String getContenttime(){ return contenttime;}

}