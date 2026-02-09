package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.building.Building;
import engine.mobile.building.UnitProducer;
import engine.process.GameBuilder;
import engine.process.MobileInterface;
import engine.process.chrono.*;

/**
 * 
 * @author LE RAY Yann
 *
 */
public class MainGUI extends JFrame implements Runnable {
	
	
	private static final long serialVersionUID = 1L;

	private Map map;

	private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);
	
	private String typeSelection;
	
	private MobileInterface manager;

	private GameDisplay dashboard;

	public MainGUI(String title) {
		super(title);
		init();
	}

	private void init() {
		
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		KeyControls keyControls = new KeyControls();
		
		//This part is used to see text input if wanted
		
		/*
		JTextField textField = new JTextField();
		textField.addKeyListener(keyControls);
		contentPane.add(textField, BorderLayout.SOUTH);
		*/
		
		
		
		//THIS PART IS FOR TEST ONLY WILL BE REMOVED

		javax.swing.JPanel RightPanel = new javax.swing.JPanel();
		RightPanel.setBackground(java.awt.Color.GRAY); 
		

		javax.swing.JButton testButton = new javax.swing.JButton("Test Bat");

		testButton.addActionListener(new ActionListener() { // bouton temporaire pour test
		    public void actionPerformed(ActionEvent e) {
		        manager.selectBuilding("PRODUCER");
		        typeSelection="build";
		    }
		});

		javax.swing.JButton testButton2 = new javax.swing.JButton("Test Unit");

		testButton2.addActionListener(new ActionListener() { // bouton temporaire pour test
		    public void actionPerformed(ActionEvent e) {
		        manager.selectUnit("INFANTRY");
		        typeSelection="unitAllie";
		    }
		});
		
		javax.swing.JButton testButton3 = new javax.swing.JButton("Test Unit ennemy");

		testButton3.addActionListener(new ActionListener() { // bouton temporaire pour test
		    public void actionPerformed(ActionEvent e) {
		        manager.selectUnit("INFANTRY");
		        typeSelection="unitEnnemy";
		    }
		});
		
		RightPanel.add(testButton);
		RightPanel.add(testButton2);
		RightPanel.add(testButton3);
		contentPane.add(RightPanel, BorderLayout.EAST);


		
		
		map = GameBuilder.buildMap();
		manager = GameBuilder.buildInitMobile(map);
		dashboard = new GameDisplay(map, manager);
		

		MouseControls mouseControls = new MouseControls();
		dashboard.addMouseListener(mouseControls);

		dashboard.setPreferredSize(preferredSize);
		contentPane.add(dashboard, BorderLayout.CENTER);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setPreferredSize(preferredSize);
		setResizable(false);
	}
	
	
	@Override
	public void run() {
		manager.firstRound();
		while (true) {
			try {
				Thread.sleep(GameConfiguration.GAME_SPEED);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}

			manager.nextRound();
			dashboard.repaint();
		}
	}

	private class KeyControls implements KeyListener {

		//this part is for the keybord interaction 
		@Override
		public void keyPressed(KeyEvent event) {
			char keyChar = event.getKeyChar();
			switch (keyChar) {

			default:
				break;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}

	private class MouseControls implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			int blockSize = GameConfiguration.BLOCK_SIZE;
	        int line = e.getY() / blockSize;
	        int column = e.getX() / blockSize;
	        System.out.println("x :"+e.getX()+" y :"+e.getY()+" - Block line : "+line+" Block column : "+column);
	        Block position = map.getBlock(line, column);	             
	         if(typeSelection!=null && typeSelection.equals("build")) {
	            manager.buildBuilding(position);
	         }
	         if(typeSelection!=null && typeSelection.equals("unitAllie")) {
	        	 manager.spawnUnit(position);
	         }if(typeSelection!=null && typeSelection.equals("unitEnnemy")) {
	        	 manager.spawnUnitEnnemy(position);
	        }else {
	        	for (Building building : manager.getBuildings()) {
		             if(building.getPosition().getLine()==line && building.getPosition().getColumn()==column) {
		            	 if(building instanceof UnitProducer && !building.getIsUnderConstruction()) {
			            	 manager.addQueue((UnitProducer) building,building.getPosition());
		            	 }
		             }
		         }
	        }
		}   

		@Override
		public void mousePressed(MouseEvent e) {
			
			Block firstBlock=manager.getMousePosition(e.getY(), e.getX());
			manager.unitMoveOrder(firstBlock);
			manager.initSelectedArea(firstBlock);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Block lastBlock=manager.getMousePosition(e.getY(), e.getX());
			manager.calculateSelectedArea(lastBlock);
			manager.unitsInSelectedArea();
			
	        
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
	}
	
	
	

}
