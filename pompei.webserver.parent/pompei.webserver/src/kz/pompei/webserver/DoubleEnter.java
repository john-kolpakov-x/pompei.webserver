package kz.pompei.webserver;

public class DoubleEnter {

  private final byte[] buffer = new byte[4];
  private int size = 0;

  public boolean hasDoubleEnter() {

    int enterCount = 0;

    for (byte b : buffer) {
      switch (b) {
        case 10:
          enterCount++;
          continue;
        case 13:
          continue;
        default:
          return false;
      }
    }

    return enterCount >= 2;
  }

  public void writeByte(int b) {
    if (size < buffer.length) {
      buffer[size++] = (byte) b;
      return;
    }

    for (int i = 0, c = buffer.length - 1; i < c; i++) {
      buffer[i] = buffer[i + 1];
    }

    buffer[buffer.length - 1] = (byte) b;
  }

  public void clean() {
    size = 0;
  }
}
