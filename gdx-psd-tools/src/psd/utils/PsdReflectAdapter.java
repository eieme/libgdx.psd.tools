package psd.utils;

import com.badlogic.gdx.utils.viewport.Viewport;

import psd.reflect.PsdGroup;

/** ���׵ķ������ */
public abstract class PsdReflectAdapter implements PsdReflectListener {
	// ����Դ
	private PsdGroup source;

	@Override
	public void onReflectSuccess(PsdGroup psdGroup) {
		this.source = psdGroup;
		onCreate(psdGroup);
	}

	/** ��ȡԴ */
	public final PsdGroup getSource() {
		return source;
	}

	@Override
	// ����������ͼ
	public void onViewportChange(Viewport viewport) {

	}

	// ���亯��
	protected abstract void onCreate(PsdGroup psdGroup);
}
