package net.ltxom.glGame.resourses;

import java.util.ArrayList;

/**
 * 继承DataResource，储存XML类型的数据资源，封装了基本XML解析功能
 * 
 * @author LTXOM
 * @version 5/19/2018
 */
public class XMLDataResourse extends DataResource
{
	private String version;
	private String encoding;

	// matchMap用来储存分析到的XML标签对
	// 比如下列XML：
	// 1:<nodeA>
	// 2: <nodeB>
	// 3: abcdf
	// 4: </nodeB>
	// 5: <nodeC />
	// 6: <nodeD>abc</nodeD>
	// 7: <nodeB>
	// 8: 123
	// 9: </nodeB>
	// 10:</nodeA>

	// 在matchMap中将会以String列表存储四个信息
	// 第一个是node名称，第二个是连接状态，0是分开标签，1是单独标签
	// 第三第四个是开始/结束位置
	// 例如以下信息：
	// 0: "nodeA, 0, 1, 10"
	// 1: "nodeB, 0, 2, 4"
	// 2: "nodeC, 1, 5, 5"
	// 3: "nodeD, 0, 6, 6"
	// 4: "nodeB, 0, 7, 9"
	private ArrayList<String> matchMap;

	// 找到首标签后但尾标签不在当前行会将
	// 首标签坐标数据放入waitList中
	private ArrayList<String> waitList;

	public XMLDataResourse(String path)
	{
		super(path);

		matchMap = new ArrayList<String>();
		waitList = new ArrayList<String>();
		analyzeXML();
	}

	private void analyzeXML()
	{
		analyzeMatchMap();
	}

	private void analyzeMatchMap()
	{
		// 首先读取版本信息
		int start = 0;
		for (int i = 0; i < this.getSource().size(); i++)
		{

			String line = this.getSource().get(i);
			if (line.startsWith("<?") && line.contains("xml version=\""))
			{
				String tempVersion = line.substring(line.indexOf("version=\"") + "version=\"".length());
				this.version = tempVersion.substring(0, tempVersion.indexOf("\""));

				String tempEncoding = line.substring(line.indexOf("encoding=\"") + "encoding=\"".length());
				this.encoding = tempEncoding.substring(0, tempEncoding.indexOf("\""));
				start = i + 1;
				break;
			}
		}

		// 填充matchMap列表
		int matchMapIndex = 0;

		a: for (int i = start; i < this.getSource().size(); i++)
		{

			// 在实际上文件中的行数:
			int index = i + 1;
			String line = this.getSource().get(i);
			String prefix;
			if (line.contains("<") && line.contains(">") && !line.trim().startsWith("</"))
			{

				prefix = line.substring(line.indexOf("<") + 1,
						line.substring(line.indexOf("<")).indexOf(" ") + line.indexOf("<"));
				if (line.endsWith("/>"))
				{
					matchMap.add(prefix + ", 1, " + index + ", " + index);
					matchMapIndex++;
					continue a;
				} else if (line.substring(line.indexOf(">")).contains("<"))
				{
					prefix = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
					matchMap.add(prefix + ", 0, " + index + ", " + index);
					matchMapIndex++;
					continue a;
				} else if (!line.contains("</"))
				{
					if (line.substring(line.indexOf("<")).indexOf(" ") > line.indexOf(">"))
					{
						prefix = line.substring(line.indexOf("<"), line.indexOf(">"));
					}
					waitList.add(prefix + "," + matchMapIndex);
					matchMap.add(prefix + ", 0, " + index);
					matchMapIndex++;
					continue a;
				}
			} else if (line.contains("</"))
			{
				prefix = line.substring(line.indexOf("</") + 2, line.indexOf(">"));
				for (int j = waitList.size() - 1; j >= 0; j--)
				{
					String[] arr = waitList.get(j).split(",");
					if (arr[0].equals(prefix))
					{
						matchMap.set(Integer.parseInt(arr[1]), matchMap.get(Integer.parseInt(arr[1])) + ", " + index);
						waitList.remove(j);
						continue a;
					}
				}
			}
		}
	}

	public String getVersion()
	{
		return version;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public ArrayList<String> getMatchMap()
	{
		return matchMap;
	}
}
