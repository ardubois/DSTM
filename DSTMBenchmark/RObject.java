// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark;


import DSTMBenchmark.*;
//import java.rmi.*;

public class RObject{
	String address;
        int port;

       public RObject(String address, int port)
	{
            this. address = address;
            this.port = port;
        }

	public String getAddress()
        { return address;}
     
	public int getPort()
        { return port;}	
}
