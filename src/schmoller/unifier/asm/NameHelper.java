package schmoller.unifier.asm;

public class NameHelper
{
	// TODO: Add in obfuscated names
	public static TypeName guiMainMenu = new TypeName("net.minecraft.client.gui.GuiMainMenu");
	public static TypeName guiButton = new TypeName("net.minecraft.client.gui.GuiButton");
	public static TypeName entityVillager = new TypeName("net.minecraft.entity.passive.EntityVillager");
	public static TypeName merchantRecipeList = new TypeName("net.minecraft.village.MerchantRecipeList");

	public static ObjectName initGui = new ObjectName(NameType.Method, guiMainMenu, "initGui");
	public static ObjectName actionPerformed = new ObjectName(NameType.Method, guiMainMenu, "actionPerformed");
	public static ObjectName buttonList = new ObjectName(NameType.Field, guiMainMenu, "buttonList");
	public static ObjectName width = new ObjectName(NameType.Field, guiMainMenu, "width");
	
	public static ObjectName id = new ObjectName(NameType.Field, guiButton, "id");
	
	public static ObjectName readEntityFromNBT = new ObjectName(NameType.Method, entityVillager, "readEntityFromNBT");
	public static ObjectName addDefaultEquipmentAndRecipies = new ObjectName(NameType.Method, entityVillager, "addDefaultEquipmentAndRecipies");
	public static ObjectName buyingList = new ObjectName(NameType.Field, entityVillager, "readEntityFromNBT");
	
	public enum NameType
	{
		Field,
		Method
	}
	
	public static class TypeName
	{
		private String mName;
		
		public TypeName(String name)
		{
			mName = name;
		}
		
		public String getName()
		{
			return mName;
		}
		
		public String getPath()
		{
			return mName.replaceAll("\\.", "/");
		}
		
		public String getDesc()
		{
			return "L" + mName.replaceAll("\\.", "/");
		}
	}
	
	public static class ObjectName
	{
		private String mName;
		private NameType mType;
		private TypeName mOwner;
		
		public ObjectName(NameType type, TypeName owner, String name)
		{
			mName = name;
			mType = type;
			mOwner = owner;
		}
		
		public String getName()
		{
			return mName;
		}
		
		
	}
}
