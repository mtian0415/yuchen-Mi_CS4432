package simpledb.buffer;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import simpledb.file.*;

/**
 * The publicly-accessible buffer manager.
 * A buffer manager wraps a basic buffer manager, and
 * provides the same methods. The difference is that
 * the methods {@link #pin(Block) pin} and 
 * {@link #pinNew(String, PageFormatter) pinNew}
 * will never return null.
 * If no buffers are currently available, then the
 * calling thread will be placed on a waiting list.
 * The waiting threads are removed from the list when 
 * a buffer becomes available.
 * If a thread has been waiting for a buffer for an
 * excessive amount of time (currently, 10 seconds)
 * then a {@link BufferAbortException} is thrown.
 * @author Edward Sciore
 */
public class BufferMgr {
   private static final long MAX_TIME = 10000; // 10 seconds
   private BasicBufferMgr bufferMgr;
   
   /**
    * Creates a new buffer manager having the specified 
    * number of buffers.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffers the number of buffer slots to allocate
    */
   public BufferMgr(int numbuffers,String replacePolicyMode) {
      bufferMgr = new BasicBufferMgr(numbuffers,replacePolicyMode);
   }
   
   /**
    * Pins a buffer to the specified block, potentially
    * waiting until a buffer becomes available.
    * If no buffer becomes available within a fixed 
    * time period, then a {@link BufferAbortException} is thrown.
    * @param blk a reference to a disk block
    * @return the buffer pinned to that block
    */
   public synchronized Buffer pin(Block blk) {
	 //CS4432_Project1: before pin a page, print out the current state of free list and hash table.
	   System.out.println("Before page "+blk+" is pined the freelist is:");
	   this.printfreelist();
	   System.out.println("Before page "+blk+" is pined the hashtable is:");
	   this.printhashtable();
	   //CS4432-Project1:
	   System.out.println(this.toString());
      try {
         long timestamp = System.currentTimeMillis();
         Buffer buff = bufferMgr.pin(blk);
         while (buff == null && !waitingTooLong(timestamp)) {
            wait(MAX_TIME);
            buff = bufferMgr.pin(blk);
         }
         if (buff == null)
            throw new BufferAbortException();
       //CS4432_Project1: After pin a page, print out the current state of free list and hash table.
         System.out.println("After page "+blk+" is pined the freelist is:");
    	   this.printfreelist();
    	   System.out.println("After page "+blk+" is pined the hashtable is:");
    	   this.printhashtable();
    	   //Cs4432-Project1:
    	   System.out.println(this.toString());
         return buff;
      }
      catch(InterruptedException e) {
         throw new BufferAbortException();
      }
   }
   
   /**
    * Pins a buffer to a new block in the specified file, 
    * potentially waiting until a buffer becomes available.
    * If no buffer becomes available within a fixed 
    * time period, then a {@link BufferAbortException} is thrown.
    * @param filename the name of the file
    * @param fmtr the formatter used to initialize the page
    * @return the buffer pinned to that block
    */
   public synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
      try {
         long timestamp = System.currentTimeMillis();
         Buffer buff = bufferMgr.pinNew(filename, fmtr);
         while (buff == null && !waitingTooLong(timestamp)) {
            wait(MAX_TIME);
            buff = bufferMgr.pinNew(filename, fmtr);
         }
         if (buff == null)
            throw new BufferAbortException();
         return buff;
      }
      catch(InterruptedException e) {
         throw new BufferAbortException();
      }
   }
   
   /**
    * Unpins the specified buffer. 
    * If the buffer's pin count becomes 0,
    * then the threads on the wait list are notified.
    * @param buff the buffer to be unpinned
    */
   public synchronized void unpin(Buffer buff) {
	 //CS4432_Project1: before unpin a page, print out the current state of free list and hash table.
	   System.out.println("Before page "+buff+" is unpined the freelist is:");
	   this.printfreelist();
	   System.out.println("Before page "+buff+" is unpined the hashtable is:");
      bufferMgr.unpin(buff);
      //cs4432-Project1:
      System.out.println(this.toString());
      if (!buff.isPinned())
         notifyAll();
      //CS4432_Project1: After unpin a page, print out the current state of free list and hash table.
      System.out.println("After page "+buff+" is unpined the freelist is:");
	   this.printfreelist();
	   System.out.println("After page "+buff+" is unpined the hashtable is:");
	   this.printhashtable();
	   //cs4432-project1:
	   System.out.println(this.toString());
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   public void flushAll(int txnum) {
      bufferMgr.flushAll(txnum);
   }
   
   /**
    * Returns the number of available (ie unpinned) buffers.
    * @return the number of available buffers
    */
   public int available() {
      return bufferMgr.available();
   }
   
   private boolean waitingTooLong(long starttime) {
      return System.currentTimeMillis() - starttime > MAX_TIME;
   }
 
   //CS4432_Project1: return a basic buffer manager
   public BasicBufferMgr getBufferMgr() {
		return bufferMgr;
	}
   
   //CS4432_Project1: print every frame Id in the free list
 public void printfreelist(){
	  Stack<Integer> temp = bufferMgr.getFreelist();
	  Iterator<Integer> item = temp.iterator();
	  while (item.hasNext()){
		  System.out.print(item.next() + ",");
	  }
	  System.out.println();
 }
 
 //CS4432_Project1: print every block & frame ID in the hash table
 public void printhashtable(){
	   Hashtable<Integer, Integer> temp = bufferMgr.getBufferPagesinPool();
	   Set<Integer> set = temp.keySet();
	   Iterator<Integer> itr = set.iterator();
	    while (itr.hasNext()) {
	      int block = itr.next();
	      System.out.println("Block: "+ block + " Buffer id is " + temp.get(block));
	    }	   
 }
 
 //CS4432-Project1:
 public String toString() {
	 return "----------------Buffers---------------\n" + 
			 this.bufferMgr.toString() +
			 "\n--------------------------------------";
 }
   
}
