package psd.reflect;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import psd.framework.PsdReflectUtil;

/**
 * PSD ��ͼƬ����
 * 
 * @author roy
 */
public class PsdImage extends Image {
	// �Զ����쵽 PSD�趨��С ȫ��
	private static boolean defAutoScale = false;
	// �Զ����쵽 PSD�趨��С
	protected boolean autoScale = defAutoScale;
	// ͼƬԴ
	protected final psd.Pic psdPic;

	public PsdImage(psd.Pic pic) {
		this(pic, null);
	}

	public PsdImage(psd.Pic pic, AssetManager assetManager) {
		this(null, pic, assetManager);
	}

	public PsdImage(psd.PsdFile psdFile, psd.Pic pic) {
		this(psdFile, pic, null);
	}

	public PsdImage(psd.PsdFile psdFile, psd.Pic pic, AssetManager assetManager) {
		super(getTexture(psdFile, pic, assetManager, defAutoScale));
		this.psdPic = pic;
	}

	//
	public psd.Pic getPsdPic() {
		return psdPic;
	}

	// ��ȡͼƬ
	protected static final TextureRegion getTexture(psd.PsdFile psdFile, psd.Pic pic,
			AssetManager assetManager, boolean scaleToPicSize) {
		if (assetManager == null) {
			assetManager = PsdGroup.getAssetManager();
		}
		TextureRegion region = null;

		if (psdFile == null || psdFile.atlas == null) {
			Texture texture = PsdReflectUtil.load(assetManager, pic.textureName, Texture.class);
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			region = new TextureRegion(texture);
		} else {
			TextureAtlas textureAtlas = PsdReflectUtil.load(assetManager, psdFile.getAtlasPath(),
					TextureAtlas.class);
			AtlasRegion atlasRegion = textureAtlas.findRegion(pic.textureName);
			if (atlasRegion != null) {
				atlasRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
				region = new TextureRegion(atlasRegion);
			}
		}

		// ����ͼƬ
		if (region != null && scaleToPicSize && pic.width > 0 && pic.height > 0) {
			float scaleX = region.getRegionWidth() / pic.width;
			float scaleY = region.getRegionHeight() / pic.height;
			Sprite sprite = new Sprite(region);
			sprite.setScale(scaleX, scaleY);
			//
			region = sprite;
		}

		return region;
	}

	// �Զ����쵽 PSD�趨��С ȫ��
	public static void setDefAutoScale(boolean defAutoScale) {
		PsdImage.defAutoScale = defAutoScale;
	}

	// �Զ����쵽 PSD�趨��С
	public void setAutoScale(boolean autoScale) {
		this.autoScale = autoScale;
	}
}
