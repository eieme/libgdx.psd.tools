package psd;

import java.io.InputStream;

import com.badlogic.gdx.files.FileHandle;
import com.keyroy.util.json.Json;
import com.keyroy.util.json.JsonAn;

/**
 * PSD 鐨勬枃浠舵弿杩�
 * 
 * @author roy
 */
public class PsdFile extends Folder {

	// 鏈�澶уぇ灏�
	public int maxWidth, maxHeight;
	// 鏂囦欢鍚�
	public String psdName;
	// 鍥剧墖闆�
	public String atlas;
	// 鏂囦欢鍙ユ焺
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

	// 鏇存柊鐖剁被瀵硅薄
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
