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
import java.util.ArrayList;

public class Empire {
	//Public, final fields that define the empire
	public final String name;
	public final Hex capital;
	public final Color color;
	public final long foundingTick;

	//Private, dynamic fields that evolve with the empire
	private HexList hexList;
	private HexList occupiedTiles;
	private ArrayList<Empire> borderEmpires;
	private ArrayList<WarDeclaration> wars;
	private ArrayList<PeaceDeal> peaceDeals;
	private int economy;
	private double stability;

	public Empire(String name, Hex capital) {
		this.name = name;
		this.capital = capital;
		this.foundingTick = Main.TICK;
		this.hexList = new HexList();
		this.occupiedTiles = new HexList();
		this.borderEmpires = new ArrayList<Empire>();
		this.wars = new ArrayList<WarDeclaration>();
		this.peaceDeals = new ArrayList<PeaceDeal>();
		this.economy = 100;
		this.stability = 0.0;

		this.annexTile(capital);
		this.capital.setColor(new Color(0, 0, 0));

		boolean isGood = false;
		Color co = new Color(0, 0, 0);
		while(!isGood) {
			int r = (int)(Math.random() * 70) + 15;
			int g = (int)(Math.random() * 70) + 15;
			int b = (int)(Math.random() * 70) + 15;

			if(Main.empires.size() == 0) {
				isGood = true;
				co = new Color(r, g, b);
			}

			for(int i = 0; i < Main.empires.size(); i++) {
				if(Math.abs(Main.empires.get(i).color.getRed() - r) + Math.abs(Main.empires.get(i).color.getGreen() - g) + Math.abs(Main.empires.get(i).color.getBlue() - b) > 15) {
					isGood = true;
					co = new Color(r, g, b);
				}
			}
		}

		this.color = co;
	}

	public Empire(String name, Hex capital, Color color) {
		this.name = name;
		this.capital = capital;
		this.color = color;
		this.foundingTick = Main.TICK;
		this.hexList = new HexList();
		this.occupiedTiles = new HexList();
		this.borderEmpires = new ArrayList<Empire>();
		this.wars = new ArrayList<WarDeclaration>();
		this.peaceDeals = new ArrayList<PeaceDeal>();
		this.economy = 100;
		this.stability = 0.0;

		this.annexTile(capital);
		this.capital.setColor(new Color(0, 0, 0));
		
	}

	public void growEconomy() {
		int deltaE = (int)(2.5 * this.hexList.size() + (this.hexList.size() < 20 ? 250 : (250 + -0.55 * Math.pow(this.hexList.size() - 20, 1.3))/3) / (this.wars.size() + 1));

		if(deltaE > 750) deltaE = 750;
		this.economy += deltaE;

		if(this.economy > 500000) this.economy = 500000;
		if(this.economy < 0) this.economy = 0;

		System.out.println(this.name + ": " + this.economy);
	}

	public void declareWar(Empire other, boolean isRebellion, boolean isLiberation) {
		WarDeclaration war = new WarDeclaration(this, other, isRebellion, isLiberation);

		this.addWarDeclaration(war);
		other.addWarDeclaration(war);

		System.out.println(this.name + " has declared war on " + other.name);
	}

	public void launchCampaign(WarDeclaration war) {
		this.economy = (int)((double)this.economy * 0.97);
		Empire other;
		if(war.t0 == this) {
			other = war.t1;
		} else {
			other = war.t0;
			if(war.t1 != this) {
				System.out.println("campaign launched by " + this.name + ", who is not in the war? (" + war.t0.name + " v. " + war.t1.name);
			}
		}

		if(Math.random() * (Main.TICK - war.tick) > 25) {
			war.makePeace(Main.DEFAULT_PEACE_DURATION);
			return;
		}

		double econAdvantage = Math.min((double)this.economy / (other.getEconomy() + 1), 9);

		HexList controlledTiles = (HexList)this.hexList.clone();
		controlledTiles.addAll(this.occupiedTiles);

		for(int i = 0; i < controlledTiles.size(); i++) {
			HexList surroundingTiles = controlledTiles.get(i).getSurroundingHexes();
			for(int j = 0; j < surroundingTiles.size(); j++) {
				if(surroundingTiles.get(j).isControlledBy(other)) {
					//System.out.println(this.name + " ey");
					int surrAdvantage = 0;

					HexList surroundingTiles2ElectricBoogaloo = surroundingTiles.get(j).getSurroundingHexes();
					for(int k = 0; k < surroundingTiles2ElectricBoogaloo.size(); k++) {
						if(surroundingTiles2ElectricBoogaloo.get(k).isControlledBy(this)) {
							surrAdvantage++;
						}
					}

					boolean rngBlessed = (Math.random() * (surrAdvantage + 1)) > 1;
					//System.out.println(this.name + ": " + econAdvantage + ", " + surrAdvantage);
					boolean takesTerritory = (war.isRebellion == false && rngBlessed && Math.random() * econAdvantage * 3 > 1) || (war.isRebellion == true && war.t0 == this && rngBlessed && Math.random() * 3 == 0) || (war.isRebellion == true && war.t1 == this && rngBlessed && Math.random() * 10 == 0);
					if(takesTerritory) {
						if(surroundingTiles.get(j).getEmpire() == this) {
							surroundingTiles.get(j).getOccupier().unoccupyTile(surroundingTiles.get(j));
						} else if(surroundingTiles.get(j).getEmpire() == other) {
							this.occupyTile(surroundingTiles.get(j));
						}
					}
				}
			}
		}
	}

	public void coordinateWars() {
		for(int i = 0; i < this.wars.size(); i++) {
			this.launchCampaign(this.wars.get(i));
		}
	}

	public void addWarDeclaration(WarDeclaration war) {
		this.wars.add(war);
	}

	public void removeWarDeclaration(WarDeclaration war) {
		for(int i = 0; i < this.wars.size(); i++) {
			if(this.wars.get(i) == war) {
				this.wars.remove(i);
				return;
			}
		}
	}

	public void addPeaceDeal(PeaceDeal peace) {
		this.peaceDeals.add(peace);
	}

	public void removePeaceDeal(PeaceDeal what) {
		for(int i = 0; i < this.peaceDeals.size(); i++) {
			if(this.peaceDeals.get(i) == what) {
				this.peaceDeals.remove(i);
				return;
			}
		}
	}

	public void decideWars() {
		for(int i = 0; i < this.borderEmpires.size(); i++) {
			Empire other = this.borderEmpires.get(i);
			if(Main.TICK - this.foundingTick > 30 && this.economy > 1000 && (int)(Math.random() * 20 * Main.empires.size()) == 0 && this.economy > other.getEconomy()) {
				boolean canWar = true;
				for(int j = 0; j < this.wars.size(); j++) {
					if(this.wars.get(j).t0 == other || this.wars.get(j).t1 == other) {
						canWar = false;
						break;
					}
				}

				for(int j = 0; j < this.peaceDeals.size(); j++) {
					if(this.peaceDeals.get(j).t0 == other || this.peaceDeals.get(j).t1 == other) {
						canWar = false;
						break;
					}
				}

				System.out.println(canWar);

				if(canWar) {
					this.declareWar(other, false, false);
				}
			}
		}
	}

	public void addBorderEmpire(Empire other) {
		for(int i = 0; i < this.borderEmpires.size(); i++) {
			if(this.borderEmpires.get(i) == other) {
				return;
			}
		}

		this.borderEmpires.add(other);
	}

	public void removeBorderEmpire(Empire other) {
		for(int i = 0; i < this.borderEmpires.size(); i++) {
			if(this.borderEmpires.get(i) == other) {
				this.borderEmpires.remove(i);
				return;
			}
		}
	}

	public void annexEmpire(Empire other) {
		System.out.println(this.name + " has annexed " + other.name);
		for(int i = 0; i < Main.empires.size(); i++) {
			Empire e = Main.empires.get(i);

			HexList hexes = e.getTilesOccupiedBy(other);
			if(hexes.size() != 0) {
				for(int j = hexes.size() - 1; j >= 0; j--) {
					other.unoccupyTile(hexes.get(j));
				}
			}

			hexes = e.getOccupiedTiles(other);
			if(hexes.size() != 0) {
				for(int j = hexes.size() - 1; j >= 0; j--) {
					e.annexTile(hexes.get(j));
				}
			}
		}

		HexList hexes = other.getEmpireTiles();
		for(int i = hexes.size() - 1; i >= 0; i--) {
			this.annexTile(hexes.get(i));
		}

		other.dissolveEmpire();
	}

	public void dissolveEmpire() {
		for(int i = 0; i < Main.empires.size(); i++) {
			Empire e = Main.empires.get(i);
			e.removeBorderEmpire(this);
		}

		for(int i = 0; i < this.wars.size(); i++) {
			WarDeclaration war = this.wars.get(i);

			Empire other;
			if(war.t0 == this) {
				other = war.t1;
			} else {
				other = war.t0;
			}

			other.removeWarDeclaration(war);
			this.removeWarDeclaration(war);
		}

		for(int i = 0; i < Main.empires.size(); i++) {
			Empire e = Main.empires.get(i);
			e.removeBorderEmpire(this);

			if(e == this) {
				Main.empires.remove(i);
			}
		}
	}

	public void annexTile(Hex hex) {
		if(hex.getEmpire() != null && hex.getEmpire() == this) {
			System.out.println("oh god what just happened 0");
			return;
		}

		if(hex.getEmpire() != null) {
			hex.getEmpire().removeTileFromList(hex);
		}

		hexList.add(hex);
		hex.setEmpire(this);
		hex.setOccupier(null);
		hex.setColor(this.color);

		HexList surroundingTiles = hex.getSurroundingHexes();
		for(int i = 0; i < surroundingTiles.size(); i++) {
			if(surroundingTiles.get(i).getEmpire() != null && surroundingTiles.get(i).getEmpire() != this) {
				this.addBorderEmpire(surroundingTiles.get(i).getEmpire());
				surroundingTiles.get(i).getEmpire().addBorderEmpire(this);
			}
		}
	}

	public void occupyTile(Hex hex) {
		if(hex.getOccupier() != null && hex.getOccupier() == this) {
			System.out.println("oh god what just happened 1");
			return;
		}

		occupiedTiles.add(hex);
		hex.setOccupier(this);
		hex.setColor(new Color(this.color.getRed() + 140, this.color.getGreen() + 140, this.color.getBlue() + 140));

		if(hex.getEmpire().capital == hex) {
			this.annexEmpire(hex.getEmpire());
		}
	}

	public void unoccupyTile(Hex hex) {
		if(hex.getOccupier() == null) {
			System.out.println("oh god what just happened 2");
			return;
		}

		for(int i = 0; i < this.occupiedTiles.size(); i++) {
			if(this.occupiedTiles.get(i) == hex) {
				this.occupiedTiles.remove(i);
				break;
			}
		}

		hex.setOccupier(null);
		hex.setColor(hex.getEmpire().color);
	}

	public boolean expandEmpire() {
		boolean annexedStuff = false;

		for(int i = this.hexList.size() - 1; i >= 0; i--) { // Making this normal instead of reversed seems to behave like a clustering function...
			HexList surroundingTiles = this.hexList.get(i).getSurroundingHexes();
			for(int j = 0; j < surroundingTiles.size(); j++) {
				if(surroundingTiles.get(j).getEmpire() == null) {
					this.annexTile(surroundingTiles.get(j));
					annexedStuff = true;
				}
			}
		}

		return annexedStuff;
	}

	public HexList getEmpireTiles() {
		return this.hexList;
	}

	public HexList getOccupiedTiles(Empire other) {
		HexList out = new HexList();

		for(int i = 0; i < this.occupiedTiles.size(); i++) {
			Hex hex = this.occupiedTiles.get(i);

			if(hex.getEmpire() == other) {
				out.add(hex);
			}
		}

		return out;
	}

	public HexList getTilesOccupiedBy(Empire other) {
		HexList out = new HexList();

		for(int i = 0; i < this.hexList.size(); i++) {
			Hex hex = this.hexList.get(i);

			if(hex.getOccupier() == other) {
				out.add(hex);
			}
		}

		return out;
	}

	public void removeTileFromList(Hex hex) {
		for(int i = 0; i < this.hexList.size(); i++) {
			if(this.hexList.get(i) == hex) {
				this.hexList.remove(i);
				return;
			}
		}

		System.out.println("wasn\'t in the list lmao");
	}

	public int getEconomy() {
		return this.economy;
	}

	public double getStability() {
		return this.stability;
	}
}