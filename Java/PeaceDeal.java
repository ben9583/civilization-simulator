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

public class PeaceDeal extends DiploObject {

	public final int duration;

	public PeaceDeal(Empire t0, Empire t1, int duration) {
		super(t0, t1);
		this.duration = duration;
	}

	public boolean checkExpiration() {
		if(Main.TICK > super.tick + this.duration) {
			for(int i = 0; i < Main.empires.size(); i++) {
				Main.empires.get(i).removePeaceDeal(this);
			}
			System.out.println("Peace deal between " + super.t0.name + " and " + super.t1.name + " has expired");
			return true;
		}
		return false;
	}
}