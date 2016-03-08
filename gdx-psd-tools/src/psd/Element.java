package psd;

import java.util.List;

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

	// �Զ������
	@JsonAn(skip = true)
	protected Object userObject;

	//
	public final void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	//
	@SuppressWarnings("unchecked")
	public final <T> T getUserObject() {
		return (T) userObject;
	}
}
