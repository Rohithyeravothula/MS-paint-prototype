package SoftwareEngineeringFinal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PaintBoardScreen extends JPanel{

	public static void main(String[] args){
		
		
		  JFrame board = new JFrame("PAINT SOFTWARE 1.0");
		  
		  try
	        {
	            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        }
	        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
	        {
	            System.out.println("Unable to set Look and feel : "+e);             
	        }
  	      board.setIconImage(Toolkit.getDefaultToolkit().getImage("F:/Documents/Eclipse Workspace1/PaintSoftware/src/softwareEngineeringTry/images/Paint-icon.png"));
          PaintBoardScreen content = new PaintBoardScreen();
	      board.setContentPane(content);
	      board.setJMenuBar(content.getMenuBar());
	      board.pack();  
	      board.setResizable(true); 
	      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	      board.setLocation( 0, 0 );
	      board.setSize(1366, 768);
	      board.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      board.setVisible(true);
	}
	
	public PaintBoardScreen(){
		this.setBackground(Color.WHITE);
		//this.setSize(new Dimension(1366, 768));
		MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
     }
	
	public static class Applet extends JApplet {
	      public void init() {
	         PaintBoardScreen content = new PaintBoardScreen();
	         content.setBorder(BorderFactory.createLineBorder(Color.GRAY,5));
	         setContentPane( content );
	         setJMenuBar(content.getMenuBar());
	      }
	   }

	private enum Shape { CURVE, LINE, RECT, OVAL, FILLED_RECT, FILLED_OVAL, SMUDGE, ERASE }

    private final static EnumSet<Shape> SHAPE_TOOLS = EnumSet.range(Shape.LINE, Shape.FILLED_OVAL);
    
    private Shape currentTool = Shape.CURVE;

    private Color currentColor = Color.BLACK;

    private Color fillColor = Color.WHITE;

    private BufferedImage drawImage = null;

    private boolean drag;

    private int startX, startY;

    private int currentX, currentY;
    
	public JMenuBar getMenuBar(){
		// TODO Auto-generated method stub
		  JMenuBar menubar = new JMenuBar();
	      JMenu colorMenu = new JMenu("Drawing Color");
	      JMenu toolMenu = new JMenu("Shape");
	      JMenu fileMenu = new JMenu("File");
	      JMenu About = new JMenu("About");
		  menubar.add(fileMenu);
	      menubar.add(colorMenu);
	      menubar.add(toolMenu);
	      menubar.add(About);
	      
	      ActionListener listener = new MenuHandler();
	      
	      JMenuItem item;
	      item = new JMenuItem("New");
	      item.addActionListener(new ActionListener(){
	    	  public void actionPerformed(ActionEvent e){
	    		  setBackground(Color.WHITE);
	    	  }
	      });
	      item.setMnemonic(KeyEvent.VK_N);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	      item.setToolTipText("New Paint Board");  
	      fileMenu.add(item);
	      item = new JMenuItem("Save");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_S);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
	      item.setToolTipText("Save Paint Board");  
	      fileMenu.add(item);
	      item = new JMenuItem("Quit");
	      item.addActionListener(new ActionListener(){
	    	  public void actionPerformed(ActionEvent e){
	    		  System.exit(0);
	    	  }
	      });
	      item.setMnemonic(KeyEvent.VK_Q);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
	      item.setToolTipText("Exit Paint Board");  
	      fileMenu.add(item);
	      
	      item = new JMenuItem("Black");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_B);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.SHIFT_MASK));
	      item.setToolTipText("Select Black Color");  
	      colorMenu.add(item);
	      item = new JMenuItem("White");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_W);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.SHIFT_MASK));
	      item.setToolTipText("Select White Color");  
	      colorMenu.add(item);
	      item = new JMenuItem("Red");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_R);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.SHIFT_MASK));
	      item.setToolTipText("Select Red Color");  
	      colorMenu.add(item);
	      item = new JMenuItem("Green");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_G);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.SHIFT_MASK));
	      item.setToolTipText("Select Green Color");  
	      colorMenu.add(item);
	      item = new JMenuItem("Blue");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_1);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.SHIFT_MASK));
	      item.setToolTipText("Select Blue Color");  
	      colorMenu.add(item);
	      item = new JMenuItem("Yellow");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_Y);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.SHIFT_MASK));
	      item.setToolTipText("Select Yellow Color");  
	      colorMenu.add(item);
	      item = new JMenuItem("Select Drawing Color...");
	      item.addActionListener(listener);
	      colorMenu.add(item);
	      colorMenu.addSeparator();
	      item = new JMenuItem("Fill Screen With Color...");
	      item.addActionListener(listener);
	      colorMenu.add(item);
	      
	      item = new JMenuItem("Curve");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_C);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
	      item.setToolTipText("Select Curve");  
	      toolMenu.add(item);
	      toolMenu.addSeparator();
	      item = new JMenuItem("Line");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_L);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
	      item.setToolTipText("Select Line");  
	      toolMenu.add(item);
	      item = new JMenuItem("Rectangle");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_1);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
	      item.setToolTipText("Select Rectangle");  
	      toolMenu.add(item);
	      item = new JMenuItem("Oval");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_C);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
	      item.setToolTipText("Select Oval");  
	      toolMenu.add(item);
	      item = new JMenuItem("Filled Rectangle");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_R);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
	      item.setToolTipText("Select Filled Rectangle");  
	      toolMenu.add(item);
	      item = new JMenuItem("Filled Oval");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_O);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
	      item.setToolTipText("Select Filled Oval");  
	      toolMenu.add(item);
	      toolMenu.addSeparator();
	      item = new JMenuItem("Erase");
	      item.addActionListener(listener);
	      item.setMnemonic(KeyEvent.VK_E);
	      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
	      item.setToolTipText("Select Erase");  
	      toolMenu.add(item);
	      
	      menubar.add(About);
	      item = new JMenuItem("Info");
	        About.add(item);
	        item.setMnemonic(KeyEvent.VK_F9);
	        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, ActionEvent.SHIFT_MASK));
	        item.setToolTipText("Details of the Software");  
	        item.addActionListener(new ActionListener(){

	        	public void actionPerformed(ActionEvent e) {
	            	JOptionPane.showMessageDialog(null, "Paint Version 1.0 \n Praneeth A S \n Rohit Yeravothula");
	            } 
	        });
	        

	      return menubar;
		}
	
	private class MenuHandler implements ActionListener {
		 public void actionPerformed(ActionEvent evt) {
		         String command = evt.getActionCommand();
		         if (command.equals("Select Drawing Color...")) {
		            Color newColor = JColorChooser.showDialog(PaintBoardScreen.this, 
		                  "Select Drawing Color", currentColor);
		            if (newColor != null)
		               currentColor = newColor;
		         }
		         else if (command.equals("Fill Screen With Color...")) {
		            Color newColor = JColorChooser.showDialog(PaintBoardScreen.this, 
		                  "Select Fill Color", fillColor);
		            if (newColor != null) {
		               fillColor = newColor;
		               Graphics graphics = drawImage.getGraphics();
		               graphics.setColor(fillColor);
		               graphics.fillRect(0,0,drawImage.getWidth(),drawImage.getHeight());
		               graphics.dispose();
		               PaintBoardScreen.this.repaint();
		            }
		         }
		         else if(command.equals("Save")){
		        	 JFileChooser choose = new JFileChooser();
		        	 choose.setMultiSelectionEnabled(true);
		        	 choose.setAcceptAllFileFilterUsed(true);
		        	 FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG File", ".jpg");
		        	 FileNameExtensionFilter filter1 = new FileNameExtensionFilter("PNG File", ".png");
		        	 choose.setFileFilter(filter);
		        	 choose.setFileFilter(filter1);
		        	 JTextArea log;
		        	 final String newline = "\n";
		        	 log = new JTextArea(5,20);
		             log.setMargin(new Insets(5,5,5,5));
		             log.setEditable(false);
		             JScrollPane logScrollPane = new JScrollPane(log);
		             Graphics2D graphics = drawImage.createGraphics();
		             int returnVal = choose.showSaveDialog(PaintBoardScreen.this);
		             if (returnVal == JFileChooser.APPROVE_OPTION) {
		                 File file = choose.getSelectedFile();
		                 
		             } else {
		                 
		             }
		             log.setCaretPosition(log.getDocument().getLength());
		         }
		         else if (command.equals("Black"))
		            currentColor = Color.BLACK;
		         else if (command.equals("White"))
		            currentColor = Color.WHITE;
		         else if (command.equals("Red"))
		            currentColor = Color.RED;
		         else if (command.equals("Green"))
		            currentColor = Color.GREEN;
		         else if (command.equals("Blue"))
		            currentColor = Color.BLUE;
		         else if (command.equals("Yellow"))
		            currentColor = Color.YELLOW;
		         else if (command.equals("Curve"))
		            currentTool = Shape.CURVE;
		         else if (command.equals("Line"))
		            currentTool = Shape.LINE;
		         else if (command.equals("Oval"))
		        	 currentTool = Shape.OVAL;
		         else if (command.equals("Rectangle"))
		             currentTool = Shape.RECT;
		          else if (command.equals("Filled Rectangle"))
		             currentTool = Shape.FILLED_RECT;
		         else if (command.equals("Filled Oval"))
		             currentTool = Shape.FILLED_OVAL;
		         else if (command.equals("Erase"))
		             currentTool = Shape.ERASE;
		      }
	   }

	public void paintComponent(Graphics g) {

        if (drawImage == null){
           createImage();
           //System.out.println("New Board");
        }

        g.drawImage(drawImage,0,0,null);

        if (drag && SHAPE_TOOLS.contains(currentTool)) {
           g.setColor(currentColor);
           putCurrentShape(g);
        }

     }

    private void createImage() {
    	drawImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics graphics = drawImage.getGraphics();
        graphics.setColor(fillColor);
        graphics.fillRect(0,0,getWidth(),getHeight());
        graphics.dispose();
     }

    private void putCurrentShape(Graphics g) {
        switch (currentTool) {
        case LINE:
           g.drawLine(startX, startY, currentX, currentY);
           break;
        case OVAL:
           putOval(g,false,startX, startY, currentX, currentY);
           break;
        case RECT:
           putRect(g,false,startX, startY, currentX, currentY);
           break;
        case FILLED_OVAL:
           putOval(g,true,startX, startY, currentX, currentY);
           break;
        case FILLED_RECT:
           putRect(g,true,startX, startY, currentX, currentY);
           break;
        }
     }

    private void putRect(Graphics g, boolean filled, int x1, int y1, int x2, int y2) {
        if (x1 == x2 || y1 == y2)
           return;
        if (x2 < x1) { 
           int temp = x1;
           x1 = x2;
           x2 = temp;
        }
        if (y2 < y1) { 
           int temp = y1;
           y1 = y2;
           y2 = temp;
        }
        if (filled)
           g.fillRect(x1,y1,x2-x1,y2-y1);
        else
           g.drawRect(x1,y1,x2-x1,y2-y1);
     }

    private void putOval(Graphics g, boolean filled, int x1, int y1, int x2, int y2) {
        if (x1 == x2 || y1 == y2)
           return;
        if (x2 < x1) { 
           int temp = x1;
           x1 = x2;
           x2 = temp;
        }
        if (y2 < y1) {  
           int temp = y1;
           y1 = y2;
           y2 = temp;
        }
        if (filled)
           g.fillOval(x1,y1,x2-x1,y2-y1);
        else
           g.drawOval(x1,y1,x2-x1,y2-y1);
     }

    private void repaintRect(int x1, int y1, int x2, int y2) {
        if (x2 < x1) { 
           int temp = x1;
           x1 = x2;
           x2 = temp;
        }
        if (y2 < y1) {  
           int temp = y1;
           y1 = y2;
           y2 = temp;
        }
        x1--;
        x2++;
        y1--;
        y2++;
        repaint(x1,y1,x2-x1,y2-y1);
     }

		private class MouseHandler implements MouseListener, MouseMotionListener {
        
        int prevX, prevY; 
        
        @Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		

		@Override
		public void mousePressed(MouseEvent evt) {
			// TODO Auto-generated method stub
			startX = prevX = currentX = evt.getX();
	         startY = prevY = currentY = evt.getY();
	         drag = true;
	         if (currentTool == Shape.ERASE) {
	            Graphics g = drawImage.getGraphics();
	            g.setColor(fillColor);
	            g.fillRect(startX-5,startY-5,10,10);
	            g.dispose();
	            repaint(startX-5,startY-5,10,10);
	         }
	    		}

		@Override
		public void mouseReleased(MouseEvent evt) {
			// TODO Auto-generated method stub
			drag = false;
	         if (SHAPE_TOOLS.contains(currentTool)) {
	            Graphics g = drawImage.getGraphics();
	            g.setColor(currentColor);
	            putCurrentShape(g);
	            g.dispose();
	            repaint();
	         }
		}
		@Override
		public void mouseDragged(MouseEvent evt) {
			// TODO Auto-generated method stub
			currentX = evt.getX();
	         currentY = evt.getY();
	         drag = true;
	         if (currentTool == Shape.CURVE) {
	            Graphics g = drawImage.getGraphics();
	            g.setColor(currentColor);
	            g.drawLine(prevX,prevY,currentX,currentY);
	            g.dispose();
	            repaintRect(prevX,prevY,currentX,currentY);
	         }
	         else if (SHAPE_TOOLS.contains(currentTool)) {
	            repaintRect(startX,startY,prevX,prevY);
	            repaintRect(startX,startY,currentX,currentY);
	         }
	         else {
	         //   applyToolAlongLine(prevX,prevY,currentX,currentY);
	         }
	         prevX = currentX;
	         prevY = currentY;

		}
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}