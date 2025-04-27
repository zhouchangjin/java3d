package com.gamewolf.java3d.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RandomAccessFileAdvance {
	
	String path;
	
	RandomAccessFile raf;
	
	public RandomAccessFileAdvance(String path) {
		this.path=path;
		
	}
	
	public void readAll(byte[] bytes) throws IOException {
		raf.readFully(bytes);
	}
	
	public void offset(long offset) throws IOException {
		raf.seek(offset);
	}
	
	public long offset() throws IOException {
		return raf.getFilePointer();
	}
	
	public void open() throws FileNotFoundException {		
		raf=new RandomAccessFile(path, "rw");
	}

	public String readString(int i) throws IOException {
		byte[] bytes=new byte[i];
		raf.read(bytes);
		String str=new String(bytes);
		return str;
	}
	
	public Integer readInt() throws IOException {
		 ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
         raf.read(buffer.array());
         buffer.rewind();
         return buffer.getInt();
	}

	public float readFloat() throws IOException {
		 ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
         raf.read(buffer.array());
         buffer.rewind();
         return buffer.getFloat();
	}

	public short readShort() throws IOException {
		 ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
         raf.read(buffer.array());
         buffer.rewind();
         return buffer.getShort();
	}

}
