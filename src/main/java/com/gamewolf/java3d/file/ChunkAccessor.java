package com.gamewolf.java3d.file;

public class ChunkAccessor {
	
	long beginOffset;
	
	int length;
	
	RandomAccessFileAdvance rafa;
	
	public ChunkAccessor() {
		
	}
	
	public ChunkAccessor(RandomAccessFileAdvance rafa,long offset,int length) {
		this.beginOffset=offset;
		this.length=length;
		this.rafa=rafa;
	}

	public long getBeginOffset() {
		return beginOffset;
	}

	public void setBeginOffset(long beginOffset) {
		this.beginOffset = beginOffset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public RandomAccessFileAdvance getRafa() {
		return rafa;
	}

	public void setRafa(RandomAccessFileAdvance rafa) {
		this.rafa = rafa;
	}
	
	

}
