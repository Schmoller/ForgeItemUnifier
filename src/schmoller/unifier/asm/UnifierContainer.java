package schmoller.unifier.asm;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;

public class UnifierContainer extends DummyModContainer
{
	public UnifierContainer()
	{
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "ForgeUnifierCore";
		meta.name = "Forge Unifier Core";
		meta.version = "0.1";
		meta.parent = "ForgeUnifier";
	}
}
