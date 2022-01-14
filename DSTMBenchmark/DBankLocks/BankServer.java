// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark.DBankLocks;


import DSTMBenchmark.*;
import java.rmi.*;

public class BankServer{
	
      // args[0] == Server ID: starts from zero
      // args[1] == Number of objects in the server

	public static void main(String[] args) throws Exception{
		
                int id = Integer.parseInt(args[0]);
                int numberObj = Integer.parseInt(args[1]);
                
                Remote[] objects = new Remote[numberObj];

                for (int i = 0; i<objects.length; i++)
                 {
                       objects[i] = new BAccount(1000);
                 }

                ServerApp server = new ServerApp();
                server.publiciseObjectsAndWait(id,objects);
		System.exit(0);
	}
}
