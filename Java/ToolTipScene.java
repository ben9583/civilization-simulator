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

public class ToolTipScene extends JComponent {
	public ToolTipScene() {
		this.setBounds(0, 0, 1366, 768);
	}

	@Override
	public void paintComponent(Graphics g) {
		if(g instanceof Graphics2D)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			Point mouse = MouseInfo.getPointerInfo().getLocation();
			int x = (mouse.x - 2 - Main.X_PADDING + Main.X_UNIT_LENGTH / 4) / Main.X_UNIT_LENGTH;
			int y = (mouse.y - 55 - Main.Y_PADDING + (((mouse.x - Main.X_PADDING + Main.X_UNIT_LENGTH / 4) / Main.X_UNIT_LENGTH) % 2 == 0 ? 0 : Main.Y_UNIT_LENGTH / 4)) / Main.Y_UNIT_LENGTH;
			SceneVector location = new SceneVector(x, y);
			Hex hex = Main.scene.getHexAt(location);
			g2.setColor(new Color(255, 255, 0));
			g2.fillRect(mouse.x - 2, mouse.y - 55, 5, 5);
			if(hex != null) {
				g2.setColor(new Color(150, 150, 150));
				g2.fillRect(mouse.x, mouse.y, 200, 150);

				g2.setColor(new Color(0, 0, 0));
				g2.drawString("Hex: [" + x + ", " + y + "]", mouse.x + 5, mouse.y + 15);
				g2.drawString("Empire: ", mouse.x + 5, mouse.y + 30);
				Empire emp = hex.getEmpire();
				if(emp == null) {
					g2.setColor(new Color(100, 100, 100));
					g2.drawString("null", mouse.x + 55, mouse.y + 30);
				} else {
					g2.setColor(emp.color);
					g2.drawString(emp.name, mouse.x + 55 , mouse.y + 30);
				}

				g2.setColor(new Color(0, 0, 0));
				g2.drawString("Occupier: ", mouse.x + 5, mouse.y + 45);
				emp = hex.getOccupier();
				if(emp == null) {
					g2.setColor(new Color(100, 100, 100));
					g2.drawString("null", mouse.x + 70, mouse.y + 45);
				} else {
					g2.setColor(emp.color);
					g2.drawString(emp.name, mouse.x + 70, mouse.y + 45);
				}

				emp = hex.getEmpire();
				if(emp == null) return;

				g2.setColor(new Color(0, 0, 0));
				g2.drawString("Economy: ", mouse.x + 5, mouse.y + 60);
				String txt = "" + emp.getEconomy();
				g2.drawString(txt, mouse.x + 70, mouse.y + 60);

				g2.drawString("Stability: ", mouse.x + 5, mouse.y + 75);
				txt = "" + emp.getStability();
				g2.drawString(txt, mouse.x + 70, mouse.y + 75);

				g2.drawString("At war with: ", mouse.x + 5, mouse.y + 90);
				txt = "";
				ArrayList<WarDeclaration> deals = emp.getWars();
				for(int i = 0; i < deals.size(); i++) {
					WarDeclaration war = deals.get(i);
					if(war.t0 == emp) {
						txt += war.t1.name;
					} else {
						txt += war.t0.name;
					}
					txt += ", ";
				}
				if(txt.length() > 0) txt = txt.substring(0, txt.length() - 1);
				g2.drawString(txt, mouse.x + 80, mouse.y + 90);

				g2.drawString("Empire tiles: ", mouse.x + 5, mouse.y + 105);
				g2.drawString("" + emp.getEmpireTiles().size(), mouse.x + 85, mouse.y + 105);

				g2.drawString("Occupied tiles: ", mouse.x + 5, mouse.y + 120);
				g2.drawString("" + emp.getOccupiedTiles().size(), mouse.x + 100, mouse.y + 120);
			}
		}
	}
}
