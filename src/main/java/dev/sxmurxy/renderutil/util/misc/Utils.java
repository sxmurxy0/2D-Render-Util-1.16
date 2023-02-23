package dev.sxmurxy.renderutil.util.misc;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.platform.GlStateManager;

import dev.sxmurxy.renderutil.Wrapper;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.util.ResourceLocation;

public class Utils implements Wrapper {
	
	private static final HashMap<Integer, FloatBuffer> kernelCache = new HashMap<>();
 
 	public static FloatBuffer getKernel(int radius) {
 		FloatBuffer buffer = kernelCache.get(radius);
 		if(buffer == null) {
		 	buffer = BufferUtils.createFloatBuffer(radius);
		 	float[] kernel = new float[radius];
		 	float sigma = radius / 2.0F;
		 	float total = 0.0F;
		 	for(int i = 0; i < radius; i++) {
		 		float multiplier = i / sigma;
		 		kernel[i] = 1.0F / (Math.abs(sigma) * 2.50662827463F) * (float) Math.exp(-0.5 * multiplier * multiplier);
		 		total += i > 0 ? kernel[i] * 2 : kernel[0];
		 	}
		 	for (int i = 0; i < radius; i++) {
		 		kernel[i] /= total;
		 	}
		 	buffer.put(kernel);
		 	buffer.flip();
		 	kernelCache.put(radius, buffer);
 		}
 		return buffer;
 	}
 	
 	public static int loadTexture(BufferedImage image) throws Exception {
		int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);
        
        for (int pixel : pixels) {
            buffer.put((byte) ((pixel >> 16) & 0xFF));
            buffer.put((byte) ((pixel >> 8) & 0xFF));
            buffer.put((byte) (pixel & 0xFF));
            buffer.put((byte) ((pixel >> 24) & 0xFF));
        }
        buffer.flip();
        
		int textureID = GlStateManager._genTexture();
		GlStateManager._bindTexture(textureID);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
		GlStateManager._bindTexture(0);
		return textureID;
	}
 
	public static int getTextureId(ResourceLocation identifier) {
		Texture abstractTexture = MC.getTextureManager().getTexture(identifier);
		if(abstractTexture == null) {
			abstractTexture = new SimpleTexture(identifier);
			MC.getTextureManager().register(identifier, abstractTexture);
		}
		return abstractTexture.getId();
	}
	
	public static void initStencilReplace() {
		MC.getMainRenderTarget().enableStencil();
        GL30.glEnable(GL30.GL_STENCIL_TEST);
        GL30.glClear(GL30.GL_STENCIL_BUFFER_BIT);
        GlStateManager._stencilFunc(GL30.GL_EQUAL, 1, 1);
        GlStateManager._stencilOp(GL30.GL_REPLACE, GL30.GL_REPLACE, GL30.GL_REPLACE);
        GlStateManager._colorMask(false, false, false, false);
	}
	
	public static void uninitStencilReplace() {
		GlStateManager._colorMask(true, true, true, true);
        GlStateManager._stencilOp(GL30.GL_KEEP, GL30.GL_KEEP, GL30.GL_KEEP);
        GlStateManager._stencilFunc(GL30.GL_EQUAL, 1, 1);
	}
	
}
	