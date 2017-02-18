package kz.pompei.webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.locks.StampedLock;
import org.testng.annotations.Test;


import static kz.pompei.webserver.RND.nanoTimeToStr;

public class WebServerTest {
  @Test
  public void testStartAndJoin() throws Exception {
    System.out.println("Hi");

    ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    System.out.println(map);
  }

  @Test
  public void hello() throws Exception {

    final int count = 1_000_000, valueRowCount = 7;

    final String[] keys = new String[count];

    {
      long time = System.nanoTime();
      for (int i = 0; i < count; i++) {
        keys[i] = RND.str(10);
      }
      time = System.nanoTime() - time;
      System.out.println("Keys prepared " + nanoTimeToStr(time));
    }

    final String[][] values = new String[valueRowCount][];
    {
      long time = System.nanoTime();
      for (int j = 0; j < valueRowCount; j++) {
        values[j] = new String[count];
        for (int i = 0; i < count; i++) {
          values[j][i] = RND.str(10);
        }
      }
      time = System.nanoTime() - time;
      System.out.println("Values prepared " + nanoTimeToStr(time));
    }

    Map<String, String> map = new HashMap<>();
    {
      long time = System.nanoTime();
      for (int i = 0; i < count; i++) {
        map.put(keys[i], values[0][i]);
      }
      time = System.nanoTime() - time;
      System.out.println("Map created " + nanoTimeToStr(time));
    }

    System.out.println("map.size = " + map.size());

    ForkJoinPool forkJoinPool = new ForkJoinPool();

    ForkJoinTask<Void> modify = forkJoinPool.submit(() -> {

      System.out.println("Hello world 1");
      System.out.println("Hello world 2");

      return null;
    });
  }


  @Test
  public void hello1() throws Exception {

    StampedLock sl = new StampedLock();
    long stamp = sl.readLock();

    ForkJoinTask<Integer> get123 = ForkJoinTask.adapt(() -> 123);
    get123.fork();
    Integer a123 = get123.get();
    System.out.println(a123);

  }
}
