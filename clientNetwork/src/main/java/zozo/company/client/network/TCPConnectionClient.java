package zozo.company.client.network;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TCPConnectionClient {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String addr;
    private int port;
    private String nickname;


    private static final Logger logger = Logger.getGlobal();
    public static String getCurrentDate() {
        Date time;
        String dTime;
        SimpleDateFormat dt1;
        time = new Date();
        dt1 = new SimpleDateFormat("HH:mm:ss");
        dTime = dt1.format(time);
        return " { " + dTime + " } ";
    }

    public TCPConnectionClient(String addr, int port) {
        this.addr = addr;
        this.port = port;
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.pressNickname();

            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {

            TCPConnectionClient.this.downService();
        }

    }



    private void pressNickname() {
        System.out.print("Press your nick: ");
        try {
            nickname = inputUser.readLine();
            out.write("Hello " + nickname + "\n");
            out.flush();
        } catch (IOException ignored) {
        }

    }


    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }


    private class ReadMsg extends Thread {
        @Override
        public void run() {
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

            String str;
            try {
                while (true) {
                    str = in.readLine();
                    if (str.equals("exit")) {
                        TCPConnectionClient.this.downService();
                        break;
                    }
                    System.out.println(str);
                    logger.info(str);

                }
            } catch (IOException e) {
                TCPConnectionClient.this.downService();
            }
        }
    }


    public class WriteMsg extends Thread {

        @Override
        public void run() {
            while (true) {
                String userWord;
                try {

                    userWord = inputUser.readLine();
                    if (userWord.equals("exit")) {
                        out.write("exit" + "\n");
                        TCPConnectionClient.this.downService();
                        break;
                    } else {
                        out.write("(" + getCurrentDate() + ") " + nickname + ": " + userWord + "\n");
                    }
                    out.flush();
                } catch (IOException e) {
                    TCPConnectionClient.this.downService();

                }

            }
        }
    }
}
