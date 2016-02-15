package gdx.keyroy.psd.tools;

import gdx.keyroy.psd.tools.models.EditorConfig;
import gdx.keyroy.psd.tools.models.EditorData;
import gdx.keyroy.psd.tools.models.PSDData;
import gdx.keyroy.psd.tools.util.FileUtil;
import gdx.keyroy.psd.tools.util.L;
import gdx.keyroy.psd.tools.util.Message;
import gdx.keyroy.psd.tools.util.MessageKey;
import gdx.keyroy.psd.tools.util.PSDUtil;
import gdx.keyroy.psd.tools.util.PSDUtil.LayerBoundary;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import library.psd.Layer;
import library.psd.LayersContainer;
import library.psd.Psd;
import psd.Element;
import psd.Folder;
import psd.LayerParam;
import psd.Pic;
import psd.PsdFile;
import psd.Text;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.keyroy.util.json.Json;

public class GdxPsdTools {

	private static final void send(String msg) {
		send(msg, null);
	}

	private static final void send(String msg, MessageKey messageKey) {
		Message.send(msg, messageKey);
	}

	public static final void export() {
		send("Exporting", MessageKey.H1);
		File packFolder = new File(EditorConfig.export_path);
		send("cleaning", MessageKey.H2);
		// ����ļ���Ŀ¼
		FileUtil.delete(packFolder);
		packFolder.mkdirs();
		send("cleaning ok", MessageKey.H2);
		// ���������ļ�
		List<PSDData> psdDatas = EditorData.getPsdDatas();
		// �����������
		Set<PSDData> errors = new HashSet<PSDData>();
		for (PSDData psdData : psdDatas) {
			try {
				send("saving json on : " + psdData.getFileName(), MessageKey.H2);
				PsdFile psdFile = translate(psdData);
				psdFile.used_texture_packer = EditorConfig.used_texture_packer;
				String json = new Json(psdFile).toString();
				System.out.println(json);
				File file = new File(packFolder, psdData.getFileName() + ".json");
				FileUtil.save(file, json);
				send("save json ok : " + file.getPath());
			} catch (Exception e) {
				// e.printStackTrace();
				errors.add(psdData);
				Message.send(L.get("error.parse_psd_file_failed") + " : " + psdData.getFilePath());
			}
		}

		// ���ͼƬ
		for (PSDData psdData : psdDatas) {
			try {// ���˵������������
				if (errors.contains(psdData) == false) {
					send("saving image on : " + psdData.getFileName(), MessageKey.H2);
					packageImage(packFolder, psdData);
					send("save image ok : " + psdData.getFilePath());
				}
			} catch (Exception e) {
				// e.printStackTrace();
				Message.send(L.get("error.parse_psd_file_failed") + " : " + psdData.getFilePath());
			}
		}

		send("all done", MessageKey.H2);
		send("export data complete : " + packFolder.getPath());
	}

	public static final void packageImage(File packFolder, PSDData psdData) {
		Psd psd = psdData.getCache();
		List<Layer> layers = new ArrayList<Layer>();
		filterImage(psd, layers);
		//
		if (EditorConfig.used_texture_packer) { // ʹ�� TexturePacker ���ͼƬ
			final Settings settings = new Settings();
			settings.maxWidth = 2048;
			settings.maxHeight = 2048;
			TexturePacker packer = new TexturePacker(settings);
			for (Layer layer : layers) {
				packer.addImage(layer.getImage(), layer.getName());
			}
			String imagePath = psdData.getFileName() + ".atlas";
			Message.send("saving image : " + imagePath);
			packer.pack(packFolder, imagePath);
		} else {
			for (Layer layer : layers) {
				BufferedImage bufferedImage = layer.getImage();
				File file = new File(packFolder, layer.getName() + ".png");
				try {
					Message.send("saving image : " + file.getPath());
					FileOutputStream outputStream = new FileOutputStream(file);
					ImageIO.write(bufferedImage, "png", outputStream);
					outputStream.close();
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

	// ����ת��
	public static final PsdFile translate(PSDData psdData) {
		Psd psd = psdData.getCache();
		PSDUtil.updatePsdLayerPosition(psd);
		Rectangle rect = PSDUtil.getMaxSize(psd);
		PsdFile psdFile = new PsdFile();
		psdFile.width = psd.getWidth();
		psdFile.height = psd.getHeight();
		psdFile.maxWidth = rect.width;
		psdFile.maxHeight = rect.height;
		psdFile.params = psdData.getLayerParams(null);
		psdFile.psdName = psdData.getFileName();
		addChild(psdData, psd, psdFile);
		return psdFile;
	}

	public static final void addChild(PSDData psdData, LayersContainer container, Folder folder) {
		for (int i = 0; i < container.getLayersCount(); i++) {
			Layer layer = container.getLayer(i);
			Element actor = null;
			if (layer.isFolder()) { // ����һ���ļ���
				actor = new Folder();
			} else if (layer.isTextLayer()) { // ����һ���ı�����
				actor = new Text();
				((Text) actor).setPsdText(layer.getPsdText());
			} else if (layer.getImage() != null) { // ����һ��ͼƬ
				actor = new Pic();
				if (EditorConfig.used_texture_packer) {
					((Pic) actor).textureName = layer.getName();
				} else {
					((Pic) actor).textureName = layer.getName() + ".png";
				}
			}

			if (actor != null) { // ����
				actor.layerName = layer.getName();
				if (actor instanceof PsdFile) {
				} else {
					LayerBoundary boundary = LayerBoundary.getLayerBoundary(layer);
					if (boundary != null) {
						actor.x = boundary.getX();
						actor.y = boundary.getY();
						actor.width = boundary.getWidth();
						actor.height = boundary.getHeight();
					} else {
						actor.x = layer.getX();
						actor.y = layer.getY();
						actor.width = layer.getWidth();
						actor.height = layer.getHeight();
					}
					if (EditorConfig.used_libgdx_coordinate) { // ʹ�� libgdx ������ϵ
						actor.y = folder.height - actor.y - actor.height;
					}
				}
				folder.childs.add(actor);
				// �¼� , ������Ҫȥ���¼���ӳ�� id
				List<LayerParam> layerParams = psdData.getLayerParams(layer);
				if (layerParams != null) {
					actor.params = new ArrayList<LayerParam>(layerParams.size());
					for (LayerParam layerParam : layerParams) {
						LayerParam newParam = new LayerParam(layerParam);
						newParam.setLayerId(null);
						actor.params.add(newParam);
					}
				}
				//
				if (actor instanceof Folder) {
					addChild(psdData, layer, (Folder) actor);
				}
			}
		}

	}
}
