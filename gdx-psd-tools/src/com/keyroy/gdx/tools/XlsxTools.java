package com.keyroy.gdx.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.json.m.JSONObject;

import com.keyroy.gdx.tools.config.XlsxToolsConfig;

public class XlsxTools {
	public static String version = "v1.0.6";
	
	public static final String CHANGELOG_STRING = "更新日志：\n"
			+ "v1.0.6 \n修改 使用 zip 的 json 文件名后缀为 bin"
			+ "\n文件名打 MD5"
			+ "\nMD5以 下划线区分";

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
			} catch (Exception localException) {
			}
		}
//		XlsxToolsConfig.load();
		
		if (cmds.containsKey("importFolder")) {
			XlsxToolsConfig.importFolder = cmds.get("importFolder");
		}

		if (cmds.containsKey("jsonFolder")) {
			XlsxToolsConfig.jsonFolder = cmds.get("jsonFolder");
		}

		if (cmds.containsKey("jsonZipFolder")) {
			XlsxToolsConfig.jsonZipFolder = cmds.get("jsonZipFolder");
		}
		if (cmds.containsKey("format")) {
			XlsxToolsConfig.format = "true".equals(cmds.get("format"));
		} else {
			XlsxToolsConfig.format = false;
		}

		if (cmds.containsKey("merge")) {
			XlsxToolsConfig.merge = "true".equals(cmds.get("merge"));
		} else {
			XlsxToolsConfig.merge = false;
		}

		if (cmds.containsKey("md5")) {
			XlsxToolsConfig.md5 = "true".equals(cmds.get("md5"));
		} else {
			XlsxToolsConfig.md5 = false;
		}
		System.out.println(CHANGELOG_STRING);
		System.out.println();
//		Logcat logcat = new Logcat("E:\\test.txt");
		System.out.println("工具版本: "+version);
		System.out.println("格式化: " + XlsxToolsConfig.format);
		System.out.println("合并 json: " + XlsxToolsConfig.merge);
		System.out.println("文档输入目录: " + XlsxToolsConfig.importFolder);
		System.out.println("json 输出目录: " + XlsxToolsConfig.jsonFolder);
		System.out.println("json zip 输出目录: " + XlsxToolsConfig.jsonFolder);
		System.out.println("当前目录："+System.getProperty("user.dir"));
		System.out.println("--------------开始执行程序--------------");
		execute();
		System.out.println("--------------程序执行完毕--------------");
		System.out.println("------------- 工具版本: "+version+" --------------");
	}

	public static final void execute() {
		createFolder(XlsxToolsConfig.jsonFolder);
		createFolder(XlsxToolsConfig.jsonZipFolder);
		
		File folder = new File(XlsxToolsConfig.importFolder);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					String fileName = file.getName();

					if (fileName != null && fileName.startsWith("!")) {
						System.out.println("跳过文件：" + fileName);
						continue;
					}
					try {
						List<JsonPack> arrays = XlsxParser.parser(file);
						// 合并数据
						if (XlsxToolsConfig.merge) {
							if(arrays.size() == 0) {
								continue;
							}
							String baseName = fileName.split("\\.")[0];
							JSONObject object = new JSONObject();
							for (JsonPack jsonPack : arrays) {
								object.put(jsonPack.getName(), jsonPack.getJsonObject());
							}
							JsonPack mergeJsonPack = new JsonPack(baseName + "_config", object);
							File jsonFile = writeJson(new File(XlsxToolsConfig.jsonFolder), mergeJsonPack, false);
							jsonFile = writeJson(new File(XlsxToolsConfig.jsonZipFolder), mergeJsonPack, true);
							System.out.println("write json : " + jsonFile.getName());
						} else {
							// 不合并数据
							for (JsonPack jsonPack : arrays) {
								File jsonFile = writeJson(new File(XlsxToolsConfig.jsonFolder), jsonPack, false);
								jsonFile = writeJson(new File(XlsxToolsConfig.jsonZipFolder), jsonPack, true);
								System.out.println("write json : " + jsonFile.getName());
							}
						}

					} catch (Exception e) {
						System.out.println("the catch msg " + e.getMessage());
						e.printStackTrace();
					}
				}
			}

		}
	}

	private static final File writeJson(File jsonFolder, JsonPack jsonPack, boolean zip) throws Exception {
		
		String json = null;
		if (XlsxToolsConfig.format)
			json = jsonPack.getJsonObject().toString(2);
		else {
			json = jsonPack.getJsonObject().toString();
		}
		
		String fileNameString =  jsonPack.getName();
		if(XlsxToolsConfig.md5) {
			String _md5 = "";
			_md5 = MD5(json);
			_md5 = _md5.toLowerCase();
			_md5 = _md5.substring(0,5);
			fileNameString += ("_"+_md5.toLowerCase());
		}
		
		File jsonFile = new File(jsonFolder, fileNameString + (zip? ".bin":".json"));
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

			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(jsonFile), "UTF-8");
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

	public static String MD5(String key) {
	    char hexDigits[] = {
	            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	    };
	    try {
	        byte[] btInput = key.getBytes();
	        // 获得MD5摘要算法的 MessageDigest 对象
	        MessageDigest mdInst = MessageDigest.getInstance("MD5");
	        // 使用指定的字节更新摘要
	        mdInst.update(btInput);
	        // 获得密文
	        byte[] md = mdInst.digest();
	        // 把密文转换成十六进制的字符串形式
	        int j = md.length;
	        char str[] = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	            byte byte0 = md[i];
	            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            str[k++] = hexDigits[byte0 & 0xf];
	        }
	        return new String(str);
	    } catch (Exception e) {
	        return null;
	    }
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
	}
}
