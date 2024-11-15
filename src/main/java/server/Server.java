package server;

import interfaces.Connectable;
import interfaces.Repository;
import view.ClientView;
import view.ServerView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Server implements Repository {


    public static boolean isActive;
    private final List<ClientView> views;
    private final List<ClientView> accounts;
    private final StringBuilder messageLog;
    private ServerView serverView;

    public Server() {
        this.views = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.messageLog = new StringBuilder();
    }

    public boolean getIsActive(){
        return isActive;
    }

    public String getMessageLog(){
        return messageLog.toString();
    }

    @Override
    public String getMessageHistory() {
        StringBuilder sb = new StringBuilder();
        String path = String.format("src/main/java/chats/historyOfChat%s", ".txt");

        if(new File(path).exists()){
            try{
                BufferedReader reader = new BufferedReader(new FileReader(path));
                String line;
                while((line = reader.readLine()) != null){
                    sb.append(line).append("\n");
                    if(!messageLog.toString().contains(line))
                        messageLog.append(line).append("\n");
                }
            }catch (RuntimeException | IOException e){
                System.out.printf(e.getMessage());
            }
        }
        return sb.toString();
    }

    @Override
    public void saveMessageHistory(String log) {
        if (!messageLog.isEmpty()){
            try {
                String path = String.format("src/main/java/chats/historyOfChat%s", ".txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(path));
                writer.write(String.valueOf(messageLog));
                writer.close();
            }catch (IOException e){
                messageLog.append(e.getMessage());
            }
        }
    }

    public List<ClientView> getClients() {
        return views;
    }

    public List<ClientView> getAccounts() {
        return accounts;
    }

    public void setClientView(ClientView client) {
        this.views.add(client);
        this.accounts.add(client);
    }

    private void updateMessagesInGUIs(ClientView client, String message){
        if(client.getClient().isLogin())
            client.getMessageHistory().append(message);
        else
            sendMessageToServerGUI(message);
    }

    public void receiveMessageFromClientGUI(String message){
        for (ClientView client : getClients()){
            updateMessagesInGUIs(client, message);
        }
        messageLog.append(message);
        saveMessageHistory(messageLog.toString());
    }

    public void shutdown() throws IOException {
        isActive = false;
        for (ClientView client : getClients()){
            client.getClient().setIsLogin(false);
            client.setStatus();
        }
        saveMessageHistory(messageLog.toString());
    }

    public void sendMessageToServerGUI(String message) {
        this.serverView.getServerLog().append(message);
    }

    public void setServerView(ServerView serverView) {
        this.serverView = serverView;
    }

    @Override
    public <T extends Connectable> void setConnection(T connectable) {
    }
}
