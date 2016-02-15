package gdx.keyroy.psd.tools.models;

public class LayerParam {
	// ͼ��id
	protected String layerId;
	// ����ID
	protected String paramId;
	// ��������
	protected String data;

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		if (data == null) {
			return paramId;
		} else {
			return paramId + " [" + data + "]";
		}
	}
}
