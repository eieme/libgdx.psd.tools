package psd.reflect;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ӳ�����
 * 
 * @author roy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { TYPE, METHOD, FIELD })
public @interface PsdAn {
	// ���� PSD Ԫ��
	public String[] value() default {};

	// ���� PSD Ԫ�ص��±�
	public int index() default 0;
}
