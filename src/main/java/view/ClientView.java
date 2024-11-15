package view;

import interfaces.Connectable;
import interfaces.MessageController;
import abstracts.GUI;
import client.Client;
import server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Random;

public class ClientView extends GUI implements Connectable, MessageController {
    private static final Random RANDOM = new Random();
    private static final String IP_ADDRESS = "127.0.1.1";
    private static final String PORT = "8080";
    private JTextField status;
    private JTextField name;
    private JTextField password;
    private JButton btnLogin;
    private JButton btnLogout;
    private JTextArea messageHistory;
    private JTextField messageField;
    private Server server;
    private final Client client;



    public ClientView(Client client){
        this.client = client;
        client.setIsLogin(false);
        initConstants(RANDOM.nextInt(300), RANDOM.nextInt(500), 700, 500);

        setBounds(getPOS_X(), getPOS_Y(), getWIDTH(), getHEIGHT());

        add(createHeaderOfWindow(), BorderLayout.NORTH);
        add(createBodyOfWindow(), BorderLayout.CENTER);
        add(createFooterOfWindow(), BorderLayout.SOUTH);
        client.setName(name.getText());
        client.setPassword(password.getText());
        setVisible(true);
    }


    public String setStatus() {
        status.setHorizontalAlignment(JTextField.CENTER);
        if(this.server != null && server.getIsActive()){
            status.setBackground(Color.GREEN);
            return "SERVER ON";
        }else {
            status.setBackground(Color.RED);
            return "SERVER OFF";
        }
    }


    @Override
    public Component createHeaderOfWindow() {
        GUIBuilder builder = new GUIBuilder();

        JTextField ipAddress = new JTextField(IP_ADDRESS);
        JTextField port = new JTextField(PORT);
        status = new JTextField();
        name = new JTextField(client.getName());
        password = new JTextField(client.getPassword());
        btnLogin = new JButton("login");
        btnLogout = new JButton("Logout");
        btnLogout.setVisible(false);

        return builder
                .setLayout(new GridBagLayout())
                .addComponent(ipAddress, 0, 0, 0.3f, 1)
                .addComponent(port, 1, 0, 0.3f, 1)
                .addComponent(status, 2, 0, 0.3f, 1)
                .addComponent(name, 0,1, 0.3f, 1)
                .addComponent(password, 1,1, 0.3f, 1)
                .addComponent(btnLogin, _ -> logIn(), 2,1, 0.3f, 1)
                .addComponent(btnLogout, _ -> logOut(), 2, 1, 0.3f, 1)
                .build();

    }

    @Override
    public Component createBodyOfWindow() {
        messageHistory = new JTextArea();
        return new JScrollPane(messageHistory);
    }

    @Override
    public Component createFooterOfWindow() {
        messageField = new JTextField();
        GUIBuilder builder = new GUIBuilder();

        return builder
                .setLayout(new GridBagLayout())
                .addComponent(messageField, l -> sendMessage(messageField.getText()), 0, 0, 0.8f, 1)
                .addComponent(new JButton("Send"), l -> sendMessage(messageField.getText()), 1, 0, 0.2f, 1)
                .build();
    }

    private void logIn() {
        if(Server.isActive && !client.isLogin()){
            status.setText(setStatus());
            messageHistory.append(server.getMessageHistory());
            client.setIsLogin(true);
            hideHeaderFields();
            if(!server.getClients().contains(this))
                server.setClientView(this);
            this.server.sendMessageToServerGUI(this.client.getName() + " " + " is connected.\n");
        }else if(!Server.isActive && client.isLogin()){
            status.setText(setStatus());
            messageHistory.append("SERVER IS DOWN\n");
            client.setIsLogin(false);
        }else if(Server.isActive && client.isLogin()){
            status.setText(setStatus());
            messageHistory.append("You are already Login.\n");
        }
    }

    void logOut() {
        this.client.setIsLogin(false);
        this.server.sendMessageToServerGUI(this.client.getName() + " " + " is disconnected.\n");
        name.setOpaque(true);
        name.setEditable(true);
        name.setBackground(new Color(255, 255,255));
        name.setFocusable(true);
        password.setOpaque(true);
        password.setEditable(true);
        password.setBackground(new Color(255, 255,255));
        password.setText(this.client.getPassword());
        password.setFocusable(true);
        btnLogout.setVisible(false);
        btnLogin.setVisible(true);
        server.getClients().remove(this);
    }

    @Override
    public void sendMessage(String message) {
        if(Server.isActive && this.client.isLogin()){
            if(!message.isEmpty()){
                messageField.setText("");
                server.receiveMessageFromClientGUI(client.getName() + ": " + message + "\n");
            }
        }else if (Server.isActive && !this.client.isLogin()){
            messageHistory.append("You are not logging\n");
        }else{
            messageHistory.append("SERVER IS DOWN.\n");
        }
    }

    @Override
    public <T extends Connectable> void setConnection(T connectable) {
        if(connectable != null){
            server = (Server) connectable;
            status.setText(setStatus());
            server.setClientView(this);
        }
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if(e.getID() == WindowEvent.WINDOW_CLOSING){
            client.setIsLogin(false);
            server.sendMessageToServerGUI(client.getName() + " " + " is disconnected.\n");
            server.getClients().remove(this);
        }
    }

    public Client getClient() {
        return client;
    }

    public JTextArea getMessageHistory() {
        return messageHistory;
    }

    public JTextField getStatus() {
        return status;
    }


    private void hideHeaderFields(){
        name.setOpaque(false);
        name.setBackground(new Color(255, 255, 255, 100));
        name.setForeground(Color.BLACK);
        name.setFocusable(false);
        name.setEditable(false);
        password.setOpaque(false);
        password.setBackground(new Color(255, 255, 255, 100));
        password.setText("********");
        password.setForeground(Color.BLACK);
        password.setFocusable(false);
        password.setEditable(false);
        btnLogin.setVisible(false);
        btnLogout.setVisible(true);
    }
}
