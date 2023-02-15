package dev.sxmurxy.renderutil;

import java.awt.Color;

import dev.sxmurxy.renderutil.util.render.BlurHelper;
import dev.sxmurxy.renderutil.util.render.DrawHelper;
import dev.sxmurxy.renderutil.util.render.DrawHelper.Part;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(RenderUtilMod.MOD_ID)
public class RenderUtilMod implements Wrapper {
	
	public static final String MOD_ID = "renderutil";

    public RenderUtilMod() {
    	MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
    	if(event.getType() == ElementType.ALL) {
    		DrawHelper.drawCircle(70, 70, 25, Color.CYAN);
    		DrawHelper.drawCirclePart(135, 70, 25, Part.THIRD_QUARTER, Color.WHITE);
    		DrawHelper.drawCirclePart(137, 70, 25, Part.FIRST_QUARTER, Color.WHITE);
    		DrawHelper.drawBlurredCircle(200, 70, 20, 5, Color.MAGENTA);
    		DrawHelper.drawCircleOutline(255, 70, 16, 4f, Color.BLUE);
    		DrawHelper.drawCircleOutline(300, 70, 16, 4f, 60, 1, Color.WHITE);
    		DrawHelper.drawRainbowCircle(360, 70, 35, 3f);
    		
    		DrawHelper.drawRect(50, 200, 70, 40, Color.GRAY);
    		DrawHelper.drawRoundedRect(140, 200, 70, 40, 7, Color.CYAN);
    		DrawHelper.drawRoundedGradientRect(230, 200, 70, 40, 7, Color.BLUE, Color.CYAN, Color.BLUE, Color.CYAN);
    		DrawHelper.drawRoundedBlurredRect(320, 210, 80, 60, 7, 10, Color.BLACK);
    		DrawHelper.drawRoundedRectOutline(420, 210, 70, 55, 6, 2, Color.WHITE);
    		DrawHelper.drawGlow(520, 190, 20, 20, 18, Color.CYAN);
    		
    		BlurHelper.drawBlur(50, 430, 200, 200, 10);
    		BlurHelper.drawRoundedBlur(300, 380, 150, 70, 10, 10);
    	}
    }
    
}
