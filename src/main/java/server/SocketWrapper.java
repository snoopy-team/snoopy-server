import java.io.InputStream;
import java.io.OutputStream;

public interface SocketWrapper {

  /**
   * Wrapper for getting the input stream for a Java Socket.
   *
   * @return input stream for a Java Socket.
   */
  public InputStream getInputStream();

  /**
   * Wrapper for getting the output stream of a Java Socket.
   *
   * @return output stream of a java socket.
   */
  public OutputStream getOutputStream();

  /**
   * Wrapper for the close function in a Java Socket.
   */
  void close();
}