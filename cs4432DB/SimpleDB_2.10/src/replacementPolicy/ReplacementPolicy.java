package replacementPolicy;

import simpledb.buffer.Buffer;

/*CS4432-Project1: if the no buffer in the empty buffer list,
A replacement policy can be used to select buffer for replace
this interface will implemented by the different replacement policies for
this assignment, we will implement LRU and Clock policy
*/
public interface ReplacementPolicy {
	
	public int chooseBufferForReplacement( Buffer[] bufferPool);

	
}
