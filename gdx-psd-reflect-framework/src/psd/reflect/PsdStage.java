package psd.reflect;

import java.lang.reflect.Method;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import psd.utils.PsdReflectUtil;

/**
 * PSD ����̨
 */
public class PsdStage extends Stage {
	private final Object reflectObject;

	//
	public PsdStage(Object reflectObject) {
		this(reflectObject, PsdReflectUtil.reflect(reflectObject));
	}

	//
	public PsdStage(Object reflectObject, Viewport viewport) {
		this(reflectObject, PsdReflectUtil.reflect(reflectObject), viewport);
	}

	//
	public PsdStage(Object reflectObject, PsdGroup psdGroup) {
		this(reflectObject, psdGroup, new ScalingViewport(Scaling.stretch, psdGroup.getWidth(),
				psdGroup.getHeight(), new OrthographicCamera()));
	}

	//
	public PsdStage(Object reflectObject, PsdGroup psdGroup, Viewport viewport) {
		super(viewport);
		this.reflectObject = reflectObject;
		addActor(psdGroup);
		// ���� ���� onViewPortChange ����
		doMethod("onViewportChange", getViewport());
	}

	/** ��������̨�Ƿ�ʧȥ�˿���Ȩ */
	public final void onControl(boolean isControl) {

	}

	// ִ�к���
	protected final void doMethod(String methodName, Object... args) {
		try {
			Class<?>[] classes = null;
			if (args != null) {
				classes = new Class<?>[args.length];
				for (int i = 0; i < args.length; i++) {
					if (args[i] != null) {
						classes[i] = args[i].getClass();
					}
				}
			}

			Method method = reflectObject.getClass().getMethod(methodName, classes);
			if (method != null) {
				method.setAccessible(true);
				method.invoke(reflectObject, args);
			}
		} catch (NoSuchMethodException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public final <T> T getReflectObject() {
		return (T) reflectObject;
	}

	public static final PsdStage reflect(Object object) {
		return new PsdStage(object, PsdReflectUtil.reflect(object));
	}

	public static final PsdStage reflect(Object object, Viewport viewport) {
		return new PsdStage(object, PsdReflectUtil.reflect(object), viewport);
	}

}
