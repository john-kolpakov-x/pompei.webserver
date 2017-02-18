package kz.pompei.webserver;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;


import static kz.pompei.webserver.RND.doubleToStr;

public class RNDTest {

  @Test
  public void doubleToStr_afterPoint() throws Exception {

    String s = doubleToStr(123.4565465636);

    Assertions.assertThat(s).isEqualTo("123.456547");

  }
}