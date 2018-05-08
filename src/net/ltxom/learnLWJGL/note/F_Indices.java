package net.ltxom.learnLWJGL.note;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

/**
 * 使用索引(Indices)绘图
 * 
 * 没有使用索引绘制矩形时，要重复指定重叠的顶点数据;使用索引绘制时，只需要指定顶点在属性数组中的索引即可
 * https://blog.csdn.net/wangdingqiaoit/article/details/51324516
 * 
 * video link:
 * https://www.youtube.com/watch?v=7NsXcedg5fo&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=6
 * 
 * @author ElegantWhelp Commented and Revised by LTXOM
 * @version 5/3/2018
 */

public class F_Indices
{

	private long window;
	private int draw_count;
	private int vertices_id;
	private int texture_id;

	private int indices_id;

	public F_Indices(String file, String windowTitle, float[] vertices, float[] text_coords, int[] indices)
	{

		initWindow(windowTitle);

		GL.createCapabilities();

		glEnable(GL_TEXTURE_2D);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		draw_count = indices.length;

		// vertices buffer
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
		buffer.put(vertices);
		buffer.flip();

		vertices_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertices_id);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		// text_coords
		buffer = BufferUtils.createFloatBuffer(text_coords.length);
		buffer.put(text_coords);
		buffer.flip();

		texture_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, texture_id);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		// indices，使用IntBuffer而不是FloatBuffer储存数据
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		indices_id = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indices_id);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		this.bindTexture(file, 0);

	}

	public void render()
	{
		//glEnableClientState(GL_VERTEX_ARRAY);
		//glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, vertices_id);
		//glVertexPointer(3, GL_FLOAT, 0, 0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, texture_id);
		//glTexCoordPointer(2, GL_FLOAT, 0, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

		
		// 使用索引就无需使用glDrawArrays()了
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indices_id);
		glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);

		//glDisableClientState(GL_VERTEX_ARRAY);
		//glDisableClientState(GL_TEXTURE_COORD_ARRAY);

	}

	public void act()
	{
		while (!glfwWindowShouldClose(window))
		{
			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT);

			glBindTexture(GL_TEXTURE_2D, texture_file_id);

			this.render();

			glfwSwapBuffers(window);
		}
	}

	public void act(G_Shaders shader)
	{
		while (!glfwWindowShouldClose(window))
		{
			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT);

			glBindTexture(GL_TEXTURE_2D, texture_file_id);
			shader.bind();

			// Set Uniform
			shader.setUniform("sampler", 0);
			this.render();

			glfwSwapBuffers(window);
		}
	}

	private int height;
	private int width;
	private int texture_file_id;
	private BufferedImage bi;

	public void bindTexture(String file, int sampler)
	{

		try
		{
			bi = ImageIO.read(new File(file));
			this.height = bi.getHeight();
			this.width = bi.getWidth();

			int[] pixels_raw = new int[width * height * 4];
			pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

			for (int height = this.height - 1; height >= 0; height--)
			{
				for (int width = 0; width < this.width; width++)
				{
					int pixel = pixels_raw[height * this.width + width];

					pixels.put((byte) ((pixel >> 16) & 0xFF)); // Red
					pixels.put((byte) ((pixel >> 8) & 0xFF)); // Green
					pixels.put((byte) ((pixel) & 0xFF)); // Blue
					pixels.put((byte) ((pixel >> 24) & 0xFF));//Alpha
				}
			}

			pixels.flip();

			texture_file_id = glGenTextures();

			//
			if (sampler >= 0 && sampler <= 31)
				glActiveTexture(GL_TEXTURE0);

			glBindTexture(GL_TEXTURE_2D, texture_file_id);

			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void initWindow(String title)
	{

		if (!glfwInit())
		{
			System.err.println("GLFW初始化失败！");
			System.exit(0);
		}

		window = glfwCreateWindow(640, 480, title, 0, 0);

		glfwShowWindow(window);

		glfwMakeContextCurrent(window);

	}

	public static void main(String[] args)
	{

		float[] quadVertices =
		{ //两个三角形拼成的长方形顶点坐标
				// 因为使用了indices，所以可以忽略输入重复的坐标
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

		new F_Indices("res/uw-quad.png", "VBO", quadVertices, texture, indices).act();
	}

}
