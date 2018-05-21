import net.ltxom.glGame.resourses.TilesXMLDataResource;

public class Tester
{

	public static void main(String[] args)
	{
		TilesXMLDataResource txdr = new TilesXMLDataResource("res/mapsData/TilesData/OutsideObjects1.tsx");
		System.out.println(txdr.getColliders().getTileName());
		System.out.println(txdr.getColliders().getX(106));
		
	}

}
