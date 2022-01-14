// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package TinyTM.DBank;

import TinyTM.Copyable;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class SConta extends UnicastRemoteObject implements IConta{ //Copyable<SConta>,IConta{
    private double saldo;
    public SConta() throws RemoteException 
    {super();}
    
    public SConta(int saldo) throws RemoteException{
        super();
        this.saldo=saldo;
        }
    public double getSaldo() throws RemoteException{
        return saldo;
    }
    public void setSaldo(double valor) throws RemoteException{
        this.saldo = valor;
    }
   
   public void deposito(double valor) throws RemoteException{
       // System.out.println("DEPOSITO!!!!!!!!!!!!!!!!!!!!!!!!!!1!!!!!!!!!!!");
        this.saldo = this.saldo + valor;
    }
   

   public boolean saque(double valor) throws RemoteException{
    if (this.saldo<valor)
         return false;
    else {
         this.saldo = this.saldo - valor;
         return true; 
        }
    }
   
   public boolean transferencia(IConta c, double valor) throws RemoteException{
    if (this.saldo<valor)
         return false;
    else {
         this.saldo = this.saldo - valor;
         c.deposito(valor);
         return true; 
        }
    }

    public void copyTo(IConta target) throws RemoteException
       { ((IConta)target).setSaldo(this.saldo);}
 
}
