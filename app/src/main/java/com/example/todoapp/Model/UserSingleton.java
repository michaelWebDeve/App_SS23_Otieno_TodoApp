package com.example.todoapp.Model;

public class UserSingleton {

    private static UserSingleton userSingleton = new UserSingleton();

    private int userId;
    private String email;
    private String password;
    private UserSingleton(){

    }

    public  static  UserSingleton getInstance(){
        return  userSingleton;
    }
    public int getId() {
        return userId;
    }

    public void setId(int id) {
        this.userId = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

