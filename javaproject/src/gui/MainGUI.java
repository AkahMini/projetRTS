package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;

import config.DefaultGameSettings;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.Player;
import engine.mobile.unit.Unit;
import engine.mobile.unit.UnitsStatsLoader;
import engine.process.GameBuilder;
import engine.process.MenuInterface;
import engine.process.MobileInterface;
import engine.process.chrono.Chronometer;
import gui.instrument.ChartManager;
import log.LoggerUtility;
//import gui.instrument.ChartManager;
/**
 * 
 * Main graphic class that create the game window and manage the different user input.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */
public class MainGUI extends JFrame implements Runnable {

	public DefaultGameSettings gameSettings = new DefaultGameSettings();

	private static final long serialVersionUID = 1L;

	private Map map;

	private boolean stop = false;
	
	private boolean running = true;
	
	private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);

	private MobileInterface manager;
	private MenuInterface menu;
	private static Logger logger = LoggerUtility.getLogger(MainGUI.class, "html");

	private GameDisplay dashboard;
	
	private ChartPanel unitChart;
	private ChartManager chartManager = new ChartManager();


	
	//launch the game with MENU as the current state
	private String currentState=GameConfiguration.GAMESTATE.get(0);

	public MainGUI(String title) {
		super(title);
		init();
	}

	private void init() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		javax.swing.JPanel chartContainer = new javax.swing.JPanel();
		chartContainer.setLayout(new javax.swing.BoxLayout(chartContainer, javax.swing.BoxLayout.Y_AXIS));
		chartContainer.setOpaque(false);

		

		map = GameBuilder.buildMap();
		menu = GameBuilder.buildInitMenu(this.gameSettings);
		unitChart = chartManager.getChartPanel();
		unitChart.setOpaque(true);
		unitChart.setBackground(java.awt.Color.BLACK);
		
		dashboard = new GameDisplay(map, manager, menu,chartManager.getChart());
		dashboard.setChartManager(chartManager);
		

		MouseControls mouseControls = new MouseControls();
		dashboard.addMouseListener(mouseControls);
		dashboard.setPreferredSize(preferredSize);

		
		dashboard.setBounds(0, 0, GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);
		contentPane.add(dashboard, BorderLayout.CENTER);

		
		this.addKeyListener(new KeyControls());
		setFocusable(true);
		requestFocusInWindow();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setPreferredSize(preferredSize);
		setResizable(false);
	}


	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(gameSettings.getEffectiveGameSpeed());
			} catch (InterruptedException e) {
				logger.fatal(e);
			}
			switch(currentState) {
				case "MENU":
					menu.updateMenu(currentState);
					break;
				case "CHOOSE":
					menu.updateMenu(currentState);
					break;
				case "PLAYING":
					menu.updateMenu(currentState);
					
					if(!stop) {
						manager.nextRound();
					}
					if(!manager.winningFaction().equals("null")) {
						menu.setWinnerFac(manager.winningFaction());
						System.out.println("in menu WinnerFac : "+manager.winningFaction());
						currentState=GameConfiguration.GAMESTATE.get(3);
					}
					break;
				case "END":
					menu.updateMenu(currentState);
					break;
			}
			dashboard.repaint();
		}
		ExitGame();
	}

	public void startGame() {
		logger.info("Game started");
		boolean istop=false;
		if(manager!=null) {
			if(manager.isGameStoped()) {
				istop=true;
			}
		}
		manager=null;
		logger.info(menu.getSelectedMode());
		manager = GameBuilder.buildInitMobile(map,this.gameSettings,menu.getSelectedFaction(),menu.getSelectedMode());
		manager.setChartManager(chartManager);
		
		if(istop) {
			manager.setIsGameStoped(true);
		}
		dashboard.resetManager(manager);
	    manager.firstRound();
	    dashboard.setFogOfWar(manager.getGameSettings().isFogOfWar());
	    currentState = GameConfiguration.GAMESTATE.get(2);
	    logger.info("jeu lancé");
	}
	
	private void ExitGame() {
		//if this message is  here = the game closed proprely
		logger.info("Game exited");
	    //put here method for saving or for stopping other thread
	    System.exit(0);
	}
	
	private class KeyControls implements KeyListener {

		//this part is for the keybord interaction 
		@Override
		public void keyPressed(KeyEvent event) {
			int keyCode = event.getKeyCode();
			switch (keyCode) {
			
			case KeyEvent.VK_ENTER:
				if (currentState.equals("MENU")) {
					currentState=GameConfiguration.GAMESTATE.get(1);
					break;
				} else if (currentState.equals("CHOOSE")) {
					startGame();
					break;
				} else if (currentState.equals("END")) {
					currentState=GameConfiguration.GAMESTATE.get(0);
					break;
				}
				break;
				
			case KeyEvent.VK_ESCAPE:
				if (currentState.equals("PLAYING")) {
					if (!stop) {
						stop = true;
					} else {
						stop = false;
					}
					manager.setIsGameStoped(stop);
					dashboard.repaint();
					break;
				}else if(currentState.equals("CHOOSE")) {
					currentState=GameConfiguration.GAMESTATE.get(0);
					break;
				}else if(currentState.equals("MENU") || currentState.equals("END")) {
					running=false;
				}
	            break;
	        
			case KeyEvent.VK_1:
				if (currentState.equals("CHOOSE")) {
					menu.setSelectedFaction("Zeus");
				}
				break;
				
			case KeyEvent.VK_2:
				if (currentState.equals("CHOOSE")) {
					menu.setSelectedFaction("Hades");
				}
				break;	
				
			case KeyEvent.VK_3:
				if (currentState.equals("CHOOSE")) {
					menu.setSelectedFaction("Poseidon");
				}
				break;
				
			case KeyEvent.VK_S:
				if (currentState.equals("CHOOSE")) {
					menu.setselectedMode(0);
				}
				break;
			case KeyEvent.VK_D:
				if (currentState.equals("CHOOSE")) {
					menu.setselectedMode(2);
				}
				break;
				
			case KeyEvent.VK_Q:
				if (currentState.equals("PLAYING")) {
					if(stop) {
						ExitGame();
					}
				}
				break;
			
			case KeyEvent.VK_A:
				if (currentState.equals("PLAYING")) {
					if(stop) {
						//do here the event for end the game ( a loose, this input is considered as giving up the game)
						currentState=GameConfiguration.GAMESTATE.get(3);
					}
				}
				break;
			case KeyEvent.VK_G:
				if (currentState.equals("PLAYING")) {
					if(stop) {
						if(manager.isAltGui()) {
							manager.setAltGui(false);
						}else {
							manager.setAltGui(true);
						}
					}
				}
				break;
				
			case KeyEvent.VK_F:
				if (currentState.equals("CHOOSE")) {
					menu.setselectedMode(1);
				}else if(currentState.equals("PLAYING")) {
					if(dashboard.getFogOfWar()) {
						dashboard.setFogOfWar(false);
					}else {
						dashboard.setFogOfWar(true);
					}
				}
				break;
			
				
			//inGame Cheats
			case KeyEvent.VK_M:
				if (currentState.equals("PLAYING")) {
					manager.motherload();
				}
				break;
			case KeyEvent.VK_I:
				if(manager.getGameSettings().isCPUActivated())
				manager.getGameSettings().setCPUActivated(false);
				else
				manager.getGameSettings().setCPUActivated(true);
				break;
			case KeyEvent.VK_W:
				if (currentState.equals("PLAYING")) {
					currentState="END";
				}
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
		}
		

		@Override
		public void mousePressed(MouseEvent e) {
			
			Block firstBlock=manager.getMousePosition(e.getY(), e.getX());
			manager.initSelectedArea(firstBlock);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
			int blockSize = GameConfiguration.BLOCK_SIZE;
			int line = e.getY() / blockSize;
			int column = e.getX() / blockSize;
			

			Block position = map.getBlock(line, column);
			if(manager.getSelectedArea().get(0).equals(position)) {
				shortClick(e,position);
			}
			else {
				longClick(e,position);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
		

		public void shortClick(MouseEvent e, Block position) {
			int blockSize = GameConfiguration.BLOCK_SIZE;
			int line = e.getY() / blockSize;
			int column = e.getX() / blockSize;
			//System.out.println("x :"+e.getX()+" y :"+e.getY()+" - Block line : "+line+" Block column : "+column);
			
			//we check if the player clicked in the button zone for x and y
			boolean xZone = (e.getX()>=1020 && e.getX()<=1240);
			boolean yZone = (e.getY()>=560 && e.getY()<=700);
			if(xZone && yZone) {
				manager.areaButtonPressed(e.getX(),e.getY());
			}

			//Move all units
			if(manager.ifBlockInGamePanel(position)) {
				manager.unitMoveOrder(position);
			}
			
			

			String buildingID = manager.getPlayer().getBuildingToBuildID();
			if(buildingID!=null&&manager.ifBlockInGamePanel(position)){
				manager.getPlayer().setBuildingToBuildID(buildingID);
				manager.buildBuilding(position,manager.getSelectedTier(),manager.getPlayer().getFactionName(),manager.getPlayer());
				if(manager.getSelectedWorker()!=null) {
					manager.addUnitsInSelectedArea(manager.getSelectedWorker());
					manager.unitMoveOrder(position);
					manager.setSelectedWorker(null);
				}
				manager.getPlayer().setBuildingToBuildID(null);
			}
			
			//create 4x4 selection, because only the top left block of a building is recognized as a building
			Block firstBlock = manager.getSelectedArea().get(0);
			line = position.getLine()-1;
			column = position.getColumn()-1;
			manager.calculateSelectedArea(map.getBlock(line, column));
			manager.getBuildingsInSelectedArea();
			

			ArrayList<Block> newSelection = new ArrayList<Block>();
			newSelection.add(firstBlock);
			manager.setSelectedArea(newSelection);
			manager.calculateSelectedArea(position);
			manager.getUnitsInSelectedArea();
		
		}
		public void longClick(MouseEvent e,Block position) {
			manager.calculateSelectedArea(position);
			manager.getUnitsInSelectedArea();
			manager.getBuildingsInSelectedArea();
		}
	}

	public DefaultGameSettings getGameSettings() {
		return gameSettings;
	}




}
