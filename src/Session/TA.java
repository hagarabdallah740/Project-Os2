
package Session;

import java.util.concurrent.Semaphore;


public class TA implements Runnable {
    private int numberofTA;
    private int numberofchairs;
    private Mutexlock wakeup;
    private Semaphore chairs;
    private Semaphore available;
    private Thread t;
    private int helpTime = 5000;


    public TA(Mutexlock wakeup, Semaphore chairs, Semaphore available, int numberofTA, int numberofchairs) {
        t = Thread.currentThread();
        this.wakeup = wakeup;
        wakeup = new Mutexlock(numberofTA);
        this.chairs = chairs;
        this.available = available;
        this.numberofTA = numberofTA;
        this.numberofchairs = numberofchairs;
    }

    @Override
    public void run() {
        while (true) {

            try {
                // Release the "wakeup" semaphore
                wakeup.release();

                // Sleep for a specified help time
                t.sleep(helpTime);

                // Check if the number of available permits in "chairs" != to the total number of chairs
                if (chairs.availablePermits() != numberofchairs) {
                    // Enter a loop if the condition is true
                    do {
                        // Sleep for a specified help time
                        t.sleep(helpTime);

                        // Release allowing a waiting thread to proceed
                        chairs.release();
                    } while (chairs.availablePermits() != numberofchairs);
                    // Repeat the loop until the total number of chairs is available
                }
            } catch (InterruptedException e) {
                //  loop  interrupted during processing
                continue;
            }
        }
    }
}