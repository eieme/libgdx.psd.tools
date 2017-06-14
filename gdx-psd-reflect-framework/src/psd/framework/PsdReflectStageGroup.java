package psd.framework;

import java.util.Stack;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import psd.reflect.PsdStage;

/***
 * 鑸炲彴缁�
 */
public class PsdReflectStageGroup implements InputProcessor {
	// 鍘嗗彶璁板綍
	private Stack<Array<Stage>> histories = new Stack<Array<Stage>>();
	// 褰撳墠缁樺埗鏁扮粍
	private Array<Stage> stageArray = new Array<Stage>(2);

	public PsdReflectStageGroup() {
	}

	public PsdReflectStageGroup(Stage stage) {
		stageArray.add(stage);
	}

	/** 娣诲姞涓�涓唬鐞嗙晫闈� */
	public final PsdReflectStageGroup add(Stage stage) {
		stageArray.add(stage);
		return this;
	}

	/** 鍒犻櫎涓�涓唬鐞嗙晫闈� */
	public final PsdReflectStageGroup remove(Stage stage) {
		stageArray.removeValue(stage, false);
		return this;
	}

	/** 娓呯┖浠ｇ悊鐣岄潰 */
	public final PsdReflectStageGroup clean() {
		stageArray.clear();
		return this;
	}

	/** 鍘嬫爤 */
	public final synchronized void push(Stage stage) {
		for (Stage cStage : stageArray) {
			if (cStage instanceof PsdStage) {
				PsdStage psdStage = (PsdStage) cStage;
				psdStage.onControlChange(false);
			}
		}

		histories.add(new Array<Stage>(stageArray));
		stageArray.clear();
		stageArray.add(stage);
	}

	/** 鎺ㄦ爤 */
	public final synchronized boolean pop() {
		if (histories.size() > 0) {
			Array<Stage> array = histories.remove(histories.size() - 1);
			stageArray.clear();
			stageArray.addAll(array);

			for (Stage cStage : stageArray) {
				if (cStage instanceof PsdStage) {
					PsdStage psdStage = (PsdStage) cStage;
					psdStage.onControlChange(true);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public final boolean keyDown(int keycode) {
		for (int i = stageArray.size - 1; i >= 0; i--) {
			Stage stage = stageArray.get(i);
			if (stage.keyDown(keycode)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean keyUp(int keycode) {
		for (int i = stageArray.size - 1; i >= 0; i--) {
			Stage stage = stageArray.get(i);
			if (stage.keyUp(keycode)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean keyTyped(char character) {
		for (int i = stageArray.size - 1; i >= 0; i--) {
			Stage stage = stageArray.get(i);
			if (stage.keyTyped(character)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for (int i = stageArray.size - 1; i >= 0; i--) {
			Stage stage = stageArray.get(i);
			if (stage.touchDown(screenX, screenY, pointer, button)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (int i = stageArray.size - 1; i >= 0; i--) {
			Stage stage = stageArray.get(i);
			if (stage.touchUp(screenX, screenY, pointer, button)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean touchDragged(int screenX, int screenY, int pointer) {
		for (int i = stageArray.size - 1; i >= 0; i--) {
			Stage stage = stageArray.get(i);
			if (stage.touchDragged(screenX, screenY, pointer)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean mouseMoved(int screenX, int screenY) {
		for (int i = stageArray.size - 1; i >= 0; i--) {
			Stage stage = stageArray.get(i);
			if (stage.mouseMoved(screenX, screenY)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean scrolled(int amount) {
		for (int i = stageArray.size - 1; i >= 0; i--) {
			Stage stage = stageArray.get(i);
			if (stage.scrolled(amount)) {
				return true;
			}
		}
		return false;
	}

	public void resize(int width, int height) {

	}

	public final void act() {
		for (Stage stage : stageArray) {
			stage.act();
		}
	}

	public final void draw() {
		for (Stage stage : stageArray) {
			stage.draw();
		}
	}

	public Batch getBatch() {
		return stageArray.get(stageArray.size - 1).getBatch();
	}

	public final Array<Stage> getStageArray() {
		return stageArray;
	}

}
