package net.ltxom.learnLWJGL.note;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/**
 * 例子展示了如何用LWJGJ3用户交互 按鼠标左键变绿，按键盘A键变红
 * 
 * video link:
 * https://www.youtube.com/watch?v=2Q7K2Ma5u1U&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=3
 * 
 * @author ElegantWhelp Commented and Revised by LTXOM
 * @version 4/24/2018
 */
public class C_Input
{
	public C_Input()
	{

		if (!glfwInit())
		{
			System.err.println("GLFW初始化失败！");
			System.exit(0);
		}

		long window = glfwCreateWindow(640, 480, "The Quad", 0, 0);

		glfwShowWindow(window);

		glfwMakeContextCurrent(window);

		GL.createCapabilities();

		float red = 0.0f;
		float green = 0.0f;
		while (!glfwWindowShouldClose(window))
		{
			glfwPollEvents();

			// glfwGetKey()第一个参数传递当前窗口，第二个传递要监听的按键
			// 若此时按下『A』键，则左侧回合GLFW_TRUE值相等
			if (glfwGetKey(window, GLFW_KEY_A) == GLFW_TRUE)
			{
				red = 1.0f;
				green = 0.0f;
			} 
			//按下ESC键使程序关闭
			else if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_TRUE)
				glfwSetWindowShouldClose(window, true);

			// glfwGetMouseButton()第一个参数传递当前窗口，第二个传递要监听的鼠标按键
			// 若此时按下『鼠标左键』，则左侧回合GLFW_PRESS值相等，若松开，则等于GLFW_RELEASE
			if (glfwGetMouseButton(window, 0) == GLFW_PRESS)
			{
				red = 0.0f;
				green = 1.0f;
			}

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			//过时，只是为了演示用：
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glColor4f(red, green, 0, 0);
				GL11.glVertex2f(-0.5f, 0.5f);

				GL11.glColor4f(red, green, 0, 0);
				GL11.glVertex2f(0.5f, 0.5f);

				GL11.glColor4f(red, green, 0, 0);
				GL11.glVertex2f(0.5f, -0.5f);

				GL11.glColor4f(red, green, 0, 0);
				GL11.glVertex2f(-0.5f, -0.5f);
			}
			GL11.glEnd();

			glfwSwapBuffers(window);
		}
	}

	public static void main(String[] args)
	{
		new C_Input();
	}

}
