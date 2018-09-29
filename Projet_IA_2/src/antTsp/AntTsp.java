package antTsp;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;
import main.Edge;
import main.Frame;
import main.Vertex;



public class AntTsp {
	
    public Ant m_ants[];
    public int m_bestPath[];
    public double m_probs[];
    public double m_distance[][];
    public double m_pheromones[][];

    public Random m_rand;
    public int m_antsNumber;
    public int m_currentVertex;
    public double m_bestPathCost;

	public int m_vertexNumber;
	public List<Vertex> m_vertexs;
	public Frame m_frame;

	
	public AntTsp(Frame frame) {        
		// Init variables
		init(frame);		
        // Add distances in the 2d array
		for(int row=0; row<m_vertexNumber; row++)
			for(int col=0; col<m_vertexNumber; col++) {
				if (row!=col)
					m_distance[row][col]=distance(m_vertexs.get(row), m_vertexs.get(col));
				else
					m_distance[row][col]=0;
			}    	
		// Initialize ants
        for (int ant=0; ant<m_antsNumber; ant++)
        	m_ants[ant]=new Ant(this);
	}
	
	
	public void init(Frame frame) {
		m_frame = frame;
		m_vertexs = frame.m_vertexs;
		m_vertexNumber = frame.m_vertexs.size();
		
		m_rand = new Random();
		m_currentVertex = 0;
        m_antsNumber = m_vertexNumber * 2;

        m_ants = new Ant[m_antsNumber];
        m_probs = new double[m_vertexNumber];
    	m_distance = new double[m_vertexNumber][m_vertexNumber];
        m_pheromones = new double[m_vertexNumber][m_vertexNumber];
	}
	
	
	public double distance(Vertex v1, Vertex v2) {
		return Math.sqrt((v2.m_x - v1.m_x)*(v2.m_x - v1.m_x)+(v2.m_y - v1.m_y)*(v2.m_y - v1.m_y));
	}
	
	
	public void launch() {
            compute();
	}


    // Modifier ici
    public int selectVertex(Ant ant) {
        if (m_rand.nextDouble()<0.01) {
            int t = m_rand.nextInt(m_vertexNumber - m_currentVertex);
            int j = -1;
            for (int i=0; i<m_vertexNumber; i++) {
                if (ant.m_visited.contains(i)==false)
                    j++;
                if (j == t)
                    return i;
            }
        }

        int row = ant.m_path[m_currentVertex];
        double d = 0.0;
        for(int col=0; col<m_vertexNumber; col++)
            if (ant.m_visited.contains(col)==false) {
            	double number = 1.0/m_distance[row][col];
                d += m_pheromones[row][col] * (number*number*number*number*number);            
            }
        for(int col=0; col<m_vertexNumber; col++) {
            if(ant.m_visited.contains(col))
            	m_probs[col] = 0.0;
            else {
            	double number= 1.0/m_distance[row][col];
                double n = m_pheromones[row][col]*(number*number*number*number*number);
                m_probs[col] = n/d;
            }
        }
        
        double r = m_rand.nextDouble();
        double sum = 0;
        for(int i=0; i<m_vertexNumber; i++) {
            sum += m_probs[i];
            if (sum >= r)
                return i;
        }
        
        return 0;
    }

    
    // Modifier ici
    public void updatePheromone() {
    	for(int row=0; row<m_vertexNumber; row++)
            for (int col=0; col<m_vertexNumber; col++)
            	m_pheromones[row][col]=m_pheromones[row][col]*0.5;
            
        for(int ant=0; ant<m_antsNumber; ant++) {
            double contribution=500/m_ants[ant].distance();
            for(int i=0; i<m_vertexNumber-1; i++) {
            	int row = m_ants[ant].m_path[i];
            	int col = m_ants[ant].m_path[i + 1];
            	m_pheromones[row][col] += contribution;
            }
            int row = m_ants[ant].m_path[m_vertexNumber - 1];
            int col = m_ants[ant].m_path[0];
            m_pheromones[row][col] += contribution;
        }
    }

    
    public void initAnts() {
    	m_currentVertex = -1;
        for (int i = 0; i<m_antsNumber; i++) {
        	m_ants[i].m_visited.clear();
        	m_ants[i].visited(m_rand.nextInt(m_vertexNumber));
        }
        m_currentVertex++;
    }

    
	public void initPheromone() {
	    for (int i=0; i<m_vertexNumber; i++)
	        for (int j = 0; j < m_vertexNumber; j++)
	        	m_pheromones[i][j] = 1.0;
    }
    

    public void compute() {
    	initPheromone();
    	
        for (int i=0; i<4500; i++) {
        	initAnts();
            while(m_currentVertex<m_vertexNumber-1) {
                for(int ant=0; ant<m_antsNumber; ant++)
                	m_ants[ant].visited(selectVertex(m_ants[ant]));
                m_currentVertex++;
            }
            updatePheromone();
            if (m_bestPath == null) {
            	m_bestPath = m_ants[0].m_path;
            	m_bestPathCost = m_ants[0].distance();
            }
            for(int ant=0; ant<m_antsNumber; ant++)
                if(m_ants[ant].distance()<m_bestPathCost) {
                	m_bestPathCost = m_ants[ant].distance();
                    m_bestPath = m_ants[ant].m_path;
                }

            Frame.m_edges.clear();
        	for(int j=1; j<m_bestPath.length; j++) {
        		Vertex v1 = m_vertexs.get(m_bestPath[j-1]);
        		Vertex v2 = m_vertexs.get(m_bestPath[j]);
        		Edge edge1 = new Edge(v1, v2);
            	Frame.m_edges.add(edge1);
        	}
        	Rectangle r = new Rectangle(0, 0, m_frame.getWidth(), m_frame.getHeight());
        	m_frame.paintImmediately(r);
        	System.out.println("i : "+i);
        }
    }
}