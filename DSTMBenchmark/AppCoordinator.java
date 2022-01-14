// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark;

import DSTMBenchmark.DBarrier;
import DSTMBenchmark.DBankLocks.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.io.BufferedReader;
import java.io.FileReader;


import java.lang.Thread;

public class AppCoordinator {

	public void runCoordinator(ProcessData processData, String [] args){

        // args[0] = NUMBER OF SERVERS
        // args[1] = NUMBER OF CLIENTS
       // args[2] = NUMBER OF OBJECTS
	


             try{
                int numberOfServers = Integer.parseInt(args[0]);
                int numberOfClients = Integer.parseInt(args[1]);
                int numberOfObjects = Integer.parseInt(args[2]);

		DBarrier barrier = new DBarrier(numberOfClients+1);
                DBarrier serverbarrier = new DBarrier(numberOfServers+1);
                Registry reg = LocateRegistry.createRegistry(1099);
                Naming.rebind("barrier",barrier);
                Naming.rebind("serverbarrier",serverbarrier);
        
                // waits for servers to be up:
                serverbarrier.await();


                // waits for clients to be up:
                barrier.await();
                long start = System.currentTimeMillis(); 
                barrier.await();// waits for all clients to finish
                long end = System.currentTimeMillis();
                System.out.println(Long.toString(end - start));

                //waits for all clients to process local data
                barrier.await();

                //BankProcessData bp = new BankProcessData();
                //Thread.sleep(1000);
                processData.processData(numberOfServers,numberOfClients,numberOfObjects);
                
                barrier.await(); // release all cleints
                serverbarrier.await(); // release all servers

                UnicastRemoteObject.unexportObject(reg,true);

                
                                 

                
                System.exit(0);
             }catch (Exception e)
                 {System.out.println(e);}
	}

    public static IDBarrier connectToBarrier(String barrierName) throws Exception
        {
              try{       
                 IDBarrier barrier = (IDBarrier) Naming.lookup(barrierName);
                 return barrier;
              }catch(NotBoundException ex){
			return 	connectToBarrier(barrierName);	
		       
	      }catch(RemoteException ex){
		        return 	connectToBarrier(barrierName);
	      }
        }

}


