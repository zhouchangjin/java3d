package com.gamewolf.java3d.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.gamewolf.java3d.model.JMesh;

public class B3DMUtil {
	
	public static List<JMesh> readMeshFromB3DM(String path){
		

		try {
			
			RandomAccessFileAdvance rafa=new RandomAccessFileAdvance(path);
			rafa.open();
			String magicHead=rafa.readString(4);
			System.out.println(magicHead);
			int version=rafa.readInt();
			System.out.println(version);
			
			int byteLength3=rafa.readInt();
			System.out.println(byteLength3);
			
			int featureJsonLength=rafa.readInt();
			System.out.println(featureJsonLength);
			
			int featureTableBinaryLength=rafa.readInt();
			System.out.println(featureTableBinaryLength);
			
			int batchTableJSONLength=rafa.readInt();
			System.out.println(batchTableJSONLength);
			
			int batchTableBinaryLegnth=rafa.readInt();
			System.out.println(batchTableBinaryLegnth);
			
			//int jsonFormat=rafa.readInt();
			//System.out.println(jsonFormat);
			
			String json=rafa.readString(featureJsonLength);
			System.out.println(json);
			
			String batchJson=rafa.readString(batchTableJSONLength);
			System.out.println(batchJson);
			
			long offset=rafa.offset();
//			
			
			//byte[] bytes=new byte[chunl]
			//RandomAccessFile raf=new RandomAccessFile("d:/test.glb", "rw");
			//raf.write(null)
//			
			
			List<JMesh> meshlist=GltfUtil.readMeshFromGlb(rafa, offset);
			System.out.println(meshlist.size());
			
			return meshlist;
//			String magicGlb=rafa.readString(4);
//			System.out.println(magicGlb);
//			
//			int versionglb=rafa.readInt();
//			System.out.println(versionglb);
//			
//			int length=rafa.readInt();
//			System.out.println(length);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}

}
