package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import antTsp.AntTsp;
import geneticTsp.GeneticTsp;



public class Frame  extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	public List<Vertex> m_vertexs;
	public static List<Edge> m_edges;

	public Frame() {
        Dimension dim = new Dimension(640, 640);
        setPreferredSize(dim);
        setBackground(Color.black);
        addMouseListener(this);
        init();
	}
	
	public void init() {
		m_vertexs = new ArrayList<>();
		m_edges = new ArrayList<>();
	}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Traveling Sales Person Ants Algorithm");
            f.setResizable(false);
            f.add(new Frame(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }


    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;    
        
        for(Vertex vertex: m_vertexs) {
            g.setColor(Color.red);
            g.drawString(""+vertex.m_id, vertex.m_x, vertex.m_y);
            g.fillOval(vertex.m_x, vertex.m_y, 5, 5);
        }
        
        for(Edge edge: m_edges) {
            g.setColor(Color.green);
        	Vertex v1 = edge.m_v1;
        	Vertex v2 = edge.m_v2;
        	g.drawLine(v1.m_x, v1.m_y, v2.m_x, v2.m_y);
        }
        
    }
 
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == MouseEvent.BUTTON1) {
			m_vertexs.add(new Vertex(m_vertexs.size(), e.getX(), e.getY()));
			this.repaint();
		}
		
		if(e.getButton() == MouseEvent.BUTTON2) {}
		
		if(e.getButton() == MouseEvent.BUTTON3) {
			//LANCER LA RECHERCHE ICI
			//AntTsp antTsp = new AntTsp(this);
			//antTsp.launch();
			new GeneticTsp(this);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
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

}
