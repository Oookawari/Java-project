package �����������ɱС��Ϸ;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GameSceneButton extends JButton{
	GameSceneButton(String name){
		super(name);
		Icon icon = new ImageIcon("src/�����������ɱС��Ϸ/��ťͼƬ.jpg");
		Icon PressIcon = new ImageIcon("src/�����������ɱС��Ϸ/��ť���ͼƬ.jpg");
		this.setIcon(icon);
		this.setHorizontalTextPosition(JButton.CENTER);
		this.setVerticalTextPosition(JButton.CENTER);
		this.setFont(new Font("�����п�", Font.BOLD, 24));
		this.setPreferredSize(new Dimension(150,80));
		this.setPressedIcon(PressIcon);
	}
	
}