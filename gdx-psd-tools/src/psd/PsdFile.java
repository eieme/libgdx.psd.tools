package psd;

import com.badlogic.gdx.files.FileHandle;
import com.keyroy.util.json.Json;
import com.keyroy.util.json.JsonAn;

/**
 * PSD ���ļ�����
 * 
 * @author roy
 */
public class PsdFile extends Folder {

	// ����С
	public int maxWidth, maxHeight;
	// �ļ���
	public String psdName;
	// ͼƬ��
	public String atlas;
	// �ļ����
	@JsonAn(skip = true)
	public FileHandle handle;

	public PsdFile() {
	}

	public PsdFile(FileHandle handle) {
		try {
			Json.fill(this, handle.read());
			this.handle = handle;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAtlasPath() {
		if (handle != null) {
			String path = handle.parent().path().replace("\\", "/");
			if (path.length() > 0) {
				return path + "/" + atlas;
			}
		}
		return atlas;
	}
}
