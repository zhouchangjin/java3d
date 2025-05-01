package com.gamewolf.java3d.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gamewolf.java3d.model.B3DM;
import com.gamewolf.java3d.model.JMesh;

import glm.mat._4.Mat4;

public class B3DMUtil {
	
	public static void exportGlbFromB3DM(String path,String outPath) {
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
						
			String json=rafa.readString(featureJsonLength);
			System.out.println(json);
			
			String batchJson=rafa.readString(batchTableJSONLength);
			System.out.println(batchJson);
			
			long offset=rafa.offset();
			int rest=(int)(byteLength3-offset);
			System.out.println(rest);
			byte[] bytes=new byte[rest];
			RandomAccessFile raf2=new RandomAccessFile(outPath, "rw");
			rafa.readAll(bytes);
			raf2.write(bytes);
			raf2.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
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
						
			String json=rafa.readString(featureJsonLength);
			System.out.println(json);
			
			String batchJson=rafa.readString(batchTableJSONLength);
			System.out.println(batchJson);
			
			long offset=rafa.offset();
			System.out.println(offset);
			
			List<JMesh> meshlist=GltfUtil.readMeshFromGlb(rafa, offset);
			System.out.println(meshlist.size());
			return meshlist;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}
	


	public static B3DM readB3DMModelFromB3DMFile(String path) {
		B3DM b3dm = new B3DM();

		try {

			RandomAccessFileAdvance rafa = new RandomAccessFileAdvance(path);
			rafa.open();
			
			String magicHead = rafa.readString(4);
			b3dm.setMagicHead(magicHead);
			
			int version = rafa.readInt();
			b3dm.setVersion(version);
			
			int byteLength = rafa.readInt();
			b3dm.setByteLength(byteLength);

			int featureJsonLength = rafa.readInt();
			b3dm.setFeatureJsonLength(featureJsonLength);
			
			int featureTableBinaryLength = rafa.readInt();
			b3dm.setFeatureTableBinaryLength(featureTableBinaryLength);
			
			int batchTableJSONLength = rafa.readInt();
			b3dm.setBatchTableJSONLength(batchTableJSONLength);
			int batchTableBinaryLegnth = rafa.readInt();
			b3dm.setBatchTableBinaryLegnth(batchTableBinaryLegnth);
			
			String json = rafa.readString(featureJsonLength);
			System.out.println(json);
			b3dm.setFeatureJson(JSONObject.parseObject(json));
			
			String batchJson = rafa.readString(batchTableJSONLength);
			b3dm.setBatchJson(JSONObject.parseObject(batchJson));
			System.out.println(batchJson);
			
			long offset = rafa.offset();
			b3dm.setGlbOffset(offset);
			
			List<JMesh> meshlist = GltfUtil.readMeshFromGlb(rafa, offset);
			b3dm.setMeshList(meshlist);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return b3dm;
	}
	
	
	
	public static void main(String args[]) {
		String path="d:/gltf/ll.b3dm";
		String outpath="d:/ll.glb";
		B3DM b3dm=B3DMUtil.readB3DMModelFromB3DMFile(path);
		Mat4 mat=b3dm.getTransformation();
		System.out.println(mat);
		//B3DMUtil.exportGlbFromB3DM(path,outpath);
	}
}
