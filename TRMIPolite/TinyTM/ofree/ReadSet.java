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
package TinyTM.ofree;

import TinyTM.Transaction;
import TinyTM.ofree.*;
import TinyTM.Copyable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.lang.Thread;

/**
 * A thread-local read set for the atomic locking object implementation.
 * @author Maurice Herlihy
 */
public class ReadSet implements Iterable <Map.Entry<ITMObjServer, Object>> {
 // static ThreadLocal<Map<ITMObjServer,Object>>local = new ThreadLocal<Map<ITMObjServer,Object>>() {
 //   protected Map<ITMObjServer,Object> initialValue() {
 //     return new HashMap<ITMObjServer,Object>();
 //   }
 // };
  
  Map<ITMObjServer,Object> set;
  
  public ReadSet() {
   set =new HashMap<ITMObjServer,Object>();
  //  set = local.get();
  }
  
 // public static ReadSet getLocal()
 //  { return new ReadSet();}

  public Iterator<Map.Entry<ITMObjServer, Object>>  iterator() {
    return set.entrySet().iterator();
  }
  public void add(ITMObjServer x, Object y) {
           set.put(x,y);
    // local.get().put(x,y);
    // set.put(x,y);
    //ReadSet.getLocal().put(x,y); 
   // System.out.println("Thread: " + myThread + " size: " + set.size());
  }
  public  int size()
  { return set.size();}
    //return local.get().size();}

  public Object get(ITMObjServer x)
   { return set.get(x);}
 // { return local.get().get(x);}

  public Object remove(ITMObjServer x)
 { return set.remove(x);}
//  { return local.get().remove(x);}

  public void clear() {
     set.clear();
 //   local.get().clear();
  }

 
}
/*
class RSEntry{

   private ITMObjServer fst;
   private Object snd;

   RSEntry (ITMObjServer a, Object b)
   { this.fst = a; this.snd = b;}

   @Override
   public boolean equals(Object other)
   {
        return this.fst == ((RSEntry)other).fst;
   }

   public ITMObjServer getFst()
    { return this.fst;}

   public Object getSnd()
    { return this.snd;}

}

*/
