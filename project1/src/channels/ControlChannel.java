package channels;

import java.io.IOException;
import java.net.DatagramPacket;

public class ControlChannel extends Channel {
    public ControlChannel(String MC_Address, int MC_Port) throws IOException {
        super(MC_Address,MC_Port);

    }
}