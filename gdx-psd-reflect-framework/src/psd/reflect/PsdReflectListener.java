package psd.reflect;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;

import psd.Element;
import psd.Param;

/**
 * ӳ��ļ���
 * 
 */
public interface PsdReflectListener {
	/** ���������� */
	public void onReflectSuccess(PsdGroup psdGroup);

	/** ���������� */
	public Actor onReflectElement(PsdGroup parent, Element element, List<Param> params,
			AssetManager assetManager) throws Exception;
}
