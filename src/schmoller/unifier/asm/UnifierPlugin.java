package schmoller.unifier.asm;

import java.awt.Desktop;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions("schmoller.unifier.asm")
public class UnifierPlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getLibraryRequestClass()
	{
		return null;
	}

	public static void versionCheck(String reqVersion, String mod)
    {
        String mcVersion = (String) FMLInjectionData.data()[4];
        if(!VersionParser.parseRange(reqVersion).containsVersion(new DefaultArtifactVersion(mcVersion)))
        {
            String err = "This version of "+mod+" does not support minecraft version "+mcVersion;
            System.err.println(err);
            
            JEditorPane ep = new JEditorPane("text/html", 
                    "<html>" +
                    err + 
                    "<br>Remove it from your coremods folder and check somewhere for updates" +
                    "</html>");

            ep.setEditable(false);
            ep.setOpaque(false);
            ep.addHyperlinkListener(new HyperlinkListener()
            {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent event)
                {
                    try
                    {
                        if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                            Desktop.getDesktop().browse(event.getURL().toURI());
                    }
                    catch(Exception e)
                    {}
                }
            });
            
            JOptionPane.showMessageDialog(null, ep, "Fatal error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
	
	@Override
	public String[] getASMTransformerClass()
	{
		versionCheck("[1.5.2]", "ForgeUnifier");
		return new String[] { "schmoller.unifier.asm.HookInjectorTransformer" };
	}

	@Override
	public String getModContainerClass()
	{
		return "schmoller.unifier.ModForgeUnifier";
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData( Map<String, Object> data )
	{
	}
	
	

}
