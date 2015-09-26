package replacementPolicy;

import simpledb.buffer.Buffer;


public class LRUPolicy implements ReplacementPolicy {

	@Override
	public int chooseBufferForReplacement(Buffer[] bufferPool) {
		int lru = 0;
		//CS4432-project1: keep track of the least recently used time 
		long lruDate = bufferPool[0].getLastModifiedDate();
		for(int i = 0; i < bufferPool.length-1; i++){
			if(!bufferPool[i].isPinned() && bufferPool[i].getLastModifiedDate() < lruDate){
				lruDate = bufferPool[i].getLastModifiedDate();
				lru = i;		
			}
		}
		
		System.out.println("\n!!!!!! I am LRU policy !!!!!!\n");
		System.out.println("I choose buffer number " + String.valueOf(lru) + " to replace\n");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
		
		return lru;
	}
	

}
