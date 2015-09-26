package replacementPolicy;

import simpledb.buffer.Buffer;
/*
 * CS4432-Project1: This class use for clock replacement policy
 * The method keep track if the current buffer is pinned or not. If the buffer pinned, point to next
 * buffer. Otherwise, we need to check if the current buffer set reference bit or not.
 */

public class ClockPolicy implements ReplacementPolicy{
	int clockPointer = 0; //CS4432-Project1: keep track the position of clock pointer.
	@Override
	public int chooseBufferForReplacement(Buffer[] bufferPool) {
		int unpinnedBuffer = -1; //CS4432-Project1: set a initial value to an unpinned buffer
		Buffer currentBuffer; //CS4432-Project1: keep track the position of current buffer.
		
		do{
			currentBuffer = bufferPool[clockPointer];
			if(!currentBuffer.isPinned()){
				if(currentBuffer.isReferenceBitset()){ //CS4432-Project1: check the currentBuffer has reference  bit set or not
					currentBuffer.unSetReferenceBit(); //CS4432-Project: unset the current buffer
				}else{
					unpinnedBuffer = clockPointer; //CS4432-Project1: if the current buffer is unset, just use this.
				}
			}
			clockPointer = (clockPointer+1) % bufferPool.length;//CS4432-Project1: point to next buffer
				
		} while(unpinnedBuffer == -1);
		
		System.out.println("\n!!!!!! I am Clock policy !!!!!!\n");
		System.out.println("I choose buffer number " + String.valueOf(unpinnedBuffer) + " to replace\n");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
		
		// TODO Auto-generated method stub
		return  unpinnedBuffer;
	}

}
