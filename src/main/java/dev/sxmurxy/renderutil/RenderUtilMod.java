package dev.sxmurxy.renderutil;

import java.awt.Color;

import dev.sxmurxy.renderutil.util.render.BloomHelper;
import dev.sxmurxy.renderutil.util.render.BlurHelper;
import dev.sxmurxy.renderutil.util.render.DrawHelper;
import dev.sxmurxy.renderutil.util.render.DrawHelper.Part;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RenderUtilMod.MOD_ID)
public class RenderUtilMod implements Wrapper {
	
	public static final String MOD_ID = "renderutil";

    public RenderUtilMod() {
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }
    
    private void setup(FMLCommonSetupEvent event) {
    	MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
    	if(event.getType() == ElementType.CROSSHAIRS) {
    		
    		DrawHelper.drawCircle(70, 70, 25, Color.CYAN);
    		DrawHelper.drawCirclePart(135, 70, 25, Part.THIRD_QUARTER, Color.WHITE);
    		DrawHelper.drawCirclePart(137, 70, 25, Part.FIRST_QUARTER, Color.WHITE);
    		DrawHelper.drawBlurredCircle(200, 70, 20, 5, Color.MAGENTA);
    		DrawHelper.drawCircleOutline(255, 70, 16, 4f, Color.BLUE);
    		DrawHelper.drawCircleOutline(300, 70, 16, 4f, 60, 1, Color.WHITE);
    		DrawHelper.drawRainbowCircle(360, 70, 35, 4f);
    		
    		DrawHelper.drawRect(50, 200, 70, 40, Color.GRAY);
    		DrawHelper.drawRoundedRect(140, 200, 70, 40, 7, Color.CYAN);
    		DrawHelper.drawRoundedGradientRect(230, 200, 70, 40, 7, Color.BLUE, Color.CYAN, Color.BLUE, Color.CYAN);
    		DrawHelper.drawRoundedBlurredRect(320, 210, 80, 60, 7, 10, Color.BLACK);
    		DrawHelper.drawRoundedGradientBlurredRect(520, 200, 90, 40, 8, 7, Color.CYAN, Color.CYAN, Color.WHITE, Color.WHITE);
    		DrawHelper.drawRoundedRectOutline(420, 210, 70, 55, 6, 2, Color.WHITE);
    		DrawHelper.drawGlow(740, 190, 50, 20, 12, Color.CYAN);
    		
    		BloomHelper.registerRenderCall(() -> {
    			DrawHelper.drawRoundedRect(640, 200, 70, 40, 7, Color.GREEN);
    			DrawHelper.drawCircle(675, 120, 20, Color.YELLOW);
    		});
    		BloomHelper.draw(11);
    		
    		DrawHelper.drawRoundedTexture(new ResourceLocation("textures/entity/steve.png"), 60, 280, 30, 30, 8, 8, 8, 8, 3);
    		DrawHelper.drawRoundedTexture(new ResourceLocation("textures/block/stone.png"), 110, 280, 30, 30, 5);

    		BlurHelper.registerRenderCall(() -> {
    			DrawHelper.drawRoundedRect(190, 370, 150, 140, 5, Color.WHITE);
    			DrawHelper.drawRect(370, 350, 100, 120, Color.WHITE);
    			
    		});
    		BlurHelper.draw(8);
    		 
    	}
    }
    
}
