// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.DBankDSTM1;


import DSTMBenchmark.*;
import java.rmi.*;
import java.util.Random;
import java.util.concurrent.Callable;
import TinyTM.ofree.*;
import TinyTM.DBank.*;
import TinyTM.*;
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
         Arrays.asList("commitsrts", Transaction.commits.get()+""),
         Arrays.asList("aborts", Transaction.aborts.get()+"")
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
        int choice2 = random.nextInt(2);
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

   
   
   BankTransaction()
   {
     transfer = new AtomicInteger(0);
     withdraw = new AtomicInteger(0);
     deposit = new AtomicInteger(0);
     balance = new AtomicInteger(0);
     commits = new AtomicInteger(0);
   }

	public void execTransaction(RObject[] objects, int op) throws Exception
        {

             TMObj<IConta>[] robjects = new TMObj[objects.length];
	     for (int i =0;i<robjects.length;i++)
	     {	  

               robjects[i] = (TMObj<IConta>) TMObj.lookupTMObj("rmi://localhost:"+objects[i].getPort()+"/"+objects[i].getAddress());
	     }

            

       int donewithdraw = 0;

         donewithdraw=(int) Transaction.atomic(new Callable<Integer>() {
	    public Integer call() throws Exception{
                int localwithdraw=0;
	    if (op == 0)
		{
                    IConta lc1 = robjects[0].openWrite();
                  IConta lc2 = robjects[1].openWrite();
                  IConta lc3 = robjects[2].openWrite();
                  IConta lc4 = robjects[3].openWrite();
                    lc1.transferencia(lc2,100);
                    lc3.transferencia(lc4,100);
		}


	    if (op == 1)
	       {
                   IConta lc1 = robjects[0].openWrite();
                   IConta lc2 = robjects[1].openWrite();
                    IConta lc3 = robjects[2].openWrite();
                    IConta lc4 = robjects[3].openWrite();
                   lc1.deposito(100);
                   if(lc2.saque(100)) {localwithdraw ++;}
                    lc3.deposito(100);
                   if(lc4.saque(100)) {localwithdraw++;}
		}

            if (op == 2)
	       {
                   IConta lc1 = robjects[0].openRead();
                   IConta lc2 = robjects[1].openRead();
                   IConta lc3 = robjects[2].openRead();
                   IConta lc4 = robjects[3].openRead();
                   lc1.getSaldo();
                   lc2.getSaldo();
                   lc3.getSaldo();
                   lc4.getSaldo();
		}
             
           // ClientApp.doSomeComputation();

             return localwithdraw;
            }});

    //SANITY CHECK:
          commits.getAndIncrement();

          if (op == 0)
		{
                    transfer.getAndIncrement();
                    transfer.getAndIncrement();
                }


	    if (op == 1)
	       { 
                for(int i =0;i<donewithdraw;i++)
                   {withdraw.getAndIncrement();}
                 
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
