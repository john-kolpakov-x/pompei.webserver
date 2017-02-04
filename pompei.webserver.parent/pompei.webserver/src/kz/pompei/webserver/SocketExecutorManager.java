package kz.pompei.webserver;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketExecutorManager {

  private final SocketExecutor socketExecutor;

  public SocketExecutorManager(SocketExecutor socketExecutor) {
    this.socketExecutor = socketExecutor;
  }

  public final AtomicBoolean working = new AtomicBoolean(true);

  private final BlockingQueue<ExecuteThread> freeThreads = new LinkedBlockingQueue<>();

  private class ExecuteThread extends Thread {

    final BlockingQueue<Socket> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
      OUT:
      while (working.get()) {

        while (working.get() && queue.size() > 0) {
          Socket socket = null;
          try {
            socket = queue.take();
          } catch (InterruptedException ignore) {
          }

          if (!working.get()) break OUT;

          if (socket != null) try {
            socketExecutor.execute(socket);
          } catch (Throwable throwable) {
            throwable.printStackTrace();
          }
        }

        if (!working.get()) break OUT;

        freeThreads.add(this);
      }
    }

    public void execute(Socket socket) {
      queue.add(socket);
    }
  }


  public void execute(Socket socket) {
    if (socket == null) throw new NullPointerException("socket == null");

    if (!working.get()) throw new RuntimeException("Not working");

    ExecuteThread thread;
    try {
      thread = freeThreads.take();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    thread.execute(socket);
  }

  public void startThreads(int threadCount) {
    if (!working.get()) throw new RuntimeException("Not working");
    for (int i = 0; i < threadCount; i++) {
      new ExecuteThread().start();
    }
  }

  public void stop() {
    if (!working.get()) return;
    working.set(false);

    while (freeThreads.size() > 0) {
      ExecuteThread thread = freeThreads.poll();
      if (thread != null) thread.interrupt();
    }
  }
}
