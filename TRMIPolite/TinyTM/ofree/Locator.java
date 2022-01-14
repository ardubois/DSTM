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

/**
 * Encapsulates transactional synchronization for obstruction-free objects.
 * @author Maurice Herlihy
 */
import TinyTM.*;

import java.util.concurrent.atomic.AtomicReference;

class Locator {
    AtomicReference<ITransaction> owner;
    //ITransaction owner;
    Copyable oldVersion;
    Copyable newVersion;
    Locator() {
      owner = new AtomicReference(Transaction.COMMITTED);
      //owner = Transaction.COMMITTED;
    }
    Locator(Copyable version) {
      this();
      newVersion = version;
    }
}
