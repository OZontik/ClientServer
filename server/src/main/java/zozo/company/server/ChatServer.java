package zozo.company.server;

import zozo.company.server.network.TCPConnection;
import zozo.company.server.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class ChatServer implements TCPConnectionListener{
    private static final Logger logger = Logger.getGlobal();
    public static void main( String[] args )
    {
        new ChatServer();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer(){
        ConnectionS connectionS = new ConnectionS();
        connectionS.readFileSettings();
        int serverPort = connectionS.getServerPort();

        System.out.println("Server running.... ");
        try(ServerSocket serverSocket = new ServerSocket(serverPort)) {
            while (true){
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e){
                    System.out.println("TCPConnection exception: " + e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(String value){
        try {
            LogManager.getLogManager().readConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Handler fileHandler = new FileHandler();
            logger.setUseParentHandlers(false);
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(value);
        logger.info(value);


        for (int i = 0; i < connections.size(); i++)  connections.get(i).sendString(value);

    }
}
