package simpledb.buffer;

import simpledb.server.SimpleDB;
import simpledb.file.*;

/**
 * An individual buffer.
 * A buffer wraps a page and stores information about its status,
 * such as the disk block associated with the page,
 * the number of times the block has been pinned,
 * whether the contents of the page have been modified,
 * and if so, the id of the modifying transaction and
 * the LSN of the corresponding log record.
 * @author Edward Sciore
 */
public class Buffer {
   private Page contents = new Page();
   private Block blk = null;
   //CS4432-Project1:keep track of number of pin, pin counter.
   private int pins = 0;
   //CS4432-Project1: keep track the last modify time
   private long lastModified = 0;
 //CS4432-Project1: modifiedBy serves as dirty bit, if it is -1, the frame doesn't need to be written back to disk; 
 //CS4432-Project1: else, frame has been modified by transaction, transaction id is modifiedBy value.
   private int modifiedBy = -1;  // negative means not modified
   private int logSequenceNumber = -1; // negative means no corresponding log record
   private int bufID; // CS4432-Project1: An integer to the ID of the buffer.
   //CS4432-Project1: Set a reference bit to a buffer
   private int referenceBit = 0;

   /**
    * Creates a new buffer, wrapping a new 
    * {@link simpledb.file.Page page}.  
    * This constructor is called exclusively by the 
    * class {@link BasicBufferMgr}.   
    * It depends on  the 
    * {@link simpledb.log.LogMgr LogMgr} object 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * That object is created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    */
   public Buffer() {}
   
   /**
    * CS4432-Project1
    * Creates a new buffer with a specified buffer ID.
    * @param bufID the ID of the new buffer
    */
   public Buffer(int bufID) {
	   this.bufID = bufID;
	   updateTime();
   }
   
   /**
    * CS4432-Project1
    * Returns the ID of the buffer.
    * @return the ID of the buffer
    */
   public int getBufID() {
	   return bufID;
   }
   
   /**
    * Returns the integer value at the specified offset of the
    * buffer's page.
    * If an integer was not stored at that location,
    * the behavior of the method is unpredictable.
    * @param offset the byte offset of the page
    * @return the integer value at that offset
    */
   public int getInt(int offset) {
	   updateTime();
      return contents.getInt(offset);
   }
    // CS4432-project1 Return the last modify date of buffer
    public long getLastModifiedDate(){
    	return lastModified;
    }
    

   /**
    * Returns the string value at the specified offset of the
    * buffer's page.
    * If a string was not stored at that location,
    * the behavior of the method is unpredictable.
    * @param offset the byte offset of the page
    * @return the string value at that offset
    */
   public String getString(int offset) {
	   //CS4432-Project1: Update modify time as current time
	   updateTime();
      return contents.getString(offset);
   }

   /**
    * Writes an integer to the specified offset of the
    * buffer's page.
    * This method assumes that the transaction has already
    * written an appropriate log record.
    * The buffer saves the id of the transaction
    * and the LSN of the log record.
    * A negative lsn value indicates that a log record
    * was not necessary.
    * @param offset the byte offset within the page
    * @param val the new integer value to be written
    * @param txnum the id of the transaction performing the modification
    * @param lsn the LSN of the corresponding log record
    */
   public void setInt(int offset, int val, int txnum, int lsn) {
	  //CS4432-Project1: This step set this frame dirty(has been modified), and record which transaction perform the modification. 
      modifiedBy = txnum;
      if (lsn >= 0)
	      logSequenceNumber = lsn;
      contents.setInt(offset, val);
    //CS4432-Project1: Update modify time as current time
      updateTime();
   }
   
   //CS4342-Project1: Set the reference bit as 1
   public void setReferenceBit(){
	   referenceBit = 1;
   }
   
   //CS4342-Project1: Set the reference bit as 1
   public void unSetReferenceBit(){
	   referenceBit = 0;
   }
   
   //CS4432-Project1: check if the current Buffer has a refefrence bit or not
   public boolean isReferenceBitset(){
	   return referenceBit == 1;	   
   }

   /**
    * Writes a string to the specified offset of the
    * buffer's page.
    * This method assumes that the transaction has already
    * written an appropriate log record.
    * A negative lsn value indicates that a log record
    * was not necessary.
    * The buffer saves the id of the transaction
    * and the LSN of the log record.
    * @param offset the byte offset within the page
    * @param val the new string value to be written
    * @param txnum the id of the transaction performing the modification
    * @param lsn the LSN of the corresponding log record
    */
   public void setString(int offset, String val, int txnum, int lsn) {
	  //CS4432-Project1: This step set this frame dirty(has been modified), and record which transaction perform the modification. 
      modifiedBy = txnum;
      if (lsn >= 0)
	      logSequenceNumber = lsn;
      contents.setString(offset, val);
    //CS4432-Project1: Update modify time as current time
      updateTime();
   }

   /**
    * Returns a reference to the disk block
    * that the buffer is pinned to.
    * @return a reference to a disk block
    */
   public Block block() {
      return blk;
   }

   /**
    * Writes the page to its disk block if the
    * page is dirty.
    * The method ensures that the corresponding log
    * record has been written to disk prior to writing
    * the page to disk.
    */
   void flush() {
	 //CS4432-Project1: modifiedBy >= 0 means frame has been modified 
      if (modifiedBy >= 0) {
         SimpleDB.logMgr().flush(logSequenceNumber);
         contents.write(blk);
         //CS4432-Project1: after been written to disk, set modifiedBy=-1 means frame is not dirty at this point.
         modifiedBy = -1;
      }
   }
 //CS4432-Project1: This method is update modify time as current time
   private void updateTime() {
	   lastModified = System.currentTimeMillis();
   }
   
   /**
    * Increases the buffer's pin count.
    */
   void pin() {
	  updateTime();
      pins++;
   }

   /**
    * Decreases the buffer's pin count.
    */
   void unpin() {
	   
      pins--;
   }

   /**
    * Returns true if the buffer is currently pinned
    * (that is, if it has a nonzero pin count).
    * @return true if the buffer is pinned
    */
   public boolean isPinned() {
      return pins > 0;
   }

   /**
    * Returns true if the buffer is dirty
    * due to a modification by the specified transaction.
    * @param txnum the id of the transaction
    * @return true if the transaction modified the buffer
    */
   boolean isModifiedBy(int txnum) {
	 //CS4432-Project1: check if modified by this transaction.
      return txnum == modifiedBy;
   }

   /**
    * Reads the contents of the specified block into
    * the buffer's page.
    * If the buffer was dirty, then the contents
    * of the previous page are first written to disk.
    * @param b a reference to the data block
    */
   void assignToBlock(Block b) {
      flush();
      blk = b;
      contents.read(blk);
      pins = 0;
    //CS4432-Project1: Update modify time as current time
      updateTime();
   }

   /**
    * Initializes the buffer's page according to the specified formatter,
    * and appends the page to the specified file.
    * If the buffer was dirty, then the contents
    * of the previous page are first written to disk.
    * @param filename the name of the file
    * @param fmtr a page formatter, used to initialize the page
    */
   void assignToNew(String filename, PageFormatter fmtr) {
      flush();
      fmtr.format(contents);
      blk = contents.append(filename);
      pins = 0;
    //CS4342_Project1:Used by LRU.Update the last modified time as current time if a block assign to a bufer.
      updateTime();
   }
   
   
   //CS4432_Project1:display the state of buffer for testing purpose
   public String toString() {
	   String str = "";
	   
	   str = "(" +  this.bufID + ")";
	   str += ",";
	   str += (this.blk == null) ? "[]" : this.blk.toString();
	   str += ",";
	   str += (this.isPinned()) ? "Pinned" : "NotPinned";
	   
	   return str;
   }
}