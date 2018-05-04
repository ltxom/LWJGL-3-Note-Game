package net.ltxom.learnLWJGL.note;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Shader就是在OpenGL原有的传统固定线管的编程基础上的增强，使之可以仅仅通过操控shader文件而改变渲染方式的目的
 * 
 * *Shader使用GLSL语言编译
 * 
 * 基本概念：https://www.douban.com/note/577992763/
 * 
 * 
 * 
 * video link:
 * https://www.youtube.com/watch?v=q_dS3JuoeDw&index=6&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u
 * 
 * @author ElegantWhelp Commented and Revised by LTXOM
 * @version 5/3/2018
 */
public class G_Shaders
{

	private int program;
	// process vertex
	private int vertexShader;
	// process color
	private int fragmentShader;

	public G_Shaders(String vsFile, String vfFile)
	{

		program = glCreateProgram();

		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, this.readFile(vsFile));
		glCompileShader(vertexShader);

		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) != 1)
		{
			System.err.println(glGetShaderInfoLog(vertexShader));
			System.exit(0);
		}

		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, this.readFile(vfFile));
		glCompileShader(fragmentShader);

		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != 1)
		{
			System.err.println(glGetShaderInfoLog(fragmentShader));
			System.exit(0);
		}

		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);

		glBindAttribLocation(program, 0, "vertices");

		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) != 1)
		{
			System.err.println(glGetProgramInfoLog(program));
			System.exit(0);
		}

		glValidateProgram(program);
		if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1)
		{
			System.err.println(glGetProgramInfoLog(program));
			System.exit(0);
		}
	}

	public void bind()
	{
		glUseProgram(program);
	}

	private String readFile(String file)
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(new File(file)));
			String line;
			while ((line = br.readLine()) != null)
			{
				sb.append(line + "\n");
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				br.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static void main(String[] args)
	{
		float[] quadVertices =
		{ //
				-0.5f, 0.5f, 0, // TOP LEFT 0
				0.5f, 0.5f, 0, // TOP RIGHT 1
				0.5f, -0.5f, 0, // BOT RIGHT 2
				-0.5f, -0.5f, 0, // TOP RIGHT 3
		};
		float[] texture =
		{ // 材质坐标
				0, 1, // TOP LEFT
				1, 1, // TOP RIGHT
				1, 0, // BOT RIGHT
				0, 0, // TOP RIGHT
		};

		int[] indices =
		{ //指定顶点在属性数组中的索引
				0, 1, 2, // 对应上方quadVertices的坐标，绘制点0、1、2即为Top Triangle
				2, 3, 0 // 对应上方quadVertices的坐标，绘制点2、3、0即为Bot Triangle
		};

		new F_Indices("res/uw-quad.png", "VBO", quadVertices, texture, indices)
				.act(new G_Shaders("shaders/shader.vertices", "shaders/shader.fragment"));

	}
}
