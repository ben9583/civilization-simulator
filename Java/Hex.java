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

public class Hex extends Polygon {

	private static final float SQRT_3 = 1.732f;

	private final int x;
	private final int y;
	private final int width;
	private final Scene scene;
	private Color color;
	private Color savedColor;

	private Empire empire;
	private Empire occupier;

	private boolean isClustered;

	//Constructs a hex at the screen coordinates (x, y)
	public Hex(Scene scene, int x, int y, int width, Color color) {
		super();

		int factor1 = (int)((float)width * 0.5); //x-value distance from center to top/bottom left/right points
		int factor2 = (int)((float)factor1 * SQRT_3); //y-value distance from center to top/bottom left/right points

		int[] xVals = {x - width, x - factor1, x + factor1, x + width, x + factor1, x - factor1};
		int[] yVals = {y, y - factor2, y - factor2, y, y + factor2, y + factor2};

		super.xpoints = xVals;
		super.ypoints = yVals;
		super.npoints = 6;

		this.scene = scene;
		this.x = (x - Main.X_PADDING) / Main.X_UNIT_LENGTH;
		this.y = (y - Main.Y_PADDING) / Main.Y_UNIT_LENGTH;
		this.width = width;
		this.color = color;
		this.savedColor = color;
		this.empire = null;
		this.isClustered = false;
	}

	//Constructs a hexagon along a hexagon grid at location where unit sizes are (xUnitLength, yUnitLength) and offset by (xPadding, yPadding)
	public Hex(Scene scene, SceneVector location, int xUnitLength, int yUnitLength, int xPadding, int yPadding, int width, Color color) {
		super();

		int factor1 = (int)((float)width * 0.5);
		int factor2 = (int)((float)factor1 * SQRT_3);

		int x = (location.x * xUnitLength + xPadding);
		int y = (location.y * yUnitLength + yPadding) - (location.x % 2 == 0 ? factor2 : 0);

		int rightX = x + factor1;
		int leftX = rightX - (2 * factor1);
		
		int upY = y + factor2;
		int downY = upY - (2 * factor2);

		int[] xVals = {x - width, leftX, rightX, x + width, rightX, leftX};
		int[] yVals = {y, downY, downY, y, upY, upY};

		super.xpoints = xVals;
		super.ypoints = yVals;
		super.npoints = 6;

		this.scene = scene;
		this.x = (x - Main.X_PADDING) / Main.X_UNIT_LENGTH;
		this.y = (y - Main.Y_PADDING) / Main.Y_UNIT_LENGTH;
		this.width = width;
		this.color = color;
		this.savedColor = color;
		this.empire = null;
		this.isClustered = false;
	}

	//Does the same as the last constructor, but uses constants from Main class
	public Hex(Scene scene, SceneVector location, Color color) {
		super();

		int factor1 = (int)((float)Main.HEX_WIDTH * 0.5);
		int factor2 = (int)((float)factor1 * SQRT_3);

		int x = (location.x * Main.X_UNIT_LENGTH + Main.X_PADDING);
		int y = (location.y * Main.Y_UNIT_LENGTH + Main.Y_PADDING) - (location.x % 2 == 0 ? factor2 : 0);
		
		int rightX = x + factor1;
		int leftX = rightX - (2 * factor1);
		
		int upY = y + factor2;
		int downY = upY - (2 * factor2);

		int[] xVals = {x - Main.HEX_WIDTH, leftX, rightX + 1, x + Main.HEX_WIDTH + 1, rightX + 1, leftX};
		int[] yVals = {y, downY, downY, y, upY, upY};

		super.xpoints = xVals;
		super.ypoints = yVals;
		super.npoints = 6;

		this.scene = scene;
		this.x = (x - Main.X_PADDING) / Main.X_UNIT_LENGTH;
		this.y = (y - Main.Y_PADDING) / Main.Y_UNIT_LENGTH;
		this.width = Main.HEX_WIDTH;
		this.color = color;
		this.savedColor = color;
		this.empire = null;
		this.isClustered = false;
	}

	public HexList getSurroundingHexes() {
		HexList out = new HexList(6);

		if(this.scene.getHexAt(new SceneVector(this.x - 1, this.y)) != null) {
			out.add(this.scene.getHexAt(new SceneVector(this.x - 1, this.y)));
		}
		if(this.scene.getHexAt(new SceneVector(this.x + 1, this.y)) != null) {
			out.add(this.scene.getHexAt(new SceneVector(this.x + 1, this.y)));
		}
		if(this.scene.getHexAt(new SceneVector(this.x, this.y - 1)) != null) {
			out.add(this.scene.getHexAt(new SceneVector(this.x, this.y - 1)));
		}
		if(this.scene.getHexAt(new SceneVector(this.x, this.y + 1)) != null) {
			out.add(this.scene.getHexAt(new SceneVector(this.x, this.y + 1)));
		}
		
		if(this.x % 2 == 0) {
			if(this.scene.getHexAt(new SceneVector(this.x + 1, this.y + 1)) != null) {
				out.add(this.scene.getHexAt(new SceneVector(this.x + 1, this.y + 1)));
			}
			if(this.scene.getHexAt(new SceneVector(this.x - 1, this.y + 1)) != null) {
				out.add(this.scene.getHexAt(new SceneVector(this.x - 1, this.y + 1)));
			}
		} else {
			if(this.scene.getHexAt(new SceneVector(this.x + 1, this.y - 1)) != null) {
				out.add(this.scene.getHexAt(new SceneVector(this.x + 1, this.y - 1)));
			}
			if(this.scene.getHexAt(new SceneVector(this.x - 1, this.y - 1)) != null) {
				out.add(this.scene.getHexAt(new SceneVector(this.x - 1, this.y - 1)));
			}
		}

		return out;
	}

	public HexList getCluster() {
		System.out.println("BEGIN CLUSTER FUNCTION");
		HexList out = new HexList();
		out.add(this);

		for(int i = 0; i < out.size(); i++) {
			HexList surroundingTiles = out.get(i).getSurroundingHexes();
			System.out.println(surroundingTiles.size() + " surrounding tiles");
			for(int j = 0; j < surroundingTiles.size(); j++) {
				Hex thatTile = surroundingTiles.get(j);
				if(!thatTile.isClustered && thatTile.getEmpire() == this.getEmpire() && thatTile.getOccupier() == this.getOccupier()) {
					out.add(thatTile);
					thatTile.isClustered = true;
					System.out.println("check");
				} else {
					System.out.println("nope");
				}
			}
		}

		System.out.println("Size: " + out.size());
		for(int i = 0; i < out.size(); i++) {
			out.get(i).isClustered = false;
		}

		System.out.println("END CLUSTER FUNCTION");
		return out;
	}

	public boolean isControlledBy(Empire e) {
		return (this.empire == e && this.occupier == null) || (this.empire != e && this.occupier == e);
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setColor(Color color) {
		this.savedColor = color;
	}

	public Color getColor() {
		return this.color;
	}

	public void updateColor() {
		this.color = this.savedColor;
	}

	public void setEmpire(Empire empire) {
		this.empire = empire;
	}

	public Empire getEmpire() {
		return this.empire;
	}

	public void setOccupier(Empire occupier) {
		this.occupier = occupier;
	}

	public Empire getOccupier() {
		return this.occupier;
	}

}