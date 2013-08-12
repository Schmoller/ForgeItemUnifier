package schmoller.unifier;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

public class Utilities
{
	private static Map<Integer, ItemData> idMap = null;
	
	/**
	 * Finds what mod owns an item
	 * @return Returns the mod container for the mod that owns it,
	 * 		or the Minecraft dummy container if its part of vanilla minecraft,
	 * 		or null if the owner cannot be determined
	 */
	public static ModContainer findOwningMod(ItemStack item)
	{
		if(idMap == null)
			idMap = getPrivateValue(GameData.class, null, "idMap");

		ItemData data = idMap.get(item.itemID);
		
		String modId = null;
		if(data != null)
			modId = data.getModId();

		if(modId == null)
			return null;

		if(modId.equals("Minecraft"))
			return Loader.instance().getMinecraftModContainer();
		
		for(ModContainer mod : Loader.instance().getActiveModList())
		{
			if(mod.getMod() == null)
				continue;
			
			if(mod.getModId().equals(modId))
				return mod;
		}
		
		return null;
	}

	/**
	 * Gets the display name of an item even before the mods have finished loading
	 */
	public static String getSafeDisplayName(ItemStack item)
	{
		if(item == null)
			return "";
		
		String name = String.format("%d:%d", item.itemID, item.getItemDamage());

		try
		{
			name = item.getItem().getLocalizedName(item);
		}
		catch(Exception e)
		{
			// Some mods throw exceptions here during init phase
		}
		
		try
		{
			if(name == null || name.isEmpty())
				name = LanguageRegistry.instance().getStringLocalization(item.getItem().getUnlocalizedName(item) + ".name");
			if(name == null || name.isEmpty())
				name = item.getItemName();
		}
		catch(Exception e)
		{
			// Some mods throw exceptions here during init phase
		}
		
		if(name == null || name.isEmpty())
			name = String.format("%d:%d", item.itemID, item.getItemDamage());
		
		return name;
	}
	
	public static <T> T getPrivateValue(Class clazz, Object instance, String fieldName)
	{
		try
		{
			Field f = clazz.getDeclaredField(fieldName);
			f.setAccessible(true);
			return (T)f.get(instance);
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e);
		}
	}
	public static <T> T getPrivateValue(Object instance, String fieldName)
	{
		assert(instance != null);
		try
		{
			Field f = instance.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			return (T)f.get(instance);
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e);
		}
	}
	
	public static void setPrivateValue(Class clazz, Object instance, Object value, String fieldName)
	{
		
		try
		{
			Field f = clazz.getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(instance, value);
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e);
		}
	}
	
	public static boolean isServer()
	{
		if(FMLCommonHandler.instance().getSide() == Side.SERVER)
			return true;
		
		return FMLCommonHandler.instance().getMinecraftServerInstance() != null;
	}
	
	public static boolean isClient()
	{
		return FMLCommonHandler.instance().getSide() == Side.CLIENT;
	}
}
