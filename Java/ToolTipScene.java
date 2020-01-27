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

			SceneVector location = new SceneVector((mouse.x - Main.X_PADDING + Main.X_UNIT_LENGTH / 4) / Main.X_UNIT_LENGTH, (mouse.y - 45 - Main.Y_PADDING + (((mouse.x - Main.X_PADDING + Main.X_UNIT_LENGTH / 4) / Main.X_UNIT_LENGTH) % 2 == 0 ? 0 : Main.Y_UNIT_LENGTH / 4)) / Main.Y_UNIT_LENGTH);
			Hex hex = Main.scene.getHexAt(location);
			g2.setColor(new Color(255, 255, 0));
			g2.fillRect(mouse.x, mouse.y - 45, 5, 5);
			if(hex != null) {
				g2.setColor(new Color(150, 150, 150));
				g2.fillRect(mouse.x, mouse.y, 200, 150);

				g2.setColor(new Color(0, 0, 0));
				g2.drawString("Empire: ", mouse.x + 5, mouse.y + 15);
				Empire emp = hex.getEmpire();
				if(emp == null) {
					g2.setColor(new Color(100, 100, 100));
					g2.drawString("null", mouse.x + 55, mouse.y + 15);
				} else {
					g2.setColor(emp.color);
					g2.drawString(emp.name, mouse.x + 55 , mouse.y + 15);
				}

				g2.setColor(new Color(0, 0, 0));
				g2.drawString("Occupier: ", mouse.x + 5, mouse.y + 30);
				emp = hex.getOccupier();
				if(emp == null) {
					g2.setColor(new Color(100, 100, 100));
					g2.drawString("null", mouse.x + 70, mouse.y + 30);
				} else {
					g2.setColor(emp.color);
					g2.drawString(emp.name, mouse.x + 70, mouse.y + 30);
				}

				emp = hex.getEmpire();
				if(emp == null) return;

				g2.setColor(new Color(0, 0, 0));
				g2.drawString("Economy: ", mouse.x + 5, mouse.y + 45);
				String txt = "" + emp.getEconomy();
				g2.drawString(txt, mouse.x + 70, mouse.y + 45);

				g2.drawString("Stability: ", mouse.x + 5, mouse.y + 60);
				txt = "" + emp.getStability();
				g2.drawString(txt, mouse.x + 70, mouse.y + 60);

				g2.drawString("At war with: ", mouse.x + 5, mouse.y + 75);
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
				g2.drawString(txt, mouse.x + 80, mouse.y + 75);
			}
		}
	}
}