package net.ltxom.learnLWJGL.note;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

/**
 * 例子展示了如何使用VBO技术
 * 
 * 将在下一个版本更新注释
 * 
 * VBO：VertexBufferObject, 讲模型的顶点信息放在显卡的显存的空间，而不占用CPU的内存空间
 * 
 * 
 * video link:
 * https://www.youtube.com/watch?v=2Q7K2Ma5u1U&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=5
 * 
 * @author ElegantWhelp Commented and Revised by LTXOM
 * @version 4/27/2018
 */
public class E_VBO
{
	private long window;
	private int draw_count;
	private int v_id;
	private int texture_id;

	public E_VBO(String file, String windowTitle, float[] vertices, float[] text_coords)
	{
		initWindow(windowTitle);

		GL.createCapabilities();

		glEnable(GL_TEXTURE_2D);

		draw_count = vertices.length / 3;

		// vertices buffer
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
		buffer.put(vertices);
		buffer.flip();

		v_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// text_coords
		buffer = BufferUtils.createFloatBuffer(text_coords.length);
		buffer.put(text_coords);
		buffer.flip();

		texture_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, texture_id);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		this.bindTexture(file);

		while (!glfwWindowShouldClose(window))
		{
			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT);

			glBindTexture(GL_TEXTURE_2D, texture_file_id);

			this.render();

			glfwSwapBuffers(window);
		}
	}

	public void render()
	{
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		glVertexPointer(3, GL_FLOAT, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, texture_id);
		glTexCoordPointer(2, GL_FLOAT, 0, 0);

		glDrawArrays(GL_TRIANGLES, 0, draw_count);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);

	}

	private int height;
	private int width;
	private int texture_file_id;
	private BufferedImage bi;

	public void bindTexture(String file)
	{
		
		try
		{
			bi = ImageIO.read(new File(file));
			this.height = bi.getHeight();
			this.width = bi.getWidth();

			int[] pixels_raw = new int[width * height * 4];
			pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

			for (int height = 0; height < this.height; height++)
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

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		float[] triangleVertices =
		{ -1, -1, 0, 0, 1, 0, 1, -1, 0 };
		float[] triangle2Vertices =
		{ -0.5f, 0.5f, 0, // TOP LEFT
				0.5f, 0.5f, 0, // TOP RIGHT
				0.5f, -0.5f, 0 // BOT RIGHT
		};

		float[] quadVertices =
		{ // TopTriangle
				-0.5f, 0.5f, 0, // TOP LEFT
				0.5f, 0.5f, 0, // TOP RIGHT
				0.5f, -0.5f, 0, // BOT RIGHT

				// BotTriangle
				0.5f, -0.5f, 0, // TOP LEFT
				-0.5f, -0.5f, 0, // TOP RIGHT
				-0.5f, 0.5f, 0 // BOT RIGHT
		};

		float[] texture =
		{ // 和上面对应
				0, 0, //
				1, 0, //
				1, 1, //

				1, 1, //
				0, 1, //
				0, 0//
		};
		new E_VBO("res/uw-quad.png", "VBO", quadVertices, texture);
	}

}
