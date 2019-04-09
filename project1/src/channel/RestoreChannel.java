package channels;

import java.io.IOException;
import java.net.DatagramPacket;

public class RestoreChannel extends Channel {
    public RestoreChannel(String MDR_Address, int MDR_Port) throws IOException {
        super(MDR_Address,MDR_Port);

    }
}