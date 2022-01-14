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
/**
 * Interface that exports public copyTo method
 * @param <T> type
 * @author Maurice Herlihy
 */
public interface Copyable<T> extends Remote{
  public void copyTo(T target) throws RemoteException;  
}

