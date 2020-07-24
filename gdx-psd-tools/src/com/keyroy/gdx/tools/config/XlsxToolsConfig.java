package com.keyroy.gdx.tools.config;

import java.io.File;
import java.io.FileInputStream;

import com.keyroy.gdx.tools.Config;
import com.keyroy.util.json.Json;

import gdx.keyroy.psd.tools.util.FileUtil;

public class XlsxToolsConfig {

	//	导入目录
	public static String importFolder = "excel";
	//	导出目录
	public static String jsonFolder = "json";
	//	导出目录
	public static String jsonZipFolder = "json zip";
	//格式化	
	public static boolean format = false;
	// 合并json
	public static boolean merge = true;	
	// 打 MD5	
	public static boolean md5 = false;
	

	public static final void load() {
		try {
			File file = getFile();
			if (file.exists()) {
				FileInputStream inputStream = new FileInputStream(file);
				Json json = new Json(inputStream);
				inputStream.close();
				json.toObject(Config.class);
				//
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void save() {
		try {
			Json json = new Json(new Config());
			String text = json.toString();
			FileUtil.save(getFile(), text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final File getFile() {
		return new File(".xlsx_tools_config");
	}
}
