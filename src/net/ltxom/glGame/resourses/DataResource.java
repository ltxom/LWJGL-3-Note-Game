package net.ltxom.glGame.resourses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import static net.ltxom.glGame.util.Terminal.PREFIX;

/**
 * 储存数据资源
 * 
 * @author LTXOM
 * @version 5/19/2018
 */
public class DataResource extends Resourse
{
	private ArrayList<String> sourceList;

	/**
	 * 资源文件必须是数据文件，构造器将读取其内容存放至sourceList中
	 */
	public DataResource(String path)
	{
		super(path, ResourseType.DATA);
		sourceList = new ArrayList<String>();
		BufferedReader bfr = null;
		try
		{
			bfr = new BufferedReader(new FileReader(this.getFile()));
			String line;
			while ((line = bfr.readLine()) != null)
			{
				sourceList.add(line);
			}
		} catch (IOException e)
		{
			System.err.println(PREFIX + "无法读取文件：" + this.getPath());
			e.printStackTrace();
		} finally
		{
			try
			{
				bfr.close();
			} catch (IOException e)
			{
				System.err.println(PREFIX + "无法关闭文件流，以下是错误信息");
				e.printStackTrace();
			}
		}

	}

	/**
	 * @return 得到数据资源的内容
	 */
	public ArrayList<String> getSource()
	{
		return sourceList;
	}
}
