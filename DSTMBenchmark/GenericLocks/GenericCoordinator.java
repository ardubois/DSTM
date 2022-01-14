// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.GenericLocks;

import DSTMBenchmark.*;
import DSTMBenchmark.DBarrier;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.io.BufferedReader;
import java.io.FileReader;



public class GenericCoordinator {

	public static void main(String[] args) throws Exception
        {
               
           AppCoordinator coordinator = new AppCoordinator();
           GenericProcessData processData = new GenericProcessData();
           coordinator.runCoordinator(processData,args);

         }

    

}

class GenericProcessData implements ProcessData{

    public void processData(int numberOfServers, int numberOfClients, int numberOfObjects) throws Exception
    {

        
          	int setminus = 0;
          	int setplus = 0;
        	int gets = 0;
   		int transfer = 0;
		int commits = 0;
//		int commitsrts = 0;
		int aborts = 0;
                String[] data;
                BufferedReader csvReader;

        for(int i=0; i< numberOfClients; i++)
        {
             csvReader = new BufferedReader(new FileReader("client"+i+".out"));
           
             data = csvReader.readLine().split(",");
             setminus = setminus + Integer.parseInt(data[1]);
             data = csvReader.readLine().split(",");
             setplus = setplus + Integer.parseInt(data[1]);
	     data = csvReader.readLine().split(",");
             gets = gets + Integer.parseInt(data[1]);
             data = csvReader.readLine().split(",");
             transfer = transfer + Integer.parseInt(data[1]);
             data = csvReader.readLine().split(",");
             commits = commits + Integer.parseInt(data[1]);
             //data = csvReader.readLine().split(",");
             //commitsrts = commitsrts + Integer.parseInt(data[1]);
	     data = csvReader.readLine().split(",");
             aborts = aborts + Integer.parseInt(data[1]);
        
             csvReader.close();
        }

        //Transaction.setLocal(Transaction.COMMITTED);
       
        int total = 0;

        IObject objtemp;
          
        /*System.out.println("before list!");

        String names[] = Naming.list("rmi://localhost:1666");
        for (int i = 0; i < names.length; i++) {
               System.out.println(names[i]);
        }*/

       //System.out.println("after list!");

        for (int i = 0; i < numberOfServers; i++) {
           for(int j = 0; j< numberOfObjects; j ++)
              {
           // System.out.println("//localhost:"+(1666+i)+"/object"+j);
                 objtemp = (IObject) Naming.lookup("//localhost:"+(1666+i)+"/object"+j);
                 total = total + objtemp.sumAll();
          
               }
          }
         System.out.printf ("Total of commits: %d\n", commits);
         System.out.printf ("Total of aborts: %d \n", aborts);
         System.out.printf ("Total of setminus: %d \n", setminus);
         System.out.printf ("Total of setplus: %d \n", setplus);
 	 System.out.printf ("Total of gets: %d \n", gets);
	System.out.printf ("Total of transfer: %d \n", transfer);
         int totalOperations = setminus +setplus+gets+transfer;
       //  int totalExpected = ((commits-(transfer/2)) * 4) + transfer;
         System.out.printf ("Total of operations: %d \n", totalOperations);
         int totalMoney = ((4*numberOfServers*numberOfObjects*1000) + (setplus*100))- (setminus*100);
         System.out.printf ("Total value of fields: %d (Expected: %d)\n", total,totalMoney);

    }
}

