package psd;

import java.util.ArrayList;
import java.util.List;

import psd.utils.ElementFilter;
import psd.utils.ElementNameFilter;

/**
 * PSD ���ļ���ͼ��
 * 
 * @author roy
 */
public class Folder extends Element {

	// �Ӷ���
	public List<Element> childs = new ArrayList<Element>();

	// ���²�������
	@Override
	protected void updateParam() {
		super.updateParam();
		for (Element element : childs) {
			element.updateParam();
		}
	}

	// ���¸������
	@Override
	protected void updateParent(Folder parent) {
		super.updateParent(parent);
		for (Element element : childs) {
			element.updateParent(this);
		}
	}

	// ���� �ļ���
	protected void updatePsdFile(PsdFile psdFile) {
		super.updatePsdFile(psdFile);
		for (Element element : childs) {
			element.updatePsdFile(psdFile);
		}
	}

	// ����Ԫ��
	public final List<Element> filter(ElementFilter filter) {
		List<Element> list = new ArrayList<Element>(3);
		filter(this, filter, list);
		return list;
	}

	public final Element get(String layerName) {
		return get(layerName, 0);
	}

	public final Element get(String layerName, int index) {
		if (layerName.indexOf('/') == -1) {
			return get(new ElementNameFilter(layerName));
		} else {
			return getElementByPath(layerName, index);
		}

	}

	// ��ȡһ��
	public final Element get(ElementFilter filter) {
		return filterOne(this, filter);
	}

	// ���� ����
	private static final void filter(Element element, ElementFilter filter, List<Element> out) {
		if (filter.accept(element)) {
			out.add(element);
		}
		if (element instanceof Folder) {
			Folder folder = (Folder) element;
			for (Element child : folder.childs) {
				filter(child, filter, out);
			}
		}
	}

	//
	private static final Element filterOne(Element element, ElementFilter filter) {
		if (filter.accept(element)) {
			return element;
		}
		if (element instanceof Folder) {
			Element rt = null;
			Folder folder = (Folder) element;
			for (Element child : folder.childs) {
				rt = filterOne(child, filter);
				if (rt != null) {
					return rt;
				}
			}
		}
		return null;
	}

	// ����·�����Ҷ���
	public final Element getElementByPath(String path) {
		return getElementByPath(path, 0);
	}

	// ����·�����Ҷ���
	public final Element getElementByPath(String path, int index) {
		if (path != null) {
			return getElementByPath(path.split("/"), index);
		}
		return null;
	}

	// ����·�����Ҷ���
	private final Element getElementByPath(String[] paths, int index) {
		Folder folder = this;
		Element rt = null;
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			if (path == null || path.length() == 0) {
				continue;
			} else {
				if (i == paths.length - 1) {
					rt = getChild(folder, paths[i], index);
				} else {
					rt = getChild(folder, paths[i], 0);
				}
				//
				if (rt != null && rt instanceof Folder) {
					folder = (Folder) rt;
				} else if (i != paths.length - 1) {
					return null;
				}
			}
		}
		return rt;
	}

	// �������Ʋ����Ӷ���
	private static final Element getChild(Folder folder, String name, int index) {
		int counter = 0;
		for (Element child : folder.childs) {
			if (child.layerName.equals(name)) {
				if (counter == index) {
					return child;
				} else {
					counter++;
				}
			}
		}
		return null;
	}

}
