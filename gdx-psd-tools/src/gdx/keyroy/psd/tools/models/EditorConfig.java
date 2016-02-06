package gdx.keyroy.psd.tools.models;

import gdx.keyroy.psd.tools.util.FileUtil;
import gdx.keyroy.psd.tools.util.L;
import gdx.keyroy.psd.tools.util.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.keyroy.util.json.Json;

public class EditorConfig {
	// ����·��
	public static String export_path;
	// ʹ�� LIBGDX ������ϵ
	public static boolean used_libgdx_coordinate;
	// ʹ�� TexturePacker ���ͼƬ
	public static boolean used_texture_packer;
	// ʹ�� Android Assets ���ƹ淶
	public static boolean used_android_assets_name;

	// ������Ŀ���ط�ʽ
	public static final void load() {
		try {
			File file = getFile();
			if (file.exists()) {
				FileInputStream inputStream = new FileInputStream(file);
				Json json = new Json(inputStream);
				inputStream.close();
				json.toObject(EditorConfig.class);
				//
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (export_path == null) {
			export_path = new File(FileUtil.getRoot(), "out").getPath();
		}
	}

	// GDX ��Ŀ���ط�ʽ
	public static final void loadOnGdx() {
		try {
			FileHandle handle = Gdx.files.internal("EditorConfig");
			InputStream inputStream = handle.read();
			Json json = new Json(inputStream);
			inputStream.close();
			json.toObject(EditorConfig.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void save() {
		try {
			Json json = new Json(new EditorConfig());
			String text = json.toString();
			System.out.println("save config : " + text);
			FileUtil.save(getFile(), text);
			Message.send(L.get("Message.config_save") + "    " + new Date().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final File getFile() {
		return new File(EditorConfig.class.getSimpleName());
	}
}
