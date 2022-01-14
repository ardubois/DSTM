/*
 *
 * Initial code taken from:
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * + Fixed Bugs
 * + Merged abstractions
 * + Added validation through a read set
 * + Added Distributed STM
 *
 * Universidade Federal de Pelotas 2022
 * 
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */
package TinyTM;


import java.rmi.*;
import TinyTM.ofree.TMObjServer;
import TinyTM.ofree.ITMObjServer;
//import java.util.concurrent.atomic.AtomicReference;

public interface ITransaction extends Remote{
  public enum Status {ABORTED, ACTIVE, COMMITTED};
    

  public Status getStatus() throws RemoteException;

  public boolean commit() throws RemoteException;

  public boolean abort() throws RemoteException;

/// readset

   public void addRS(ITMObjServer x, Object y) throws RemoteException;
 
  public  int sizeRS() throws RemoteException;
 
  public Object getRS(ITMObjServer x) throws RemoteException;
 
  public Object removeRS(ITMObjServer x) throws RemoteException;
 
  public void clearRS() throws RemoteException;
 
  public boolean validateReadSet() throws RemoteException;

  public void CMresolve(ITransaction enemy) throws RemoteException;
   

 }
