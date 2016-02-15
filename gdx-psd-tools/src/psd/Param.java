package psd;

import gdx.keyroy.psd.tools.models.LayerParam;

public class Param {
	// ����ID
	protected String paramId;
	// ��������
	protected String data;

	public Param() {
	}

	public Param(LayerParam param) {
		this.paramId = param.getParamId();
		this.data = param.getData();
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
