package psd.reflect;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;

import psd.Element;

/**
 * ӳ��ļ���
 * 
 */
public interface PsdReflectListener {
	/** ���������� */
	public void onReflectSuccess(PsdGroup psdGroup);

	/** ���������� */
	public Actor onReflectElement(PsdGroup parent, Element element, AssetManager assetManager)
			throws Exception;
}
