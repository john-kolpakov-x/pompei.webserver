package kz.pompei.webserver;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.StampedLock;

public class PairFactory {

  private abstract class AbstractPair implements Pair {
    long value = 0, invertedValue = 0;

    final AtomicInteger incCount = new AtomicInteger(0);
    final AtomicLong incNanos = new AtomicLong(0);

    @Override
    public int incCount() {
      return incCount.get();
    }

    @Override
    public long incNanos() {
      return incNanos.get();
    }
  }

  private class PairNoSync extends AbstractPair {
    @Override
    public void reset() {
      value = 0;
      invertedValue = 0;
    }

    @Override
    public void inc(long delta) {
      long time = System.nanoTime();

      value += delta;
      invertedValue -= delta;

      time = System.nanoTime() - time;

      incNanos.addAndGet(time);
      incCount.incrementAndGet();
    }

    @Override
    public long sum() {
      return value + invertedValue;
    }
  }

  public Pair create(PairType pairType) {
    switch (pairType) {
      case NO_SYNC:
        return new PairNoSync();
      case Stamped_Lock:
        return new PairStampedLock();
      case Synchronized:
        return new PairSynchronized();
      default:
        throw new IllegalArgumentException("Unknown pair type = " + pairType);
    }
  }

  private class PairStampedLock extends AbstractPair {

    final StampedLock locker = new StampedLock();

    @Override
    public void reset() {
      long stamp = locker.writeLock();
      try {

        value = 0;
        invertedValue = 0;

      } finally {
        locker.unlock(stamp);
      }
    }

    @Override
    public void inc(long delta) {
      long time = System.nanoTime();

      long stamp = locker.writeLock();
      try {
        value += delta;
        invertedValue -= delta;
      } finally {
        locker.unlock(stamp);
      }
      time = System.nanoTime() - time;

      incNanos.addAndGet(time);
      incCount.incrementAndGet();
    }

    @Override
    public long sum() {
      long stamp = locker.readLock();
      try {
        return value + invertedValue;
      } finally {
        locker.unlock(stamp);
      }
    }
  }

  private class PairSynchronized extends AbstractPair {
    @Override
    public void reset() {
      synchronized (this) {
        value = 0;
        invertedValue = 0;
      }
    }

    @Override
    public void inc(long delta) {
      long time = System.nanoTime();

      synchronized (this) {
        value += delta;
        invertedValue -= delta;
      }

      time = System.nanoTime() - time;

      incNanos.addAndGet(time);
      incCount.incrementAndGet();
    }

    @Override
    public long sum() {
      synchronized (this) {
        return value + invertedValue;
      }
    }
  }
}
