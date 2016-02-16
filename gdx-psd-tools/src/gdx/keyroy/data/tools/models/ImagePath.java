package gdx.keyroy.data.tools.models;

import java.io.File;

//�� �ļ�������
public class ImagePath {
	// �ļ��ĵ�ַ
	protected String filePath;

	public ImagePath() {

	}

	public ImagePath(File file) {
		this.filePath = file.getPath();
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

}
