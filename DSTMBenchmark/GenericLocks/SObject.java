// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.GenericLocks;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
//import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

public class SObject extends UnicastRemoteObject implements IObject{ //Copyable<SConta>,IConta{
    private int field0;
    private int field1;
    private int field2;
    private int field3;
    private Semaphore lock;

    public SObject() throws RemoteException 
    { super();
      this.lock = new Semaphore(1);
      this.field0 = 0;
      this.field1 = 0;
      this.field2 = 0;
      this.field3 = 0;
    }
    
    public SObject(int initialValue) throws RemoteException{
        super();
        this.lock = new Semaphore(1);
        this.field0 = initialValue;
          this.field1 = initialValue;
          this.field2 = initialValue;
          this.field3 = initialValue;
        }

    
      public int getField0() throws RemoteException
   {
           return field0;
   }
  
   public void setField0(int v)  throws RemoteException
   {
     	this.field0 = v; 
   }

  public int getField1() throws RemoteException
   {
           return field1;
   }
  
   public void setField1(int v)  throws RemoteException
   {
     	this.field1 = v; 
   }

  public int getField2() throws RemoteException
   {
           return field2;
   }
  
   public void setField2(int v)  throws RemoteException
   {
     	this.field2 = v; 
   }

  public int getField3() throws RemoteException
   {
           return field3;
   }
  
   public void setField3(int v)  throws RemoteException
   {
     	this.field3 = v; 
   }   

  public int sumAll() throws RemoteException
   {
           return field0+field1+field2+field3;
   }
   public boolean tryLock() throws RemoteException{
       return lock.tryAcquire();
   }
   public void unlock() throws RemoteException{
       lock.release();
   }
}
