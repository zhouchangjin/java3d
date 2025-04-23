package com.gamewolf.java3d.test;

import java.util.List;

import com.gamewolf.java3d.file.GltfUtil;
import com.gamewolf.java3d.file.WaveFrontUtil;
import com.gamewolf.java3d.model.JMesh;

public class TestGltf {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<JMesh> mesh=GltfUtil.readMeshFromGltf("D:\\三维模型\\gltf_wooden_stool\\wooden_stool.gltf");
		System.out.print(mesh);
		for(int i=0;i<mesh.size();i++) {
			WaveFrontUtil.SaveMeshToWaveFont(mesh.get(i), "meshi", "d:/"+i+".obj");
		}
	}

}
