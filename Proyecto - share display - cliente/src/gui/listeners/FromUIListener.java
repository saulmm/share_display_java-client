package gui.listeners;

import java.awt.Dimension;

public interface FromUIListener {
	public void onReady(Dimension dim, String name, ToUIListener toUI);
}
