package com.keyroy.gdx.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.keyroy.util.json.Json;

import gdx.keyroy.psd.tools.util.FileUtil;
import gdx.keyroy.psd.tools.util.Messager;
import gdx.keyroy.psd.tools.util.PSDUtil;
import gdx.keyroy.psd.tools.util.PSDUtil.LayerBoundary;
import gdx.keyroy.psd.tools.util.SwingUtil;
import gdx.keyroy.psd.tools.util.SwingUtil.DropInAdapter;
import gdx.keyroy.psd.tools.widgets.DialogProgress;
import library.psd.Layer;
import library.psd.LayersContainer;
import library.psd.Psd;
import library.psd.parser.object.PsdText;
import psd.Element;
import psd.Folder;
import psd.Pic;
import psd.PsdFile;
import psd.Text;

public class GdxPsdToolsForCocosStudio {

	private static JFrame frmGdxpsdtools;
	private JCheckBox cleanFolder;
	private JCheckBox saveImage;
	private JCheckBox saveAtlas;
	private JCheckBox rotateImage;
	private JCheckBox formatLayerName;
	private JLabel filePanel;
	private static int tagIndex = 0;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					SwingUtil.initWindowsLookAndFeel();
					GdxPsdToolsForCocosStudio window = new GdxPsdToolsForCocosStudio();
					window.frmGdxpsdtools.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		for (int i = 0; i < 10; i++) {
			tenNum();
		}
	}

	/**
	 * Create the application.
	 */
	public GdxPsdToolsForCocosStudio() {
		Config.load();
		initialize();
		initCheckBox();
		initFileListener();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGdxpsdtools = new JFrame();
		frmGdxpsdtools.setTitle("GdxPsdTools - For CocosStudio");
		frmGdxpsdtools.setBounds(100, 100, 668, 320);
		frmGdxpsdtools.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		frmGdxpsdtools.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		filePanel = new JLabel("\u62D6\u62FD psd/xlsx/png \u6587\u4EF6\u5230\u8FD9\u4E2A\u9762\u677F");
		filePanel.setBackground(Color.WHITE);
		filePanel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(filePanel);

		JPanel configPanel = new JPanel();
		frmGdxpsdtools.getContentPane().add(configPanel, BorderLayout.NORTH);
		configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

		cleanFolder = new JCheckBox("\u6E05\u7A7A\u65E7\u6587\u4EF6");
		configPanel.add(cleanFolder);

		configPanel.add(new JLabel(" "));
		saveImage = new JCheckBox("\u6253\u5305\u56FE\u7247");
		configPanel.add(saveImage);

		configPanel.add(new JLabel(" "));

		saveAtlas = new JCheckBox("\u6253\u5305\u6210 Atlas \u56FE\u7247\u96C6");
		configPanel.add(saveAtlas);
		configPanel.add(new JLabel(" "));

		rotateImage = new JCheckBox("\u652F\u6301 Atlas \u56FE\u7247\u65CB\u8F6C");
		configPanel.add(rotateImage);
		configPanel.add(new JLabel(" "));

		formatLayerName = new JCheckBox("\u683C\u5F0F\u5316PSD\u56FE\u5C42\u540D\u79F0");
		configPanel.add(formatLayerName);
	}

	private final void initCheckBox() {
		cleanFolder.setSelected(Config.cleanFolder);
		saveImage.setSelected(Config.saveImage);
		saveAtlas.setSelected(Config.saveAtlas);
		formatLayerName.setSelected(Config.formatLayerName);
		//
		//
		cleanFolder.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Config.cleanFolder = cleanFolder.isSelected();
				Config.save();
			}
		});
		//
		saveImage.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Config.saveImage = saveImage.isSelected();
				Config.save();
			}
		});
		//
		saveAtlas.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Config.saveAtlas = saveAtlas.isSelected();
				Config.save();
			}
		});
		rotateImage.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Config.rotateImage = rotateImage.isSelected();
				Config.save();
			}
		});
		//
		formatLayerName.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Config.formatLayerName = formatLayerName.isSelected();
				Config.save();
			}
		});
	}

	private final void initFileListener() {
		SwingUtil.addDropIn(filePanel, new DropInAdapter() {
			@Override
			public void onDropIn(final List<File> files) {
				//
				// 打包
				final DialogProgress dialogProgress = new DialogProgress();
				SwingUtil.center(frmGdxpsdtools, dialogProgress);
				new Thread(new Runnable() {
					@Override
					public void run() {
						dialogProgress.setVisible(true);
						boolean tryImage = true;
						final File folder = new File(FileUtil.getRoot(), "GdxPsdToolsExports");
						if (Config.cleanFolder) {// 清空文件夹
							FileUtil.delete(folder);
						}
						if (folder.exists() == false) { // 创建文件夹
							folder.mkdirs();
						}
						for (File file : files) {
							try {

								if (onDropIn(folder, file)) {
									tryImage = false;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (tryImage) {
							exportAtlsafile(folder, files);
						}
						dialogProgress.dispose();
					}
				}).start();
			}

			public boolean onDropIn(File folder, File file) throws Exception {
				if (file.getName().endsWith(".psd")) { // 打包 PSD
					export(folder, new Psd(file));
					return true;
				} else if (file.getName().endsWith(".xlsx")) {
					exportXlsx(folder, file);
					return true;
				}
				return false;
			}
		});
	}

	// 导出图片集
	public static final void exportAtlsafile(File folder, final List<File> files) {
		final Settings settings = new Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		settings.paddingX = 2;
		settings.paddingY = 2;
		
		settings.pot = false;
		settings.rotation = Config.rotateImage;
		settings.filterMin = TextureFilter.Linear;
		settings.filterMag = TextureFilter.Linear;
		
		TexturePacker packer = new TexturePacker(settings);
		String fileName = null;
		if (Config.formatLayerName) {
			fileName = "" + System.currentTimeMillis();
		}
		for (File file : files) {
			try {
				BufferedImage image = ImageIO.read(file);
				packer.addImage(image, file.getName());
				if (fileName == null) {
					fileName = file.getName().substring(0, file.getName().indexOf("."));
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		packer.pack(folder, fileName + ".atlas");
	}

	public static final void exportXlsx(File folder, File file) throws Exception {
		List<JsonPack> arrays = XlsxParser.parser(file);
		for (JsonPack jsonPack : arrays) {
			File jsonFile = writeJson(folder, jsonPack);
			//jsonFile = writeJson(folder, jsonPack);
			System.out.println("write josn : " + jsonFile.getName());
		}
	}

	private static final File writeJson(File jsonFolder, JsonPack jsonPack) throws Exception {
		File jsonFile = new File(jsonFolder, jsonPack.getName() + ".json");
		String json = jsonPack.getJsonObject().toString();
		//
		FileWriter fileWriter = new FileWriter(jsonFile);
		fileWriter.write(json);
		fileWriter.flush();
		fileWriter.close();

		return jsonFile;
	}

	public static final void export(File folder, Psd psd) throws Exception {
		PSDUtil.updateLayerParent(psd);
		//
		if (Config.saveImage) { // 保存图片
			// 收集图片图层
			List<Layer> layers = new ArrayList<Layer>();
			filterImage(psd, layers);
			if (Config.saveAtlas) { // 保存为图片集
				final Settings settings = new Settings();
				settings.pot = false;
				settings.rotation = Config.rotateImage;
				settings.maxWidth = 2048;
				settings.maxHeight = 2048;
				settings.filterMin = TextureFilter.Linear;
				settings.filterMag = TextureFilter.Linear;
				TexturePacker packer = new TexturePacker(settings);
				for (Layer layer : layers) {
					packer.addImage(layer.getImage(), getImageName(layer,false));
				}
				String imagePath = psd.getName().replace(".psd", ".atlas");
				Messager.send("saving image : " + imagePath);
				packer.pack(folder, imagePath);
			} else {
				File psdFolder = new File(folder, psd.getName().replace(".psd", ""));
				if (psdFolder.exists() == false) { // 创建文件夹
					psdFolder.mkdirs();
				}
				for (Layer layer : layers) {
					System.out.println("导出图片-》 "+psd.getName()+"    " +folder);
					ImageIO.write(layer.getImage(), "png", new File(psdFolder, getImageName(layer,true)));
				}
			}
		}
//		// 保存 PSD 数据
//		PsdFile psdFile = translate(psd);
//		FileUtil.save(new File(folder, psd.getName().replace(".psd", ".json")), new Json(psdFile).toString());
//	
		//保存PSD 数据 为 CSD
		
		PsdFile psdFile = translate2Csd(psd);
		String string = parsing2Csd(psdFile);
		FileUtil.save(new File(folder, psd.getName().replace(".psd", ".csd")), string);
		
		
	}
	
	private static final void filterImage(LayersContainer container, List<Layer> out) {
		for (int i = 0; i < container.getLayersCount(); i++) {
			Layer layer = container.getLayer(i);
			if (layer.isFolder() || layer.isTextLayer()) {
			} else if (layer.getImage() != null) {
				out.add(layer);
			}
			filterImage(layer, out);
		}
	}

	private static final String getImageName(Layer layer,boolean needExtension) {
		String name = layer.getName();
		// 这里需要去掉图片中的参数
		if (name.indexOf("@") != -1) {
			String[] sp = name.split("@", 2);
			name = sp[0];
		}
		if (name == null) {
			name = "_";
		}
		
		return name + (needExtension?".png":"");
	}

	public static final PsdFile translate(Psd psd) {
		PSDUtil.updatePsdLayerPosition(psd);
		Rectangle rect = PSDUtil.getMaxSize(psd);
		PsdFile psdFile = new PsdFile();
		psdFile.width = psd.getWidth();
		psdFile.height = psd.getHeight();
		psdFile.maxWidth = rect.width;
		psdFile.maxHeight = rect.height;
		psdFile.psdName = psd.getName();
		if (Config.saveAtlas) {
			psdFile.atlas = psd.getName().replace(".psd", ".atlas");
		}
		// 参数
		addChild(psd, psd, psdFile);
		return psdFile;
	}
	
	public static final void addChild(Psd psd, LayersContainer container, Folder folder) {

		for (int i = 0; i < container.getLayersCount(); i++) {
			Layer layer = container.getLayer(i);
			Element actor = null;
			if (layer.isFolder()) { // 这是一个文件夹
				actor = new Folder();
			} else if (layer.isTextLayer()) { // 这是一个文本对象
				Text text = new Text();
				PsdText psdText = layer.getPsdText();
				text.text = psdText.value;
				text.a = psdText.a;
				text.r = psdText.r;
				text.g = psdText.g;
				text.b = psdText.b;
				text.fontSize = psdText.fontSize;
				actor = text;
			} else if (layer.getImage() != null) { // 这是一个图片
				actor = new Pic();
				((Pic) actor).textureName = getImageName(layer,true);
			}

			if (actor != null) { // 坐标
				actor.layerName = layer.getName();
				actor.isVisible = layer.isVisible();
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
					// libgdx 坐标系 , Y 相反
					actor.y = folder.height - actor.y - actor.height;
				}
				folder.childs.add(actor);

				//
				if (actor instanceof Folder) {
					addChild(psd, layer, (Folder) actor);
				}
			}
		}

	}
	
	
	
	
	/////////////////////以下是导出 cocos studio 的数据

	private static String guid() {
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.toString());
		return uuid.toString();
	}
	private static String tenNum() {
        Random rd = new Random();
		return rd.nextInt()+"";
	}
	
	private static String getExtendTab(int extendTabCount) {
		String extendTab = "";
		for (int i = 0; i < extendTabCount; i++) {
			extendTab+="\t";
		}
		return extendTab;
	}
	private static String Csd_Begin(String docName,float docWidth,float docHeight) {
		String str = "<GameFile>\n";
		str += "\t<PropertyGroup Name=\"" + docName + "\" Type=\"Layer\" ID=\"" + guid() + "\" Version=\"3.10.0.0\" />\n";
		str += "\t<Content ctype=\"GameProjectContent\">\n";
		str += "\t\t<Content>\n";
		str += "\t\t<Animation Duration=\"0\" Speed=\"1.0000\" />\n";
		str += "\t\t<ObjectData Name=\"Layer\" ctype=\"GameLayerObjectData\">\n";
		str += "\t\t\t<Size X=\"" + docWidth + "\" Y=\"" + docHeight + "\" />\n";
		str += "\t\t\t<Children>\n";
		return str;
	}
	private static String Csd_End() {
		
		String str = "";
		str += "\t\t\t</Children>\n";
	    str += "\t\t</ObjectData>\n";
	    str += "\t\t</Content>\n";
		str += "\t</Content>\n";
		str += "</GameFile>\n";
		return str;
	}
	
	private static String Csd_Sprite(Element actor,int extendTabCount) {
		String json = "";
		String extendTab = getExtendTab(extendTabCount);
	
		json += extendTab+"\t\t\t\t<AbstractNodeData Name=\"" + actor.layerName + "\" ActionTag=\"" + tenNum() + "\" Tag=\"" + tagIndex + "\" IconVisible=\"False\" ctype=\"SpriteObjectData\">\n";
		tagIndex += 1;
		// json += "\t\t\t\t\t<Position X=\"" + (x - Math.round(width) * 0.5000) + "\" Y=\"" + (y - Math.round(height) * 0.5000) + "\" />\n";
		json += extendTab+"\t\t\t\t\t<Size X=\"" + actor.width + "\" Y=\"" + actor.height+ "\" />\n";
        json += extendTab+"\t\t\t\t\t<AnchorPoint ScaleX=\"0.5000\" ScaleY=\"0.5000\" />\n";

		json += extendTab+"\t\t\t\t\t<Position X=\"" + (actor.x + actor.width/2) + "\" Y=\"" + (actor.y + actor.height/2) + "\" />\n";
        json += extendTab+"\t\t\t\t\t<Scale ScaleX=\"1.0000\" ScaleY=\"1.0000\" />\n";
        json += extendTab+"\t\t\t\t\t<CColor A=\"255\" R=\"255\" G=\"255\" B=\"255\" />\n";
        json += extendTab+"\t\t\t\t\t<PrePosition X=\"0.0000\" Y=\"0.0000\" />\n";
        json += extendTab+"\t\t\t\t\t<PreSize X=\"0.0000\" Y=\"0.0000\" />\n";
        json += extendTab+"\t\t\t\t\t<FileData Type=\"Normal\" Path=\"" + ((Pic) actor).textureName + "\" Plist=\"\"/>\n";
        json += extendTab+"\t\t\t\t\t<BlendFunc Src=\"1\" Dst=\"771\" />\n";
		json += extendTab+"\t\t\t\t</AbstractNodeData>\n";
		
		return json;
	}

	private static String Csd_Layout(Element actor,int extendTabCount) {
		String json = "";
		String extendTab = getExtendTab(extendTabCount);
		json += extendTab+"\t\t\t\t<AbstractNodeData Name=\"" + actor.layerName + "\" ActionTag=\"" + tenNum() + "\" Tag=\"" + tagIndex + "\" IconVisible=\"False\" TouchEnable=\"True\" ClipAble=\"False\" BackColorAlpha=\"102\" ColorAngle=\"90.0000\" ctype=\"PanelObjectData\">\n";
		tagIndex += 1;	
		json += extendTab+"\t\t\t\t\t<Size X=\"" + actor.width + "\" Y=\"" + actor.height+ "\" />\n";
		json += extendTab+"\t\t\t\t\t<Children>\n";
		
		 List<Element> childs = ((Folder)actor).childs;
		for (Element element : childs) {
			json += classification(element,extendTabCount+1);
		}
		
		json += extendTab+"\t\t\t\t\t</Children>\n";
		json += extendTab+"\t\t\t\t\t<AnchorPoint ScaleX=\"0\" ScaleY=\"0\" />\n";
		json += extendTab+"\t\t\t\t\t<Position X=\"" + (actor.x ) + "\" Y=\"" + (actor.y) + "\" />\n";
	     
		
		json += extendTab+"\t\t\t\t\t<Scale ScaleX=\"1.0000\" ScaleY=\"1.0000\" />\n";
        json += extendTab+"\t\t\t\t\t<CColor A=\"255\" R=\"255\" G=\"255\" B=\"255\" />\n";
        json += extendTab+"\t\t\t\t\t<PrePosition X=\"0.0000\" Y=\"0.0000\" />\n";
        json += extendTab+"\t\t\t\t\t<PreSize X=\"0.0000\" Y=\"0.0000\" />\n";


        json += extendTab+ "\t\t\t\t\t<SingleColor A=\"255\" R=\"150\" G=\"200\" B=\"255\" />\n";
        json += extendTab+ "\t\t\t\t\t<FirstColor A=\"255\" R=\"150\" G=\"200\" B=\"255\" />\n";
        json += extendTab+ "\t\t\t\t\t<EndColor A=\"255\" R=\"150\" G=\"200\" B=\"255\" />\n";
        json += extendTab+ "\t\t\t\t\t<ColorVector ScaleY=\"1.0000\" />\n";
        
        json += extendTab+"\t\t\t\t</AbstractNodeData>\n";
				
		return json;
	}
	private static String Csd_Button(Element actor,int extendTabCount) {
		String json = "";
		String extendTab = getExtendTab(extendTabCount);
	
		json += extendTab+"\t\t\t\t<AbstractNodeData Name=\"" + actor.layerName + "\" ActionTag=\"" + tenNum() + "\" Tag=\"" + tagIndex + "\" IconVisible=\"False\" TouchEnable=\"True\" FontSize=\"14\" ctype=\"ButtonObjectData\">\n";
		tagIndex += 1;
		json += extendTab+"\t\t\t\t\t<Size X=\"" + actor.width + "\" Y=\"" + actor.height+ "\" />\n";
		json += extendTab+"\t\t\t\t\t<AnchorPoint ScaleX=\"0.5000\" ScaleY=\"0.5000\" />\n";
		json += extendTab+"\t\t\t\t\t<Position X=\"" + (actor.x + actor.width/2) + "\" Y=\"" + (actor.y + actor.height/2) + "\" />\n";
		json += extendTab+"\t\t\t\t\t<Scale ScaleX=\"1.0000\" ScaleY=\"1.0000\" />\n";
        json += extendTab+"\t\t\t\t\t<CColor A=\"255\" R=\"255\" G=\"255\" B=\"255\" />\n";
        json += extendTab+"\t\t\t\t\t<PrePosition X=\"0.0000\" Y=\"0.0000\" />\n";
        json += extendTab+"\t\t\t\t\t<PreSize X=\"0.0000\" Y=\"0.0000\" />\n";
        
        json += extendTab+"\t\t\t\t\t<TextColor A=\"255\" R=\"65\" G=\"65\" B=\"70\" />\n";
        
        json += extendTab+"\t\t\t\t\t<NormalFileData Type=\"Normal\" Path=\"" + ((Pic) actor).textureName + "\" Plist=\"\"/>\n";
        json += extendTab+"\t\t\t\t\t<OutlineColor A=\"255\" R=\"255\" G=\"0\" B=\"0\" />\n";
        json += extendTab+"\t\t\t\t\t<ShadowColor A=\"255\" R=\"110\" G=\"110\" B=\"110\" />\n";
        json += extendTab+"\t\t\t\t</AbstractNodeData>\n";
		return json;
	}
	private static String Csd_ImageView(Element actor,int extendTabCount) {
		String json = "";
		String extendTab = getExtendTab(extendTabCount);
	
		json += extendTab+"\t\t\t\t<AbstractNodeData Name=\"" + actor.layerName + "\" ActionTag=\"" + tenNum() + "\" Tag=\"" + tagIndex + "\" IconVisible=\"False\" ctype=\"ImageViewObjectData\">\n";
		tagIndex += 1;
		json += extendTab+"\t\t\t\t\t<Size X=\"" + actor.width + "\" Y=\"" + actor.height+ "\" />\n";
		json += extendTab+"\t\t\t\t\t<AnchorPoint ScaleX=\"0.5000\" ScaleY=\"0.5000\" />\n";
		json += extendTab+"\t\t\t\t\t<Position X=\"" + (actor.x + actor.width/2) + "\" Y=\"" + (actor.y + actor.height/2) + "\" />\n";
		json += extendTab+"\t\t\t\t\t<Scale ScaleX=\"1.0000\" ScaleY=\"1.0000\" />\n";
        json += extendTab+"\t\t\t\t\t<CColor A=\"255\" R=\"255\" G=\"255\" B=\"255\" />\n";
        json += extendTab+"\t\t\t\t\t<PrePosition X=\"0.0000\" Y=\"0.0000\" />\n";
        json += extendTab+"\t\t\t\t\t<PreSize X=\"0.0000\" Y=\"0.0000\" />\n";
        json += extendTab+"\t\t\t\t\t<FileData Type=\"Normal\" Path=\"" + ((Pic) actor).textureName + "\" Plist=\"\"/>\n";
		json += extendTab+"\t\t\t\t</AbstractNodeData>\n";
		
		return json;
	}
	
	private static String classification (Element actor,int extendTabCount) {
		
		

		if(actor instanceof Folder){
			return Csd_Layout(actor,extendTabCount);
		}
		if(actor.layerName.startsWith("btn")||actor.layerName.startsWith("Btn")){
			return Csd_Button(actor, extendTabCount);
		}
		if(actor.layerName.startsWith("img")){
			
			return Csd_ImageView(actor, extendTabCount);
		}
		if(actor instanceof Pic){
			return Csd_Sprite(actor,extendTabCount);
		}
		return null;
	}
		
	public static String parsing2Csd(PsdFile psdFile) {
		String csd_data = "";
		csd_data+=Csd_Begin(psdFile.psdName.replace(".psd", ""), psdFile.width, psdFile.height);
		
		List<Element> childs = psdFile.childs;
		for (Element element : childs) {
			csd_data += classification(element,0);
		}
		
		csd_data+=Csd_End();
		return csd_data;
	}
	
	
	public static final PsdFile translate2Csd(Psd psd) {
		
		PSDUtil.updatePsdLayerPosition(psd);
		Rectangle rect = PSDUtil.getMaxSize(psd);
		PsdFile psdFile = new PsdFile();
		psdFile.width = psd.getWidth();
		psdFile.height = psd.getHeight();
		psdFile.maxWidth = rect.width;
		psdFile.maxHeight = rect.height;
		psdFile.psdName = psd.getName();
		if (Config.saveAtlas) {
			psdFile.atlas = psd.getName().replace(".psd", ".atlas");
		}
		
		// 参数
		addChildWithCsd(psd, psd, psdFile);
	
		return psdFile;
	}
	
	public static final void addChildWithCsd(Psd psd, LayersContainer container, Folder folder) {

		
		for (int i = 0; i < container.getLayersCount(); i++) {
			Layer layer = container.getLayer(i);
			Element actor = null;
			if (layer.isFolder()) { // 这是一个文件夹
				actor = new Folder();
			} else if (layer.isTextLayer()) { // 这是一个文本对象
				Text text = new Text();
				PsdText psdText = layer.getPsdText();
				text.text = psdText.value;
				text.a = psdText.a;
				text.r = psdText.r;
				text.g = psdText.g;
				text.b = psdText.b;
				text.fontSize = psdText.fontSize;
				actor = text;
			} else if (layer.getImage() != null) { // 这是一个图片
				actor = new Pic();
				((Pic) actor).textureName = psd.getName().replace(".psd", "")+"/"+getImageName(layer,true);
			}

			if (actor != null) { // 坐标
				actor.layerName = layer.getName();
				actor.isVisible = layer.isVisible();
				if (actor instanceof PsdFile) {
				} else {
					LayerBoundary boundary = LayerBoundary.getLayerBoundary(layer);
					
					if (boundary != null) {

						if(actor.layerName.contains("#full")){//当图层为 文件夹时， 有参数为 #full 则使用画布尺寸
							actor.layerName = actor.layerName.replace("#full", "");
							
							actor.offsetX = boundary.getX();
							actor.offsetY = boundary.getY();
							actor.x = 0;
							actor.y = 0;
							actor.width = psd.getWidth();
							actor.height = psd.getHeight();
						}else{
							actor.x = boundary.getX() + folder.offsetX;
							actor.y = boundary.getY() + folder.offsetY;
							actor.width = boundary.getWidth();
							actor.height = boundary.getHeight();
						}						
					} else {
						actor.x = layer.getX() + folder.offsetX;
						actor.y = layer.getY() + folder.offsetY;
						actor.width = layer.getWidth();
						actor.height = layer.getHeight();
						
					}
					
					
					
					// libgdx 坐标系 , Y 相反
					actor.y = folder.height - actor.y - actor.height;
				}
				folder.childs.add(actor);

				//
				if (actor instanceof Folder) {
					addChildWithCsd(psd, layer, (Folder) actor);
				}
			}
		}
		
	
	}
	
}
