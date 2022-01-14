// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.DBankLocks;


import DSTMBenchmark.*;
import java.rmi.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;
import java.io.FileWriter;

public class BankClient{

public static void main(String[] args) throws Exception{
		

		System.out.println("começa");

		int clientid = Integer.parseInt(args[0]);

                ClientApp app = new ClientApp();
                BankTransaction transaction = new BankTransaction();
        
                app.executeClient(new ChooseOPBank(), new BankSaveData(),transaction,args);

                // app returns when all clients were executed
                // now it is time to save data for sanity check
	        
               // saveData(clientid,transaction);
                  

		System.exit(0);
	}

}

class BankSaveData implements SaveData{

   	public void saveData(int clientid,ExecTransaction trans) throws Exception
        {
        BankTransaction transaction = (BankTransaction) trans;

        List<List<String>> rows = Arrays.asList(
        Arrays.asList("withdraw", transaction.withdraw.get()+""),
        Arrays.asList("deposit", transaction.deposit.get()+""),
        Arrays.asList("balance", transaction.balance.get()+""),
        Arrays.asList("transfer", transaction.transfer.get()+""),
        Arrays.asList("commits", transaction.commits.get()+""),
        // Arrays.asList("commitsrts", TThread.commits.get()+""),
         Arrays.asList("aborts", transaction.aborts.get()+"")
        );



        System.out.println("gravando arquivo");

       FileWriter csvWriter = new FileWriter("client" + clientid + ".out");


       for (List<String> rowData : rows) {
       csvWriter.append(String.join(",", rowData));
       csvWriter.append("\n");
       }

      csvWriter.flush();
      csvWriter.close();

     }


}

class ChooseOPBank implements ChooseOP{

    public int chooseOP(int writes, Random random)
    {   int op = 0;
        int choice = random.nextInt(100)+1;
        //int choice2 = random.nextInt(2);
        if(choice<=writes)
          {
             op = random.nextInt(2);
          } else { op = 2;}
         return op;
         
    }
}

class BankTransaction implements ExecTransaction{


        static AtomicInteger transfer; 
        static AtomicInteger withdraw;
        static AtomicInteger deposit; 
        static AtomicInteger balance;
        static AtomicInteger commits;
 	static AtomicInteger aborts;


       BankTransaction()
   	{
     	transfer = new AtomicInteger(0);
     	withdraw = new AtomicInteger(0);
     	deposit = new AtomicInteger(0);
     	balance = new AtomicInteger(0);
     	commits = new AtomicInteger(0);
	aborts = new AtomicInteger(0);
   	}


	public boolean allAcquired(boolean acquired[])
	{
           for (int i=0;i<acquired.length;i++)
		   if(!acquired[i])
			   return false;
	   return true;
	}

	public void execTransaction(RObject[] objects, int op) throws Exception
        {

            IBAccount[] robjects= new IBAccount[objects.length];

	   for(int i=0;i<robjects.length;i++)
		   robjects[i]= (IBAccount) Naming.lookup("rmi://localhost:"+objects[i].getPort()+"/"+objects[i].getAddress());

	    boolean [] acquiredLocks = new boolean[robjects.length];

            do
             {
                   for(int i =0;i<acquiredLocks.length;i++)
		    {
                      acquiredLocks[i] = robjects[i].tryLock();
		    }  
                       
		   if (! allAcquired(acquiredLocks))
                   {
                     for(int i =0;i<acquiredLocks.length;i++)
		     { 
	            	     if(acquiredLocks[i]) {robjects[i].unlock();}
                     }
                     aborts.getAndIncrement();
                   }
	     }while(!allAcquired(acquiredLocks));

          
            
            
               

           Boolean withdraw1 = false;
            Boolean withdraw2 = false;  

	    if (op == 0)
		{
                    robjects[0].transfer(robjects[1],100);
                    robjects[2].transfer(robjects[3],100);
                    
		}


	    if (op == 1)
	       {
                   robjects[0].deposit(100);
                   withdraw1= robjects[1].withdraw(100);
                   robjects[2].deposit(100);
                   withdraw2=robjects[3].withdraw(100);
                   
		}

            if (op == 2)
	       {   robjects[0].getBalance();
                   robjects[1].getBalance();
                   robjects[2].getBalance();
                   robjects[3].getBalance();
                   
		}
             
             //if(op<0 || op>2) { throw new Exception("invalid op");}
           
            // ClientApp.doSomeComputation();


             for(int i =0;i<acquiredLocks.length;i++)
		     { 
	           	robjects[i].unlock();
                     }
             
    

             commits.getAndIncrement();

          if (op == 0)
		{
                    transfer.getAndIncrement();
                    transfer.getAndIncrement();
                }


	    if (op == 1)
	       { if(withdraw1) {withdraw.getAndIncrement();}
                 if(withdraw2) {withdraw.getAndIncrement();}
                 deposit.getAndIncrement();
                 deposit.getAndIncrement();
                }

            if (op == 2)
	       {   balance.getAndIncrement();
                   balance.getAndIncrement();
                   balance.getAndIncrement();
                   balance.getAndIncrement();
		}

            
	}
}
