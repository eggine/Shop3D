package ymd.ImageServer;

import java.awt.EventQueue;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				ImageViewer viewer = new ImageViewer();
				viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				viewer.setVisible(true);
			}
		});
	}
}

class ImageViewer extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8608757845151023967L;

	public ImageViewer() {
		mImageServer=new ImageServer();
		mServerThread=new Thread(mImageServer);
		mServerThread.start();
		
		setTitle("ImageServer");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		label = new JLabel();
		add(label);
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		JMenu menu = new JMenu("File");
		menubar.add(menu);
		JMenuItem openItem = new JMenuItem("Open");
		menu.add(openItem);
		JMenuItem exitItem = new JMenuItem("Close");
		menu.add(exitItem);
		
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int result = chooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					String name = chooser.getSelectedFile().getPath();
					label.setIcon(new ImageIcon(name));
				}
			}
		});
		
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
	}

	private JLabel label;
	private JFileChooser chooser;
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 400;
	private ImageServer mImageServer;
	private Thread mServerThread;
}