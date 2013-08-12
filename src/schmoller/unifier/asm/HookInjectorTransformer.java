package schmoller.unifier.asm;

import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import org.objectweb.asm.Opcodes;

import cpw.mods.fml.relauncher.IClassTransformer;

public class HookInjectorTransformer implements IClassTransformer
{
	@Override
	public byte[] transform( String name, String transformedName, byte[] bytes )
	{
		// Do a test injection
		if(name.equals("net.minecraft.client.gui.GuiMainMenu"))
		{
			ClassNode node = ASMUtilities.getClassNode(bytes);
			MethodNode method = ASMUtilities.getMethodNode(node, "initGui");
			InsnList list = new InsnList();
	        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "schmoller/unifier/UnifierHooks", "testHook", "()V"));
	        method.instructions.insert(method.instructions.get(1), list);

			return ASMUtilities.getBytes(node);
		}
		else if(name.equals("net.minecraft.entity.passive.EntityVillager"))
		{
			ClassNode node = ASMUtilities.getClassNode(bytes);
			MethodNode readFromNBTMethod = ASMUtilities.getMethodNode(node, "readEntityFromNBT");
			MethodNode addDefEquipMethod = ASMUtilities.getMethodNode(node, "addDefaultEquipmentAndRecipies");
			
			InsnList list = new InsnList();
			list.add(new VarInsnNode(Opcodes.ALOAD, 0));
			list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityVillager", "buyingList", "Lnet/minecraft/village/MerchantRecipeList;"));
			list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "schmoller/unifier/UnifierHooks", "onMerchantLoad", "(Lnet/minecraft/village/MerchantRecipeList;)V"));
			
			readFromNBTMethod.instructions.insertBefore(findInjectionPointVillagerREFNBT(readFromNBTMethod), list);
			
			list = new InsnList();
			list.add(new VarInsnNode(Opcodes.ALOAD, 0));
			list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityVillager", "buyingList", "Lnet/minecraft/village/MerchantRecipeList;"));
			list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "schmoller/unifier/UnifierHooks", "onMerchantLoad", "(Lnet/minecraft/village/MerchantRecipeList;)V"));
			
			addDefEquipMethod.instructions.insertBefore(findInjectionPointVillagerEquip(addDefEquipMethod), list);
			
			return ASMUtilities.getBytes(node);
		}
		return bytes;
	}

	public AbstractInsnNode findInjectionPointVillagerREFNBT(MethodNode method)
	{
		Iterator<AbstractInsnNode> it = method.instructions.iterator();
		AbstractInsnNode last = null;
		boolean found = false;
		while(it.hasNext())
		{
			AbstractInsnNode node = it.next();
			
			if(found)
				return node;
			
			if(node.getOpcode() == Opcodes.PUTFIELD)
			{
				FieldInsnNode fNode = (FieldInsnNode)node;
				
				if(fNode.owner.equals("net/minecraft/entity/passive/EntityVillager") && fNode.name.equals("buyingList"))
					found = true;
			}
		}
		
		throw new RuntimeException("Injection point in EntityVillager for readEntityFromNBT was not found");
	}
	public AbstractInsnNode findInjectionPointVillagerEquip(MethodNode method)
	{
		Iterator<AbstractInsnNode> it = method.instructions.iterator();
		AbstractInsnNode last = null;
		while(it.hasNext())
		{
			AbstractInsnNode node = it.next();
			
			if(node.getOpcode() == Opcodes.RETURN)
				last = node;
		}
		
		if(last != null)
			return last;
		
		throw new RuntimeException("Injection point in EntityVillager for addDefaultEquipmentAndRecipies was not found");
	}
}
