
package Session;

import java.util.concurrent.Semaphore;


public class Student implements Runnable {
    private int numberofchairs;
    private int numberofTA;
    private int programTime;
    private int counts = 0;
    private int studentNum;
    private Mutexlock wakeup;
    private Semaphore chairs;
    private Semaphore available;
    private int helpTime = 5000;
    private Thread t;

    public Student(int programTime, Mutexlock wakeup, Semaphore chairs, Semaphore available, int studentNum, int numberofchairs, int numberofTA) {
        this.programTime = programTime;
        this.wakeup = wakeup;
        wakeup = new Mutexlock(numberofTA);
        this.chairs = chairs;
        this.available = available;
        this.studentNum = studentNum;
        t = Thread.currentThread();
        this.numberofchairs = numberofchairs;
        this.numberofTA = numberofTA;
    }

    @Override
    public void run() {
        while (true) {
            try {
                t.sleep(programTime * 1000);
                // Acquire the "available" semaphore
                if (available.tryAcquire()) {
                    try {
                        // Block until wakeup anthore semaphore
                        wakeup.take();

                        // Sleep for a specified help time
                        t.sleep(helpTime);
                    } catch (InterruptedException e) {

                        continue;
                    } finally {
                        // Release the "available" semaphore
                        available.release();
                    }
                } else {
                    // If available Chairs
                    if (chairs.tryAcquire()) {
                        try {
                            // Acquire
                            available.acquire();

                            // Sleep
                            t.sleep(helpTime);

                            // Release
                            available.release();
                        } catch (InterruptedException e) {

                            continue;
                        }
                    } else {
                        // If both "available" and "chairs" not acquired, sleep for a specified help time
                        t.sleep(helpTime);
                    }
                }
            } catch (InterruptedException e) {
                // Break the loop
                break;
            }
        }
    }
}