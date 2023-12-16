
package Session;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;



class Mutexlock {
    private int num;
    private boolean signal = false;
    private final ReentrantLock entlock;
    private final Condition self[];

    public Mutexlock(int taNum) {
        num = taNum;
        entlock = new ReentrantLock();
        self = new Condition[taNum];
        for (int i = 0; i < num; i++) {
            self[i] = entlock.newCondition();
        }
    }

    public void take() {
        // Acquire the lock
        entlock.lock();
        try {
            //the resource is available
            this.signal = true;
            try {

                self[num - 1].signal();
            } catch (NullPointerException ex) {
                //Null
            }
        } finally {
            // Release the lock to allow other threads to access the critical section
            entlock.unlock();
        }
    }

    public void release() {
        try {
            // Acquire the lock a
            entlock.lock();
            // Wait until that the resource is available
            while (!this.signal) {
                try {
                    // Put the current thread to wait until signaled by another thread
                    self[num - 1].await();
                } catch (NullPointerException ex) {

                } catch (InterruptedException ex) {
                    // Handle Null
                    Logger.getLogger(Mutexlock.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Reset the signal flag to false resource is no longer available
            this.signal = false;
        } finally {
            // Release the lock to allow other threads to access the critical section
            entlock.unlock();
        }
    }
}