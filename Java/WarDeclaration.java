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

public class WarDeclaration extends DiploObject {

	public final boolean isRebellion;
	public final boolean isLiberation;

	public WarDeclaration(Empire t0, Empire t1, boolean isRebellion, boolean isLiberation) {
		super(t0, t1);
		this.isRebellion = isRebellion;
		this.isLiberation = isLiberation;
	}

	public void makePeace(int peaceLength) {
		HexList occupiedByt0 = super.t0.getOccupiedTiles(super.t1);
		HexList occupiedByt1 = super.t1.getOccupiedTiles(super.t0);

		for(int i = occupiedByt0.size() - 1; i >= 0; i--) {
			//super.t0.annexTile(occupiedByt0.get(i));

			HexList cluster = occupiedByt0.get(i).getCluster(this.t0);

			if(cluster.get(0) == super.t0.capital) {
				for(int j = 0; j < cluster.size(); j++) {
					if(cluster.get(j).getEmpire() == t1) {
						super.t0.annexTile(cluster.get(j));
						occupiedByt0.remove(cluster.get(j));
					}
				}
			} else if(cluster.size() < 11) {
				for(int j = 0; j < cluster.size(); j++) {
					super.t0.unoccupyTile(cluster.get(j));
					occupiedByt0.remove(cluster.get(j));
				}
			} else {
				Empire newEmpire = new Empire(cluster.getRandomHex());
				for(int j = 0; j < cluster.size(); j++) {
					if(cluster.get(j) != newEmpire.capital) {
						newEmpire.annexTile(cluster.get(j));
						occupiedByt0.remove(cluster.get(j));
					}
				}
			}

			i = occupiedByt0.size() - 1;
		}

		for(int i = occupiedByt1.size() - 1; i >= 0; i--) {
			//super.t0.annexTile(occupiedByt0.get(i));

			HexList cluster = occupiedByt1.get(i).getCluster(this.t1);

			if(cluster.get(0) == super.t1.capital) {
				for(int j = 0; j < cluster.size(); j++) {
					if(cluster.get(j).getEmpire() == t0) {
						super.t1.annexTile(cluster.get(j));
						occupiedByt1.remove(cluster.get(j));
					}
				}
			} else if(cluster.size() < 11) {
				for(int j = 0; j < cluster.size(); j++) {
					super.t1.unoccupyTile(cluster.get(j));
					occupiedByt1.remove(cluster.get(j));
				}
			} else {
				Empire newEmpire = new Empire(cluster.getRandomHex());
				for(int j = 0; j < cluster.size(); j++) {
					if(cluster.get(j) != newEmpire.capital) {
						newEmpire.annexTile(cluster.get(j));
						occupiedByt1.remove(cluster.get(j));
					}
				}
			}

			i = occupiedByt1.size() - 1;
		}

		PeaceDeal peace = new PeaceDeal(super.t0, super.t1, peaceLength);
		t0.addPeaceDeal(peace);
		t1.addPeaceDeal(peace);
		t0.removeWarDeclaration(this);
		t1.removeWarDeclaration(this);

		System.out.println("Peace has been made between " + super.t0.name + " and  " + super.t1.name);
	}
}