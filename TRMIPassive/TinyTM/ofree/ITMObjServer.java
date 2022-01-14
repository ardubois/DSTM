// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package TinyTM.ofree;

import java.rmi.*;
import TinyTM.*;


public interface ITMObjServer<T extends Copyable<T>> extends Remote{
  

  

  public T  openWriteRemote(ITransaction tx) throws RemoteException;

  
  
  public T openReadRemote(ITransaction tx) throws RemoteException;

  public T openSequential() throws RemoteException;

 // public void close() throws RemoteException;
 
  public boolean validateEntry(Object version) throws RemoteException;
 
  
}
