// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.GenericLocks;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public interface IObject extends Remote{ //Copyable<SConta>,IConta{
    
  public int getField0() throws RemoteException;
  
   public void setField0(int v)  throws RemoteException;


   public int getField1() throws RemoteException;
  
   public void setField1(int v)  throws RemoteException;


   public int getField2() throws RemoteException;
  
   public void setField2(int v)  throws RemoteException;


   public int getField3() throws RemoteException;
  
   public void setField3(int v)  throws RemoteException;


    public int sumAll() throws RemoteException;

   public boolean tryLock() throws RemoteException;
   public void unlock() throws RemoteException;
}
