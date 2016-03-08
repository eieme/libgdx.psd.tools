package gdx.keyroy.psd.tools.util;

public class ZN {
	public static class Message {
		public static final String data_save = "������Ϣ�ѱ���";
		public static final String config_save = "������Ϣ�ѱ���";
	}

	public static class Window {
		public static final String title = "Psd ͼ����Ϣ����";
		public static final String data_manage = "���ݱ༭����";
	}

	public static class Menu {
		public static final String system = "ϵͳ";
		public static final String open_psd = "�� PSD �ļ�";
		public static final String open_param = "�� INI �ļ���";
		public static final String open_folder = "�� �ļ���";
		public static final String exit = "�˳�";
		public static final String delete_psd_file = "ɾ�� PSD �ļ�";
		public static final String delete_param_file = "ɾ�� ���� �ļ� ";
		public static final String delete_layer_param = "ɾ��ͼ�����";
		//
		public static final String update_param_file = "ˢ�²�����Ϣ ";
		public static final String open_param_file = "�򿪲����ļ�";
		//
		public static final String add_param = "����¼�";
		public static final String jump_param = "�������¼�ͼ��";
		public static final String del_param = "ɾ���¼�";
		//
		public static final String export = "����";
		public static final String pack = "���";
		public static final String pack_config = "��������趨";
		public static final String about = "����";
		public static final String help = "����";
		public static final String source_code = "Դ�����ַ";
		//
		public static final String new_element = "��Ԫ��";
		public static final String new_element_list = "����������Ԫ��";

		public static final String close_all = "�ر� ȫ�� ҳ��";
		public static final String close_others = "�ر� ���� ҳ��";
		public static final String close_this = "�ر�ҳ��";
		//
		public static final String reset_element = "����Ԫ��ID";
		public static final String del_element = "ɾ��Ԫ��";

	}

	public static class Label {
		public static final String psd_file_tree = "PSD �ļ��б�";
		public static final String param_file_tree = "INI �ļ��б�";
		public static final String param_file_count = "�ļ�����";
		public static final String psd_layer_tree = "����ͼ��ṹ";
		public static final String layer_param_table = "���ݲ����б�";
		//
		public static final String element_tree = "�����б����";
		public static final String element_collections = "Ԫ�ؼ���";
		public static final String element_class = "�����";
		public static final String element_resource = "��Դ����";
		public static final String field_tree = "�������";

		public static final String class_name = "����";
		public static final String field_value = "����ֵ";
		//
		public static final String notice = "ע�� : ɾ��Ԫ�ز��ָܻ�";
	}

	public static class Dialog {
		public static final String yes = "��";
		public static final String no = "��";
		public static final String delete_psd_file = "ȷ��Ҫɾ���ļ� , �����ļ����ָܻ�";
		public static final String delete_param_file = "ɾ��ѡ�е� �����趨 �ļ�?";
		public static final String delete_layer_param = "ȷ��Ҫɾ�� ѡ�е�ͼ�����?";
		//
		public static final String frame_config = "�����趨���";
		//
		public static final String reset_element = "����Ԫ��ID";
		public static final String del_class_element = "ɾ��Ԫ��";
	}

	public static class Error {
		public static final String parse_psd_file_failed = "���� PSD �ļ�ʧ��";
		public static final String parse_ini_file_failed = "���� ini �ļ�ʧ��";
		public static final String init_data_failed = "��ʼ������ʧ��";
	}

	public static class Text {
		public static final String layer_id = "ͼ��ID";
		public static final String param_key = "��";
		public static final String param_val = "ֵ";
		public static final String layer_name = "ͼ������";
		//
		public static final String add_param = "����¼�";
		public static final String input_param_value = "�����¼���ֵ";
		//
		public static final String used_clean_folder = "���Ŀ¼����";
		public static final String pack_folder = "����Ŀ¼";
		public static final String select_folder = "ѡ��Ŀ¼";
		public static final String used_libgdx_coordinate = "ʹ�� libgdx ������ϵ";
		public static final String used_texture_packer = "ʹ�� TexturePacker ���ͼƬ";
		public static final String used_android_assets_name = "ʹ�� Android Assets ���ƹ淶";
		public static final String copy_to_clipboard = "���������а�";
		//
		public static final String help = "ʹ�ð���" + "\n����PSD�ļ��ķ��� " + "\n��קPSD�ļ� ���� �ļ��� , ��  [PSD�ļ��б�] (����)���"
				+ "\n����INI�ļ��ķ��� \n��קINI�ļ� ���� �ļ��� , ��  [INI�ļ��б�] (����)���" + "\n\nINI �Ĳ����趨��ʽ ������"
				+ "\nA : key=val  (����Ϊ  �ı��༭��ʽ , �����ַ���)"
				+ "\nB : key=[v1,v2,v3] (����Ϊ   ʹ��ѡ��ʽ , ѡ����ֵ , �����ֵ�����Զ�������޸�)" + "\n\nINI ���� ֻ�����趨�� ͼ���� "
				+ "\n��ӷ�ʽ : ��[����ͼ��ṹ] (����) ���  , ��ѡ�ڵ� , �Ҽ�->����¼�->ѡ������->�༭������ֵ"
				+ "\nɾ����ʽ : ��[���ݲ����б�] (����) ��� , ��ѡ, ���߶�ѡ , �Ҽ� ->ɾ��ͼ�����"
				+ "\n�޸ķ�ʽ : ��[���ݲ����б�] (����) ��� , ˫�� 'ֵ' �� , �޸Ĳ���" + "\n\n��������" + "\n�˵� ���� -> ���";

		//
		public static final String input_element_id = "�����µ�Ԫ��ID";
	}

	public static void main(String[] args) {
		try {
			PropertiesUtil.save(ZN.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
