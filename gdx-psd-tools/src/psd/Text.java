package psd;

/***
 * PSD ������
 * 
 * @author roy
 */
public class Text extends Element {
	// ��ʾ����
	public String text;
	// ������ɫ
	public float a, r, g, b;
	// ���ִ�С
	public int fontSize;

	public void setPsdText(String text, float a, float r, float g, float b, int fontSize) {
		this.text = text;
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
		this.fontSize = fontSize;
	}
}
