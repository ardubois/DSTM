// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.DBankLocks;

import DSTMBenchmark.*;
import DSTMBenchmark.DBarrier;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.io.BufferedReader;
import java.io.FileReader;



public class BankCoordinator {

	public static void main(String[] args) throws Exception
        {
               
           AppCoordinator coordinator = new AppCoordinator();
           BankProcessData processData = new BankProcessData();
           coordinator.runCoordinator(processData,args);

         }

    

}

class BankProcessData implements ProcessData{

    public void processData(int numberOfServers, int numberOfClients, int numberOfObjects) throws Exception
    {

        
          	int withdraw = 0;
          	int deposit = 0;
        	int balance = 0;
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
             withdraw = withdraw + Integer.parseInt(data[1]);
             data = csvReader.readLine().split(",");
             deposit = deposit + Integer.parseInt(data[1]);
	     data = csvReader.readLine().split(",");
             balance = balance + Integer.parseInt(data[1]);
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
       
        double total = 0;

        IBAccount objtemp;
          
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
                 objtemp = (IBAccount) Naming.lookup("//localhost:"+(1666+i)+"/object"+j);
                 total = total + objtemp.getBalance();
          
               }
          }
         System.out.printf ("Total of commits: %d\n", commits);
         System.out.printf ("Total of aborts: %d \n", aborts);
         System.out.printf ("Total of withdraw: %d \n", withdraw);
         System.out.printf ("Total of deposit: %d \n", deposit);
 	 System.out.printf ("Total of balance: %d \n", balance);
	System.out.printf ("Total of transfer: %d \n", transfer);
         int totalOperations = withdraw +deposit+balance+transfer;
       //  int totalExpected = ((commits-(transfer/2)) * 4) + transfer;
         System.out.printf ("Total of operations: %d \n", totalOperations);
         int totalMoney = ((numberOfServers*numberOfObjects*1000) + (deposit*100))- (withdraw*100);
         System.out.printf ("Total of money: %.00f (Expected: %d)\n", total,totalMoney);

    }
}

