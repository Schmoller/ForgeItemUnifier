package schmoller.unifier.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiButtonUnifier extends GuiButton
{
    public GuiButtonUnifier(int id, int x, int y)
    {
        super(id, x, y, 20, 20, "");
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
    	super.drawButton(par1Minecraft, par2, par3);
        if (this.drawButton)
        {
        	Icon ico = Item.ingotIron.getIconFromDamage(0);
        	
        	par1Minecraft.getTextureManager().bindTexture(GuiButton.buttonTextures);

        	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	par1Minecraft.getTextureManager().bindTexture(par1Minecraft.getTextureManager().getResourceLocation(Item.ingotIron.getSpriteNumber()));
            
            this.drawTexturedModelRectFromIcon(this.xPosition + 2, this.yPosition + 2, ico, 16, 16);
        }
    }
}
