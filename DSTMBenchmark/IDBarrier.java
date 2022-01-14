// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark;

import java.util.concurrent.CyclicBarrier;
import java.rmi.*;

public interface IDBarrier extends Remote{
 
   public void await() throws Exception;
 


}
