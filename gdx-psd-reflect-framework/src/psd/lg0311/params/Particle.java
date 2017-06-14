package psd.lg0311.params;

public class Particle {
	// 鏂囦欢鍚嶇О , 杩欎釜涓嶆槸鍏ㄥ悕 , 姣斿 aaa.fnt , 鍐欎綔 aaa
	public String na;
	// 鏂囦欢鍏ㄨ矾寰�
	public String path;

	public final String getPath() {
		if (path != null) {
			return path;
		} else if (na != null) {
			return "particles/" + na;
		}
		return null;
	}
}
