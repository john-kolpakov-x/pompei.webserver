package kz.pompei.webserver;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class SocketExecutor {
  public void execute(Socket socket) throws Throwable {
    InputStream inputStream = socket.getInputStream();
    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 8 * 1024);

    DoubleEnter doubleEnter = new DoubleEnter();
    Headers headers = new Headers();

    while (!doubleEnter.hasDoubleEnter()) {
      int b = bufferedInputStream.read();
      if (b < 0) break;

      headers.writeByte(b);
      doubleEnter.writeByte(b);
    }

    doubleEnter.clean();
    headers.prepare();

    headers.params.get("");

    System.out.println("request " + headers.request);
    for (Map.Entry<String, String> e : headers.params.entrySet()) {
      System.out.println(e.getKey() + "=" + e.getValue());
    }
  }
}
