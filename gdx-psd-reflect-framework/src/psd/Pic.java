package psd;

/***
 * PSD ��ͼƬ
 * 
 * @author roy
 */
public class Pic extends Element {
	// ͼƬ����
	public String textureName;

	@Override
	protected void onParam(String paramString) {
		super.onParam(paramString);
		textureName = textureName.replace(paramString, "").replace("@", "");
	}
}
