package psd.lg0311;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import psd.Element;
import psd.Folder;
import psd.Param;
import psd.framework.PsdReflectAdapter;
import psd.lg0311.params.Color;
import psd.lg0311.params.FS;
import psd.lg0311.params.Fnt;
import psd.lg0311.params.Particle;
import psd.lg0311.widget.ColorWidget;
import psd.lg0311.widget.FntWidget;
import psd.lg0311.widget.FrameAnimationWidget;
import psd.lg0311.widget.ParticleWidge;
import psd.lg0311.widget.ProgressBarWidget;
import psd.reflect.PsdAn;
import psd.reflect.PsdGroup;
import psd.reflect.PsdImage;
import psd.utils.Filter;

/**
 * 锟�?鍗曠殑 鍙嶅皠鎿嶄綔瀵硅薄
 */
public abstract class PsdAdapter extends PsdReflectAdapter {
	public static boolean debug = true;
	// 瀵硅瘽锟�?
	private Array<DialogAdapter> dialogs = new Array<DialogAdapter>(2);

	@Override
	protected void onCreate(PsdGroup psdGroup) {

	}

	// 鏇挎崲鎸囧畾鐨勫锟�?
	protected final DialogAdapter showDialog(DialogAdapter dialogAdapter) {
		// 姣忎釜dialog瀵硅薄 閮藉簲璇ュ叿鏈夊敮锟�?锟�?
		for (DialogAdapter dialog : dialogs) {
			if (dialog.getClass().equals(dialogAdapter.getClass())) {
				return dialogAdapter;
			}
		}
		// 鍒涘缓鑿滃崟瀵硅薄
		PsdGroup.reflect(dialogAdapter);
		dialogAdapter.setParent(this);
		dialogs.add(dialogAdapter);
		// 娣诲姞鍒版樉绀哄锟�?
		float duration = dialogAdapter.show(getSource());
		getSource().addAction(Actions.delay(duration, new Action() {
			@Override
			public boolean act(float delta) {
				onDialogChange(dialogs.size);
				return true;
			}
		}));
		return dialogAdapter;

	}

	// 鍏抽棴瀵硅瘽锟�?
	protected final DialogAdapter hideDialog() {
		if (dialogs.size > 0) {
			DialogAdapter dialogAdapter = dialogs.pop();
			// 杩欓噷鏄剧ず鍏抽棴鍔ㄧ敾
			dialogAdapter.close();
			onDialogChange(dialogs.size);
			return dialogAdapter;
		}
		return null;
	}

	protected final void hideDialog(DialogAdapter adapter) {
		dialogs.removeValue(adapter, false);
		onDialogChange(dialogs.size);
	}

	// 瀵硅瘽妗嗚涓烘敼锟�?
	protected void onDialogChange(int dialogSize) {
	}

	// 鏇挎崲鎸囧畾鐨勫锟�?
	protected final static void replace(Actor target, Actor insteadOf, int align) {
		insteadOf.setPosition(target.getX(align), target.getY(align), align);
		insteadOf.setName(target.getName());
		target.getParent().addActorBefore(target, insteadOf);
		target.remove();
	}

	// 鏇挎崲鎸囧畾鐨勫锟�?
	protected final static void replace(Actor target, Actor insteadOf) {
		insteadOf.setBounds(target.getX(), target.getY(), target.getWidth(), target.getHeight());
		insteadOf.setName(target.getName());
		target.getParent().addActorBefore(target, insteadOf);
		target.remove();
	}

	// 鏇挎崲鎸囧畾鐨勮矾锟�?
	protected final Actor replace(String path, Actor insteadOf) {
		return replace(path, insteadOf, 0, Align.bottomLeft);
	}

	// 鏇挎崲鎸囧畾鐨勮矾锟�?
	protected final Actor replace(String path, Actor insteadOf, int index) {
		return replace(path, insteadOf, index, Align.bottomLeft);
	}

	// 鏇挎崲鎸囧畾鐨勮矾锟�?
	protected final Actor replace(String path, Actor insteadOf, int index, int align) {
		Actor actor = getSource().findActor(path, index);
		if (actor != null) {
			replace(actor, insteadOf, align);
			return insteadOf;
		} else {
			throw new IllegalArgumentException("actor not found : " + path);
		}
	}

	// 鏌ユ壘瀵硅薄
	public final <T extends Actor> T findActor(String name) {
		return getSource().findActor(name);
	}

	// 鏌ユ壘瀵硅薄
	public final <T extends Actor> T findActor(String name, int index) {
		return getSource().findActor(name, index);
	}

	// 鏌ユ壘瀵硅薄
	public final Array<Actor> findActor(String... paths) {
		Array<Actor> array = new Array<Actor>(paths.length);
		for (int i = 0; i < paths.length; i++) {
			array.add(findActor(paths[i]));
		}
		return array;
	}

	// 涓績瀵归綈
	protected final void center(Actor actor, String path) {
		center(actor, findActor(path));
	}

	// 涓績瀵归綈
	protected final void center(Actor actor, Actor anchor) {
		anchor(actor, anchor, Align.center);
	}

	// 瀵归綈鐩爣
	protected final void anchor(Actor actor, Actor anchor, int align) {
		actor.setPosition(anchor.getX(align), anchor.getY(align), align);
	}

	protected final FrameAnimationWidget translateToFrameAnimationWidget(String actorPath) {
		FrameAnimationWidget frameAnimationWidget = new FrameAnimationWidget((PsdGroup) findActor(actorPath));
		replace(findActor(actorPath), frameAnimationWidget);
		return frameAnimationWidget;
	}

	// 杞崲鎸囧畾浣嶇疆 涓鸿繘搴︽潯妯″紡
	protected final ProgressBarWidget translateToProgressBarWidget(String actorPath) {
		ProgressBarWidget progressBarWidget = new ProgressBarWidget((PsdImage) findActor(actorPath));
		replace(findActor(actorPath), progressBarWidget);
		return progressBarWidget;
	}

	// 澧炲姞鎸夐挳鏁堟灉
	protected final List<Actor> initButtonStyle(Filter<Actor> filter) {
		List<Actor> array = getSource().filter(filter);
		for (Actor actor : array) {
			try {
				initButtonStyle(actor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	// 澧炲姞鎸夐挳鏁堟灉
	protected final Array<Actor> initButtonStyle(String... paths) {
		Array<Actor> array = new Array<Actor>();
		for (String path : paths) {
			try {
				Actor actor = getSource().findActor(path);
				initButtonStyle(actor);
				array.add(actor);
			} catch (Exception e) {
				throw new IllegalArgumentException("error on : " + path);
			}

		}
		return array;
	}

	// 澧炲姞鎸夐挳鏁堟灉
	protected static final void initButtonStyle(Actor... actors) {
		for (Actor actor : actors) {
			initButtonStyle(actor);
		}
	}

	// 澧炲姞鎸夐挳鏁堟灉
	protected static final Actor initButtonStyle(final Actor actor) {
		actor.setOrigin(actor.getWidth() / 2, actor.getHeight() / 2);
		actor.addListener(new ClickListener() {
			private float pressedScale = 0.85f;
			private float duration = 0.1f;

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
				play(actor, pressedScale);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int b) {
				play(actor, 1);
			}

			public void play(final Actor actor, float scale) {
				if (actor != null) {
					actor.clearActions();
					actor.addAction(Actions.scaleTo(scale, scale, duration));
				}
			}
		});
		return actor;
	}

	// 闅愯棌 鍏冪礌
	protected final void hide(String... paths) {
		for (String path : paths) {
			if (path.endsWith("/")) {
				PsdGroup group = findActor(path.substring(0, path.length() - 1));
				for (Actor actor : group.getChildren()) {
					actor.setVisible(false);
				}
				group.setVisible(false);
			} else {
				findActor(path).setVisible(false);
			}
		}
	}

	// 鏄剧ず 鍏冪礌
	protected final void display(String... paths) {
		for (String path : paths) {
			if (path.endsWith("/")) {
				PsdGroup group = findActor(path.substring(0, path.length() - 1));
				for (Actor actor : group.getChildren()) {
					actor.setVisible(true);
				}
				group.setVisible(true);
			} else {
				Actor actor = findActor(path);
				actor.setVisible(true);
				if (actor.getParent() != null && actor.getParent().isVisible() == false) {
					actor.getParent().setVisible(true);
				}
			}
		}
	}

	// 鐢熸垚鏄犲皠瀵硅薄
	public final PsdGroup reflect() {
		return PsdGroup.reflect(this);
	}

	// 鐢熸垚鏄犲皠瀵硅薄
	public static final PsdGroup reflect(Object object) {
		return PsdGroup.reflect(object);
	}

	@SuppressWarnings("unchecked")
	// 鑾峰彇鎸囧畾锟�? Action 瀵硅薄
	public static final <T extends Action> T getAction(Actor actor, Class<T> clazz) {
		for (Action action : actor.getActions()) {
			if (action.getClass().equals(clazz)) {
				return (T) action;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	// 鑾峰彇鎸囧畾锟�? Actor 瀵硅薄
	public static final <T extends Actor> T getActor(Group group, Class<T> clazz) {
		for (Actor actor : group.getChildren()) {
			if (actor.getClass().equals(clazz)) {
				return (T) actor;
			}
		}
		return null;
	}

	// 缂撳瓨鎸囧畾鐨勭晫锟�?
	public static final void loadPsdResource(Class<? extends PsdAdapter> clazz) {
		String jsonPath = getJsonPath(clazz);
		if (jsonPath != null) {
			loadPsdResource(jsonPath);
		}
	}

	// 鑾峰彇Json鐨勫姞杞借矾锟�?
	private static final String getJsonPath(Object object) {
		// 鏄犲皠鐨凱SD璺緞
		String psdPath = null;
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

	public final void __print(String msg) {
		if (debug) {
			System.out.println(getMethodName() + msg);
		}
	}

	public final void __print(Object... msg) {
		if (debug) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < msg.length; i++) {
				buffer.append(msg[i].toString());
				if (i != msg.length - 1) {
					buffer.append("    ");
				}
			}
			System.out.println(getMethodName() + buffer.toString());
		}
	}

	private final String getMethodName() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		return "[" + stacktrace[3].getLineNumber() + " " + getClass().getSimpleName() + "."
				+ stacktrace[3].getMethodName() + "]";
	}

	@Override
	public Actor onReflectElement(PsdGroup parent, Element element, AssetManager assetManager)
			throws Exception {
		Actor actor = null;
		if (element.getParams() != null) {
			for (Param param : element.getParams()) {
				if (param.getId().equals(FntWidget.getId())) {
					Fnt fnt = param.reflect(Fnt.class);
					if (fnt != null) {
						FntWidget fntWidget = new FntWidget(fnt.getPath());
						actor = fntWidget;
					}
				} else if (param.getId().equals(FrameAnimationWidget.getId()) && element instanceof Folder) {
					actor = super.onReflectElement(parent, element, assetManager);
					if (actor instanceof PsdGroup) {
						FrameAnimationWidget fsWidget = new FrameAnimationWidget((PsdGroup) actor);
						FS fs = param.reflect(FS.class);
						if (fs != null) {
							fsWidget.setCurrentIndex(fs.i);
							fsWidget.setDelay(fs.delay);
							fsWidget.setDuration(fs.t);
							if (fs.play) {
								fsWidget.startAnimation();
							}
						}
						actor = fsWidget;
					}
				} else if (param.getId().equals(ParticleWidge.class)) {
					Particle particle = param.reflect(Particle.class);
					if (particle != null && particle.getPath() != null) {
						ParticleWidge particleWidge = new ParticleWidge(particle.getPath());
						actor = particleWidge;
					}
				} else if (param.getId().equals(ColorWidget.class)) {
					Color color = param.reflect(Color.class);
					if (color != null && color.rgba != null) {
						ColorWidget colorWidget = new ColorWidget(
								com.badlogic.gdx.graphics.Color.valueOf(color.rgba));
						colorWidget.setFillScreen(color.fillScreen);
						//
						actor = colorWidget;
					}
				}
			}
		}
		//
		if (actor == null) {
			return super.onReflectElement(parent, element, assetManager);
		} else {
			return actor;
		}
	}
}
