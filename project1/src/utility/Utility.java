package utility;

import java.io.File;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;

public class Utility {
  public static int MAX_WAIT_TIME = 400;
  public static int PUTCHUNK_TRIES = 5;

  // Receives a file, gets its name and use sha256 to encode it
  public static final String getFileName(File file) {
    String fileUniqueStr = file.getName();
    MessageDigest sha_256 = MessageDigest.getInstance("SHA-256");
    byte[] hashed = sha_256.digest(fileUniqueStr.getBytes());
    return DatatypeConverter.printHexBinary(hashed);
  }
}