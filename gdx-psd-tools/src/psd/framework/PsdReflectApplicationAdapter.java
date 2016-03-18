package psd.framework;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import psd.reflect.PsdStage;

/***
 * ��̨��
 */
public abstract class PsdReflectApplicationAdapter extends ApplicationAdapter {
	private static PsdReflectStageGroup stageGroup = new PsdReflectStageGroup();

	@Override
	public void create() {
		Gdx.input.setInputProcessor(stageGroup);
		onCreate();
	}

	// ��ʼ������
	protected abstract void onCreate();

	@Override
	public void render() {
		// �����Ļ��ɫ
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// ֧����ɫ���
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		// ֧��͸��ͨ��
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		// ִ�ж���
		stageGroup.act();
		// ִ�л���
		stageGroup.draw();
	}

	// ��ȡ������
	public static PsdReflectStageGroup getStageGroup() {
		return stageGroup;
	}

	/** ���õ�ǰҳ�� */
	public static final void set(Object object) {
		Stage stage = toStage(object);
		if (stage != null) {
			stageGroup.clean();
			stageGroup.add(stage);
		} else {
			throw new IllegalArgumentException("stage == null , form " + object.getClass().getName());
		}
	}

	/** ѹջ , ��ʾ��ҳ�� */
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
		} else {
			return new PsdStage(object);
		}
	}

}
