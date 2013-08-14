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
		if(name.equals(NameHelper.guiMainMenu.getName()))
		{
			ClassNode node = ASMUtilities.getClassNode(bytes);
			MethodNode initGui = ASMUtilities.getMethodNode(node, NameHelper.initGui.getName(), "()V");
			MethodNode actionPerformed = ASMUtilities.getMethodNode(node, NameHelper.actionPerformed.getName(), ASMUtilities.buildDescription("V", NameHelper.guiButton));
			InsnList list = new InsnList();

			list.add(new VarInsnNode(Opcodes.ALOAD, 0));
			list.add(new FieldInsnNode(Opcodes.GETFIELD, NameHelper.guiMainMenu.getPath(), NameHelper.buttonList.getName(), "Ljava/util/List;"));
			list.add(new TypeInsnNode(Opcodes.NEW, "schmoller/unifier/gui/GuiButtonUnifier"));
			list.add(new InsnNode(Opcodes.DUP));
			list.add(new IntInsnNode(Opcodes.SIPUSH, 800));
			list.add(new VarInsnNode(Opcodes.ALOAD, 0));
			list.add(new FieldInsnNode(Opcodes.GETFIELD, NameHelper.guiMainMenu.getPath(), NameHelper.width.getName(), "I"));
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
			list.add(new FieldInsnNode(Opcodes.GETFIELD, NameHelper.guiButton.getPath(), NameHelper.id.getName(), "I"));
			list.add(new IntInsnNode(Opcodes.SIPUSH, 800));
			LabelNode l23 = new LabelNode();
			list.add(new JumpInsnNode(Opcodes.IF_ICMPNE, l23));
			list.add(new VarInsnNode(Opcodes.ALOAD, 0));
			list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "schmoller/unifier/UnifierHooks", "onOpenGlobalOptions", "(" + NameHelper.guiMainMenu.getDesc() + ";)V"));
			list.add(l23);
			list.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
			
			actionPerformed.instructions.insertBefore(findInjectionPointMainMenuActionPerformed(actionPerformed), list);

			return ASMUtilities.getBytes(node);
		}
		else if(name.equals(NameHelper.entityVillager.getName()))
		{
			ClassNode node = ASMUtilities.getClassNode(bytes);
			MethodNode readFromNBTMethod = ASMUtilities.getMethodNode(node, NameHelper.readEntityFromNBT.getName(), ASMUtilities.buildDescription("V", NameHelper.nbtTagCompound));
			MethodNode addDefEquipMethod = ASMUtilities.getMethodNode(node, NameHelper.addDefaultEquipmentAndRecipies.getName(), "(I)V");
			
			InsnList list = new InsnList();
			list.add(new VarInsnNode(Opcodes.ALOAD, 0));
			list.add(new FieldInsnNode(Opcodes.GETFIELD, NameHelper.entityVillager.getPath(), NameHelper.buyingList.getName(), NameHelper.merchantRecipeList.getDesc() + ";"));
			list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "schmoller/unifier/UnifierHooks", "onMerchantLoad", "(" + NameHelper.merchantRecipeList.getDesc() + ";)V"));
			
			readFromNBTMethod.instructions.insertBefore(findInjectionPointVillagerREFNBT(readFromNBTMethod), list);
			
			list = new InsnList();
			list.add(new VarInsnNode(Opcodes.ALOAD, 0));
			list.add(new FieldInsnNode(Opcodes.GETFIELD, NameHelper.entityVillager.getPath(), NameHelper.buyingList.getName(), NameHelper.merchantRecipeList.getDesc() + ";"));
			list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "schmoller/unifier/UnifierHooks", "onMerchantLoad", "(" + NameHelper.merchantRecipeList.getDesc() + ";)V"));
			
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
				if(fNode.owner.equals(NameHelper.entityVillager.getPath()) && fNode.name.equals(NameHelper.buyingList.getName()))
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
				if(field.owner.equals(NameHelper.guiMainMenu.getPath()) && field.name.equals(NameHelper.insertionPointVar.getName()) && field.desc.equals("Ljava/lang/Object;"))
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
