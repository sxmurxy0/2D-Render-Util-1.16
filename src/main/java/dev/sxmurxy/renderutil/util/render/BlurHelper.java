package dev.sxmurxy.renderutil.util.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.platform.GlStateManager;

import dev.sxmurxy.renderutil.Wrapper;
import net.minecraft.client.shader.Framebuffer;

public class BlurHelper implements Wrapper {

    private static final Shader BLUR_SHADER = new Shader("gaussian_blur.frag");
    private static final HashMap<Integer, FloatBuffer> weightCache = new HashMap<>();
    private static final Framebuffer frameBuffer = new Framebuffer(1, 1, true, true);

    private static void setupUniforms(float dir1, float dir2, int radius) {
        BLUR_SHADER.setUniformi("textureIn", 0);
        BLUR_SHADER.setUniformf("texelSize", 1.0F / (float)WINDOW.getGuiScaledWidth(), 1.0F / (float)WINDOW.getGuiScaledHeight());
        BLUR_SHADER.setUniformf("direction", dir1, dir2);
        BLUR_SHADER.setUniformf("radius", radius);
      
        FloatBuffer weightBuffer = weightCache.get(radius);
        if(weightBuffer == null) {
        	weightBuffer = BufferUtils.createFloatBuffer(256);
	        for (int i = 0; i <= radius; i++) {
	            weightBuffer.put(calculateGaussianValue(i, radius / 2f));
	        }
	        weightBuffer.rewind();
	        weightCache.put(radius, weightBuffer);
        }
        GL30.glUniform1fv(BLUR_SHADER.getUniform("weights"), weightBuffer);
    }
    
    public static Framebuffer setupBuffer(Framebuffer frameBuffer) {
        if(frameBuffer.width != WINDOW.getGuiScaledWidth() || frameBuffer.height != WINDOW.getGuiScaledHeight()) {
    		frameBuffer.destroyBuffers();
        	frameBuffer.createBuffers(WINDOW.getGuiScaledWidth() , WINDOW.getGuiScaledHeight(), true);
        }
        return frameBuffer;
    }
	
	public static float calculateGaussianValue(float x, float sigma) {
        double output = 1.0 / Math.sqrt(2.0 * Math.PI * (sigma * sigma));
        return (float)(output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
	}
    
    public static void drawBlur(int radius) {
    	GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
       
        setupBuffer(frameBuffer);
        frameBuffer.clear(true);
		
		frameBuffer.bindWrite(false);
		BLUR_SHADER.load();
		setupUniforms(0, 1, radius);
		GlStateManager._bindTexture(MC.getMainRenderTarget().getColorTextureId());
		Shader.draw();
		BLUR_SHADER.unload();
		frameBuffer.unbindWrite();

		MC.getMainRenderTarget().bindWrite(true);
		BLUR_SHADER.load();
		setupUniforms(1, 0, radius);
		GlStateManager._bindTexture(frameBuffer.getColorTextureId());
		Shader.draw();
		BLUR_SHADER.unload();
		
		GlStateManager._bindTexture(0);
		GlStateManager._disableBlend();
    }
    
    public static void drawBlur(double x, double y, double width, double height, int radius) {
    	GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        
    	MC.getMainRenderTarget().bindWrite(true);
    	MC.getMainRenderTarget().enableStencil();
    	
        GL30.glEnable(GL30.GL_STENCIL_TEST);
        GL30.glClear(GL30.GL_STENCIL_BUFFER_BIT);
        GlStateManager._stencilFunc(GL30.GL_EQUAL, 1, 1);
        GlStateManager._stencilOp(GL30.GL_REPLACE, GL30.GL_REPLACE, GL30.GL_REPLACE);
        GlStateManager._colorMask(false, false, false, false);
        
        DrawHelper.drawRect(x, y, width, height, Color.WHITE);
        
        GlStateManager._colorMask(true, true, true, true);
        GlStateManager._stencilFunc(GL30.GL_EQUAL, 1, 1);
        GlStateManager._stencilOp(GL30.GL_KEEP, GL30.GL_KEEP, GL30.GL_KEEP);
        
    	MC.getMainRenderTarget().unbindWrite();
        
    	setupBuffer(frameBuffer);
        frameBuffer.clear(true);
		
		frameBuffer.bindWrite(false);
		BLUR_SHADER.load();
		setupUniforms(0, 1, radius);
		GlStateManager._bindTexture(MC.getMainRenderTarget().getColorTextureId());
		Shader.draw();
		BLUR_SHADER.unload();
		frameBuffer.unbindWrite();
		
		MC.getMainRenderTarget().bindWrite(true);
		BLUR_SHADER.load();
		setupUniforms(1, 0, radius);
		GlStateManager._bindTexture(frameBuffer.getColorTextureId());
		Shader.draw();
		BLUR_SHADER.unload();
		
		GlStateManager._bindTexture(0);
		GL30.glDisable(GL30.GL_STENCIL_TEST);
		GlStateManager._disableBlend();
    }
    
    public static void drawRoundedBlur(double x, double y, double width, double height, double rRadius, int bRadius) {
    	GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        GL30.glEnable(GL30.GL_STENCIL_TEST);
        GL30.glClear(GL30.GL_STENCIL_BUFFER_BIT);
        GL30.glEnable(GL30.GL_ALPHA_TEST);
        
    	MC.getMainRenderTarget().bindWrite(true);
    	MC.getMainRenderTarget().enableStencil();
    	
        GlStateManager._stencilFunc(GL30.GL_EQUAL, 1, 1);
        GlStateManager._stencilOp(GL30.GL_REPLACE, GL30.GL_REPLACE, GL30.GL_REPLACE);
        GlStateManager._colorMask(false, false, false, false);
        
        DrawHelper.drawRoundedRect(x, y, width, height, rRadius, Color.WHITE);
        
        GlStateManager._colorMask(true, true, true, true);
        GlStateManager._stencilFunc(GL30.GL_EQUAL, 1, 1);
        GlStateManager._stencilOp(GL30.GL_KEEP, GL30.GL_KEEP, GL30.GL_KEEP);
        
    	MC.getMainRenderTarget().unbindWrite();
        
    	setupBuffer(frameBuffer);
        frameBuffer.clear(true);
		
		frameBuffer.bindWrite(false);
		BLUR_SHADER.load();
		setupUniforms(0, 1, bRadius);
		GlStateManager._bindTexture(MC.getMainRenderTarget().getColorTextureId());
		Shader.draw();
		BLUR_SHADER.unload();
		frameBuffer.unbindWrite();
		
		MC.getMainRenderTarget().bindWrite(true);
		BLUR_SHADER.load();
		setupUniforms(1, 0, bRadius);
		GlStateManager._bindTexture(frameBuffer.getColorTextureId());
		Shader.draw();
		BLUR_SHADER.unload();
		
		GlStateManager._bindTexture(0);
		GL30.glDisable(GL30.GL_ALPHA_TEST);
		GL30.glDisable(GL30.GL_STENCIL_TEST);
		GlStateManager._disableBlend();
    }

}
