// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.GenericDSTM;


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


public class GenericClient{

public static void main(String[] args) throws Exception{
		//System.out.println("começa");

		int clientid = Integer.parseInt(args[0]);

                ClientApp app = new ClientApp();
                GenericTransaction transaction = new GenericTransaction();
        
                app.executeClient(new ChooseOPGeneric(), new GenericSaveData(),transaction,args);

                // app returns when all clients were executed
                // now it is time to save data for sanity check
	        
               // saveData(clientid,transaction);
                  

		System.exit(0);
	}

}

class GenericSaveData implements SaveData{

   	public void saveData(int clientid,ExecTransaction trans) throws Exception
        {
        GenericTransaction transaction = (GenericTransaction) trans;

        List<List<String>> rows = Arrays.asList(
        Arrays.asList("setminus", transaction.setminus.get()+""),
        Arrays.asList("setplus", transaction.setplus.get()+""),
        Arrays.asList("gets", transaction.gets.get()+""),
        Arrays.asList("transfer", transaction.transfer.get()+""),
        Arrays.asList("commits", transaction.commits.get()+""),
         Arrays.asList("commitsrts", Transaction.commits.get()+""),
         Arrays.asList("aborts", Transaction.aborts.get()+"")
        );



      //  System.out.println("gravando arquivo");

       FileWriter csvWriter = new FileWriter("client" + clientid + ".out");


       for (List<String> rowData : rows) {
       csvWriter.append(String.join(",", rowData));
       csvWriter.append("\n");
       }

      csvWriter.flush();
      csvWriter.close();

     }


}

class ChooseOPGeneric implements ChooseOP{

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

class GenericTransaction implements ExecTransaction{

   static AtomicInteger transfer; 
   static AtomicInteger setminus;
   static AtomicInteger setplus; 
   static AtomicInteger gets;
   static AtomicInteger commits;

   
   
   GenericTransaction()
   {
     transfer = new AtomicInteger(0);
     setminus = new AtomicInteger(0);
     setplus = new AtomicInteger(0);
     gets = new AtomicInteger(0);
     commits = new AtomicInteger(0);
   }

	public void execTransaction(RObject[] objects, int op) throws Exception
        {

             TMObj<IObject>[] robjects = new TMObj[objects.length];
	     for (int i =0;i<robjects.length;i++)
	     {	  

               robjects[i] = (TMObj<IObject>) TMObj.lookupTMObj("rmi://localhost:"+objects[i].getPort()+"/"+objects[i].getAddress());
	     }

            
       IObject lobjects[] = new IObject[robjects.length];
       int donewithdraw = 0;
       

         donewithdraw=(int) Transaction.atomic(new Callable<Integer>() {
	    public Integer call() throws Exception{
                int localwithdraw=0;
                Random r = new Random();
                int result1=0,result2=0,field1=0,field2=0;
	    if (op == 0)
		{

                    for(int i=0;i<lobjects.length;i++)
                     { lobjects[i] = robjects[i].openWrite();}
                    
                    int size = lobjects.length;
                   if (size % 2 != 0) { size = size -1; }
                    for(int i=0;i<size;i+=2)
                     { 

                        field1 = r.nextInt(4);
                        field2 = r.nextInt(4);
                        result1=(int)lobjects[i].getClass().getDeclaredMethod("getField"+field1).invoke(lobjects[i]);
                        if(result1>=100)
                           {
                             lobjects[i].getClass().getDeclaredMethod("setField"+field1,int.class).invoke(lobjects[i],result1-100);
                             result2=(int)lobjects[i+1].getClass().getDeclaredMethod("getField"+field2).invoke(lobjects[i+1]);
                             lobjects[i+1].getClass().getDeclaredMethod("setField"+field2,int.class).invoke(lobjects[i+1],result2+100);
                           }

                      }  
                       
		}


	    if (op == 1)
	       {

                    for(int i=0;i<lobjects.length;i++)
                     { lobjects[i] = robjects[i].openWrite();}

                  
                   int size = lobjects.length;
                   if (size % 2 != 0) { size = size -1; }
                   for(int i=0;i<size;i+=2)
                     { 

                        field1 = r.nextInt(4);
                        field2 = r.nextInt(4);
                        result1=(int)lobjects[i].getClass().getDeclaredMethod("getField"+field1).invoke(lobjects[i]);
                        if(result1>=100)
                           {
                             lobjects[i].getClass().getDeclaredMethod("setField"+field1,int.class).invoke(lobjects[i],result1-100);
                             localwithdraw ++;
                           }
                        result2=(int)lobjects[i+1].getClass().getDeclaredMethod("getField"+field2).invoke(lobjects[i+1]);
                        lobjects[i+1].getClass().getDeclaredMethod("setField"+field2,int.class).invoke(lobjects[i+1],result2+100);

                      } 
                    
                  
		}

            if (op == 2)
	       {
                
                   for(int i=0;i<lobjects.length;i++)
                     { lobjects[i] = robjects[i].openRead();}
 
                   for(int i=0;i<lobjects.length;i++)
                     {
                       field1 = r.nextInt(4);
                       lobjects[i].getClass().getDeclaredMethod("getField"+field1).invoke(lobjects[i]);
                     }
                   
                   
		}
             
           ClientApp.doSomeComputation();

             return localwithdraw;
            }});

    //SANITY CHECK:
          commits.getAndIncrement();

          if (op == 0)
		{   int size = lobjects.length;
                   if (size % 2 != 0) { size = size -1; }
                    for(int i=0;i<size;i+=2)
                     { 
                    transfer.getAndIncrement();
                     }
                    //transfer.getAndIncrement();
                }


	    if (op == 1)
	       { 
                for(int i =0;i<donewithdraw;i++)
                   {setminus.getAndIncrement();}
               int size = lobjects.length;
                   if (size % 2 != 0) { size = size -1; }
                 for(int i=0;i<size;i+=2)
                     { 
                 setplus.getAndIncrement();
                     }
                 
                }

            if (op == 2)
	       {  
                 for(int i=0;i<lobjects.length;i++)
                     {
                     gets.getAndIncrement();
                     }       
         	}

	}
}
