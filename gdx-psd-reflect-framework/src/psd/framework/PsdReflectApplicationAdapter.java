package psd.framework;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import psd.reflect.PsdStage;

/***
 * 鑸炲彴缁�
 */
public abstract class PsdReflectApplicationAdapter extends ApplicationAdapter {
	private static PsdReflectStageGroup stageGroup = new PsdReflectStageGroup();
	private static Class<? extends PsdStage> stageClass;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(stageGroup);
		onCreate();
	}

	// 鍒濆鍖栬皟鐢�
	protected abstract void onCreate();

	@Override
	public void render() {
		// 娓呴櫎灞忓箷棰滆壊
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// 鏀寔棰滆壊娣峰悎
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		// 鏀寔閫忔槑閫氶亾
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		// 鎵ц鍔ㄧ敾
		stageGroup.act();
		// 鎵ц缁樺埗
		stageGroup.draw();
	}

	// 鑾峰彇鍒板璞�
	public static PsdReflectStageGroup getStageGroup() {
		return stageGroup;
	}

	/** 璁剧疆褰撳墠椤甸潰 */
	public static final void set(Object object) {
		Stage stage = toStage(object);
		if (stage != null) {
			stageGroup.clean();
			stageGroup.add(stage);
		} else {
			throw new IllegalArgumentException("stage == null , form " + object.getClass().getName());
		}
	}

	/** 鍘嬫爤 , 鏄剧ず鏂伴〉闈� */
	public static final void push(Object object) {
		Stage stage = toStage(object);
		if (stage != null) {
			stageGroup.push(stage);
		} else {
			throw new IllegalArgumentException("stage == null , form " + object.getClass().getName());
		}
	}

	public static final boolean pop() {
		return stageGroup.pop();
	}

	protected static final Stage toStage(Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof Stage) {
			return (Stage) object;
		} else if (stageClass != null) {
			try {
				Constructor<? extends PsdStage> constructor = stageClass.getConstructor(Object.class);
				return constructor.newInstance(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new PsdStage(object);
	}

	public static void setStageClass(Class<? extends PsdStage> stageClass) {
		PsdReflectApplicationAdapter.stageClass = stageClass;
	}

}
