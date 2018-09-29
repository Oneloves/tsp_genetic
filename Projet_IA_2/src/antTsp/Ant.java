package antTsp;

import java.util.ArrayList;
import java.util.List;

public class Ant {

    public AntTsp m_antTsp;
    public int m_path[];
    public int m_vertexNumber; 
    public double m_distance[][];
    public List<Integer> m_visited;

    
    public Ant(AntTsp antTsp) {
    	m_antTsp = antTsp;
    	m_distance = antTsp.m_distance;
    	m_vertexNumber = antTsp.m_vertexNumber;
    	m_path = new int[antTsp.m_vertexNumber];
    	m_visited = new ArrayList<>();
    }
    
    
    public void visited(int vertex) {
    	m_path[m_antTsp.m_currentVertex + 1] = vertex;
        m_visited.add(vertex);
    }

 
    public double distance() {
    	int row = m_path[m_vertexNumber-1];
    	int col = m_path[0];
        double totalDistance = m_distance[row][col];
        for(int i=0; i<m_vertexNumber-1; i++) {
        	row = m_path[i];
        	col = m_path[i+1];
        	totalDistance += m_distance[row][col];
        }
        return totalDistance;
    }
}