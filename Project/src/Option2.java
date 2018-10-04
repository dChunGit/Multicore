import java.util.concurrent.TimeUnit;

public class Option2 extends SuperOption2 implements Runnable {
   private static int perThread = 0;
   private static int counter;
   private static AbortLock lock;

   public static int parallelIncrement(int c, int numThreads) {
      lock = new AbortLock();
      counter = c;
//      counter = c;
//      perThread = 1 / numThreads;
//      int remainder = 1 % numThreads;
//      Thread[] threads = new Thread[numThreads];
//
//      for(int a = 0; a < numThreads; a++) {
//         boolean addOne = false;
//         if(remainder > 0) {
//            addOne = true;
//            remainder--;
//         }
//         Thread thread = new Thread(new Option2(addOne, a));
//         thread.start();
//         threads[a] = thread;
//      }
//
//      for(int a = 0; a < numThreads; a++) {
//         try {
//            threads[a].join();
//         } catch (Exception e) {
//            System.out.println("ERROR");
//         }
//      }
//
//      return counter;
      Thread thread = new Thread(new Option2(false, 0));
      thread.start();
      try {
         thread.join();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return counter;
   }

   private int myCount;
   private int id;
   private int changeMe = 0;

   private Option2(boolean addOne, int id) {
      this.id = id;
      this.myCount = perThread;
      if(addOne) this.myCount++;
   }

   @Override
   public void run() {
      /*for(int a = 0; a < this.myCount; a++) {
         increment(this.id);
      }*/
      lock.lock(this);
      changeMe = 1;
      counter = 1;
      tester = 1;
      setTestee(1);
      lock.abort(this);
      changeMe = 0;
      System.out.println(tester + " " + getTestee());
   }

   private static void increment(Option2 item) {
      lock.lock(item);
      counter = 1;
      lock.abort(item);
   }

}
