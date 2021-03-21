import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class that represents a socket wrapper for a Java server. Throws IllegalStateExceptions
 * whenever streams get hairy, otherwise calls Java Socket's built in functions.
 */
public class ServerSocketWrapper implements SocketWrapper {
  private Socket serverSocket;

  public ServerSocketWrapper(Socket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public OutputStream getOutputStream() {
    try {
      return this.serverSocket.getOutputStream();
    } catch (IOException e) {
      throw new IllegalStateException("Socket Output Exception");
    }
  }

  @Override
  public InputStream getInputStream() {
    try {
      return this.serverSocket.getInputStream();
    } catch (IOException e) {
      throw new IllegalStateException("Socket Input Exception");
    }
  }

  @Override
  public void close() {
    try {
      serverSocket.close();
    } catch (IOException e) {
      throw new IllegalStateException("Socket Closing Exception");
    }
  }
}