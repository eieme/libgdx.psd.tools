package psd.utils;

import com.badlogic.gdx.utils.viewport.Viewport;

import psd.reflect.PsdGroup;

/**
 * ӳ��ļ���
 * 
 * @author roy
 * 
 */
public interface PsdReflectListener {
	public void onReflectSuccess(PsdGroup psdGroup);

	public void onViewportChange(Viewport viewport);
}
