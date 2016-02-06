package psd;

import library.psd.parser.object.PsdText;

/***
 * PSD ������
 * @author roy
 */
public class Text extends Element {
	// ��ʾ����
	public String text;
	// ������ɫ
	public float a, r, g, b;
	// ���ִ�С
	public int fontSize;

	public void setPsdText(PsdText psdText) {
		this.text = psdText.value;
		this.a = psdText.a;
		this.r = psdText.r;
		this.g = psdText.g;
		this.b = psdText.b;
		this.fontSize = psdText.fontSize;
	}
}
