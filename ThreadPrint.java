import  java.util.concurrent.locks.ReentrantLock;
public class ThreadPrint {

    public  static  final  ReentrantLock locks=new ReentrantLock();

    public  static  int num=1;
    public  static  char letter='a';
    public  static volatile boolean flag=true;
    public static void main(String[] args) throws InterruptedException {


        Thread T1=new Thread(()->{
                while(num<=26){
                if (flag) {
                    try {
                        locks.lock();
                        System.out.println(Thread.currentThread().getName() + ":" + num);
                        num++;
                        flag = false;

                    } finally {
                        locks.unlock();
                    }
                }

            }}
        );

        Thread T2=new Thread(()-> {
            while(letter<='z')
                if (!flag) {
                    try {
                        locks.lock();
                        System.out.println(Thread.currentThread().getName() + ":" + letter);
                        letter++;
                        flag = true;


                    } finally {
                        locks.unlock();
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
