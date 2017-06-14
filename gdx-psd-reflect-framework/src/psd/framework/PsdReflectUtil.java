package psd.framework;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import psd.Element;
import psd.PsdFile;
import psd.loaders.FileManage;
import psd.reflect.PsdAn;
import psd.reflect.PsdGroup;
import psd.reflect.PsdImage;
import psd.reflect.PsdLabel;
import psd.reflect.PsdReflectListener;

public class PsdReflectUtil {

	// 璁剧疆澶у皬
	public static final void setBounds(psd.Element element, Actor actor) {
		if (element != null && actor != null) {
			actor.setBounds(element.x, element.y, element.width, element.height);
			actor.setName(element.layerName);
		}
	}

	// 鍑嗗椤圭洰
	public static synchronized final <T> T load(AssetManager assetManager, String fileName, Class<T> clazz) {
		if (assetManager.isLoaded(fileName, clazz)) {
		} else {
			assetManager.load(fileName, clazz);
			assetManager.finishLoading();
		}
		return assetManager.get(fileName, clazz);
	}

	// 鑾峰彇Json鐨勫姞杞借矾寰�
	private static final String getJsonPath(Object object) {
		// 鏄犲皠鐨凱SD璺緞
		String psdPath = null;
		if (object instanceof PsdReflectAdapter) { // 鐙珛鐨勮幏鍙朖SON璺緞鍑芥暟
			psdPath = ((PsdReflectAdapter) object).getPsdJsonPath();
		}

		if (psdPath == null) { // 娌℃湁鑾峰彇鍒癙SD 璺緞 , 灏濊瘯瑙ｆ瀽 @PsdAn
			Class<?> reflectClass = (object instanceof Class<?>) ? (Class<?>) object : object.getClass();
			PsdAn an = reflectClass.getAnnotation(PsdAn.class);
			if (an != null) {
				if (an.value().length == 0) {
					psdPath = reflectClass.getSimpleName() + ".json";
				} else {
					psdPath = an.value()[0];
				}
			}
		}
		return psdPath;
	}

	public static final PsdGroup reflect(Object object) {
		PsdReflectListener listener = (object instanceof PsdReflectListener) ? (PsdReflectListener) object
				: null;
		// 鏄犲皠鐨凱SD璺緞
		String psdPath = getJsonPath(object);
		if (psdPath == null) { // 娌℃湁鑾峰彇鍒版槧灏勪娇鐢ㄧ殑 PSD 璺緞
			throw new IllegalArgumentException("can not reflect a PsdGroup by : "
					+ object.getClass().getName() + "  , try add annotation @PsdAn");
		} else {
			try {
				PsdGroup psdGroup = null;
				PsdFile psdFile = null;
				if (object instanceof PsdGroup) {
					psdGroup = (PsdGroup) object;
					psdFile = (PsdFile) psdGroup.getPsdFolder();
				} else {
					// 鍔犺浇瀵硅薄
					psdFile = FileManage.get(psdPath, PsdFile.class);
					// 鐢熸垚缁撴瀯
					psdGroup = new PsdGroup(psdFile, PsdGroup.getAssetManager(), listener);
				}

				Class<?> reflectClass = (object instanceof Class<?>) ? (Class<?>) object : object.getClass();
				// 淇缁勪綅缃�
				PsdReflectUtil.setBounds(psdFile, psdGroup);
				// 鏄犲皠 鍙傛暟
				Field[] fields = reflectClass.getDeclaredFields();
				for (Field field : fields) {
					PsdAn an = field.getAnnotation(PsdAn.class);
					if (an != null && (Actor.class.isAssignableFrom(field.getType())
							|| Element.class.isAssignableFrom(field.getType()))) {
						Actor actor = null;
						if (an.value().length > 0) {// 灏濊瘯鐩存帴鑾峰彇鎸囧畾瀵硅薄
							actor = psdGroup.findActor(an.value()[0], an.index());
						} else {
							actor = psdGroup.findActor(field.getName(), 0);
						}

						if (actor != null) {
							field.setAccessible(true);
							if (Actor.class.isAssignableFrom(field.getType())) {
								field.set(object, actor);
							}
						}
					}
				}
				// 鏄犲皠 鍑芥暟
				Method[] methods = reflectClass.getDeclaredMethods();
				for (Method method : methods) {
					PsdAn an = method.getAnnotation(PsdAn.class);
					if (an != null) {
						// 灏濊瘯鐩存帴鑾峰彇鎸囧畾瀵硅薄
						List<Actor> actors = new ArrayList<Actor>(2);
						for (String actorName : an.value()) {
							Actor actor = psdGroup.findActor(actorName, an.index());
							if (actor != null && actors.contains(actor) == false) {
								actors.add(actor);
							}
						}
						for (Actor actor : actors) {
							actor.addListener(new MethordClickListener(object, method, actor));
						}
					}
				}
				// 婵�娲荤洃鍚�
				if (listener != null) {
					listener.onReflectSuccess(psdGroup);
				}
				//
				return psdGroup;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	// 灏� JSON 瀵硅薄 , 杞崲鎴� Actor 瀵硅薄
	public static final Actor toGdxActor(psd.Element element, AssetManager assetManager,
			PsdReflectListener listener) throws Exception {
		Actor actor = null;
		if (element instanceof psd.Folder) {
			psd.Folder psdFolder = (psd.Folder) element;
			actor = new PsdGroup(psdFolder, assetManager, listener);
		} else if (element instanceof psd.Pic) {
			psd.Pic pic = (psd.Pic) element;
			actor = new PsdImage(pic, assetManager);
		} else if (element instanceof psd.Text) {
			psd.Text psdText = (psd.Text) element;
			actor = new PsdLabel(psdText);
		}
		actor.setVisible(element.isVisible);
		actor.setName(element.layerName);
		return actor;
	}

	protected static final class MethordClickListener extends ClickListener {
		final Object object;
		final Method method;
		final Actor actor;
		final Object[] params;

		public MethordClickListener(Object object, Method method, Actor actor) {
			this.object = object;
			this.method = method;
			this.actor = actor;
			this.params = new Object[method.getParameterTypes().length];
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			method.setAccessible(true);
			try {
				Class<?>[] classes = method.getParameterTypes();
				int actorIndex = getIndex(classes, Actor.class);
				if (actorIndex != -1) {
					params[actorIndex] = actor;
				}
				method.invoke(object, params);
				for (int i = 0; i < params.length; i++) {
					params[i] = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private final int getIndex(Class<?>[] classes, Class<?> target) {
			for (int i = 0; i < classes.length; i++) {
				if (target.isAssignableFrom(classes[i])) {
					return i;
				}
			}
			return -1;
		}
	}

}
