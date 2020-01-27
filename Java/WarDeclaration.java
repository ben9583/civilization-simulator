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

		for(int i = 0; i < occupiedByt0.size(); i++) {
			super.t0.annexTile(occupiedByt0.get(i));
		}

		for(int i = 0; i < occupiedByt1.size(); i++) {
			super.t1.annexTile(occupiedByt1.get(i));
		}

		PeaceDeal peace = new PeaceDeal(super.t0, super.t1, peaceLength);
		t0.addPeaceDeal(peace);
		t1.addPeaceDeal(peace);
		t0.removeWarDeclaration(this);
		t1.removeWarDeclaration(this);

		System.out.println("Peace has been made between " + super.t0.name + " and  " + super.t1.name);
	}
}