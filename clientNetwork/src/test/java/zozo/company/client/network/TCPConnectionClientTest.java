package zozo.company.client.network;

import junit.framework.TestCase;
import org.junit.Test;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TCPConnectionClientTest extends TestCase {
    @Test
    void getCurrentDateTest() {
        Date time;
        String dTime;
        String actual = TCPConnectionClient.getCurrentDate();
        time = new Date();

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
        dTime = DATE_FORMAT.format(time);
        String expected = " { " + dTime + " } ";
        assertEquals(expected, actual);

    }

}