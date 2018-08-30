package com.keyroy.gdx.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class XlsxTools {
	  public static String importFolder = "excel";

	  public static String jsonFolder = "json";

	  public static String jsonZipFolder = "json zip";

	  public static boolean format = false;

	  public static void main(String[] args) {
	    HashMap<String, String> cmds = new HashMap<String, String>();
	    String[] arrayOfString1 = args;
	    int j = args.length;
	    for (int i = 0; i < j; i++) {
			String cmd = arrayOfString1[i];
			try {
			   String[] sp = cmd.split("=");
			   String key = sp[0].trim();
			   String val = sp[1].trim();
			   cmds.put(key, val);
			}
			catch (Exception localException)
			{
			} 
	    }
	    if (cmds.containsKey("importFolder")) {
	      importFolder = (String)cmds.get(importFolder);
	    }

	    if (cmds.containsKey("jsonFolder")) {
	      jsonFolder = (String)cmds.get(jsonFolder);
	    }

	    if (cmds.containsKey("jsonZipFolder")) {
	      jsonZipFolder = (String)cmds.get(jsonZipFolder);
	    }

	    if (cmds.containsKey("format")) {
	    	format = true;
	    }else {
	    	format = false;	    	
	    }
	    
    	System.out.println("format:"+format);
	    execute();
	    System.out.println("ok");
	  }

	  public static final void execute()
	  {
	    createFolder(jsonFolder);
	    createFolder(jsonZipFolder);

	    File folder = new File(importFolder);
	    if (folder.exists()) {
	      File[] files = folder.listFiles();
	      if (files != null){
		        for (File file : files){
		        	  try {
		  	            List<JsonPack> arrays = XlsxParser.parser(file);
		  	            for (JsonPack jsonPack : arrays)
		  	            {
		  	              File jsonFile = writeJson(new File(jsonFolder), jsonPack, false);
		  	              jsonFile = writeJson(new File(jsonZipFolder), jsonPack, true);
		  	              System.out.println("write json : " + jsonFile.getName());
		  	            }
		  	          } catch (Exception e) {
		  	        	  System.out.println("the catch msg "+e.getMessage());
		  	            e.printStackTrace();
		  	          }
		        }	    	  
	      }
	        
	    }
	  }

	  private static final File writeJson(File jsonFolder, JsonPack jsonPack, boolean zip)
	    throws Exception
	  {
	    File jsonFile = new File(jsonFolder, jsonPack.getName() + ".json");
	    String json = null;
	    if (format)
	      json = jsonPack.getJsonObject().toString(2);
	    else {
	      json = jsonPack.getJsonObject().toString();
	    }
	    FileOutputStream fileOutputStream = new FileOutputStream(jsonFile);
	    if (zip) {
	      GZIPOutputStream outputStream = new GZIPOutputStream(fileOutputStream);
	      outputStream.write(json.getBytes(Charset.forName("UTF-8")));
	      outputStream.close();
	    } else {
//	      FileWriter fileWriter = new FileWriter(jsonFile);
//	      fileWriter.write(json);
//	      fileWriter.flush();
//	      fileWriter.close();
	      
	      OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(jsonFile),"UTF-8");
	      out.write(json);
	      out.flush();
	      out.close();
	    }
	    return jsonFile;
	  }

	  private static final File createFolder(String path) {
	    File file = new File(path);
	    delete(file);
	    file.mkdirs();
	    return file;
	  }

	  public static final void delete(File file) {
	    if (file.isDirectory()) {
	      File[] files = file.listFiles();
	      if (files != null) {
	        for (int i = 0; i < files.length; i++) {
	          delete(files[i]);
	        }
	      }
	    }
	    file.delete();
	  }}
