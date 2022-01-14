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

package TinyTM.contention;

import TinyTM.*;
import TinyTM.exceptions.PanicException;
import java.rmi.*;

/**
 * Contention Manager Interface for TinyTM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 *
 * @author Maurice Herlihy
 */
public abstract class ContentionManager {

  static ThreadLocal<ContentionManager> local = new ThreadLocal<ContentionManager>() {

    @Override
    protected ContentionManager initialValue() {
      try {
        return (ContentionManager) new BackoffManager();//Defaults.MANAGER.newInstance();
      } catch (Exception ex) {
        throw new PanicException(ex);
      }
    }
  };

  public abstract void resolve(Transaction me, ITransaction other) throws RemoteException;

  public static ContentionManager getLocal() {
    return local.get();
  }

  public static void setLocal(ContentionManager m) {
    local.set(m);
  }
}
