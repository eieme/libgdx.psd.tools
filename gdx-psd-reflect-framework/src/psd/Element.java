package psd;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.keyroy.util.json.JsonAn;

/**
 * PSD Ԫ��
 * 
 * @author roy
 */
public class Element {
	// ͼ������
	public String layerName;
	// ���� , ��С
	public int x, y, width, height;
	// ȥ�������Ժ� , ʣ�������
	public String name;
	// �󶨲���
	public List<Param> params;
	// �Ƿ���ʾ
	public boolean isVisible;
	// �������
	@JsonAn(skip = true)
	protected Folder parent;
	// �ļ�����
	@JsonAn(skip = true)
	protected PsdFile psdFile;

	// �Զ������
	@JsonAn(skip = true)
	protected Object userObject;
	// �Զ������
	// @JsonAn(skip = true)
	// protected Actor actor;

	// ���²���
	protected void updateParam() {
		if (layerName != null) {
			int idx = layerName.indexOf("@");
			if (idx != -1) {
				String src = layerName;
				if (idx == 0) {
				} else {
					this.name = src.substring(0, idx);
					src = src.substring(idx + 1);
				}
				String[] ps = src.split("@");
				params = new ArrayList<Param>(ps.length);
				for (String param : ps) {
					params.add(new Param(param));
				}
			}
		}
	}

	// ���� �ļ���
	protected void updateParent(Folder parent) {
		this.parent = parent;
	}

	// ���� �ļ���
	protected void updatePsdFile(PsdFile psdFile) {
		this.psdFile = psdFile;
	}

	// �����û��Զ���Ļ�������
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

	// ��ȡ ���ļ���
	public final Folder getParent() {
		return parent;
	}

	public final List<Param> getParams() {
		return params;
	}

	// ��ȡ��ǰ��·��
	public final String getPath() {
		Array<String> paths = new Array<String>();
		paths.add(layerName);
		Folder folder = parent;
		while (folder != null) {
			if (folder.layerName != null) {
				paths.add(folder.layerName);
			} else if (folder instanceof PsdFile) {
				paths.add(((PsdFile) folder).psdName);
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
