package protocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;



public class Delete implements Runnable{


    public Backup(String file_path, int replicationDegree) {
		this.path = file_path;
		this.replicationDegree = replicationDegree;
    }
    
}