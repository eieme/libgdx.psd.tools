package psd.reflect;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 鏄犲皠瀵硅薄
 * 
 * @author roy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { TYPE, METHOD, FIELD })
public @interface PsdAn {
	// 寮曠敤 PSD 鍏冪礌
	public String[] value() default {};

	// 寮曠敤 PSD 鍏冪礌鐨勪笅鏍�
	public int index() default 0;
}
