package psd.loaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import psd.PsdFile;
import psd.loaders.PsdFileLoader.PsdFileParameter;
import psd.loaders.RunnableAssetLoader.RunnableParameter;
import psd.utils.Filter;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FileManage {
	// Ĭ�ϵ� �ļ�������
	private static FileHandleResolver fileHandleResolver;
	// Ĭ�ϵ���Դ������
	private static AssetManagerProxy assetManager;

	/** �����ļ���Դ **/
	public static final FileHandle file(String fileName) {
		if (fileHandleResolver != null) {
			return fileHandleResolver.resolve(fileName);
		}
		return Gdx.files.internal(fileName);
	}

	/** ��ȡ��Դ������ **/
	public static final AssetManagerProxy getAssetManager() {
		if (assetManager == null) {
			assetManager = new AssetManagerProxy();
		}
		return assetManager;
	}

	/** ������Դ���ص��ĵ������� , �������ݵĽ��ܲ��� **/
	public static final void setHandleResolver(FileHandleResolver resolver) {
		fileHandleResolver = resolver;
		assetManager = new AssetManagerProxy(resolver);

	}

	/** �����Դ */
	public static final void mark(String tag) {
		getAssetManager().mark(tag);
	}

	/** ��ȡ�����Դ */
	public static Mark getCurrentMark() {
		return getAssetManager().getCurrentMark();
	}

	/** �ͷ���Դ */
	public static void unload(List<AssetDescriptor> descriptors) {
		getAssetManager().unload(descriptors);
	}

	/** ������ȡ��Դ */
	public static <T> T get(String fileName, Class<T> clazz) {
		AssetManager assetManager = getAssetManager();
		if (assetManager.isLoaded(fileName, clazz) == false) {
			assetManager.load(fileName, clazz);
			assetManager.finishLoading();
		}
		return assetManager.get(fileName, clazz);
	}

	/**
	 * ���� PsdGroup ��Ҫʹ�õ���Դ
	 * 
	 * @param fileName
	 */
	public static void loadPsdResource(String fileName) {
		AssetManager assetManager = getAssetManager();
		if (assetManager.isLoaded(fileName, PsdFile.class) == false) {
			assetManager.load(fileName, PsdFile.class, new PsdFileParameter(true));
		}
	}

	/**
	 * ���ؽ���
	 * 
	 * @param runnable
	 */
	public static void load(Runnable runnable) {
		AssetManagerProxy assetManager = getAssetManager();
		assetManager.load(runnable);
	}

	/** ������Դ */
	public static void load(String fileName, Class<?> clazz) {
		getAssetManager().load(fileName, clazz);
	}

	/** ������Դ */
	public static void load(List<AssetDescriptor> descriptors) {
		getAssetManager().load(descriptors);
	}

	/** ��ѯ�Ƿ��������Դ */
	public static boolean isLoad(String fileName, Class<?> clazz) {
		return getAssetManager().isLoaded(fileName, clazz);
	}

	/** ���¼���ͼƬ , ���ڽ������ʱ ͼƬ��ʧ������ */
	public static void reload(List<String> textures) {
		getAssetManager().reload(textures);
	}

	/** ���¼���ͼƬ , ���ڽ������ʱ ͼƬ��ʧ������ */
	public static void reload(Mark mark) {
		if (mark != null) {
			List<AssetDescriptor> descriptors = mark.filter(Texture.class);
			getAssetManager().load(descriptors);
		}
	}

	/** ���¼���ͼƬ , ���ڽ������ʱ ͼƬ��ʧ������ */
	public static void reload() {
		reload(getCurrentMark());
	}

	/** �����ļ������� */
	public static void setLoader(Class type, AssetLoader loader) {
		getAssetManager().setLoader(type, loader);
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
		private FileHandleResolver resolver;

		public AssetManagerProxy() {
			this(new InternalFileHandleResolver());
		}

		public AssetManagerProxy(FileHandleResolver resolver) {
			super(fileHandleResolver);
			this.resolver = resolver;
			initLoader();
		}

		private final void initLoader() {
			// ����� ��ͼƬ������
			setLoader(Texture.class, new LinearTextureLoader(resolver));
			// PsdFile ������
			setLoader(PsdFile.class, new PsdFileLoader(resolver));
			// TextureAtlas ��Դ , ��֪��Ϊʲô , ʹ��Ĭ�ϵĲ���
			setLoader(TextureAtlas.class, new PsdTextureAtlasLoader(resolver));
			// ���ڼ��ؽ�����
			setLoader(Runnable.class, new RunnableAssetLoader(resolver));

		}

		private final void mark(String tag) {
			Mark markTag = new Mark(tag);
			markTags.push(markTag);
			currentMark = markTag;
		}

		private Mark getCurrentMark() {
			return currentMark;
		}

		public final synchronized void load(Runnable runnable) {
			super.load(runnable.toString(), Runnable.class, new RunnableParameter(runnable));
		}

		public final synchronized <T> void load(String fileName, Class<T> type,
				AssetLoaderParameters<T> parameter) {
			// ��� Loader�Ƿ���� , Ĭ��Ϊ JSON ����
			if (getLoader(type) == null) {
				setLoader(type, new JsonDataAssetLoader<T>(resolver, type));
			}

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
