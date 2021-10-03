package zozo.company.client;


import zozo.company.client.network.TCPConnectionClient;



public class ClientNet {

    public static void main(String[] args) {
        ConnectionS connectionS = new ConnectionS();
        connectionS.readFileSettings();
        int serverPort = connectionS.getServerPort();
        String ipAddr = connectionS.getIpAddr();
        new TCPConnectionClient(ipAddr, serverPort);
    }
}
