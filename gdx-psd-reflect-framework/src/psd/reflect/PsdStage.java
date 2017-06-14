package psd.reflect;

import java.lang.reflect.Method;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import psd.PsdFile;
import psd.framework.PsdReflectUtil;
import psd.loaders.FileManage;

/**
 * PSD 鐨勮垶鍙�
 */
public class PsdStage extends Stage {
	private final Object reflectObject;

	public PsdStage(String fileName) {
		this(null, new PsdGroup(FileManage.get(fileName, PsdFile.class)));
	}

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
		// 榛樿娣诲姞鍒板睆骞曚腑蹇�
		psdGroup.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
		addActor(psdGroup);
		// 灏濊瘯 鍙嶅皠 onViewPortChange 鍑芥暟
		doMethod("onViewportChange", getViewport());
	}

	/** 鏍囪杩欎釜鑸炲彴鏄惁澶卞幓浜嗘帶鍒舵潈 */
	public final void onControlChange(boolean isControl) {

	}

	// 鎵ц鍑芥暟
	protected final void doMethod(String methodName, Object... args) {
		if (reflectObject == null) {

		} else {
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
