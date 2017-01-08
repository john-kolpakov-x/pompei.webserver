package kz.pompei.webserver;

import org.testng.annotations.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class AsdTest {
  @Test
  public void test() throws Exception {
    Asd asd = new Asd();

    String str = asd.readSomeResource();

    assertThat(str).isEqualTo("Hello world!");
  }
}
