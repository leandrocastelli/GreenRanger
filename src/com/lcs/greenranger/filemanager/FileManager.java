package com.lcs.greenranger.filemanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.lcs.greenranger.R;

public class FileManager {

	private static final String TAG = "FileManager";
	private static FileManager instance;
	private FileManager(){};
	private final String filename = "green.mp3";
	
	public static FileManager getInstance(){
		if(instance==null)
			instance = new FileManager();
		return instance;
	}
	
	private String copyFile(Context ctx){
		String path = null;
		InputStream in;
		OutputStream out;
		in = ctx.getResources().openRawResource(R.raw.green);
		String state = Environment.getExternalStorageState();
		
		try{
			if(Environment.MEDIA_MOUNTED.equals(state)){
				File temp = new File(Environment.getExternalStorageDirectory()+"/GreenRanger/");
				if(!temp.exists())
					temp.mkdir();
				
				path = temp.getPath()+"/"+filename;
				
			}
			else {
				path ="/data/data/"+ctx.getPackageName()+"/"+filename;
			}
			out = new FileOutputStream(path);
			byte[] buffer = new byte[1024];
	        int read;
	        while ((read = in.read(buffer)) != -1) {
	            out.write(buffer, 0, read);
	        }
	        
	        in.close();
			in = null;
			out.flush();
			out.close();
		}
		catch (FileNotFoundException ex)
		{
			Log.e(TAG, ex.getMessage());
			return null;
		}
		catch (IOException e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
		
		return path;
	}
	public String getFilePath(Context ctx ){
		String path = fileExists(ctx);
		if(path!=null)
		{
			return path;
		}
		else {
			path = copyFile(ctx);
		}
		return path;
	}
	
	private String fileExists(Context ctx) {
		File file ;
		String state = Environment.getExternalStorageState();
		String path;
		if(Environment.MEDIA_MOUNTED.equals(state)){
			path = Environment.getExternalStorageDirectory()+"/GreenRanger/"+filename;
			
		}
		else {
			path = "/data/data/"+ctx.getPackageName()+"/"+filename;
		}
		file = new File(path);
		if(file.exists()){
			return path;
		}
		else {
			return null;
		}
	}
}
