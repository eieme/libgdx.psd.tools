package psd.reflect;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;

import psd.Element;

/**
 * 鏄犲皠鐨勭洃鍚�
 * 
 */
public interface PsdReflectListener {
	/** 鍙嶅皠瀵硅薄瀹屾垚 */
	public void onReflectSuccess(PsdGroup psdGroup);

	/** 鍙嶅皠瀵硅薄瀹屾垚 */
	public Actor onReflectElement(PsdGroup parent, Element element, AssetManager assetManager)
			throws Exception;
}
