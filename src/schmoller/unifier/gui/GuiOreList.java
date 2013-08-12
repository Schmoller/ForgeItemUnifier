package schmoller.unifier.gui;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.common.ModContainer;

import schmoller.unifier.Mappings;
import schmoller.unifier.OreCategory;
import schmoller.unifier.Utilities;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.OreDictionary;

public class GuiOreList extends GuiScreen
{
	private OreCategory mCategory;
	private Mappings mMappings;
	private ArrayList<Entry<String, Set<ItemStack>>> mOreDict;
	private GuiUnifierSettings mParent;
	
	private int x;
	private int y;
	
	private float scaleX;
	private float scaleY;
	
	private int mScrollOffset = 0;
	private float mInitialMouseClickY;
	private boolean mClickedScrollbar;
	
	private int mSelected = -1;
	private long mLastClickTime = 0;
	
	public GuiOreList( GuiUnifierSettings parent, int left, int top, int width, int height, Mappings mappings, OreCategory category)
	{
		this.width = width;
		this.height = height;
		
		this.x = left;
		this.y = top;
		
		mc = parent.getMC();
		fontRenderer = parent.getFontRenderer();
		
		mParent = parent;
		
		mCategory = category;
		mMappings = mappings;
		
		mOreDict = new ArrayList<Entry<String,Set<ItemStack>>>();
		Set<String> oreNames = mappings.getMappableOres(category);
		for(String oreName : oreNames)
		{
			List<ItemStack> ores = OreDictionary.getOres(oreName);
			
			if(ores.size() > 1)
			{
				HashSet<ItemStack> items = new HashSet<ItemStack>();
				items.addAll(ores);
				mOreDict.add(new AbstractMap.SimpleEntry(oreName, items));
			}
		}
		
		ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		
		scaleX = mc.displayWidth / (float)res.getScaledWidth();
		scaleY = mc.displayHeight / (float)res.getScaledHeight();
	}

	protected int getSize()
	{
		return mOreDict.size();
	}

	protected void elementClicked( int index, boolean doubleClick )
	{
		// TODO Auto-generated method stub

	}

	protected void drawBackground()
	{
		
	}
	
	protected int getElementSize(int index)
	{
		return 35;
	}
	
	public Entry<String, Set<ItemStack>> getSelected()
	{
		if(mSelected == -1)
			return null;
		
		return mOreDict.get(mSelected);
	}

	
	protected void drawSlot( int index, int x, int y, Tessellator tes )
	{
		Entry<String, Set<ItemStack>> entry = mOreDict.get(index);
		
		fontRenderer.drawString(fontRenderer.trimStringToWidth(entry.getKey(), width), this.x + 26, y + 9, 0xFFFFFF);
		fontRenderer.drawString("Available: " + entry.getValue().size(), this.x + 26, y + 10 + fontRenderer.FONT_HEIGHT, 0xAAAAAA);
		
		ItemStack item = mMappings.getMapping(entry.getKey());
		
		if(item != null)
			mParent.drawItemStack(item, x + 6, y + 9);
		else
			// TODO: Render unknown symbol here
			mParent.drawItemStack(new ItemStack(Block.bedrock), x + 6, y + 9);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
	}
	
	protected void slotHover(int index, int x, int y, int mouseX, int mouseY, Tessellator tes)
	{
		Entry<String, Set<ItemStack>> entry = mOreDict.get(index);
		ItemStack item = mMappings.getMapping(entry.getKey());
		
		if (mouseX >= 6 && mouseX < 6 + 18 && mouseY >= 9 && mouseY < 9 + 18)
		{
			List list;
			
			if(item != null)
			{
				list = item.getTooltip(this.mc.thePlayer, false);
				for (int k = 0; k < list.size(); ++k)
		        {
		            if (k == 0)
		                list.set(k, "\u00a7" + Integer.toHexString(item.getRarity().rarityColor) + (String)list.get(k));
		            else
		                list.set(k, EnumChatFormatting.GRAY + (String)list.get(k));
		        }
				
				ModContainer container = Utilities.findOwningMod(item);
				
				if(container == null)
					list.add(EnumChatFormatting.YELLOW + "Unknown");
				else
					list.add(EnumChatFormatting.YELLOW + container.getName());
			}
			else
			{
				list = new ArrayList<String>();
				list.add(EnumChatFormatting.YELLOW + "Not Mapped");
			}
			
			mParent.drawHoveringText(list, x + mouseX, y + mouseY, fontRenderer);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
	{
		Tessellator tes = Tessellator.instance;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		int bottom = (int)((y + height) * scaleY);
		bottom = mc.displayHeight - bottom;
		
		GL11.glScissor((int)(x * scaleX), bottom, (int)(width * scaleX), (int)(height * scaleY));
		int length = 0;
		for(int i = 0; i < getSize(); ++i)
		{
			int slotHeight = getElementSize(i);
			int yOffset = y + length - mScrollOffset;
			if(i == mSelected)
			{
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                
//                tes.startDrawingQuads();
//                tes.setColorOpaque_I(8421504);
//                tes.addVertexWithUV((double)x, (double)(yOffset + slotHeight), 0.0D, 0.0D, 1.0D);
//                tes.addVertexWithUV((double)x + width - 8, (double)(yOffset + slotHeight), 0.0D, 1.0D, 1.0D);
//                tes.addVertexWithUV((double)x + width - 8, (double)(yOffset), 0.0D, 1.0D, 0.0D);
//                tes.addVertexWithUV((double)x, (double)(yOffset), 0.0D, 0.0D, 0.0D);
//                tes.draw();
                tes.startDrawingQuads();
                tes.setColorRGBA_I(0, 128);
                tes.addVertexWithUV((double)(x), (double)(yOffset + slotHeight), 0.0D, 0.0D, 1.0D);
                tes.addVertexWithUV((double)(x + width - 8), (double)(yOffset + slotHeight), 0.0D, 1.0D, 1.0D);
                tes.addVertexWithUV((double)(x + width - 8), (double)(yOffset), 0.0D, 1.0D, 0.0D);
                tes.addVertexWithUV((double)(x), (double)(yOffset), 0.0D, 0.0D, 0.0D);
                tes.draw();
                
                drawRect(x, yOffset, x + width - 8, yOffset + 1, 0xff2222aa);
                drawRect(x, yOffset + slotHeight - 1, x + width - 8, yOffset + slotHeight, 0xff2222aa);
                drawRect(x, yOffset, x + 1, yOffset + slotHeight, 0xff2222aa);
                drawRect(x+ width - 9, yOffset, x + width - 8, yOffset + slotHeight, 0xff2222aa);
                
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			
			drawSlot(i, x, yOffset, tes);
			
			length += slotHeight;
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		int contentHeight = getContentHeight();
		int scrollSize = Math.max(contentHeight - height, 0);
		
		
		int scrollHandleSize = (int)((1 - Math.min(scrollSize / 300f,1)) * (height - 20)) + 20;
		int scrollTop = (int)((height - scrollHandleSize) * ((float)mScrollOffset / scrollSize)) + y;
		

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		this.zLevel = 400.0F;
		
		tes.startDrawingQuads();
        tes.setColorRGBA_I(0, 255);
        tes.addVertexWithUV((double)(x + width) - 8, (double)y + height, zLevel, 0.0D, 1.0D);
        tes.addVertexWithUV((double)(x + width), (double)y + height, zLevel, 1.0D, 1.0D);
        tes.addVertexWithUV((double)(x + width), (double)y, zLevel, 1.0D, 0.0D);
        tes.addVertexWithUV((double)(x + width) - 8, (double)y, zLevel, 0.0D, 0.0D);
        tes.draw();
        
        tes.startDrawingQuads();
        tes.setColorRGBA_I(8421504, 255);
        tes.addVertexWithUV((double)(x + width) - 8, (double)(scrollTop + scrollHandleSize), zLevel, 0.0D, 1.0D);
        tes.addVertexWithUV((double)(x + width), (double)(scrollTop + scrollHandleSize), zLevel, 1.0D, 1.0D);
        tes.addVertexWithUV((double)(x + width), (double)scrollTop, zLevel, 1.0D, 0.0D);
        tes.addVertexWithUV((double)(x + width) - 8, (double)scrollTop, zLevel, 0.0D, 0.0D);
        tes.draw();
        tes.startDrawingQuads();
        tes.setColorRGBA_I(12632256, 255);
        tes.addVertexWithUV((double)(x + width) - 8, (double)(scrollTop + scrollHandleSize - 1), zLevel, 0.0D, 1.0D);
        tes.addVertexWithUV((double)((x + width) - 1), (double)(scrollTop + scrollHandleSize - 1), zLevel, 1.0D, 1.0D);
        tes.addVertexWithUV((double)((x + width) - 1), (double)scrollTop, zLevel, 1.0D, 0.0D);
        tes.addVertexWithUV((double)(x + width) - 8, (double)scrollTop, zLevel, 0.0D, 0.0D);
        tes.draw();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		// Do scrolling
		if (Mouse.isButtonDown(0))
        {
            if (mInitialMouseClickY == -1.0F)
            {
                boolean var7 = true;
                mClickedScrollbar = false;

                if (mouseY >= y && mouseY < y + height)
                {
                	int relY = mouseY - y + mScrollOffset;
                	
                    //var11 = var10 / this.slotHeight;

                    
//                        boolean var12 = var11 == this.selectedIndex && System.currentTimeMillis() - this.lastClickTime < 250L;
//                        this.elementClicked(var11, var12);
//                        this.selectedIndex = var11;
//                        this.lastClickTime = System.currentTimeMillis();
//                    else if (mouseX >= x && mouseX < x + width && var10 < 0)
//                    {
//                        this.func_27255_a(mouseX - boxLeft, mouseY - this.top + (int)this.scrollDistance - 4);
//                        var7 = false;
//                    }

                    if (mouseX >= x + width - 8 && mouseX < x + width)
                    {
                    	mClickedScrollbar = true;
                    	mInitialMouseClickY = mouseY;
                    }
                    else if (mouseX >= x && mouseX < x + width && relY >= 0 && relY < contentHeight)
                    {
                    	int index = 0;
                    	int h = 0;
                    	for(index = 0; index < getSize(); ++index)
                    	{
                    		int hh = getElementSize(index);
                    		if(mouseY - y + mScrollOffset >= h && mouseY - y + mScrollOffset < h + hh)
                    			break;
                    		
                    		h += hh;
                    	}
                    	
                    	boolean doubleClick = (index == mSelected && System.currentTimeMillis() - mLastClickTime < 250L); 
                    	elementClicked(index, doubleClick);
                    	mSelected = index;
                    	mLastClickTime = System.currentTimeMillis();
                    	mInitialMouseClickY = (float)mouseY;
                    }
                }
                else
                {
                    mInitialMouseClickY = -2.0F;
                }
            }
            else if (mInitialMouseClickY >= 0.0F)
            {
            	if(mClickedScrollbar)
            	{
	            	float per = Math.min(Math.max((mouseY - y) / (float)height, 0),1);
	            	mScrollOffset = (int)(per * scrollSize);
            	}
            	else
            	{
            		
            	}
            }
        }
        else
        {
            while (Mouse.next())
            {
                int dir = Mouse.getEventDWheel();

                if (dir != 0)
                {
                    if (dir > 0)
                    	dir = -1;
                    else if (dir < 0)
                    	dir = 1;

                    mScrollOffset += dir * 20;
                    mScrollOffset = Math.max(Math.min(mScrollOffset, scrollSize), 0);
                }
            }

            mInitialMouseClickY = -1.0F;
            
            int relY = mouseY - y + mScrollOffset;
    		if (mouseX >= x && mouseX < x + width && relY >= 0 && relY < contentHeight)
            {
            	int index = 0;
            	int h = 0;
            	for(index = 0; index < getSize(); ++index)
            	{
            		int hh = getElementSize(index);
            		if(mouseY - y + mScrollOffset >= h && mouseY - y + mScrollOffset < h + hh)
            			break;
            		
            		h += hh;
            	}
            	
            	slotHover(index, x, y - mScrollOffset + h, mouseX - x, mouseY - y + mScrollOffset - h, tes);
            }
        }
		
		
	}
	
	private int getContentHeight()
	{
		int height = 0;
		for(int i = 0; i < getSize(); ++i)
			height += getElementSize(i);
		
		return height;
	}
}

