package psd.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import psd.PsdFile;

@SuppressWarnings("rawtypes")
public class FileManage {
	// Ĭ�ϵ���Դ������
	private static AssetManagerProxy assetManager;

	/** ��ȡ��Դ������ **/
	public static final AssetManager getAssetManager() {
		if (assetManager == null) {
			assetManager = new AssetManagerProxy();
		}
		return assetManager;
	}

	/** ������Դ���ص��ĵ������� , �������ݵĽ��ܲ��� **/
	public static final void setHandleResolver(FileHandleResolver fileHandleResolver) {
		assetManager = new AssetManagerProxy(fileHandleResolver);
	}

	/** �����Դ */
	public static final void mark(String tag) {
		if (assetManager != null) {
			assetManager.mark(tag);
		}
	}

	/** ��ȡ�����Դ */
	public static Mark getCurrentMark() {
		if (assetManager != null) {
			return assetManager.getCurrentMark();
		}
		return null;
	}

	/** �ͷ���Դ */
	public static void unload(List<AssetDescriptor> descriptors) {
		if (assetManager != null) {
			assetManager.unload(descriptors);
		}
	}

	/** ������Դ */
	public static void load(List<AssetDescriptor> descriptors) {
		if (assetManager != null) {
			assetManager.load(descriptors);
		}
	}

	/** ���¼���ͼƬ , ���ڽ������ʱ ͼƬ��ʧ������ */
	public synchronized void reload(List<String> textures) {
		if (assetManager != null) {
			assetManager.reload(textures);
		}
	}

	/** ���¼���ͼƬ , ���ڽ������ʱ ͼƬ��ʧ������ */
	public synchronized void reload(Mark mark) {
		if (assetManager != null) {
			List<AssetDescriptor> descriptors = mark.filter(Texture.class);
			assetManager.load(descriptors);
		}
	}

	/**
	 * ��Դ�������Ĵ���,���ڼ������ݼ���״�� <br>
	 * ���� 1 , ��¼��ǰ����״̬<br>
	 * ���� 2 , ������Դ���ؼ�¼��<br>
	 * ���� 3 , �����ָ�������Դ
	 */
	private static class AssetManagerProxy extends AssetManager {
		private Stack<Mark> markTags = new Stack<Mark>();
		private Mark currentMark;

		public AssetManagerProxy() {
			this(new InternalFileHandleResolver());
		}

		public AssetManagerProxy(FileHandleResolver fileHandleResolver) {
			super(fileHandleResolver);
			initLoader(fileHandleResolver);
		}

		private final void initLoader(FileHandleResolver resolver) {
			setLoader(PsdFile.class, new PsdFileAssetsLoader(resolver));
		}

		private final void mark(String tag) {
			Mark markTag = new Mark(tag);
			markTags.push(markTag);
			currentMark = markTag;
		}

		private Mark getCurrentMark() {
			return currentMark;
		}

		public final synchronized <T> void load(String fileName, Class<T> type,
				AssetLoaderParameters<T> parameter) {
			super.load(fileName, type, parameter);
			if (currentMark != null) {
				currentMark.record(fileName, type, parameter);
			}
		}

		public synchronized void unload(List<AssetDescriptor> descriptors) {
			for (AssetDescriptor assetDescriptor : descriptors) {
				unload(assetDescriptor.fileName);
			}
		}

		public synchronized void load(List<AssetDescriptor> descriptors) {
			for (AssetDescriptor assetDescriptor : descriptors) {
				load(assetDescriptor);
			}
		}

		public synchronized void reload(List<String> textures) {
			for (String texture : textures) {
				assetManager.load(texture, Texture.class);
			}
		}
	}

	public static class Mark {
		// ��Դ
		private Array<AssetDescriptor> elements = new Array<AssetDescriptor>(50);
		// ��ǩ����
		private final String tag;

		public Mark(String tag) {
			this.tag = tag;
		}

		// ��¼
		@SuppressWarnings("unchecked")
		private final void record(String fileName, Class<?> type, AssetLoaderParameters<?> parameter) {
			for (AssetDescriptor assetDescriptor : elements) {
				if (assetDescriptor.fileName.equals(fileName) && assetDescriptor.type.equals(type)) {
					return;
				}
			}
			elements.add(new AssetDescriptor(fileName, type, parameter));
		}

		/** ���˱����Դ */
		public final List<AssetDescriptor> filter(Filter<AssetDescriptor> filter) {
			List<AssetDescriptor> list = new ArrayList<AssetDescriptor>();
			for (AssetDescriptor assetDescriptor : elements) {
				if (filter.accept(assetDescriptor)) {
					list.add(assetDescriptor);
				}
			}
			return list;
		}

		/** ���˱����Դ */
		public final List<AssetDescriptor> filter(Filter<String> filter, Class<?> clazz) {
			List<AssetDescriptor> list = new ArrayList<AssetDescriptor>();
			for (AssetDescriptor assetDescriptor : elements) {
				if (clazz.equals(assetDescriptor.type)
						&& (filter == null || filter.accept(assetDescriptor.fileName))) {
					list.add(assetDescriptor);
				}
			}
			return list;
		}

		/** ���˱����Դ */
		public final List<AssetDescriptor> filter(Class<?> clazz) {
			return filter(null, clazz);
		}

		// ��ȡ��ǩ����
		public final String getTag() {
			return tag;
		}

		// ��ȡ��¼����Դ�б�
		public final Array<AssetDescriptor> getElements() {
			return elements;
		}
	}
}
