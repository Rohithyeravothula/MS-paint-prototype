package softwareEngineeringTry;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.EnumSet;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class PaintScreen extends JPanel{

	public static void main(String[] args) {
	      JFrame window = new JFrame("PAINT");
	      PaintScreen content = new PaintScreen();
	      window.setContentPane(content);
	      window.setJMenuBar(content.getMenuBar());
	      window.pack();  
	      window.setResizable(false); 
	      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	      window.setLocation( 100, 100 );
	      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      window.setVisible(true);
	   }
	
	public static class Applet extends JApplet {
	      public void init() {
	         PaintScreen content = new PaintScreen();
	         content.setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
	         setContentPane( content );
	         setJMenuBar(content.getMenuBar());
	      }
	   }

    private enum Tool { CURVE, LINE, RECT, OVAL, FILLED_RECT, FILLED_OVAL, SMUDGE, ERASE }

    private final static EnumSet<Tool> SHAPE_TOOLS = EnumSet.range(Tool.LINE, Tool.FILLED_OVAL);
    
    private Tool currentTool = Tool.CURVE;

    private Color currentColor = Color.BLACK;

    private Color fillColor = Color.WHITE;

    private BufferedImage OSC = null;

    private boolean dragging;

    private int startX, startY;

    private int currentX, currentY;

    public PaintScreen() {
        setPreferredSize(new Dimension(640,480));
        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
     }

    public void paintComponent(Graphics g) {

        if (OSC == null)
           createOSC();

        g.drawImage(OSC,0,0,null);

        if (dragging && SHAPE_TOOLS.contains(currentTool)) {
           g.setColor(currentColor);
           putCurrentShape(g);
        }

     }

    private void createOSC() {
        OSC = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics osg = OSC.getGraphics();
        osg.setColor(fillColor);
        osg.fillRect(0,0,getWidth(),getHeight());
        osg.dispose();
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

	public JMenuBar getMenuBar() {
		// TODO Auto-generated method stub
		  JMenuBar menubar = new JMenuBar();
	      JMenu colorMenu = new JMenu("Color");
	      JMenu toolMenu = new JMenu("Tool");
	      menubar.add(colorMenu);
	      menubar.add(toolMenu);
	      ActionListener listener = new MenuHandler();
	      JMenuItem item;
	      item = new JMenuItem("Draw With Black");
	      item.addActionListener(listener);
	      colorMenu.add(item);
	      item = new JMenuItem("Draw With White");
	      item.addActionListener(listener);
	      colorMenu.add(item);
	      item = new JMenuItem("Draw With Red");
	      item.addActionListener(listener);
	      colorMenu.add(item);
	      item = new JMenuItem("Draw With Green");
	      item.addActionListener(listener);
	      colorMenu.add(item);
	      item = new JMenuItem("Draw With Blue");
	      item.addActionListener(listener);
	      colorMenu.add(item);
	      item = new JMenuItem("Draw With Yellow");
	      item.addActionListener(listener);
	      colorMenu.add(item);
	      item = new JMenuItem("Select Drawing Color...");
	      item.addActionListener(listener);
	      colorMenu.add(item);
	      colorMenu.addSeparator();
	      item = new JMenuItem("Fill With Color...");
	      item.addActionListener(listener);
	      colorMenu.add(item);
	      item = new JMenuItem("Curve");
	      item.addActionListener(listener);
	      toolMenu.add(item);
	      toolMenu.addSeparator();
	      item = new JMenuItem("Line");
	      item.addActionListener(listener);
	      toolMenu.add(item);
	      item = new JMenuItem("Rectangle");
	      item.addActionListener(listener);
	      toolMenu.add(item);
	      item = new JMenuItem("Oval");
	      item.addActionListener(listener);
	      toolMenu.add(item);
	      item = new JMenuItem("Filled Rectangle");
	      item.addActionListener(listener);
	      toolMenu.add(item);
	      item = new JMenuItem("Filled Oval");
	      item.addActionListener(listener);
	      toolMenu.add(item);
	      toolMenu.addSeparator();
	      item = new JMenuItem("Smudge");
	      item.addActionListener(listener);
	      toolMenu.add(item);
	      item = new JMenuItem("Erase");
	      item.addActionListener(listener);
	      toolMenu.add(item);
	      return menubar;
	}
	
	private class MenuHandler implements ActionListener {
		 public void actionPerformed(ActionEvent evt) {
		         String command = evt.getActionCommand();
		         if (command.equals("Select Drawing Color...")) {
		            Color newColor = JColorChooser.showDialog(PaintScreen.this, 
		                  "Select Drawing Color", currentColor);
		            if (newColor != null)
		               currentColor = newColor;
		         }
		         else if (command.equals("Fill With Color...")) {
		            Color newColor = JColorChooser.showDialog(PaintScreen.this, 
		                  "Select Fill Color", fillColor);
		            if (newColor != null) {
		               fillColor = newColor;
		               Graphics osg = OSC.getGraphics();
		               osg.setColor(fillColor);
		               osg.fillRect(0,0,OSC.getWidth(),OSC.getHeight());
		               osg.dispose();
		               PaintScreen.this.repaint();
		            }
		         }
		         else if (command.equals("Draw With Black"))
		            currentColor = Color.BLACK;
		         else if (command.equals("Draw With White"))
		            currentColor = Color.WHITE;
		         else if (command.equals("Draw With Red"))
		            currentColor = Color.RED;
		         else if (command.equals("Draw With Green"))
		            currentColor = Color.GREEN;
		         else if (command.equals("Draw With Blue"))
		            currentColor = Color.BLUE;
		         else if (command.equals("Draw With Yellow"))
		            currentColor = Color.YELLOW;
		         else if (command.equals("Curve"))
		            currentTool = Tool.CURVE;
		         else if (command.equals("Line"))
		            currentTool = Tool.LINE;
		         else if (command.equals("Oval"))
		             currentTool = Tool.OVAL;
		         else if (command.equals("Filled Rectangle"))
		             currentTool = Tool.FILLED_RECT;
		         else if (command.equals("Filled Oval"))
		             currentTool = Tool.FILLED_OVAL;
		         else if (command.equals("Smudge"))
		             currentTool = Tool.SMUDGE;
		         else if (command.equals("Erase"))
		             currentTool = Tool.ERASE;
		      }
	   }

		private class MouseHandler implements MouseListener, MouseMotionListener {
		          
		          int prevX, prevY; 
		          double[][] smudgeRed, smudgeGreen, smudgeBlue; 
		          
		          void applyToolAlongLine(int x1, int y1, int x2, int y2) {
		             Graphics g = OSC.getGraphics();
		             g.setColor(fillColor);  
		             int w = OSC.getWidth();  
		             int h = OSC.getHeight();  
		             int dist = Math.max(Math.abs(x2-x1),Math.abs(y2-y1));
		             double dx = (double)(x2-x1)/dist;
		             double dy = (double)(y2-y1)/dist;
		             for (int d = 1; d <= dist; d++) {
		                int x = (int)Math.round(x1 + dx*d);
		                int y = (int)Math.round(y1 + dy*d);
		                if (currentTool == Tool.ERASE) {
		                   g.fillRect(x-5,y-5,10,10);
		                   repaint(x-5,y-5,10,10);
		                }
		                else { 
		                   for (int i = 0; i < 7; i++)
		                        for (int j = 0; j < 7; j++) {
		                           int r = y + j - 3;
		                           int c = x + i - 3;
		                           if (!(r < 0 || r >= h || c < 0 || c >= w || smudgeRed[i][j] == -1)) {
		                              int curCol = OSC.getRGB(c,r);
		                              int curRed = (curCol >> 16) & 0xFF;
		                              int curGreen = (curCol >> 8) & 0xFF;
		                              int curBlue = curCol & 0xFF;
		                              int newRed = (int)(curRed*0.7 + smudgeRed[i][j]*0.3);
		                              int newGreen = (int)(curGreen*0.7 + smudgeGreen[i][j]*0.3);
		                              int newBlue = (int)(curBlue*0.7 + smudgeBlue[i][j]*0.3);
		                              int newCol = newRed << 16 | newGreen << 8 | newBlue;
		                              OSC.setRGB(c,r,newCol);
		                              smudgeRed[i][j] = curRed*0.3 + smudgeRed[i][j]*0.7;
		                              smudgeGreen[i][j] = curGreen*0.3 + smudgeGreen[i][j]*0.7;
		                              smudgeBlue[i][j] = curBlue*0.3 + smudgeBlue[i][j]*0.7;
		                           }
		                        }
		                     repaint(x-3,y-3,7,7);
		                  }
		               }
		               g.dispose();
		            }
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
			         dragging = true;
			         if (currentTool == Tool.ERASE) {
			            Graphics g = OSC.getGraphics();
			            g.setColor(fillColor);
			            g.fillRect(startX-5,startY-5,10,10);
			            g.dispose();
			            repaint(startX-5,startY-5,10,10);
			         }
			         else if (currentTool == Tool.SMUDGE) {
			            if (smudgeRed == null) {
			               smudgeRed = new double[7][7];
			               smudgeGreen = new double[7][7];
			               smudgeBlue = new double[7][7];
			            }
			            int w = OSC.getWidth();
			            int h = OSC.getHeight();
			            int x = evt.getX();
			            int y = evt.getY();
			            for (int i = 0; i < 7; i++)
			               for (int j = 0; j < 7; j++) {
			                  int r = y + j - 3;
			                  int c = x + i - 3;
			                  if (r < 0 || r >= h || c < 0 || c >= w) {
			                     smudgeRed[i][j] = -1;
			                  }
			                  else {
			                     int color = OSC.getRGB(c,r);
			                     smudgeRed[i][j] = (color >> 16) & 0xFF;
			                     smudgeGreen[i][j] = (color >> 8) & 0xFF;
			                     smudgeBlue[i][j] = color & 0xFF;
			                  }
			               }
			         }
				}

				@Override
				public void mouseReleased(MouseEvent evt) {
					// TODO Auto-generated method stub
					dragging = false;
			         if (SHAPE_TOOLS.contains(currentTool)) {
			            Graphics g = OSC.getGraphics();
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
			         if (currentTool == Tool.CURVE) {
			            Graphics g = OSC.getGraphics();
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
			            applyToolAlongLine(prevX,prevY,currentX,currentY);
			            System.out.println("Apply Tool Along Line Used");
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

