import java.util.ArrayList;

import net.ltxom.glGame.resourses.XMLDataResourse;

public class Tester
{

	public static void main(String[] args)
	{
		XMLDataResourse xmlData = new XMLDataResourse("res/mapsData/OutsideObjects1.tsx");
		ArrayList<String> source = xmlData.getSource();
		// for (int i = 0; i < source.size(); i++)
		// {
		// System.out.println(source.get(i));
		// }

		for (int i = 0; i < xmlData.getMatchMap().size(); i++)
		{
			System.out.println(i + ":" + xmlData.getMatchMap().get(i));
		}
		System.out.println();
		

		XMLDataResourse subXMLData = new XMLDataResourse(xmlData.getContent(5));
		
		for (int i = 0; i < subXMLData.getMatchMap().size(); i++)
		{
			System.out.println(i + ":" + subXMLData.getMatchMap().get(i));
		}
		System.out.println();
	}

}
