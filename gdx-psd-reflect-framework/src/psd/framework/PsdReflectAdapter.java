package psd.framework;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.viewport.Viewport;

import psd.loaders.FileManage;
import psd.reflect.PsdGroup;
import psd.reflect.PsdReflectListener;

/** ���׵ķ������ */
public abstract class PsdReflectAdapter extends FileManage implements PsdReflectListener {
	// ����Դ
	private PsdGroup source;

	@Override
	public final void onReflectSuccess(PsdGroup psdGroup) {
		this.source = psdGroup;
		onCreate(psdGroup);
	}

	// ���亯��
	protected abstract void onCreate(PsdGroup psdGroup);

	// ����������ͼ
	protected void onViewportChange(Viewport viewport) {
	}

	/** ��ͣ��ʾ */
	protected void onHide() {
	}

	/** ������ʾ */
	protected void onShow() {
	}

	/** �������� */
	protected final <T> T get(Class<T> clazz, String fileName) {
		AssetManager assetManager = getAssetManager();
		if (assetManager.isLoaded(fileName, clazz) == false) {
			assetManager.load(fileName, clazz);
			assetManager.finishLoading();
		}
		return assetManager.get(fileName, clazz);
	}

	/** ��ʾ���� */
	protected final void show(Object object) {
		PsdReflectApplicationAdapter.set(object);
	}

	/** ѹջ��ʾ��һ������ */
	protected final void push(Object object) {
		PsdReflectApplicationAdapter.push(object);
	}

	/** ��ʾ��һ��ѹջ���� */
	protected final boolean back() {
		return PsdReflectApplicationAdapter.pop();
	}

	/** ��ȡԴ */
	protected final PsdGroup getSource() {
		return source;
	}

}
