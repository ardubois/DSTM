// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.DBankLocks;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
//import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

public class BAccount extends UnicastRemoteObject implements IBAccount{ //Copyable<SConta>,IConta{
    private double balance;
    private Semaphore lock;

    public BAccount() throws RemoteException 
    { super();
      this.balance = 0;
      this.lock = new Semaphore(1);
    }
    
    public BAccount(int balance) throws RemoteException{
        super();
        this.balance=balance;
        this.lock = new Semaphore(1);
        }

    public double getBalance() throws RemoteException{
        return balance;
    }
    public void setBalance(double value) throws RemoteException{
        this.balance = value;
    }
   
   public void deposit(double value) throws RemoteException{
        this.balance = this.balance + value;
    }
   

   public boolean withdraw(double value) throws RemoteException{
    if (this.balance<value)
         return false;
    else {
         this.balance = this.balance - value;
         return true; 
        }
    }
   
   public boolean transfer(IBAccount c, double value) throws RemoteException{
    if (this.balance<value)
         return false;
    else {
         this.balance = this.balance - value;
         c.deposit(value);
         return true; 
        }
    }
   public boolean tryLock() throws RemoteException{
       return lock.tryAcquire();
   }
   public void unlock() throws RemoteException{
       lock.release();
   }
}
