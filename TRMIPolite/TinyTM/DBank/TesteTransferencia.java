// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package TinyTM.DBank;

import java.util.concurrent.atomic.AtomicInteger;
import TinyTM.Transaction;
import java.util.Random;
import java.util.concurrent.Callable;
import TinyTM.ofree.*;

class TesteTransferencia{
 
  private final static int THREADS = 1;
  private final static int CONTAS = 3; 

  AtomicInteger depositos;

  
  AtomicInteger transfer;
  AtomicInteger missed;
  Random random;
  TMObj<IConta>[] contas;
  public static void main(String[]args)
  {
     TesteTransferencia teste = new TesteTransferencia();
     teste.init();
     try{
     teste.testParallel();
     }catch(Exception e) {System.out.println(e);}
     
  }

  public void init(){
     depositos = new AtomicInteger(0);

    transfer = new AtomicInteger(0);
    missed = new AtomicInteger(0);
    random = new Random(this.hashCode());
   // random.setSeed(System.currentTimeMillis()); // comment out for determinstic
 
// reset

    Transaction.setLocal(Transaction.COMMITTED);
    Transaction.commits.set(0);
    Transaction.aborts.set(0);
    }

    public void testParallel() throws Exception {
      //reset();
      System.out.printf("TestParallel(%d)\n", THREADS);
      System.out.println("Contas: " + CONTAS);

      contas = new TMObj[CONTAS];
      for (int i = 0; i < contas.length; i++) {
    //  System.out.println("conta"+i);
      contas[i] = (TMObj<IConta>) TMObj.lookupTMObj("conta"+i);
      }

     
      

   

   /* 
    double total = 0; 
   for (int i=0; i< contas.length; i++)
     { System.out.println("Saldo "+i+"=" + contas[i].getSaldo())

;
       total = total + contas[i].getSaldo();
     }
     System.out.println("Total: " + total);
*/
      Thread[] threads = new Thread[THREADS];
      for (int i = 0; i < threads.length; i++) {
        threads[i] = new TestThread(i);
      }
      for (int i = 0; i < threads.length; i++) {
        threads[i].start();
      }
     // Thread.sleep(10000);
     // for (int i = 0; i < threads.length; i++) {
       // threads[i].interrupt();
      //}
      for (int i = 0; i < threads.length; i++) {
       
        threads[i].join();
       
      }
   sanityCheck(); 
   System.exit(0);
    
 
     } 

private class TestThread extends java.lang.Thread {
    int index;
    public TestThread(int myID) {
      index = myID;
    }
    public void run() {
      try {
          int c1= random.nextInt(CONTAS-1);
          int c2= random.nextInt(CONTAS-1);
          //int c=0;
          boolean result = true;
          
              result =Transaction.atomic(new Callable<Boolean>() {
                public Boolean call() throws Exception{
                //    IConta lc1 = contas[c1].openWrite();
                 // IConta lc2 = contas[c2].openWrite();
                 // lc1.transferencia(lc2,100);
                IConta lc0,lc1,lc2;
                 lc0 = contas[0].openWrite();
                 lc1 = contas[1].openWrite();
                 lc2 = contas[2].openWrite();
                 lc0 = contas[0].openRead();
                lc1 = contas[1].openRead();
                 lc2 = contas[2].openRead();
              // IConta lc1 = contas[c1].openWrite();
                
                // lc1.deposito(1000);
                 //lc2.getSaldo();
                  //SConta lc0 = contas[c1].openRead();
                 //SConta lc1 = contas[c2].openRead();
                 //SConta lc2 = contas[c1].openWrite();
                 // SConta lc3 = contas[c2].openWrite();
                 // lc0.getSaldo();
                  //lc1.transferencia(lc2,100);
                  return true;
                }
              });
    
            if (result) {
             // transfer.getAndIncrement();
               depositos.getAndIncrement();
            } else {
              missed.getAndIncrement();
            }
          
       } catch (Exception ex) {
            System.out.println (ex);
       }
   }

  }


  
/*    private class TestThread extends java.lang.Thread {
    int index;
    public TestThread(int myID) {
      index = myID;
    }
    public void run() {
      try {
          int c;
          if (CONTAS == 1) { c=0;} else {c= random.nextInt(CONTAS);}
          //int c=0;
         // if (c==9) {System.out.println("9");} //else {System.out.println(c);}
          boolean result = true;
          
              TThread.doIt(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                  IConta lc = contas[c].openWrite();
                 // System.out.println("vai chamar o metodo");
                  lc.deposito(1000);
                 // System.out.println("chamou o metodo");
                  
                  return true;
                }
              });
    
            if (result) {
              depositos.getAndIncrement();
            } else {
              missed.getAndIncrement();
            }
          
       } catch (Exception ex) {
            System.out.println (ex);
       }
   }

  }
*/

 public void sanityCheck() throws Exception {
   
    int length = 0;
    Transaction.setLocal(Transaction.COMMITTED);
    System.out.printf("depositos: %d, transfer: %d, missed: %d\n",
        depositos.get(), transfer.get(), missed.get());
    System.out.printf("commits: %d, aborts: %d\n",
        Transaction.commits.get(), Transaction.aborts.get());
   
    double total = 0;

    for (int i=0; i< contas.length; i++)
       total = total + ((IConta)contas[i].openRead()).getSaldo();

     System.out.println("Total: " + total);
     System.out.println("Esperado: " + ((contas.length*1000) +(depositos.get() * 1000)));

 }

}
