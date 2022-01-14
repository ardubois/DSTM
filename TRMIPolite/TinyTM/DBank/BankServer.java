// Universidade Federal de Pelotas 2022
// This work is licensed under a Creative Commons
package TinyTM.DBank;

import TinyTM.*;
import TinyTM.ofree.*;
import java.rmi.registry.LocateRegistry;


public class BankServer
{
    public static void main(String argv[])
    {
        try
        {
            LocateRegistry.createRegistry(1099);

            TMObjServer.rebind("conta0",new SConta(1000));
            TMObjServer.rebind("conta1",new SConta(1000));
            TMObjServer.rebind("conta2",new SConta(1000));
            TMObjServer.rebind("conta3",new SConta(1000));
            TMObjServer.rebind("conta4",new SConta(1000));
            TMObjServer.rebind("conta5",new SConta(1000));
            TMObjServer.rebind("conta6",new SConta(1000));
            TMObjServer.rebind("conta7",new SConta(1000));
            TMObjServer.rebind("conta8",new SConta(1000));
            TMObjServer.rebind("conta9",new SConta(1000));
           // TMObjServer.rebind("conta10",new SConta(1000));
            
        }
        catch (Exception e)
        {
            System.out.println("Problema ao criar o servidor: \n"+e.toString());
        }
    }
}

