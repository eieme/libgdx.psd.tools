package gdx.keyroy.data.tools.models;

import gdx.keyroy.psd.tools.util.TextureUnpacker;

import java.io.File;

import com.keyroy.util.json.JsonAn;

//�� �ļ�������
public class ImagePath {
	// �ļ��ĵ�ַ
	protected String filePath;

	@JsonAn(skip = true)
	protected TextureUnpacker unpacker;

	public ImagePath() {

	}

	public ImagePath(File file) {
		this.filePath = file.getPath();
	}

	public boolean exist() {
		return new File(filePath).exists();
	}

	public boolean isAtlas() {
		return filePath.endsWith(".atlas");
	}

	// �ļ��ĵ�ַ
	public String getFilePath() {
		return filePath;
	}

	// �ļ��ĵ�ַ
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public final String getFileName() {
		return new File(filePath).getName();
	}

	public TextureUnpacker getUnpacker() {
		if (unpacker == null) {
			File file = new File(filePath);
			unpacker = new TextureUnpacker(file, file.getParentFile(), false);
		}
		return unpacker;
	}

}
