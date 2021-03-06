package utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.nio.charset.StandardCharsets;

public class Utility {
  public static int MAX_WAIT_TIME = 400;

  public static int PUTCHUNK_TRIES = 5;

  public static String CRLF = "\r\n";

  public static int INITIAL_WAIT_TIME = 1000;

  public static int DELETE_TRIES = 5;

  public static int CHUNK_SIZE = 64000;

  public static long CAPACITY = 8000000;

  public static final String getFileSHA(File file) {

    String fileUniqueStr = file.getName() + file.lastModified();

    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] messageDigest = md.digest(fileUniqueStr.getBytes());
      BigInteger no = new BigInteger(1, messageDigest);
      String hashtext = no.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }

      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      System.out.println("Exception thrown" + " for incorrect algorithm: " + e);

      return null;
    }
  }

  public static int getRandomValue(int value){
    Random gerador = new Random();
    return gerador.nextInt(value+1);
  }

  public static ArrayList<byte[]> getChunks(String path) throws IOException {
    ArrayList<byte[]> result = new ArrayList<byte[]>();
    File file = new File(path);
    long size = file.length();
    int chunksNr = (int) ((size / CHUNK_SIZE) + 1);
    try {
      FileInputStream fileStream = new FileInputStream(file);
      BufferedInputStream bufferedFile = new BufferedInputStream(fileStream);
      for (int i = 0; i < chunksNr; i++) {
        byte[] aux = new byte[CHUNK_SIZE];
        byte[] buf;
        int fileData = bufferedFile.read(aux);
        if (fileData == -1)
          buf = new byte[0];
        else if (fileData < CHUNK_SIZE)
          buf = new byte[fileData];
        else
          buf = new byte[CHUNK_SIZE];
        System.arraycopy(aux, 0, buf, 0, buf.length);
        result.add(buf);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return result;
  }
}