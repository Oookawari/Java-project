package 五子棋多人厮杀小游戏;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GameSceneButton extends JButton{
	GameSceneButton(String name){
		super(name);
		Icon icon = new ImageIcon("src/五子棋多人厮杀小游戏/按钮图片.jpg");
		Icon PressIcon = new ImageIcon("src/五子棋多人厮杀小游戏/按钮点击图片.jpg");
		this.setIcon(icon);
		this.setHorizontalTextPosition(JButton.CENTER);
		this.setVerticalTextPosition(JButton.CENTER);
		this.setFont(new Font("华文行楷", Font.BOLD, 24));
		this.setPreferredSize(new Dimension(150,80));
		this.setPressedIcon(PressIcon);
	}
	
}