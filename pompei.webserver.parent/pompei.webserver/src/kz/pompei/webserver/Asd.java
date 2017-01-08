package kz.pompei.webserver;

import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class Asd {

  public String readSomeResource() throws Exception {
    InputStream in = getClass().getResourceAsStream("asd.txt");


    String str = IOUtils.toString(in, "UTF-8");

    return str;
  }
}
