package view;

import client.Client;
import interfaces.Connectable;
import abstracts.GUI;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ServerView extends GUI implements Connectable {

    private final JTextArea serverLog;
    private Server server;

    public ServerView() {

        serverLog = new JTextArea();
        initConstants(900, 200, 400, 400);
        setBounds(getPOS_X(), getPOS_Y(), getWIDTH(), getHEIGHT());
        add(createHeaderOfWindow(), BorderLayout.CENTER);
        add(createFooterOfWindow(), BorderLayout.SOUTH);
        setResizable(false);
        setVisible(true);
    }


    @Override
    public Component createHeaderOfWindow() {
        GUIBuilder builder = new GUIBuilder();

        return builder
                .addComponent(new JScrollPane(serverLog), 0, 0, 1f, 1f)
                .build();
    }

    @Override
    public Component createFooterOfWindow() {

        GUIBuilder builder = new GUIBuilder();
        return builder
                .addComponent(new JButton("Start"), l -> startServer(), 0, 0, 0.45f, 1)
                .addComponent(new JButton("New Chat"), l -> addNewClient(), 1, 0, 0.15f, 1)
                .addComponent(new JButton("Stop"), l -> stopServer(), 2, 0, 0.45f, 1)
                .build();
    }

    private void stopServer() {
        try{
            Server.isActive = false;
            for (ClientView client : server.getAccounts()) {
                client.getStatus().setText(client.setStatus());
            }
            while(!server.getClients().isEmpty()){
                server.getClients().getFirst().logOut();
            }
            server.saveMessageHistory(server.getMessageLog());
        }catch (RuntimeException e){
            server.sendMessageToServerGUI(e.getMessage());
        }

    }

    private void startServer() {
        if (!Server.isActive) {
            server.setServerView(this);
            Server.isActive = true;
            serverLog.append("SERVER IS STARTED.\n");
            if(server.getClients().size() == server.getAccounts().size()){
                for (ClientView client : server.getClients()) {
                    client.getStatus().setText(client.setStatus());
                }
            }else{
                server.getClients().clear();
                for (ClientView clientView : server.getAccounts()){
                    server.getClients().add(clientView);
                }
                for (ClientView client : server.getClients()) {
                    client.getStatus().setText(client.setStatus());
                }
            }

        }
    }

    @Override
    public <T extends Connectable> void setConnection(T connectable) {
        if(connectable != null){
            this.server = (Server) connectable;
        }
    }

    public JTextArea getServerLog() {
        return serverLog;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if(e.getID() == WindowEvent.WINDOW_CLOSING){
            try {
                this.server.shutdown();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.exit(0);
        }
    }

    private void addNewClient(){
        ClientView newChat = new ClientView(new Client());
        newChat.setConnection(this.server);
    }

}
