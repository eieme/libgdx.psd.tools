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
	// 榛樿鐨勮祫婧愬姞杞藉櫒缁�
	private static final Array<AssetManagerProxy> assets = new Array<AssetManagerProxy>();
	// 榛樿鐨� 鏂囦欢澶勭悊鍙ユ焺
	private static FileHandleResolver fileHandleResolver;
	// 榛樿鐨勮祫婧愬姞杞藉櫒
	private static AssetManagerProxy assetManager;

	/** 鍔犺浇鏂囦欢璧勬簮 **/
	public static final FileHandle file(String fileName) {
		if (fileHandleResolver != null) {
			return fileHandleResolver.resolve(fileName);
		}
		return Gdx.files.internal(fileName);
	}

	/** 璁剧疆璧勬簮鍔犺浇鐨勬枃妗ｉ�傞厤鍣� , 鐢ㄤ簬鏁版嵁鐨勮В瀵嗘搷浣� **/
	public static final void setHandleResolver(FileHandleResolver resolver) {
		fileHandleResolver = resolver;
		assetManager = new AssetManagerProxy(resolver);
		assets.clear();
		assets.add(assetManager);
	}

	/** 鑾峰彇璧勬簮鍔犺浇鍣� **/
	public static final AssetManagerProxy getAssetManager() {
		if (assetManager == null) {
			assetManager = new AssetManagerProxy();
			assets.clear();
			assets.add(assetManager);
		}
		return assetManager;
	}

	/** 鑾峰彇 assetManage缁� **/
	public static final AssetManagerProxy getAssetManager(String key) {
		if (key == null) {
			for (AssetManagerProxy proxy : assets) {
				if (proxy == null) {
					return proxy;
				}
			}
		} else {
			for (AssetManagerProxy proxy : assets) {
				if (key.equals(proxy.key)) {
					return proxy;
				}
			}
		}
		return null;
	}

	/** 鎻掑叆鏂扮殑 assetManage缁� **/
	public static final AssetManagerProxy setAssetManager(String key) {
		AssetManagerProxy proxy = getAssetManager(key);
		if (proxy != null) {
			assetManager = proxy;
		} else {
			if (fileHandleResolver != null) {
				proxy = new AssetManagerProxy(fileHandleResolver);
			} else {
				proxy = new AssetManagerProxy();
			}
			proxy.key = key;
			assets.add(proxy);
			assetManager = proxy;
		}
		return assetManager;
	}

	/** 鍒犻櫎 assetManage缁� **/
	public static final AssetManagerProxy removeAssetManager(String key) {
		AssetManagerProxy proxy = getAssetManager(key);
		if (proxy != null) {
			assets.removeValue(proxy, false);
			if (assetManager.equals(proxy)) {
				assetManager = null;
			}
			if (assets.size > 0) {
				assetManager = assets.get(assets.size - 1);
			}
			return proxy;
		}
		return null;
	}

	/** 鏍囪璧勬簮 */
	public static final void mark(String tag) {
		getAssetManager().mark(tag);
	}

	/** 鑾峰彇鏍囪璧勬簮 */
	public static Mark getCurrentMark() {
		return getAssetManager().getCurrentMark();
	}

	/** 閲婃斁璧勬簮 */
	public static void unload(List<AssetDescriptor> descriptors) {
		getAssetManager().unload(descriptors);
	}

	/** 閲婃斁璧勬簮 */
	public static void unloadMark(String tag) {
		getAssetManager().unloadMark(tag);
	}

	/** 绔嬪嵆鑾峰彇璧勬簮 */
	public static final <T> T get(String fileName, Class<T> clazz) {
		AssetManagerProxy assetManager = getAssetManager();
		if (assetManager.isLoaded(fileName, clazz) == false) {
			assetManager.load(fileName, clazz);
			assetManager.finishLoading();
		}
		return assetManager.get(fileName, clazz);
	}

	/**
	 * 鍔犺浇 PsdGroup 闇�瑕佷娇鐢ㄧ殑璧勬簮
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
	 * 鍔犺浇杩涘害
	 * 
	 * @param runnable
	 */
	public static void load(Runnable runnable) {
		AssetManagerProxy assetManager = getAssetManager();
		assetManager.load(runnable);
	}

	/** 鍔犺浇璧勬簮 */
	public static void load(String fileName, Class<?> clazz) {
		getAssetManager().load(fileName, clazz);
	}

	/** 鍔犺浇璧勬簮 */
	public static void load(List<AssetDescriptor> descriptors) {
		getAssetManager().load(descriptors);
	}

	/** 鏌ヨ鏄惁鍔犺浇浜嗚祫婧� */
	public static boolean isLoad(String fileName, Class<?> clazz) {
		return getAssetManager().isLoaded(fileName, clazz);
	}

	/** 閲嶆柊鍔犺浇鍥剧墖 , 鐢ㄤ簬瑙ｅ喅杩斿洖鏃� 鍥剧墖涓㈠け鐨勯棶棰� */
	public static void reload(List<String> textures) {
		getAssetManager().reload(textures);
	}

	/** 閲嶆柊鍔犺浇鍥剧墖 , 鐢ㄤ簬瑙ｅ喅杩斿洖鏃� 鍥剧墖涓㈠け鐨勯棶棰� */
	public static void reload(Mark mark) {
		if (mark != null) {
			List<AssetDescriptor> descriptors = mark.filter(Texture.class);
			getAssetManager().load(descriptors);
		}
	}

	/** 閲嶆柊鍔犺浇鍥剧墖 , 鐢ㄤ簬瑙ｅ喅杩斿洖鏃� 鍥剧墖涓㈠け鐨勯棶棰� */
	public static void reload() {
		reload(getCurrentMark());
	}

	/** 璁剧疆鏂囦欢鍔犺浇鍣� */
	public static void setLoader(Class type, AssetLoader loader) {
		getAssetManager().setLoader(type, loader);
	}

	/**
	 * 璧勬簮鍔犺浇鍣ㄧ殑浠ｇ悊,鐢ㄤ簬鐩戝惉鏁版嵁鍔犺浇鐘跺喌 <br>
	 * 鍔熻兘 1 , 璁板綍褰撳墠鍔犺浇鐘舵��<br>
	 * 鍔熻兘 2 , 璁剧疆璧勬簮鍔犺浇璁板綍鐐�<br>
	 * 鍔熻兘 3 , 鎵归噺鎭㈠鍔犺浇璧勬簮
	 */
	public static class AssetManagerProxy extends AssetManager {
		private Stack<Mark> markTags = new Stack<Mark>();
		private Mark currentMark;
		private FileHandleResolver resolver;
		private String key;

		public AssetManagerProxy() {
			this(new InternalFileHandleResolver());
		}

		public AssetManagerProxy(FileHandleResolver resolver) {
			super(fileHandleResolver);
			this.resolver = resolver;
			initLoader();
		}

		private final void initLoader() {
			// 鎶楅敮榻� 鐨勫浘鐗囧姞杞藉櫒
			setLoader(Texture.class, new LinearTextureLoader(resolver));
			// PsdFile 鍔犺浇鍣�
			setLoader(PsdFile.class, new PsdFileLoader(resolver));
			// TextureAtlas 鐨勬簮 , 涓嶇煡閬撲负浠�涔� , 浣跨敤榛樿鐨勪笉琛�
			setLoader(TextureAtlas.class, new PsdTextureAtlasLoader(resolver));
			// 鐢ㄤ簬鍔犺浇杩涘害鏉�
			setLoader(Runnable.class, new RunnableAssetLoader(resolver));

		}

		private final void mark(String tag) {
			Mark markTag = new Mark(tag);
			markTags.push(markTag);
			currentMark = markTag;
		}

		private final Mark getMark(String tag) {
			for (Mark mark : markTags) {
				if (mark.tag.equals(tag)) {
					return mark;
				}
			}
			return null;
		}

		private Mark getCurrentMark() {
			return currentMark;
		}

		public final synchronized void load(Runnable runnable) {
			super.load(runnable.toString(), Runnable.class, new RunnableParameter(runnable));
		}

		public final synchronized <T> void load(String fileName, Class<T> type,
				AssetLoaderParameters<T> parameter) {
			// 妫�鏌� Loader鏄惁瀛樺湪 , 榛樿涓� JSON 瑙ｆ瀽
			if (getLoader(type) == null) {
				setLoader(type, new JsonDataAssetLoader<T>(resolver, type));
			}

			super.load(fileName, type, parameter);
			if (currentMark != null) {
				currentMark.record(fileName, type, parameter);
			}
		}

		public synchronized void unloadMark(String tag) {
			Mark mark = getMark(tag);
			if (mark != null) {
				for (AssetDescriptor assetDescriptor : mark.elements) {
					System.out.println("unloadMark : " + assetDescriptor.fileName);
					if (assetDescriptor.fileName.indexOf(".atlas") != -1
							|| assetDescriptor.fileName.indexOf(".png") != -1) {
						System.out.println();
					}
					unload(assetDescriptor.fileName);
				}
				// 鍒犻櫎寮曠敤
				markTags.remove(mark);
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
		// 璧勬簮
		private Array<AssetDescriptor> elements = new Array<AssetDescriptor>(50);
		// 鏍囩鍚嶇О
		private final String tag;

		public Mark(String tag) {
			this.tag = tag;
		}

		// 璁板綍
		private final void record(String fileName, Class<?> type, AssetLoaderParameters<?> parameter) {
			for (AssetDescriptor assetDescriptor : elements) {
				if (assetDescriptor.fileName.equals(fileName) && assetDescriptor.type.equals(type)) {
					return;
				}
			}
			elements.add(new AssetDescriptor(fileName, type, parameter));
		}

		/** 杩囨护鏍囪璧勬簮 */
		public final List<AssetDescriptor> filter(Filter<AssetDescriptor> filter) {
			List<AssetDescriptor> list = new ArrayList<AssetDescriptor>();
			for (AssetDescriptor assetDescriptor : elements) {
				if (filter.accept(assetDescriptor)) {
					list.add(assetDescriptor);
				}
			}
			return list;
		}

		/** 杩囨护鏍囪璧勬簮 */
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

		/** 杩囨护鏍囪璧勬簮 */
		public final List<AssetDescriptor> filter(Class<?> clazz) {
			return filter(null, clazz);
		}

		// 鑾峰彇鏍囩鍚嶇О
		public final String getTag() {
			return tag;
		}

		// 鑾峰彇璁板綍鐨勮祫婧愬垪琛�
		public final Array<AssetDescriptor> getElements() {
			return elements;
		}
	}
}
