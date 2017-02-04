package kz.pompei.webserver;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Headers {
  public String request;
  public final Map<String, String> params = new HashMap<>();

  public String method, target, version;

  private final AtomicReference<ByteArrayOutputStream> contentStream
    = new AtomicReference<>(new ByteArrayOutputStream());

  public void writeByte(int b) {
    contentStream.get().write(b);
  }

  public void prepare() throws Exception {
    request = null;
    params.clear();
    boolean first = true;
    for (String line : contentStream.getAndSet(new ByteArrayOutputStream())
      .toString("UTF-8").split("\n")) {

      if (first) {
        request = line;
        first = false;
      } else {
        int i = line.indexOf(':');
        if (i >= 0) params.put(line.substring(0, i), line.substring(i + 1));
      }

    }

    {
      String[] split = request.split("\\s+");
      method = split[0];
      target = split[1];
      version = split[2];
    }
  }
}
