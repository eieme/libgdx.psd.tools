package psd;

import org.json.m.JSONObject;

public class Param {
	// ����ID
	protected String id;
	// ��������
	protected JSONObject json;

	public Param() {
	}

	public Param(String text) {
		setText(text);
	}

	public final void setText(String text) {
		int st, ed;
		if ((st = text.indexOf("{")) != -1 && (ed = text.lastIndexOf("}")) != -1) {
			this.id = text.substring(0, st);
			this.json = new JSONObject(text.substring(st, ed + 1));
		} else {
			this.id = text;
		}
	}

	// ��ȡ������ ID
	public final String getId() {
		return id;
	}

	// ��ȡ JSON ��������
	public final JSONObject getJson() {
		return json;
	}

	//
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("@");
		buffer.append(id);
		if (json != null) {
			buffer.append(json.toString());
		}
		return buffer.toString();
	}

	public static void main(String[] args) {
		String src = "id{\"p\":123,\"p2\":\"aljdlwadlajw dwajlkajwd a\"}";
		Param param = new Param(src);
		System.out.println(param.toString());
	}
}
