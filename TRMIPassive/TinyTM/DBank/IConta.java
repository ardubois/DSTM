// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package TinyTM.DBank;

import TinyTM.Copyable;

import java.rmi.*;

public interface IConta extends Remote,Copyable<IConta>{

   public double getSaldo() throws RemoteException;

   public void setSaldo(double Saldo) throws RemoteException;
   
   void deposito(double valor) throws RemoteException;
   

   boolean saque(double valor) throws RemoteException;
   
   boolean transferencia(IConta c, double valor) throws RemoteException;
 
}
