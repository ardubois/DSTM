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

import TinyTM.exceptions.AbortedException;
import TinyTM.exceptions.PanicException;
import java.util.concurrent.Callable;
import java.util.Map;
import TinyTM.ofree.TMObjServer;
import TinyTM.ofree.ITMObjServer;
import java.util.concurrent.atomic.AtomicReference;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;
import TinyTM.ofree.ReadSet;
import TinyTM.ofree.TMObj;
import java.util.concurrent.atomic.AtomicInteger;
import TinyTM.contention.*;
public class Transaction extends UnicastRemoteObject implements ITransaction{
//  public enum Status {ABORTED, ACTIVE, COMMITTED};
  static public final AtomicInteger commits = new AtomicInteger(0);
  static public final AtomicInteger aborts = new AtomicInteger(0);
  public static final Transaction COMMITTED = initCOMMITTED();
  public static final Transaction ABORTED = initABORTED();
  public ContentionManager cm;
  private final AtomicReference<Status> status;
  private ReadSet readset = new ReadSet();
  static ThreadLocal<Transaction> local = new ThreadLocal<Transaction>() {
    @Override
    protected Transaction initialValue() {
      return initCOMMITTED();
    }
  };

  public static Transaction initCOMMITTED() 
  { Transaction t=null; 
   try{
     t= new Transaction(Status.COMMITTED);
    } catch (RemoteException e) {}
    return t;
  }

  public static Transaction initABORTED() 
  { Transaction t=null; 
   try{
     t= new Transaction(Status.ABORTED);
    } catch (RemoteException e) {}
    return t;
  }

  public Transaction() throws RemoteException{
    super();
    status = new AtomicReference<Status>(Status.ACTIVE);
    cm = new BackoffManager();
  }
  private Transaction(Transaction.Status myStatus) throws RemoteException {
   
    status = new AtomicReference<Status>(myStatus);
  }
  public Status getStatus() throws RemoteException {
     //System.out.println("Transaction getstatus client");
    return status.get();
  }
  public boolean commit() throws RemoteException{
     //System.out.println("Transaction commit client");
    return status.compareAndSet(Status.ACTIVE, Status.COMMITTED);
  }

  public boolean abort() throws RemoteException{
       //System.out.println("Transaction abort client!");
    return status.compareAndSet(Status.ACTIVE, Status.ABORTED);
  }
  public static Transaction getLocal()  {
    return local.get();
  }
  public static void setLocal(Transaction transaction) {
    local.set(transaction);
  }
 
 /// readset

 public void addRS(ITMObjServer x, Object y)throws RemoteException {
       readset.add(x,y); 
     }
  public int sizeRS()throws RemoteException
  { return readset.size();}

  public Object getRS(ITMObjServer x) throws RemoteException
  {   //System.out.println("Transaction getRS client!");
     return readset.get(x);}

  public Object removeRS(ITMObjServer x) throws RemoteException
  { //System.out.println("removeRS cliente");
     return readset.remove(x);}

  public void clearRS() throws RemoteException {
    readset.clear();
  }
  public boolean validateReadSet() throws RemoteException{
    
   
     for (Map.Entry<ITMObjServer, Object> e : readset)
      {   
          ITMObjServer server = e.getKey(); 
          Object version = e.getValue();
          if(!server.validateEntry(version))
           {return false;}

      }
      return true;
     

  }

  public void CMresolve(ITransaction enemy) throws RemoteException
   {  cm.resolve(this,enemy);}

  public static <T> T atomic(Callable<T> xaction) throws Exception {
    T result;
    Transaction me;
    Thread myThread = Thread.currentThread();
    
    while (!myThread.isInterrupted()) {
      me = new Transaction();
      Transaction.setLocal(me);
      try {
        result = xaction.call();
        if ( me.validateReadSet() && me.commit()) {
          commits.getAndIncrement();
      
          return result;
        }
      } catch (AbortedException e) {
      } catch (InterruptedException e) {
        myThread.interrupt();
      } catch (Exception e) {
        throw new PanicException(e);
      }
      aborts.getAndIncrement();
      
    }
    throw new InterruptedException();
  }

}
