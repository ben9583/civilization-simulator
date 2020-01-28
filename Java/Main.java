/*

MIT License

Copyright (c) 2020 Ben Plate

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Main {

	//Standard large radius of a hexagon
	public static final int HEX_WIDTH = 12;

	//Size of a SceneVector unit, in pixels
	public static final int X_UNIT_LENGTH = (int)(1.5f * HEX_WIDTH);
	public static final int Y_UNIT_LENGTH = (int)(1.732f * HEX_WIDTH);

	//Offset of the SceneVector unit from (0, 0), in pixels
	public static final int X_PADDING = 30;
	public static final int Y_PADDING = 30;

	//Simplex noise generator configurations
	public static final long SEED = System.currentTimeMillis();
	public static final int DISPLACEMENT = (int)(Math.random() * 100000);
	public static final double CONTINENT_ROUGHNESS = 0.5;
	public static final double WATER_WORLD = 0.3;
	public static final int INITIAL_EMPIRES = 16;

	//Game Preferences
	public static final int DEFAULT_PEACE_DURATION = 15;

	//Global, dynamic information
	public static long TICK = 0;
	public static ArrayList<Empire> empires = new ArrayList<Empire>();
	public static int COUNT = 1;
	public static JFrame frame;
	public static Scene scene;

	public static void processTick() {
		for(int i = 0; i < Main.empires.size(); i++) {
			Empire emp = Main.empires.get(i);

			emp.growEconomy();
			boolean expanding = emp.expandEmpire();

			if(!expanding) {
				emp.decideWars();
			}

			emp.coordinateWars();
		}

		Main.TICK += 1;
	}

	public static void main(String[] args) {
		//1. Create the frame.
		frame = new JFrame("hello");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Thread toolTipThread = new Thread(new ToolTipThread());
		toolTipThread.start();

		//2. Create a scene and add hexes.
		scene = new Scene();
		Color white = new Color(200, 200, 200);
		OpenSimplexNoise noiseGen = new OpenSimplexNoise(SEED);

		double randVal = Math.random() * 10000;

		for(int i = 0; i < 60; i++) {
			for(int j = 0; j < 30; j++) {

				double x = i * 0.225 * CONTINENT_ROUGHNESS + DISPLACEMENT;
				double y = j * 0.225 * CONTINENT_ROUGHNESS + DISPLACEMENT;

				if(noiseGen.eval(x, y) > (WATER_WORLD - 0.5) * 2) {
					scene.addHex(new Hex(scene, new SceneVector(i, j), white));
				}
			}
		}

		for(int i = 0; i < Main.INITIAL_EMPIRES; i++) {
			Hex h = scene.getRandomHex();
			if(h.getEmpire() == null) {
				Empire emp = new Empire(scene.getRandomHex());
				Main.empires.add(emp);

				Main.COUNT++;
			}
		}

		//3. Add the scene to the frame and show it.
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(scene);
		scene.setBounds(0, 0, 1366, 768);

		frame.setSize(1366, 718);
		frame.setVisible(true);

		System.out.println("hi");
		while(true) {
			try {
				Thread.sleep(500);
			} catch(InterruptedException e) {
				System.out.println("bruh");
			}

			processTick();
			scene.colorHexes();
			frame.revalidate();
			frame.repaint();
		}
	}
}