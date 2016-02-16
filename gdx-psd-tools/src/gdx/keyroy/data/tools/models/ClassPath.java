package gdx.keyroy.data.tools.models;

import java.io.File;

//�� �ļ�������
public class ClassPath {
	// jar �ļ��ĵ�ַ
	protected String jarPath;
	// class ������
	protected String className;

	public ClassPath() {

	}

	public ClassPath(Class<?> clazz) {
		this.className = clazz.getName();
	}

	public ClassPath(File jarFile, Class<?> clazz) {
		if (jarFile != null) {
			this.jarPath = jarFile.getPath();
		}
		this.className = clazz.getName();
	}

	// �ļ��ĵ�ַ
	public String getJarPath() {
		return jarPath;
	}

	// �ļ��ĵ�ַ
	public String getClassName() {
		return className;
	}

	public final String getFileName() {
		return new File(className).getName().replace(".jar", "");
	}

}
