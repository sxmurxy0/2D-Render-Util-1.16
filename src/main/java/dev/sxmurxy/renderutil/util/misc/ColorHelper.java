package dev.sxmurxy.renderutil.util.misc;

import java.awt.Color;

public class ColorHelper {
	
	// alpha [0, 255]
	public static Color injectAlpha(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}
	
	// alpha [0, 1]
	public static Color injectAlpha(Color color, float alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255.0f));
	}
	
	public static Color getColor(int color) {
		int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        int a = color >> 24 & 0xFF;
		return new Color(r, g, b, a);
	}
	
	public static float[] getColorComps(Color color) {
		return new float[] {color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f};
	}
    
}
