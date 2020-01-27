public class ToolTipThread implements Runnable {
	public ToolTipThread() {

	}

	public void run() {
		ToolTipScene scene = new ToolTipScene();

		Main.frame.getContentPane().add(scene);

		while(true) {
			try {
				Thread.sleep(5);
			} catch(InterruptedException e) {
				System.out.println("bruh");
			}

			Main.frame.revalidate();
			Main.frame.repaint();
		}
	}
}