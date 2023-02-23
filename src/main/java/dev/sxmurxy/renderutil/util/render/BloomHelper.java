package dev.sxmurxy.renderutil.util.render;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.opengl.GL30;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;

import dev.sxmurxy.renderutil.Wrapper;
import dev.sxmurxy.renderutil.util.misc.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;

public class BloomHelper implements Wrapper {

	private static final Shader bloom = new Shader("bloom.frag");
	private static final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();
	private static final Framebuffer inFrameBuffer = new Framebuffer(WINDOW.getWidth(), WINDOW.getHeight(), true, Minecraft.ON_OSX);
    private static final Framebuffer outFrameBuffer = new Framebuffer(WINDOW.getWidth(), WINDOW.getHeight(), true, Minecraft.ON_OSX);
    
    public static void registerRenderCall(IRenderCall rc) {
    	renderQueue.add(rc);
    }
    
    public static void draw(int radius) {
    	if(renderQueue.isEmpty())
			return;
    	
    	setupBuffer(inFrameBuffer);
    	setupBuffer(outFrameBuffer);
    	
    	inFrameBuffer.bindWrite(true);
    	
    	while(!renderQueue.isEmpty()) {
    		renderQueue.poll().execute();
    	}
    	
    	outFrameBuffer.bindWrite(true);
    	
    	bloom.load();
    	bloom.setUniformf("radius", radius);
    	bloom.setUniformi("sampler1", 0);
    	bloom.setUniformi("sampler2", 20);
    	bloom.setUniformfb("kernel", Utils.getKernel(radius));
    	bloom.setUniformf("texelSize", 1.0F / (float)WINDOW.getWidth(), 1.0F / (float)WINDOW.getHeight());
    	bloom.setUniformf("direction", 2.0F, 0.0F);
    	
    	GlStateManager._enableBlend();
    	GlStateManager._blendFunc(GL30.GL_ONE, GL30.GL_SRC_ALPHA);
    	GL30.glAlphaFunc(GL30.GL_GREATER, 0.0001f);
    	
	    inFrameBuffer.bindRead();
	    Shader.draw();
    	
	    MC.getMainRenderTarget().bindWrite(false);
	    GlStateManager._blendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
	    
	    bloom.setUniformf("direction", 0.0F, 2.0F);
	    
	    outFrameBuffer.bindRead();
	    GL30.glActiveTexture(GL30.GL_TEXTURE20);
	    inFrameBuffer.bindRead();
	    GL30.glActiveTexture(GL30.GL_TEXTURE0);
	    Shader.draw();
	    
	    bloom.unload();
	    inFrameBuffer.unbindRead();
	    GlStateManager._disableBlend();
    }
    
    private static Framebuffer setupBuffer(Framebuffer frameBuffer) {
		if(frameBuffer.width != WINDOW.getWidth() || frameBuffer.height != WINDOW.getHeight()) 
			frameBuffer.resize(WINDOW.getWidth(), WINDOW.getHeight(), Minecraft.ON_OSX);
		else 
			frameBuffer.clear(Minecraft.ON_OSX);
		frameBuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		return frameBuffer;
	}
    
}
