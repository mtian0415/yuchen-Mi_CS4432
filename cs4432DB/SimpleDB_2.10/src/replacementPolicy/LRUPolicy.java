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
		return lru;
	}
	

}
