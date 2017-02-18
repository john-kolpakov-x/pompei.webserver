package kz.pompei.webserver;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinTask;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PairFactoryTest {

  private PairFactory pairFactory;

  @DataProvider
  public static Object[][] pairsInList_DP() {
    final int length = PairType.values().length;
    final Object[][] ret = new Object[length][];
    for (int i = 0; i < length; i++) {
      ret[i] = new Object[]{PairType.values()[i]};
    }
    return ret;
  }

  @BeforeMethod
  public void createPairFactory() {
    pairFactory = new PairFactory();
  }

  @Test(dataProvider = "pairsInList_DP")
  public void pairsInList(PairType pairType) throws Exception {

    final int pairCount = 3, valueCount = 30_000, threadCount = 60;

    final Random random = new SecureRandom();

    long values[] = new long[valueCount];
    for (int i = 0; i < valueCount; i++) {
      values[i] = random.nextInt(20_000) - 10_000;
    }

    final List<Pair> pairList = new ArrayList<>();

    for (int i = 0; i < pairCount; i++) {
      pairList.add(pairFactory.create(pairType));
    }

    final List<ForkJoinTask<?>> threadList = new ArrayList<>();

    for (int i = 0; i < threadCount; i++) {
      final Pair pair = pairList.get(i % pairList.size());
      List<Long> list = new ArrayList<>(values.length);
      for (long value : values) {
        list.add(value);
      }
      Collections.shuffle(list, random);
      threadList.add(ForkJoinTask.adapt(() -> list.forEach(pair::inc)));
    }

    threadList.forEach(ForkJoinTask::fork);
    threadList.forEach(ForkJoinTask::join);

    System.out.println("Pairs " + pairType + " : valueCount = " + valueCount);
    pairList.forEach(p -> System.out.println("  sum = " + RND.toLenRight("" + p.sum(), 10)
      + ", inc avg time = " + RND.nanoTimeToStr(p.incNanos(), p.incCount(), 10)));
  }
}
