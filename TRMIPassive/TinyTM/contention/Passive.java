// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package TinyTM.contention;

import TinyTM.Transaction;
import java.util.Random;
import TinyTM.*;
import java.rmi.*;
import TinyTM.exceptions.*;

public class Passive extends ContentionManager {
  private static final int MAX_ABORTS = 10;
  private int aborts = 0;
  public Passive() {this.aborts =0;}

  public void resolve(Transaction me, ITransaction other) throws RemoteException {
   if(aborts<MAX_ABORTS){
       aborts++;
       me.abort(); throw new AbortedException();
    }else
    {
      other.abort();
    }
    
  }  
}
