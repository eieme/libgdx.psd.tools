package psd.reflect;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import psd.Element;
import psd.PsdFile;
import psd.framework.PsdReflectUtil;
import psd.loaders.FileManage;
import psd.utils.ElementFilter;

/**
 * PSD ���ļ���
 * 
 * @author roy
 */
public class PsdGroup extends WidgetGroup {
	// �ļ��е�Դ
	private final psd.Folder psdFolder;

	public PsdGroup(PsdFile psdFile) {
		this(psdFile, psdFile, getAssetManager());
	}

	public PsdGroup(psd.Folder psdFolder, PsdFile psdFile, AssetManager assetManager) {
		this.psdFolder = psdFolder;
		this.setSize(psdFolder.width, psdFolder.height);
		setName(psdFolder.layerName);
		for (psd.Element element : this.psdFolder.childs) {
			try {
				element.setParent(psdFolder);
				addActor(PsdReflectUtil.toGdxActor(psdFile, element, assetManager));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public float getPrefWidth() {
		return getWidth();
	}

	@Override
	public float getPrefHeight() {
		return getHeight();
	}

	@Override
	public <T extends Actor> T findActor(final String name) {
		return findActor(name, 0);
	}

	public <T extends Actor> T findActor(final String name, int index) {
		Element element = psdFolder.get(name, index);
		if (element != null) {
			return element.getActor();
		}
		return null;
	}

	// ����Ԫ��
	public final List<Actor> filter(psd.utils.Filter<Actor> filter) {
		List<Actor> actors = new ArrayList<Actor>();
		filter(this, filter, actors);
		return actors;
	}

	// ����Ԫ��
	private final void filter(Actor actor, psd.utils.Filter<Actor> filter, List<Actor> out) {
		if (filter.accept(actor)) {
			out.add(actor);
		} else if (actor instanceof Group) {
			for (Actor cActor : ((Group) actor).getChildren()) {
				filter(cActor, filter, out);
			}
		}
	}

	// ����Ԫ��
	public final List<Actor> filter(final ActorFilter filter) {
		List<Element> elements = psdFolder.filter(new ElementFilter() {
			@Override
			public boolean accept(Element element) {
				return filter.accept(element, (Actor) element.getActor());
			}
		});
		List<Actor> actors = new ArrayList<Actor>(elements.size());
		for (Element element : elements) {
			actors.add((Actor) element.getActor());
		}
		return actors;
	}

	//
	public psd.Folder getPsdFolder() {
		return psdFolder;
	}

	public static final PsdGroup reflect(Object object) {
		return PsdReflectUtil.reflect(object);
	}

	public static final AssetManager getAssetManager() {
		return FileManage.getAssetManager();
	}

	public static interface ActorFilter {
		public boolean accept(Element element, Actor actor);
	}

}
