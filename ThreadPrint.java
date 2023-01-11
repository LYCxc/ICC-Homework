import  java.util.concurrent.locks.ReentrantLock;
import  java.util.concurrent.locks.Condition;
public class ThreadPrint {
    public  static  final  ReentrantLock locks=new ReentrantLock();
    private  static  int num=1;
    private static  char letter='a';
    private static volatile boolean flag=true;
    public static void main(String[] args) throws InterruptedException {
        Thread T1=new Thread(()->{
                while(num<=26){
                   if (flag) {
                       locks.lock();
                            try{
                        System.out.println(Thread.currentThread().getName() + ":" + num);
                        num++;
                        flag = false;
                            }
                            finally {
                                locks.unlock();
                                    }
                            }
            }}
        );

        Thread T2=new Thread(()-> {
            while (letter <= 'z') {
                if (!flag) {
                    locks.lock();
                          try {
                        System.out.println(Thread.currentThread().getName() + ":" + letter);
                        letter++;
                        flag = true;
                              }
                          finally {
                               locks.unlock();
                          }
                }
            }
        }
        );
        T1.start();
        T2.start();
        T1.join();
        T2.join();
    }
}

class ThreadPrint2{
    private static int num=1;
    private  static char letter='a';
    private static final  ReentrantLock lock=new ReentrantLock();
    private static final Condition isNumber=lock.newCondition();
    private static final Condition isLetter=lock.newCondition();
    public static void main(String[] args) throws  InterruptedException {
      Thread t1 =new Thread(()->{
           lock.lock();
           try {
               while(num<=26)
               {   isNumber.signal();
                   System.out.println(Thread.currentThread().getName()+":"+num);
                   isLetter.await();
                   num++;
               }
               }
           catch (InterruptedException e){
               e.printStackTrace();
           }
           finally {
               lock.unlock();
           }

           });
      Thread t2=new Thread(()->{
          lock.lock();
          try {
              while(letter<='z')
              {
                  isLetter.signal();
                  System.out.println(Thread.currentThread().getName()+":"+letter);
                  letter++;
                  isNumber.await();
              }
          }
          catch (InterruptedException e){
              e.printStackTrace();
          }
          finally {
              lock.unlock();
          }
      });
      t1.start();
      t2.start();
    }
}
