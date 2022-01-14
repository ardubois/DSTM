// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark;

import java.util.concurrent.CyclicBarrier;
//import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class DBarrier extends UnicastRemoteObject implements IDBarrier {
   int hosts = 0;
   CyclicBarrier barrier;

   DBarrier(int hosts) throws Exception
     {
         this.hosts=hosts;
         this.barrier = new CyclicBarrier(hosts);
     }	

   public void await() throws Exception{
 
       barrier.await();
   }


}
