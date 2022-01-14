// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package DSTMBenchmark;


import DSTMBenchmark.*;
import java.rmi.*;
//import java.util.Random;

public interface ExecTransaction{

	public void execTransaction(RObject[] objects, int op) throws Exception;
}        
