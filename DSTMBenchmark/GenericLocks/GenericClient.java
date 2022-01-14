// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.GenericLocks;


import DSTMBenchmark.*;
import java.rmi.*;
import java.util.Random;
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
        // Arrays.asList("commitsrts", TThread.commits.get()+""),
         Arrays.asList("aborts", transaction.aborts.get()+"")
        );



       // System.out.println("gravando arquivo");

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
        //int choice2 = random.nextInt(2);
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
 	static AtomicInteger aborts;


       GenericTransaction()
   	{
     	transfer = new AtomicInteger(0);
     	setminus = new AtomicInteger(0);
     	setplus = new AtomicInteger(0);
     	gets = new AtomicInteger(0);
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

            IObject[] robjects= new IObject[objects.length];

	   for(int i=0;i<robjects.length;i++)
		   robjects[i]= (IObject) Naming.lookup("rmi://localhost:"+objects[i].getPort()+"/"+objects[i].getAddress());

	    boolean [] acquiredLocks = new boolean[robjects.length];

	// tries to acquiere locks for objects

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

          
         // now I have all the locks   
            
               

           int field1=0,field2=0,result1=0,result2=0;
           Random r = new Random();
            
	    if (op == 0)
		{
		    int size = robjects.length;
                   if (size % 2 != 0) { size = size -1; }

                    for(int i=0;i<size;i+=2)
                     { 

                        field1 = r.nextInt(4);
                        field2 = r.nextInt(4);
                        result1=(int)robjects[i].getClass().getDeclaredMethod("getField"+field1).invoke(robjects[i]);
                        if(result1>=100)
                           {
                             robjects[i].getClass().getDeclaredMethod("setField"+field1,int.class).invoke(robjects[i],result1-100);
                             result2=(int)robjects[i+1].getClass().getDeclaredMethod("getField"+field2).invoke(robjects[i+1]);
                             robjects[i+1].getClass().getDeclaredMethod("setField"+field2,int.class).invoke(robjects[i+1],result2+100);
                           }
                        transfer.getAndIncrement();
                      } 
                  
                    
		}


	    if (op == 1)
	       {

                  int size = robjects.length;
                   if (size % 2 != 0) { size = size -1; }
                  for(int i=0;i<size;i+=2)
                     { 

                        field1 = r.nextInt(4);
                        field2 = r.nextInt(4);
                        result1=(int)robjects[i].getClass().getDeclaredMethod("getField"+field1).invoke(robjects[i]);
                        if(result1>=100)
                           {
                             robjects[i].getClass().getDeclaredMethod("setField"+field1,int.class).invoke(robjects[i],result1-100);
                             setminus.getAndIncrement();
                           }
                        result2=(int)robjects[i+1].getClass().getDeclaredMethod("getField"+field2).invoke(robjects[i+1]);
                        robjects[i+1].getClass().getDeclaredMethod("setField"+field2,int.class).invoke(robjects[i+1],result2+100);
                        setplus.getAndIncrement();
                      } 
                   
                   
		}

            if (op == 2)
               {   

                  for(int i=0;i<robjects.length;i++)
                     {
                       field1 = r.nextInt(4);
                       robjects[i].getClass().getDeclaredMethod("getField"+field1).invoke(robjects[i]);
                       gets.getAndIncrement();
                     }

                  
                   
		}
             
             //if(op<0 || op>2) { throw new Exception("invalid op");}
           
            ClientApp.doSomeComputation();


             for(int i =0;i<acquiredLocks.length;i++)
		     { 
	           	robjects[i].unlock();
                     }
             
    

             commits.getAndIncrement();

      
            
	}
}
