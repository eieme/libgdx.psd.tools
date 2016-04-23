package psd;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
	// �󶨲���
	public List<Param> params;
	// �Ƿ���ʾ
	public boolean isVisible;
	// �������
	@JsonAn(skip = true)
	protected Folder parent;
	// �Զ������
	@JsonAn(skip = true)
	protected Object userObject;
	// �Զ������
	@JsonAn(skip = true)
	protected Actor actor;

	// �����û��Զ���Ļ�������
	public final void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	//
	@SuppressWarnings("unchecked")
	public final <T> T getUserObject() {
		return (T) userObject;
	}

	// �����û��Զ���� Actor ����
	public final void setActor(Actor actor) {
		this.actor = actor;
	}

	//
	@SuppressWarnings("unchecked")
	public final <T extends Actor> T getActor() {
		return (T) actor;
	}

	// ���� �ļ���
	public final void setParent(Folder parent) {
		this.parent = parent;
	}

	// ��ȡ ���ļ���
	public final Folder getParent() {
		return parent;
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

}
