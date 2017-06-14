package psd;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.keyroy.util.json.JsonAn;

/**
 * PSD 鍏冪礌
 * 
 * @author roy
 */
public class Element {
	// 鍥惧眰鍚嶇О
	public String layerName;
	// 鍧愭爣 , 澶у皬
	public int x, y, width, height;
	// // 鍘绘帀鍙傛暟浠ュ悗 , 鍓╀綑鐨勫悕绉�
	// public String name;
	// 缁戝畾鍙傛暟
	public List<Param> params;
	// 鏄惁鏄剧ず
	public boolean isVisible;
	// 鐖剁被瀵硅薄
	@JsonAn(skip = true)
	protected Folder parent;
	// 鏂囦欢瀵硅薄
	@JsonAn(skip = true)
	protected PsdFile psdFile;

	// 鑷畾涔夊璞�
	@JsonAn(skip = true)
	protected Object userObject;
	// 鑷畾涔夊璞�
	// @JsonAn(skip = true)
	// protected Actor actor;

	// 鏇存柊鍙傛暟
	protected void updateParam() {
		if (layerName != null) {
			int idx = layerName.indexOf("@");
			if (idx != -1) {
				String src = layerName;
				if (idx == 0) {
				} else {
					layerName = src.substring(0, idx);
					src = src.substring(idx + 1);
				}
				String[] ps = src.split("@");
				params = new ArrayList<Param>(ps.length);
				for (String param : ps) {
					onParam(param);
				}
			}
		}
	}

	protected void onParam(String paramString) {
		params.add(new Param(paramString));
	}

	// 璁剧疆 鏂囦欢澶�
	protected void updateParent(Folder parent) {
		this.parent = parent;
	}

	// 璁剧疆 鏂囦欢澶�
	protected void updatePsdFile(PsdFile psdFile) {
		this.psdFile = psdFile;
	}

	// 璁剧疆鐢ㄦ埛鑷畾涔夌殑缂撳瓨鏁版嵁
	public final void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	//
	@SuppressWarnings("unchecked")
	public final <T> T getUserObject() {
		return (T) userObject;
	}

	public final PsdFile getPsdFile() {
		return psdFile;
	}

	// 鑾峰彇 鐖舵枃浠跺す
	public final Folder getParent() {
		return parent;
	}

	public final List<Param> getParams() {
		return params;
	}

	// 鑾峰彇褰撳墠鐨勮矾寰�
	public final String getPath() {
		Array<String> paths = new Array<String>();
		paths.add(layerName);
		Folder folder = parent;
		while (folder != null) {
			if (folder instanceof PsdFile) {
				paths.add(((PsdFile) folder).psdName);
				break;
			} else if (folder.layerName != null) {
				paths.add(folder.layerName);
			}
			folder = folder.parent;
		}

		StringBuffer buffer = new StringBuffer();
		for (int i = paths.size - 1; i >= 0; i--) {
			String name = paths.get(i);
			buffer.append(name);
			if (name.equals(layerName) == false) {
				buffer.append("/");
			}
		}
		return buffer.toString();
	}

	@Override
	public String toString() {
		return getPath();
	}

	public static void main(String[] args) {
		Element element = new Element();
		element.layerName = "test@p{\"firstName\":\"John\" , \"lastName\":\"Doe\"}@p2{\"first\":\"1\" , \"last\":\"2\"}";
		element.updateParam();
		System.out.println(element.params);
	}

}
