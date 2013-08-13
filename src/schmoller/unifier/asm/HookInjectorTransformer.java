package schmoller.unifier.asm;

import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import org.objectweb.asm.Opcodes;

import cpw.mods.fml.relauncher.IClassTransformer;

public class HookInjectorTransformer implements IClassTransformer
{
	@Override
	public byte[] transform( String name, String transformedName, byte[] bytes )
	{
		if(name.equals("net.minecraft.client.gui.GuiMainMenu"))
		{
			ClassNode node = ASMUtilities.getClassNode(bytes);
			MethodNode initGui = ASMUtilities.getMethodNode(node, "initGui");
			MethodNode actionPerformed = ASMUtilities.getMethodNode(node, "actionPerformed");
			InsnList list = new InsnList();

			list.add(new VarInsnNode(Opcodes.ALOAD, 0));
			list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiMainMenu", "buttonList", "Ljava/util/List;"));
			list.add(new TypeInsnNode(Opcodes.NEW, "schmoller/unifier/gui/GuiButtonUnifier"));
			list.add(new InsnNode(Opcodes.DUP));
			list.add(new IntInsnNode(Opcodes.SIPUSH, 800));
			list.add(new VarInsnNode(Opcodes.ALOAD, 0));
			list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiMainMenu", "width", "I"));
			list.add(new InsnNode(Opcodes.ICONST_2));
			list.add(new InsnNode(Opcodes.IDIV));
			list.add(new IntInsnNode(Opcodes.BIPUSH, 104));
			list.add(new InsnNode(Opcodes.IADD));
			list.add(new VarInsnNode(Opcodes.ILOAD, 3));
			list.add(new IntInsnNode(Opcodes.BIPUSH, 72));
			list.add(new InsnNode(Opcodes.IADD));
			list.add(new IntInsnNode(Opcodes.BIPUSH, 12));
			list.add(new InsnNode(Opcodes.IADD));
			list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "schmoller/unifier/gui/GuiButtonUnifier", "<init>", "(III)V"));
			list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z"));
			list.add(new InsnNode(Opcodes.POP));
			
			initGui.instructions.insertBefore(findInjectionPointMainMenuInit(initGui), list);
			
			list = new InsnList();
			list.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			list.add(new VarInsnNode(Opcodes.ALOAD, 1));
			list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiButton", "id", "I"));
			list.add(new IntInsnNode(Opcodes.SIPUSH, 800));
			LabelNode l23 = new LabelNode();
			list.add(new JumpInsnNode(Opcodes.IF_ICMPNE, l23));
			list.add(new VarInsnNode(Opcodes.ALOAD, 0));
			list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "schmoller/unifier/UnifierHooks", "onOpenGlobalOptions", "(Lnet/minecraft/client/gui/GuiMainMenu;)V"));
			list.add(l23);
			list.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			
			actionPerformed.instructions.insertBefore(findInjectionPointMainMenuActionPerformed(actionPerformed), list);

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
	
	public AbstractInsnNode findInjectionPointMainMenuInit(MethodNode method)
	{
		Iterator<AbstractInsnNode> it = method.instructions.iterator();
		AbstractInsnNode last = null;
		while(it.hasNext())
		{
			AbstractInsnNode node = it.next();
			
			if(node.getOpcode() == Opcodes.ALOAD)
				last = node;
			else if(node.getOpcode() == Opcodes.GETFIELD)
			{
				FieldInsnNode field = (FieldInsnNode)node;
				if(field.owner.equals("net/minecraft/client/gui/GuiMainMenu") && field.name.equals("field_104025_t") && field.desc.equals("Ljava/lang/Object;"))
				{
					if(last == null)
						throw new RuntimeException("Injection point in GuiMainMenu for initGui was not found");
					return last;
				}
			}
		}
		
		throw new RuntimeException("Injection point in GuiMainMenu for initGui was not found");
	}
	
	public AbstractInsnNode findInjectionPointMainMenuActionPerformed(MethodNode method)
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
		
		throw new RuntimeException("Injection point in GuiMainMenu for actionPerformed was not found");
	}
}
