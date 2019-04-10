package utility;


import java.io.File;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class Utility {
  public static int MAX_WAIT_TIME = 400;

  public static int PUTCHUNK_TRIES = 5;

  public static int CHUNK_SIZE = 64000;



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

  static String sha256(String input) throws NoSuchAlgorithmException {
    MessageDigest mDigest = MessageDigest.getInstance("SHA256");
    byte[] result = mDigest.digest(input.getBytes());
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < result.length; i++) {
      sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
  }

  public static ArrayList<String> getChunks(String path, int headerSize) throws IOException{
    System.out.println(headerSize);
    ArrayList<String> result = new ArrayList<String>();
    File file = new File(path);
    long size = file.length();
    int chunksNr = (int) ((size / CHUNK_SIZE) + 1);
    try {
      FileInputStream fileInput = new FileInputStream(file);
      for (int i = 0; i < chunksNr; i++) {
        byte[] buf = new byte[CHUNK_SIZE];
        fileInput.read(buf);
        System.out.println(buf.length);
        String value = new String(buf, StandardCharsets.UTF_8);
        String rest = value +
        result.add(value);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    
    return result;
  }
}