package com.gamewolf.java3d.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import com.gamewolf.java3d.model.JMesh;
import com.gamewolf.java3d.model.JTriangle;
import com.gamewolf.java3d.model.JVertexSimple;

import glm.vec._2.Vec2;

public class WaveFrontUtil {
	
	public static void SaveMeshToWaveFont(JMesh mesh,String obj_name,String file_path) {
		
		try(BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file_path))){
			String nameLIne="o"+obj_name;
			bufferedWriter.write(nameLIne);
			bufferedWriter.newLine();
			List<JVertexSimple> list=mesh.getVertices();
			for(int i=0;i<list.size();i++) {
				JVertexSimple v=list.get(i);
				String vline="v "+v.getPosition().x+" "+v.getPosition().y+" "+v.getPosition().z;
				bufferedWriter.write(vline);
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
			
			List<Vec2> uvmap=mesh.getUvMaps();
			
			for(int i=0;i<uvmap.size();i++) {
				Vec2 uv=uvmap.get(i);
				String uvStr="vt "+uv.x+" "+uv.y;
				bufferedWriter.write(uvStr);
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
			
			List<JTriangle> trilist=mesh.getTriangles();
			
			for(int i=0;i<trilist.size();i++) {
				JTriangle t=trilist.get(i);
				int vid0=t.getVertex(0)+1;
				int vid1=t.getVertex(1)+1;
				int vid2=t.getVertex(2)+1;
				String uvPart0="";
				String uvPart1="";
				String uvPart2="";
				if(uvmap.size()>0) {
					int tvid0=t.getUv(0)+1;
					int tvid1=t.getUv(1)+1;
					int tvid2=t.getUv(2)+1;
					uvPart0="/"+tvid0;
					uvPart1="/"+tvid1;
					uvPart2="/"+tvid2;
				}
				
				String faceLine="f "+vid0+uvPart0+" "+vid1+uvPart1+" "+vid2+uvPart2;
				bufferedWriter.write(faceLine);
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
			
			
		}catch(Exception e) {
			
		}
		
		
	}

}
