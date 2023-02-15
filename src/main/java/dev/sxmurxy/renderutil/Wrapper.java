package dev.sxmurxy.renderutil;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;

public interface Wrapper {
		
	Minecraft MC = Minecraft.getInstance();
	MainWindow WINDOW = MC.getWindow();
	
}
