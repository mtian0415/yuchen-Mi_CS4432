package simpledb.server;

import simpledb.remote.*;

import java.rmi.registry.*;

public class Startup {
   public static void main(String args[]) throws Exception {
	   //
	   if(args.length != 2){
		  System.out.println("The number of argument should be two");
		  return;
	   }
	   //
	   if((!args[1].equals("LRU")) && (!args[1].equals("Clock"))){
		   System.out.println("The expected argument is either \"LRU\" or \"Clock\"");
		   return;
	   }
	   
  
      // configure and initialize the database
      SimpleDB.init(args[0],args[1]);
      
      // create a registry specific for the server on the default port
      Registry reg = LocateRegistry.createRegistry(1099);
      
      // and post the server entry in it
      RemoteDriver d = new RemoteDriverImpl();
      reg.rebind("simpledb", d);
      
      System.out.println("database server ready");
      System.out.println("******************************************************");
      System.out.println("******************************************************");
   }
}
