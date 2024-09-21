package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

public class Main {
	public static void main(String[] args) {
		

		try {
			// aplicamos el tema Flatlaf
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new App();

			}

		});
	}

}
