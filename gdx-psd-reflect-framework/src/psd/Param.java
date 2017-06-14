package psd;

import org.json.m.JSONObject;

import com.keyroy.util.json.Json;

public class Param {
	// 鍙傛暟ID
	protected String id;
	// 鍙傛暟鍐呭
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
			String jsonStr = text.substring(st, ed + 1);
			try {
				this.json = new JSONObject(jsonStr);
			} catch (Exception e) {// 閿欒鐨勫瓧绗︿覆灏濊瘯鏂扮殑瑙ｆ瀽鏂瑰紡
				jsonStr = jsonStr.replace("{", "").replace("}", "");
				this.json = new JSONObject();
				try {
					for (String keyVal : jsonStr.split(",")) {
						String[] sp = keyVal.split(":");
						if (sp.length == 2) {
							json.put(sp[0], sp[1]);
						}
					}
				} catch (Exception e2) {
				}
			}
		} else {
			this.id = text;
		}
	}

	// 鑾峰彇鍙傛暟鐨� ID
	public final String getId() {
		return id;
	}

	// 鑾峰彇 JSON 鍙傛暟瀵硅薄
	public final JSONObject getJson() {
		return json;
	}

	public final <T> T reflect(Class<T> clazz) {
		try {
			if (json != null) {
				return new Json(json).toObject(clazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
