package hk.edu.cuhk.ie.iems5722.a4_1155152375.entities;

import java.io.Serializable;

public class Chatroom implements Serializable {

    private int id;
    private String name;

    public Chatroom(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setId(int id){
        this.id=id;
    }
}
