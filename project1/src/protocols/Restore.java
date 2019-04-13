package protocols;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;


import utility.Utility;
import file.*;
import peer.*;
import channels.*;

public class Restore implements Runnable{
    protected String path;
    protected int replicationDegree;
    
    public Restore(String path){
        this.path=path;
    }

    @Override
    public void run(){
        
    }
}