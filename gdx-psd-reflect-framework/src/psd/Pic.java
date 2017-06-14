package psd;

/***
 * PSD 鐨勫浘鐗�
 * 
 * @author roy
 */
public class Pic extends Element {
	// 鍥剧墖鍚嶇О
	public String textureName;

	@Override
	protected void onParam(String paramString) {
		super.onParam(paramString);
		textureName = textureName.replace(paramString, "").replace("@", "");
	}
}
