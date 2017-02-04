package kz.pompei.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class WebServer {
  private final int port;

  private final SocketExecutorManager socketExecutorManager = new SocketExecutorManager(new SocketExecutor());

  public WebServer(int port) {
    this.port = port;
  }

  private ServerSocket serverSocket;

  public void startAndJoin() throws IOException {
    socketExecutorManager.startThreads(7);

    serverSocket = new ServerSocket(port);

    while (socketExecutorManager.working.get()) {
      Socket socket;
      try {
        socket = serverSocket.accept();
      } catch (SocketException e) {
        break;
      }
      socketExecutorManager.execute(socket);
    }

  }

  public void stop() throws IOException {
    serverSocket.close();
    socketExecutorManager.stop();
  }
}
