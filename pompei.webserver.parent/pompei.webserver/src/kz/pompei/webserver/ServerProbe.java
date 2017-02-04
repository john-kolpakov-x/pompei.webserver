package kz.pompei.webserver;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerProbe {
  public static void main(String[] args) throws Throwable {

    ServerSocket serverSocket = new ServerSocket(8989);

    Socket socket = serverSocket.accept();

    SocketExecutor socketExecutor = new SocketExecutor();
    socketExecutor.execute(socket);
  }
}
