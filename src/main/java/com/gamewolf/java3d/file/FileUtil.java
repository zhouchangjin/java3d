package com.gamewolf.java3d.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileUtil {
	
	public static String readFile2String(String path) {
		StringBuffer sb=new StringBuffer();
		try(BufferedReader br=new BufferedReader(new FileReader(new File(path)))){
			String line="";
			while((line=br.readLine())!=null) {
				sb.append(line);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
