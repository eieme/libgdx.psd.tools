package psd.reflect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * PSD ���ı�����
 * 
 * @author roy
 */
public class PsdLabel extends Label {
	// �ı�Դ
	protected final psd.Text psdText;

	public PsdLabel(psd.Text psdText) {
		super(psdText.text, getLabelStyle(psdText));
		this.psdText = psdText;
	}

	//
	public psd.Text getPsdText() {
		return psdText;
	}

	private static final LabelStyle getLabelStyle(psd.Text psdText) {
		Label.LabelStyle style = new LabelStyle(new BitmapFont(),
				new Color(psdText.r, psdText.g, psdText.b, psdText.a));
		try {
			// GDX ��Ĭ�ϴ�СΪ 15pt ,com/badlogic/gdx/utils/arial-15.png
			if (style.font != null) {
				style.font.scale((float) psdText.fontSize / 15f);
			}
		} catch (Exception e) {
		}
		return style;
	}
}
