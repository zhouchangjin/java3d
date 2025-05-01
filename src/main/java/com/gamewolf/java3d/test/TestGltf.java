package com.gamewolf.java3d.test;

import java.util.List;

import com.gamewolf.java3d.file.B3DMUtil;
import com.gamewolf.java3d.file.D3TileUtil;
import com.gamewolf.java3d.file.GltfUtil;
import com.gamewolf.java3d.file.WaveFrontUtil;
import com.gamewolf.java3d.model.JMesh;

import glm.mat._4.Mat4;

public class TestGltf {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		List<JMesh> mesh=GltfUtil.readMeshFromGltf("D:\\三维模型\\gltf_wooden_stool\\wooden_stool.gltf");
//		System.out.print(mesh);
//		for(int i=0;i<mesh.size();i++) {
//			WaveFrontUtil.SaveMeshToWaveFont(mesh.get(i), "meshi", "d:/"+i+".obj");
//		}
		String path="d:/gltf/ll.b3dm";
		//List<JMesh> b3dm=B3DMUtil.readMeshFromB3DM(path);
		B3DMUtil.readB3DMModelFromB3DMFile(path);
		//WaveFrontUtil.SaveMeshToWaveFont(b3dm.get(0), "dragon", "d:/building.obj");
		//String path="d:/test.glb";
		//List<JMesh> meshgbl=GltfUtil.readMeshFromGlb(path);
//		int i=0;
//		for(JMesh msh:b3dm) {
//			i++;
//			WaveFrontUtil.SaveMeshToWaveFont(msh, "cube", "d:/dragon_low"+i+".obj");
//		}
//		Mat4 mat4=new Mat4(4.843178171884396,
//          1.2424271388626869,
//          0,
//          0,
//          -0.7993230372483163,
//          3.115888059101095,
//          3.827835456922795,
//          0,
//          0.9511613309563466,
//          -3.7077778261067222,
//          3.2167803336138237,
//          0,
//          1215011.9317263428,
//          -4736309.3434217675,
//          4081602.0044800863,
//          1);
//		Mat4 mat4=new Mat4(1.0f);
//		mat4.translate(null)
//		D3TileUtil.sampleJMesh2Grid(b3dm.get(0),mat4);
	}

}
