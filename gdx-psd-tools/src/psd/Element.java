package psd;

import gdx.keyroy.psd.tools.models.LayerParam;

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
	public List<LayerParam> params;

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
