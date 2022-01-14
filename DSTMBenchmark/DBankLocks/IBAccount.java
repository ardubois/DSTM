// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.DBankLocks;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public interface IBAccount extends Remote{ //Copyable<SConta>,IConta{
    
   public double getBalance() throws RemoteException;
   public void setBalance(double value) throws RemoteException;
   
   public void deposit(double value) throws RemoteException;
   

   public boolean withdraw(double value) throws RemoteException;
   
   public boolean transfer(IBAccount c, double value) throws RemoteException;

   public boolean tryLock() throws RemoteException;
   public void unlock() throws RemoteException;
}
