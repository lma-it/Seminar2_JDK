package client;

import utils.AccauntDB;

public class Client {

    private String name;
    private String password;
    private boolean isLogin;


    public Client() {
        this.name = AccauntDB.getRandomName();
        this.password = AccauntDB.getRandomPassword();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogin() {
        return this.isLogin;
    }

    public void setIsLogin(boolean login) {
        this.isLogin = login;
    }

    public boolean getLogin() {
        return this.isLogin;
    }
}