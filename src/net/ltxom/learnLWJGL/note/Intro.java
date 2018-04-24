package net.ltxom.learnLWJGL.note;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWVidMode;

/**
 * 这部分代码的实例使用glfw创建了一个窗口
 * 
 * 在这里要了解一些hint，handle的概念
 * 
 * 视频链接：（后半部分，讲的不够详细）
 * https://www.youtube.com/watch?v=VH9KAhjXVFM&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u
 * 
 * 有关glfw更详细的内容，可以在下面链接了解(下面的例子使用C艹编写的, 但内容都一样的)
 * http://www.glfw.org/docs/latest/window_guide.html#window_hints
 * 
 * 善用文档： https://javadoc.lwjgl.org/org/lwjgl/glfw/GLFW.html
 * 
 * @author ElegantWhelp Commented and Revised by LTXOM
 * @version 4/24/2018
 */
public class Intro
{

	public static void main(String[] args)
	{
		// Initializes the GLFW library. Before most GLFW functions can be used, 
		// GLFW must be initialized, and before an application terminates GLFW 
		// should be terminated in order to free any resources allocated during 
		// or after initialization.
		if (!glfwInit())
		{
			throw new IllegalStateException("初始化GLFW失败！");
		}

		/*
		 * 用在glfwCreateWindow()之前，给新建的窗口一些小Hint
		 * 
		 * 两个参数是键值对，第一个设置属性，第二个设置参数 
		 * 比如说下方例子： 
		 * 第一行第一个键是GLFW_RESIZABLE，设定窗口是否能够重设大小
		 * 第一行第二个值是GLFW_FALSE，不允许窗口设置大小
		 * 
		 * 第二行第一个键是GLFW_VISIBLE，设定窗口可见性
		 * 第二行第二个值是GLFW_FALSE，不让窗口显示
		 * 
		 * 进文档里可以查到详细的键值表：
		 * https://javadoc.lwjgl.org/org/lwjgl/glfw/GLFW.html#glfwWindowHint-int-int-
		 * 
		 */
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

		/*
		 * 创建一个OpenGL窗口
		 * 
		 * 查文档：https://javadoc.lwjgl.org/org/lwjgl/glfw/GLFW.html#glfwCreateWindow-int-int-java.nio.ByteBuffer-long-long-
		 * 
		 * 参数分别是：
		 * int width, int height, String title, long monitor, long share
		 * width：窗口的宽 
		 * height：窗口的高 
		 * title：窗口标题
		 * 
		 * 后面的两个参数还没讲到： 
		 * monitor：the monitor to use for fullscreen mode, or NULL for
		 * windowed mode share：the window whose context to share resources with, or NULL
		 * to not share resources
		 *
		 * 返回：返回的是一个OpenGL特殊的数值，这个数值被称为『handle』，或者叫『unique ID』，用来指代这个window的id
		 */
		long window = glfwCreateWindow(640, 400, "Hello World!", 0, 0);

		// 得到当前显示器的对象，用于下面得到当前显示器的宽高
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		// 设置窗口位置，根据当前显示器的分辨率与窗口大小使之居中显示
		glfwSetWindowPos(window, (videoMode.width() - 640) / 2, (videoMode.height() - 400) / 2);

		// 如果上面的是glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE)，那么就没有必要调用下面的方法使窗口显示了
		glfwShowWindow(window);

		// 若窗口不被关闭
		while (!glfwWindowShouldClose(window))
		{
			// Processes all pending events.
			glfwPollEvents();
		}

		// Destroys all remaining windows and cursors, restores any modified gamma ramps and frees any other allocated resources.
		glfwTerminate();
	}

}
