import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A mock of a socket wrapper that allows us to test our Java sockets.
 */
public class ServerSocketWrapperSpy implements SocketWrapper {
  private InputStream customInputStream;
  private OutputStream customOutputStream;
  private boolean isClosed;

  /**
   * Takes in a string to act as a custom stream input, for testing purposes.
   *
   * @param customInput string that would act as the input stream to a socket.
   */
  public ServerSocketWrapperSpy(String customInput) {
    this.customInputStream = new ByteArrayInputStream(customInput.getBytes());
    this.customOutputStream = new ByteArrayOutputStream();
    this.isClosed = false;
  }

  @Override
  public InputStream getInputStream() {
    return this.customInputStream;
  }

  @Override
  public OutputStream getOutputStream() {
    try {
      this.customOutputStream.flush();
    } catch (IOException e) {
      System.out.println("IOException in your mock. Something seriously wrong has happened.");
    }
    return this.customOutputStream;
  }

  @Override
  public void close() {
    this.isClosed = true;
    System.out.println("Called closed!");
  }

  /**
   * Gets the output that's been written to our byte array output stream.
   *
   * @return our output.
   */
  public String getOutput() {
    return customOutputStream.toString();
  }

  /**
   * Checks to see if close has been called on this socket.
   *
   * @return whether or not the socket has been closed.
   */
  public boolean isClosed() {
    return this.isClosed;
  }
}
