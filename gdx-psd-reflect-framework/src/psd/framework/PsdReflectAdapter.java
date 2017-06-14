package psd.framework;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

import psd.Element;
import psd.loaders.FileManage;
import psd.reflect.PsdGroup;
import psd.reflect.PsdReflectListener;

/** 绠�鏄撶殑鍙嶅皠瀵硅薄 */
public abstract class PsdReflectAdapter extends FileManage implements PsdReflectListener {
	// 鏁版嵁婧�
	private PsdGroup source;

	@Override
	public final void onReflectSuccess(PsdGroup psdGroup) {
		this.source = psdGroup;
		onCreate(psdGroup);
	}

	// 鍙嶅皠鍑芥暟
	protected abstract void onCreate(PsdGroup psdGroup);

	// 璁剧疆浜嗘柊瑙嗗浘
	protected void onViewportChange(Viewport viewport) {
	}

	/** 鏆傚仠鏄剧ず */
	protected void onHide() {
	}

	/** 杩斿洖鏄剧ず */
	protected void onShow() {
	}

	/** 鏄剧ず瀵硅薄 */
	protected final void show(Object object) {
		PsdReflectApplicationAdapter.set(object);
	}

	/** 鍘嬫爤鏄剧ず涓嬩竴涓璞� */
	protected final void push(Object object) {
		PsdReflectApplicationAdapter.push(object);
	}

	/** 鏄剧ず涓婁竴涓帇鏍堝璞� */
	protected final boolean back() {
		return PsdReflectApplicationAdapter.pop();
	}

	/** 鑾峰彇婧� */
	protected PsdGroup getSource() {
		return source;
	}

	/** 鑾峰彇Psd瀵煎嚭Json鐨勮矾寰� */
	protected String getPsdJsonPath() {
		return null;
	}

	/** 鍙嶅皠瀵硅薄瀹屾垚 */
	@Override
	public Actor onReflectElement(PsdGroup parent, Element element, AssetManager assetManager)
			throws Exception {
		return PsdReflectUtil.toGdxActor(element, assetManager, this);
	}

}
