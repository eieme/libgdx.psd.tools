package psd.reflect;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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
		super(getTexture(psdFile, pic, assetManager));
		this.psdPic = pic;
	}

	//
	public psd.Pic getPsdPic() {
		return psdPic;
	}

 

	// ��ȡͼƬ
	protected static final TextureRegion getTexture(psd.PsdFile psdFile, psd.Pic pic,
			AssetManager assetManager) {
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

		return region;
	}
}
