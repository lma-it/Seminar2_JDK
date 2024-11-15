import client.Client;
import server.Server;
import view.ClientView;
import view.ServerView;

public class ChatChatMain {
    public static void main(String[] args) {
        Server server = new Server();
        new ServerView().setConnection(server);
        new ClientView(new Client()).setConnection(server);
        new ClientView(new Client()).setConnection(server);
    }
}
