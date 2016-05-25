package psd;

import java.io.InputStream;

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
		this.layerName = psdName;
	}

	public PsdFile(InputStream inputStream) {
		try {
			Json.fill(this, inputStream);
			updateParent(this);
			updatePsdFile(this);
			updateParam();
			this.layerName = psdName;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PsdFile(FileHandle handle) {
		this(handle.read());
		try {
			this.handle = handle;
			this.layerName = psdName;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ���¸������
	@Override
	protected void updateParent(Folder parent) {
		for (Element element : childs) {
			element.updateParent(this);
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
