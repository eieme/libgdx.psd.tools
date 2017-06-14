package psd.lg0311;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import psd.lg0311.widget.ColorWidget;
import psd.reflect.PsdGroup;

public abstract class DialogAdapter extends PsdAdapter {
	// 鏄惁鑷姩鍏抽棴
	private boolean isAutoClose = true;
	//
	private PsdAdapter parent;

	//
	@Override
	protected final void onCreate(PsdGroup psdGroup) {
		// 璁剧疆鑳屾櫙鑹�
		setBackgroundColor(0x00000077);
		// 鍥炶皟
		doCreate(psdGroup);
	}

	// 鏄剧ず DIALOG
	protected final float show(PsdGroup parent) {
		PsdGroup group = getSource();
		parent.addActor(group);
		return onShow(group);
	}

	// 鏄剧ず
	protected float onShow(PsdGroup group) {
		// 鍔ㄧ敾鎾斁鏃堕棿
		float duration = 0.25f;
		group.setColor(1, 1, 1, 0);
		group.setOrigin(group.getWidth() / 2, group.getHeight() / 2);
		group.addAction(Actions.alpha(1, duration));
		group.setScale(0);
		group.addAction(Actions.scaleTo(1, 1, duration));
		return duration;
	}

	// 鍏抽棴 DIALOG
	protected final void close() {
		PsdGroup group = getSource();
		onClose(group);
		parent.hideDialog(this);
	}

	protected void onClose(PsdGroup group) {
		// 鍔ㄧ敾鎾斁鏃堕棿
		float duration = 0.25f;
		group.addAction(Actions.alpha(0, duration, Interpolation.sineOut));
		group.addAction(Actions.scaleTo(0, 0, duration, Interpolation.sineOut));
		group.addAction(Actions.delay(duration, new Action() {
			@Override
			public boolean act(float delta) {
				getSource().remove();
				return true;
			}
		}));
	}

	protected void setParent(PsdAdapter parent) {
		this.parent = parent;
	}

	// 璁剧疆鍏抽棴鎸夐挳
	protected final void setCloseButton(String layerName) {
		Actor actor = getSource().findActor(layerName);
		initButtonStyle(actor);
		actor.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				close();
			}
		});
	}

	// 璁剧疆鑳屾櫙鑹�
	protected final void setBackgroundColor(int rgba) {
		ColorWidget colorPainter = new ColorWidget(rgba);
		colorPainter.setSize(getSource().getWidth(), getSource().getHeight());
		colorPainter.setFillScreen(true);
		getSource().addActorAt(0, colorPainter);
		//
		colorPainter.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// 鏄惁鑷姩鍏抽棴
				if (isAutoClose) {
					close();
				}
			}
		});
	}

	// 寤惰繜鏄剧ず鎸夐挳
	protected final void showDelayPop(Array<Actor> array) {
		array.reverse();
		showDelayPop(array, 0.6f, 0.4f);
	}

	// 寤惰繜鏄剧ず鎸夐挳
	protected final void showDelayPop(Array<Actor> array, float delay, float duration) {
		for (int i = 0; i < array.size; i++) {
			Actor actor = array.get(i);
			actor.setOrigin(actor.getWidth() / 2, actor.getHeight() / 2);
			actor.setColor(1, 1, 1, 0);
			actor.setScale(0);
			Action action = Actions.delay(delay * (i + 1), Actions.parallel(Actions.alpha(1, duration),
					Actions.scaleTo(1, 1, duration, Interpolation.swingOut)));
			actor.addAction(action);
		}
	}

	public void setAutoClose(boolean isAutoClose) {
		this.isAutoClose = isAutoClose;
	}

	// 鏋勯�犲嚱鏁�
	protected abstract void doCreate(PsdGroup psdGroup);
}
