package kz.pompei.webserver;

public interface Pair {

  void reset();

  void inc(long delta);


  long sum();

  int incCount();

  long incNanos();

}
