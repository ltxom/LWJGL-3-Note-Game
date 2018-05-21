import net.ltxom.glGame.resources.MapXMLDataResource;
import net.ltxom.glGame.resources.TilesXMLDataResource;

public class Tester
{

	public static void main(String[] args)
	{
		MapXMLDataResource mxdr = new MapXMLDataResource("res/mapsData/Maps/Demo.tmx");
		System.out.println(mxdr.getLayer(0).layerContent.toString());

	}

}
