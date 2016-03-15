package gdx.keyroy.data.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.keyroy.util.json.Json;
import com.keyroy.util.json.JsonAn;

import gdx.keyroy.data.tools.models.ClassElement;
import gdx.keyroy.data.tools.models.ClassPath;
import gdx.keyroy.data.tools.models.ResoucePath;
import gdx.keyroy.psd.tools.util.FileUtil;
import gdx.keyroy.psd.tools.util.L;
import gdx.keyroy.psd.tools.util.MessageKey;
import gdx.keyroy.psd.tools.util.Messager;
import gdx.keyroy.psd.tools.util.PsdCache;
import library.psd.Layer;
import library.psd.LayersContainer;
import library.psd.Psd;

// ����
public class DataManage {
	@JsonAn(skip = true)
	private static int hashCode;
	// ������
	@JsonAn(skip = true)
	private static List<ClassPath> classPaths = new ArrayList<ClassPath>();
	// ��Դ����
	private static List<ResoucePath> resourcePaths = new ArrayList<ResoucePath>();

	// ����µ���
	public static final void edit(Class<?> clazz) {
		addClass(clazz);
	}

	// ����µ���
	public static final void addClass(Class<?> clazz) {
		addClass(clazz, false);
	}

	// ����µ���
	public static final void addClass(Class<?> clazz, boolean autoSave) {
		for (ClassPath classPath : classPaths) {
			if (classPath.getClassName().equals(clazz.getName())) {
				return;
			}
		}
		ClassPath classPath = new ClassPath(clazz);
		classPaths.add(classPath);
		if (autoSave) {
			save();
		}
	}

	public static final void addResource(File file, boolean autoSave) {
		_addResource(file.isDirectory() ? file : null, file);
		if (autoSave) {
			save();
		}
	}

	private static final void _addResource(File rootFolder, File file) {
		if (file != null && file.exists()) {
			for (ResoucePath resoucePath : resourcePaths) {
				if (resoucePath.getFilePath().equals(file.getPath())) {
					return;
				}
			}
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					for (File cFile : files) {
						_addResource(rootFolder, cFile);
					}
				}
			} else { // �ļ�
				resourcePaths.add(new ResoucePath(rootFolder, file));
			}
		}
	}

	/**
	 * 
	 * 
	 * 
	 */
	public static final void load() {
		try {
			File file = getFile();
			if (file.exists()) {
				FileInputStream inputStream = new FileInputStream(file);
				Json json = new Json(inputStream);
				json.toObject(DataManage.class);
				inputStream.close();
				//
			}
			// ���� ClassPath
			for (ClassPath classPath : DataManage.classPaths) {
				File folder = getFolder(classPath);
				if (folder.exists()) {
					Class<?> clazz = classPath.getClazz();
					File[] files = folder.listFiles();
					for (File jsonFile : files) {
						try {
							FileInputStream stream = new FileInputStream(jsonFile);
							Json j = new Json(stream);
							stream.close();
							Object object = j.toObject(clazz);
							ClassElement classElement = new ClassElement();
							classElement.setObjId(jsonFile.getName());
							classElement.setObject(object);
							classPath.addElement(classElement);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void save() {
		try {
			Json json = new Json(new DataManage());
			String text = json.toString();
			if (text.hashCode() != hashCode) {
				hashCode = text.hashCode();
				System.out.println("save data");
				FileUtil.save(getFile(), text);
				Messager.send(L.get("Message.data_save") + "    " + new Date().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void reset(ClassElement classElement, String id) {
		try {
			delete(classElement);
			classElement.setObjId(id);
			save(classElement);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static final void delete(ClassElement classElement) {

		ClassPath classPath = classElement.getParent();
		// ɾ��Ԫ��
		classPath.delElement(classElement);
		// ɾ���ļ�
		File folder = getFolder(classPath);
		File file = new File(folder, classElement.getObjId());
		file.delete();

	}

	// �������ļ�
	public static final void save(ClassElement classElement) {
		try {
			ClassPath classPath = classElement.getParent();
			save(classElement, getFolder(classPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �������ļ��� ָ���ļ���
	public static final void save(ClassElement classElement, File folder) {
		try {
			if (folder.exists() == false) {
				folder.mkdirs();
			}
			File jsonFile = new File(folder, classElement.getObjId());
			jsonFile.createNewFile();
			FileUtil.save(jsonFile, new Json(classElement.getObject()).toString());
			//
			ClassPath classPath = classElement.getParent();
			Messager.send(L.get("message.class_element_save") + "  " + classPath.getClassName() + "  "
					+ classElement.getObjId() + "  " + new Date().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void save(List<ClassElement> classElements) {
		for (ClassElement classElement : classElements) {
			save(classElement);
		}
	}

	private static final File getFile() {
		return new File(DataManage.class.getSimpleName());
	}

	private static final File getFolder(ClassPath classPath) {
		return new File(classPath.getClassName());
	}

	public static final List<ClassPath> getClassPaths() {
		return classPaths;
	}

	public static final List<ResoucePath> getResourcePaths() {
		return resourcePaths;
	}

	public static final void export() {
		// ������� , ������Ĭ�� ���� assets �ļ���
		// ������������ļ�
		// ������Դ���ļ��� , �� �ļ��е� ��Դ���Ƶ� ���һ���ļ���Ŀ¼ , û��Ŀ¼�ĸ��Ƶ���Ŀ¼
		Messager.send("Export", MessageKey.H1);
		File asssets = new File("assests");
		FileUtil.delete(asssets);// �������
		asssets.mkdirs();
		Messager.send("clean cache", MessageKey.H2);
		// ��������
		for (ClassPath classPath : classPaths) {
			Messager.send("Saving " + classPath.getClassName(), MessageKey.H1);
			File classFolder = getFolder(classPath);
			classFolder = new File(asssets, classFolder.getName());
			classFolder.mkdirs();
			for (ClassElement element : classPath.getElements()) {
				Messager.send("saving : " + element.getObjId(), MessageKey.H2);
				save(element, classFolder);
			}
		}
		// �����Դ����
		Messager.send("Saving Images ", MessageKey.H1);
		for (ResoucePath resourcePath : resourcePaths) {
			File file = resourcePath.getFile();
			if (resourcePath.isPSD()) { // ��� PSD
				Psd psd = PsdCache.get(file);
				List<Layer> layers = new ArrayList<Layer>();
				filterImage(psd, layers);
				final Settings settings = new Settings();
				settings.pot = false;
				settings.maxWidth = 2048;
				settings.maxHeight = 2048;
				TexturePacker packer = new TexturePacker(settings);
				for (Layer layer : layers) {
					packer.addImage(layer.getImage(), layer.getName());
				}
				String imagePath = resourcePath.getAssetsPath().replace(".psd", ".atlas");
				Messager.send("saving image : " + imagePath);
				packer.pack(asssets, imagePath);

			} else { // �����ļ�

				try {
					File copyTo = new File(asssets, resourcePath.getAssetsPath());
					FileUtil.copy(file, copyTo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final void filterImage(LayersContainer container, List<Layer> out) {
		for (int i = 0; i < container.getLayersCount(); i++) {
			Layer layer = container.getLayer(i);
			if (layer.isFolder() || layer.isTextLayer()) {
			} else if (layer.getImage() != null) {
				out.add(layer);
			}
			filterImage(layer, out);
		}
	}

}
