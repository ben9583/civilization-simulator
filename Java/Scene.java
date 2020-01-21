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
import java.awt.font.TextAttribute;
import javax.swing.*;
import java.util.Hashtable;

public class Scene extends JComponent {

	//Stores all of the hexagons to be drawn to the scene
	private HexList hexes;
	private Font titleFont;

	//Initialize hexagon list
	public Scene() {
		this.hexes = new HexList();
		this.titleFont = new Font(Font.SERIF, Font.PLAIN, 24);

		Hashtable<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
		map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
		map.put(TextAttribute.FAMILY, "Serif");
		map.put(TextAttribute.SIZE, 24);
		this.titleFont = this.titleFont.deriveFont(map);
	}

	//Finds the the hex at location, null if not present
	public Hex getRandomHex() {
		return this.hexes.get((int)(Math.random() * this.hexes.size()));
	}

	public Hex getHexAt(SceneVector location) {
		for(int i = 0; i < this.hexes.size(); i++) {
			if(this.hexes.get(i).getX() == location.x && this.hexes.get(i).getY() == location.y) {
				return this.hexes.get(i);
			}
		}

		return null;
	}

	//Takes a hexagon and adds it to hexes where it will be drawn to the scene
	public void addHex(Hex h) {

		//Verify that the hex doesn't exist
		for(int i = 0; i < this.hexes.size(); i++) {
			if(this.hexes.get(i).getX() == h.getX() && this.hexes.get(i).getY() == h.getY()) {
				this.hexes.remove(i);
			}
		}

		this.hexes.add(h);
	}

	//Paints scene to the frame
	@Override
	public void paintComponent(Graphics g) {
		if(g instanceof Graphics2D)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g2.setColor(new Color(0, 20, 70));
			g2.fillRect(0, 0, 1366, 718);

			//Go through hex list and draw all hexagons to the scene
			for(int i = 0; i < this.hexes.size(); i++) {
				Hex h = this.hexes.get(i);

				g2.setColor(h.getColor());
				g2.fillPolygon(h);
				//g2.setColor(new Color(0, 0, 0));
				//g2.draw(h.getBounds2D());
			}

			/*
			g2.setColor(new Color(255, 255, 255));
			g2.fillRect(1150, 0, 216, 768);
			*/
		}
	}
}