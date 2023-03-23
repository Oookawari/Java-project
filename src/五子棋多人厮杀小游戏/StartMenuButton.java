package 五子棋多人厮杀小游戏;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class StartMenuButton extends JButton{
	public StartMenuButton(String name) {
		super(name);
		this.setPreferredSize(new Dimension(190, 80));
	    Icon icon = new ImageIcon("src/五子棋多人厮杀小游戏/按钮2.jpg");
		Icon PressIcon = new ImageIcon("src/五子棋多人厮杀小游戏/按钮.jpg");
		this.setIcon(icon);
		this.setPressedIcon(PressIcon);
		this.setHorizontalTextPosition(JButton.CENTER);
		this.setVerticalTextPosition(JButton.CENTER);
		this.setFont(new Font("华文行楷", Font.BOLD, 24));
	}
}
