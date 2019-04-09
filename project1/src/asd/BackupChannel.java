package channels;

import java.io.IOException;
import java.net.DatagramPacket;

public class BackupChannel extends Channel {
    public BackupChannel(String MDB_Address, int MDB_Port) throws IOException {
        super(MDB_Address,MDB_Port);
    }
}