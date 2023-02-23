package dev.sxmurxy.renderutil.util.render;

import static org.lwjgl.opengl.GL11.GL_QUADS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.platform.GlStateManager;

import dev.sxmurxy.renderutil.RenderUtilMod;
import dev.sxmurxy.renderutil.Wrapper;

public class Shader implements Wrapper {
	
	public static final int VERTEX_SHADER;
	private int programId;
	
	static {
		VERTEX_SHADER = GlStateManager.glCreateShader(GL30.GL_VERTEX_SHADER);
		GlStateManager.glShaderSource(VERTEX_SHADER, getShaderSource("vertex.vert"));
		GlStateManager.glCompileShader(VERTEX_SHADER);
	}
	
	public Shader(String fragmentShaderName) {
		int programId = GlStateManager.glCreateProgram();
		try {
			int fragmentShader = GlStateManager.glCreateShader(GL30.GL_FRAGMENT_SHADER);
			GlStateManager.glShaderSource(fragmentShader, getShaderSource(fragmentShaderName));
			GlStateManager.glCompileShader(fragmentShader);
			
			int isFragmentCompiled = GL30.glGetShaderi(fragmentShader, GL30.GL_COMPILE_STATUS);
			if(isFragmentCompiled == 0) {
				GlStateManager.glDeleteShader(fragmentShader);
				System.err.println("Fragment shader couldn't compile. It has been deleted.");
			}
			
			GlStateManager.glAttachShader(programId, VERTEX_SHADER);
			GlStateManager.glAttachShader(programId, fragmentShader);
			GlStateManager.glLinkProgram(programId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.programId = programId;
	}
	
	public void load() {
		GlStateManager._glUseProgram(programId);
	}
	
	public void unload() {
		GlStateManager._glUseProgram(0);
	}
	
	public int getUniform(String name) {
		return GL30.glGetUniformLocation(programId, name);
	}
	
	public void setUniformf(String name, float... args) {
        int loc = GL30.glGetUniformLocation(programId, name);
        switch (args.length) {
            case 1:
                GL30.glUniform1f(loc, args[0]);
                break;
            case 2:
            	 GL30.glUniform2f(loc, args[0], args[1]);
                break;
            case 3:
            	 GL30.glUniform3f(loc, args[0], args[1], args[2]);
                break;
            case 4:
            	 GL30.glUniform4f(loc, args[0], args[1], args[2], args[3]);
                break;
        }
    }

    public void setUniformi(String name, int... args) {
    	int loc = GL30.glGetUniformLocation(programId, name);
        switch (args.length) {
            case 1:
                GL30.glUniform1i(loc, args[0]);
                break;
            case 2:
            	 GL30.glUniform2i(loc, args[0], args[1]);
                break;
            case 3:
            	 GL30.glUniform3i(loc, args[0], args[1], args[2]);
                break;
            case 4:
            	 GL30.glUniform4i(loc, args[0], args[1], args[2], args[3]);
                break;
        }
    }
    
    public void setUniformfb(String name, FloatBuffer buffer) {
    	GL30.glUniform1fv(GL30.glGetUniformLocation(programId, name), buffer);
    }
    
    public static void draw() {
		draw(0, 0, WINDOW.getGuiScaledWidth(), WINDOW.getGuiScaledHeight());
	}
    
	public static void draw(double x, double y, double width, double height) {
		GL30.glBegin(GL_QUADS);
		GL30.glTexCoord2d(0, 0);
		GL30.glVertex2d(x, y);
		GL30.glTexCoord2d(0, 1);
		GL30.glVertex2d(x, y + height);
		GL30.glTexCoord2d(1, 1);
		GL30.glVertex2d(x + width, y + height);
		GL30.glTexCoord2d(1, 0);
		GL30.glVertex2d(x + width, y);
		GL30.glEnd();
	}
	
	public static String getShaderSource(String fileName) {
		String source = "";
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(RenderUtilMod.class
				.getResourceAsStream("/assets/" + RenderUtilMod.MOD_ID + "/shaders/" + fileName)));
		source = bufferedReader.lines().filter(str -> !str.isEmpty()).map(str -> str = str.replace("\t", "")).collect(Collectors.joining("\n"));
		try {
			bufferedReader.close();
		} catch (IOException ignored) {
			
		}
		return source;
	}
	
}
