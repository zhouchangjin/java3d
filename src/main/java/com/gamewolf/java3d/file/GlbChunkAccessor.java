package com.gamewolf.java3d.file;

import java.io.IOException;

import com.gamewolf.java3d.model.JMesh;
import com.gamewolf.java3d.model.JTriangle;
import com.gamewolf.java3d.model.JVertexSimple;

import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

public class GlbChunkAccessor extends ChunkAccessor{

	long positionOffset;
	
	long uvOffset;
	
	long normalOffset;
	
	int vertexCnt;
	
	long indexOffset;
	
	int triangleVCnt;
	
	public GlbChunkAccessor(RandomAccessFileAdvance rafa,
			long offset, int length) {
		super(rafa, offset, length);
	}
	
	public GlbChunkAccessor() {
		
	}
	
	public GlbChunkAccessor(GlbChunkAccessorBuilder builder) {
		this.rafa=builder.rafa;
		this.beginOffset=builder.offset;
		this.length=builder.length;
		this.positionOffset=builder.positionOffset;
		this.indexOffset=builder.indexOffset;
		this.uvOffset=builder.uvOffset;
		this.normalOffset=builder.normalOffset;
		this.vertexCnt=builder.vetexCnt;
		this.triangleVCnt=builder.triangleVCnt;
	}
	
	
	public JMesh readMesh() throws IOException {
		if(this.rafa!=null) {
			JMesh mesh=new JMesh();
			rafa.offset(beginOffset+positionOffset);
			
			for(int i=0;i<vertexCnt;i++) {
				float x=rafa.readFloat();
				float y=rafa.readFloat(); 
				float z=rafa.readFloat(); 
				JVertexSimple vertex=new JVertexSimple(new Vec3(x, y, z));
				mesh.addVetex(vertex);
			}
			
			rafa.offset(beginOffset+normalOffset);
			for(int i=0;i<vertexCnt;i++) {
				float nx=rafa.readFloat();
				float ny=rafa.readFloat();
				float nz=rafa.readFloat();
				mesh.addNormal(new Vec3(nx, ny, nz));
			}
			
			if(uvOffset>0) {
				rafa.offset(beginOffset+uvOffset);
				for(int i=0;i<vertexCnt;i++){
					
					float u=rafa.readFloat();
					float v=rafa.readFloat();
					mesh.addUv(new Vec2(u,v));
				}
			}			
			
			rafa.offset(beginOffset+indexOffset);
			int tCnt=triangleVCnt/3;
			for(int i=0;i<tCnt;i++){
				
				short id1=rafa.readShort();
				short id2=rafa.readShort();
				short id3=rafa.readShort();
				JTriangle t=new JTriangle();
				t.setVertex(id1, id2, id3);
				t.setUv(id1, id2, id3);
				t.setNormal(id1, id2, id3);
				mesh.addTriangle(t);
			}
			return mesh;
		}
		
		return null;
	}
	
	
	
	public long getPositionOffset() {
		return positionOffset;
	}

	public void setPositionOffset(long positionOffset) {
		this.positionOffset = positionOffset;
	}

	public long getUvOffset() {
		return uvOffset;
	}

	public void setUvOffset(long uvOffset) {
		this.uvOffset = uvOffset;
	}

	public long getNormalOffset() {
		return normalOffset;
	}

	public void setNormalOffset(long normalOffset) {
		this.normalOffset = normalOffset;
	}

	public int getVertexCnt() {
		return vertexCnt;
	}

	public void setVertexCnt(int vertexCnt) {
		this.vertexCnt = vertexCnt;
	}

	public long getIndexOffset() {
		return indexOffset;
	}

	public void setIndexOffset(long indexOffset) {
		this.indexOffset = indexOffset;
	}
	
	public int getTriangleVCnt() {
		return triangleVCnt;
	}

	public void setTriangleVCnt(int triangleVCnt) {
		this.triangleVCnt = triangleVCnt;
	}




	public static class GlbChunkAccessorBuilder{
		
		RandomAccessFileAdvance rafa;
		
		long offset;
		
		int length;
		
		long positionOffset;
		
		long uvOffset;
		
		long normalOffset;
		
		int vetexCnt;
		
		long indexOffset;
		
		int triangleVCnt;
		
		
		
		public GlbChunkAccessorBuilder() {
			
		}
		
		public GlbChunkAccessorBuilder(RandomAccessFileAdvance rafa,long offset,int length) {
			this.rafa=rafa;
			this.offset=offset;
			this.length=length;
		}
		
		public GlbChunkAccessorBuilder setRandomAccessFileAdvance(RandomAccessFileAdvance rafa) {
			this.rafa=rafa;
			return this;
		}
		
		public GlbChunkAccessorBuilder setOffset(long offset) {
			this.offset=offset;
			return this;
		}
		
		public GlbChunkAccessorBuilder setLength(int length) {
			this.length=length;
			return this;
		}
		
		public GlbChunkAccessorBuilder setPositionOffset(long positionOffset) {
			this.positionOffset=positionOffset;
			return this;
		}
		
		public GlbChunkAccessorBuilder setNormalOffset(long noramlOffset) {
			this.normalOffset=noramlOffset;
			return this;
		}
		
		public GlbChunkAccessorBuilder setUvOffset(long uvOffset) {
			this.uvOffset=uvOffset;
			return this;
		}
		
		public GlbChunkAccessorBuilder setVetexCnt(int vetexCnt) {
			this.vetexCnt=vetexCnt;
			return this;
		}
		
		public GlbChunkAccessorBuilder setIndexOffset(int indexOffset) {
			this.indexOffset=indexOffset;
			return this;
		}
		
		public GlbChunkAccessorBuilder setTriangleVCnt(int triangleVCnt) {
			this.triangleVCnt=triangleVCnt;
			return this;
		}
		
		public GlbChunkAccessor build() {
			return new GlbChunkAccessor(this);
		}
	}

}
