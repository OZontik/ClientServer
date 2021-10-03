package zozo.company.client;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ConnectionS {
    private static int PORT;
    private static String IP_ADDR;

    public int getServerPort() {
        return PORT;
    }

    public String getIpAddr() {
        return IP_ADDR;
    }

    public void readFileSettings() {
        String[] arrayLine = new String[2];
        try {
            FileReader fileReader = new FileReader("C:\\Users\\Измерение\\IdeaProjects\\chat1\\param.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for (int i = 0; i < 2; i++){
                arrayLine[i] = bufferedReader.readLine();
            }
            PORT = Integer.parseInt(arrayLine[0]);
            IP_ADDR = arrayLine[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
