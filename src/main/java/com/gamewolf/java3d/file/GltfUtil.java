package com.gamewolf.java3d.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gamewolf.java3d.model.JMesh;
import com.gamewolf.java3d.model.JTriangle;
import com.gamewolf.java3d.model.JVertexSimple;

import glm.mat._4.Mat4;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

public class GltfUtil {
	
	public static List<GlbChunkAccessor> parseChunkAccessor(JSONObject mesh,
			int id,Mat4 mat4,JSONArray accessors,
			JSONArray bufferViews,JSONArray buffers) {
		
		JSONArray primitives=mesh.getJSONArray("primitives");
		List<GlbChunkAccessor> accessorList=new ArrayList<GlbChunkAccessor>();
		
		for(int pi=0;pi<primitives.size();pi++) {
			
			JSONObject primitive=primitives.getJSONObject(pi);
			JSONObject attributes=primitive.getJSONObject("attributes");
			Integer indices=primitive.getIntValue("indices");
			if(indices!=null) {
				int positionIdx=attributes.getIntValue("POSITION");
				int normalidx=attributes.getIntValue("NORMAL");
				int uvOffset=-1;
				if(attributes.containsKey("TEXCOORD_0")) {
					int uvIdx=attributes.getIntValue("TEXCOORD_0");
					JSONObject uvAccessor=accessors.getJSONObject(uvIdx);
					int uvBufferView=uvAccessor.getIntValue("bufferView");
					JSONObject bvUvObject=bufferViews.getJSONObject(uvBufferView);
					uvOffset=bvUvObject.getIntValue("byteOffset");
				}
								
				JSONObject indexAccessor=accessors.getJSONObject(indices);
				JSONObject positionAccessor=accessors.getJSONObject(positionIdx);
				JSONObject noramlAccessor=accessors.getJSONObject(normalidx);
				
				int positionBufferView=positionAccessor.getIntValue("bufferView");
				int normalBufferView=noramlAccessor.getIntValue("bufferView");
				
				int indexBufferView=indexAccessor.getIntValue("bufferView");
				int componentType=indexAccessor.getIntValue("componentType");
				int indexBufferLocalOffset=0;
				if(indexAccessor.containsKey("byteOffset")) {
					indexBufferLocalOffset=indexAccessor.getIntValue("byteOffset");
				}
				int normalLocalOffset=0;
				if(noramlAccessor.containsKey("byteOffset")) {
					normalLocalOffset=noramlAccessor.getIntValue("byteOffset");
				}
				int positionLocalOffset=0;
				if(positionAccessor.containsKey("byteOffset")) {
					positionLocalOffset=positionAccessor.getIntValue("byteOffset");
				}
				
				int min=-1;int max=-1;
				if(indexAccessor.containsKey("min") && indexAccessor.containsKey("max")) {
				    min=indexAccessor.getJSONArray("min").getIntValue(0);
					max=indexAccessor.getJSONArray("max").getIntValue(0);
					
				}
				
				int vertexCnt=positionAccessor.getIntValue("count");
				int triangelIndiceCnt=indexAccessor.getIntValue("count");
				
				JSONObject bvPositionObject=bufferViews.getJSONObject(positionBufferView);
				JSONObject bvNormalObject=bufferViews.getJSONObject(normalBufferView);
				JSONObject bvIndiceObject=bufferViews.getJSONObject(indexBufferView);

				//int bufferPointId=bvPositionObject.getIntValue("buffer");
				//int bufferNormalId=bvNormalObject.getIntValue("buffer");
				//int bufferVIdxId=bvIndiceObject.getIntValue("buffer");
				
				int positionOffset=bvPositionObject.getIntValue("byteOffset")+positionLocalOffset;
				int normalOffset=bvNormalObject.getIntValue("byteOffset")+normalLocalOffset;
				int indiceOffset=bvIndiceObject.getIntValue("byteOffset")+indexBufferLocalOffset;
				
				
				GlbChunkAccessor.GlbChunkAccessorBuilder builder=new GlbChunkAccessor.GlbChunkAccessorBuilder();
				builder.setPositionOffset(positionOffset)
					.setNormalOffset(normalOffset)
					.setUvOffset(uvOffset)
					.setIndexOffset(indiceOffset)
					.setVetexCnt(vertexCnt)
					.setTriangleVCnt(triangelIndiceCnt)
					.setIndexComponentType(componentType)
					.setMinMaxVertexRange(min, max)
					.setMeshId(id).setTransform(mat4);
				GlbChunkAccessor accessor= builder.build();
				accessorList.add(accessor);
			}
			
			
			
			
		}
		return accessorList;
	}
	
	public static List<GlbChunkAccessor> parseGlbChunkList(String json){
		JSONObject gltfJson=JSONObject.parseObject(json);
		//System.out.print(gltfJson);
		JSONArray accessors=gltfJson.getJSONArray("accessors");
		JSONArray meshes=gltfJson.getJSONArray("meshes");
		JSONArray buffers=gltfJson.getJSONArray("buffers");
		JSONArray bufferViews=gltfJson.getJSONArray("bufferViews");
		JSONArray nodes=gltfJson.getJSONArray("nodes");
		Map<Integer,Mat4> transformMap=new HashMap<Integer, Mat4>();
		for(int i=0;i<nodes.size();i++) {
			JSONObject node=nodes.getJSONObject(i);
			if(node.containsKey("mesh")) {
				int meshId=node.getInteger("mesh");
				if(node.containsKey("matrix")) {
					JSONArray matrixJson=node.getJSONArray("matrix");
					float matrix[]=new float[16];
					for(int fi=0;fi<16;fi++) {
						matrix[fi]=matrixJson.getFloatValue(fi);
					}
					Mat4 mat4=new Mat4(matrix);
					transformMap.put(meshId, mat4);
				}
			}
		}
		List<GlbChunkAccessor> accessorList=new ArrayList<>();
		
		for(int i=0;i<meshes.size();i++) {
			JSONObject meshi=meshes.getJSONObject(i);
			Mat4 mat4=new Mat4(1.0f);
			if(transformMap.containsKey(i)) {
			    mat4=transformMap.get(i);
			    //System.out.println(mat4);
			}
			List<GlbChunkAccessor> accessor=parseChunkAccessor(meshi,i,mat4,accessors,bufferViews,buffers);
			accessorList.addAll(accessor);
		}
		
		return accessorList;
	}
	
	public static List<JMesh> readMeshFromGlb(RandomAccessFileAdvance rafa,long offset){
		
		try {
			rafa.offset(offset);
			String magic=rafa.readString(4);
			//System.out.println(magic);
			int version=rafa.readInt();
			//System.out.println("version="+version);
			int length=rafa.readInt();
			//System.out.println("length="+length);
			
			int jsonLength=rafa.readInt();
			//System.out.println("jsonLenght="+jsonLength);
			
			Integer chunkType=rafa.readInt();
			//System.out.println("chunkType="+Integer.toHexString(chunkType));
			
			String json=rafa.readString(jsonLength);
			System.out.println(json);
			
			int chunkLength2=rafa.readInt();
			//System.out.println("chunkLength2="+chunkLength2);
			Integer chunkType2=rafa.readInt();
			//System.out.println("chunkType2="+Integer.toHexString(chunkType2));
			
			List<GlbChunkAccessor> chunklist=parseGlbChunkList(json);
			long curPos=rafa.offset();
			List<JMesh> meshLIst=new ArrayList<>();
			for(GlbChunkAccessor glbChunkAccessor:chunklist) {
				JMesh mesh=readMeshFromGlbByChunckDef(rafa,glbChunkAccessor,curPos,chunkLength2);
				meshLIst.add(mesh);
			}
			//System.out.print(chunklist.get(0));
			return meshLIst;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	public static JMesh readMeshFromGlbByChunckDef(RandomAccessFileAdvance rafa, 
			GlbChunkAccessor glbChunkAccessor,
			long curPos,int chunkLength) throws IOException {
			glbChunkAccessor.setBeginOffset(curPos);
			glbChunkAccessor.setRafa(rafa);
			glbChunkAccessor.setLength(chunkLength);
			return glbChunkAccessor.readMesh();
	}

	public static List<JMesh> readMeshFromGlb(String path){
		RandomAccessFileAdvance rafa=new RandomAccessFileAdvance(path);
		try {
			rafa.open();
			return readMeshFromGlb(rafa,0);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static List<JMesh> readMeshFromGltfJson(String json,RandomAccessFileAdvance rafa){
		
		return null;
	}
	
	public static List<JMesh> readMeshFromGltf(String path){
		
		File file=new File(path);
		String baseFolder=file.getParentFile().getAbsolutePath();
		//System.out.println(baseFolder);
		List<JMesh> meshList=new ArrayList<>();
		String content=FileUtil.readFile2String(path);
		JSONObject gltfJson=JSONObject.parseObject(content);
		JSONArray accessors=gltfJson.getJSONArray("accessors");
		JSONArray meshes=gltfJson.getJSONArray("meshes");
		JSONArray buffers=gltfJson.getJSONArray("buffers");
		JSONArray bufferViews=gltfJson.getJSONArray("bufferViews");
		for(int i=0;i<meshes.size();i++) {
			JSONObject meshi=meshes.getJSONObject(i);
			JMesh jmeshi=parseMesh(meshi,accessors,bufferViews,buffers,baseFolder);
			meshList.add(jmeshi);
		}
		
		//System.out.println(gltfJson);
		return meshList;
	}

	private static JMesh parseMesh(JSONObject meshi, JSONArray accessors, JSONArray bufferViews, JSONArray buffers,
			String baseFolder) {

		
		JSONArray primitives=meshi.getJSONArray("primitives");
		JSONObject primitive=primitives.getJSONObject(0);
		JSONObject attributes=primitive.getJSONObject("attributes");
		Integer indices=primitive.getIntValue("indices");
		if(indices!=null) {
			int positionIdx=attributes.getIntValue("POSITION");
			int normalidx=attributes.getIntValue("NORMAL");
			int uvIdx=attributes.getIntValue("TEXCOORD_0");
			JSONObject indexAccessor=accessors.getJSONObject(indices);
			JSONObject positionAccessor=accessors.getJSONObject(positionIdx);
			JSONObject noramlAccessor=accessors.getJSONObject(normalidx);
			JSONObject uvAccessor=accessors.getJSONObject(uvIdx);
			int positionBufferView=positionAccessor.getIntValue("bufferView");
			int normalBufferView=noramlAccessor.getIntValue("bufferView");
			int indexBufferView=indexAccessor.getIntValue("bufferView");
			int uvBufferView=uvAccessor.getIntValue("bufferView");
			int vertexCnt=positionAccessor.getIntValue("count");
			int triangelIndiceCnt=indexAccessor.getIntValue("count");
			
			JSONObject bvPositionObject=bufferViews.getJSONObject(positionBufferView);
			JSONObject bvNormalObject=bufferViews.getJSONObject(normalBufferView);
			JSONObject bvIndiceObject=bufferViews.getJSONObject(indexBufferView);
			JSONObject bvUvObject=bufferViews.getJSONObject(uvBufferView);
			

			int bufferPointId=bvPositionObject.getIntValue("buffer");
			int bufferNormalId=bvNormalObject.getIntValue("buffer");
			int bufferVIdxId=bvIndiceObject.getIntValue("buffer");
			
			int positionOffset=bvPositionObject.getIntValue("byteOffset");
			int normalOffset=bvNormalObject.getIntValue("byteOffset");
			int uvOffset=bvUvObject.getIntValue("byteOffset");
			int indiceOffset=bvIndiceObject.getIntValue("byteOffset");
			
			JSONObject bufferLoc=buffers.getJSONObject(bufferPointId);
			String binPath=bufferLoc.getString("uri");
			String bufferPath=baseFolder+File.separator+binPath;
			return readMesh(bufferPath,positionOffset,normalOffset,uvOffset,vertexCnt,indiceOffset,triangelIndiceCnt);
		}
		
		return null;
	}
	
	private static float ReadFloat(RandomAccessFile rs) throws IOException {
		 ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
         rs.read(buffer.array());
         buffer.rewind();
         return buffer.getFloat();
	}
	
	private static short ReadShort(RandomAccessFile rs) throws IOException {
		 ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
         rs.read(buffer.array());
         buffer.rewind();
         return buffer.getShort();
	}

	private static JMesh readMesh(String bufferPath, int positionOffset, int normalOffset, int uvOffset, int vertexCnt,
			int indiceOffset, int triangelIndiceCnt) {
		// TODO Auto-generated method stub
		JMesh mesh=new JMesh();
		try (RandomAccessFile rs=new RandomAccessFile(bufferPath,"rw")){
			rs.seek(positionOffset);
			for(int i=0;i<vertexCnt;i++) {
				float x=ReadFloat(rs);//rs.readFloat();
				float y=ReadFloat(rs);//rs.readFloat();
				float z=ReadFloat(rs);//rs.readFloat();
				JVertexSimple vertex=new JVertexSimple(new Vec3(x, y, z));
				mesh.addVetex(vertex);
			}
			
			//dis.reset();
			//dis.skip(normalOffset);
			rs.seek(normalOffset);
			for(int i=0;i<vertexCnt;i++) {
				float nx=ReadFloat(rs);
				float ny=ReadFloat(rs);
				float nz=ReadFloat(rs);
				mesh.addNormal(new Vec3(nx, ny, nz));
			}
			
			//dis.reset();
			//dis.skip(uvOffset);
			rs.seek(uvOffset);
			
			for(int i=0;i<vertexCnt;i++){
				
				float u=ReadFloat(rs);
				float v=ReadFloat(rs);
				mesh.addUv(new Vec2(u,v));
			}
			
			//dis.reset();
			//dis.skip(indiceOffset);
			rs.seek(indiceOffset);
			
			int triangleCnt=triangelIndiceCnt/3;
			for(int i=0;i<triangleCnt;i++){
				
				short id1=ReadShort(rs);
				short id2=ReadShort(rs);
				short id3=ReadShort(rs);
				JTriangle t=new JTriangle();
				t.setVertex(id1, id2, id3);
				t.setUv(id1, id2, id3);
				t.setNormal(id1, id2, id3);
				mesh.addTriangle(t);
			}

			return mesh;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return mesh;
	}

}
