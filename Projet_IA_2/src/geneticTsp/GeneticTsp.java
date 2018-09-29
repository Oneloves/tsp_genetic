package geneticTsp;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import main.Vertex;
import main.Edge;
import main.Frame;

public class GeneticTsp {

	public int m_path[];
	public double m_fitness[];
	public int m_bestPath[];
	public List<int[]> m_populations;
	
	public int m_vertexNumber;
	public int m_populationSize;
	public List<Vertex> m_cities;

	public Frame m_frame;
	
	public double recordDistance = 999999999;
	
	
	public GeneticTsp(Frame frame) {    
		m_frame = frame;
		m_vertexNumber = frame.m_vertexs.size();
		m_cities = frame.m_vertexs;	
		m_populationSize = 1500;
		
		for(int i=0; i<15500; i++) {	
			m_populations = new ArrayList<>();
			m_path = new int[m_vertexNumber];
			m_fitness = new double[m_populationSize];
		
			for(int city=0; city<m_vertexNumber; city++)
				m_path[city] = city;
		
			for(int population=0; population<m_populationSize; population++)
				m_populations.add(RandomizeArray(m_path));
		
			System.out.println("i : "+i);
			calculateFitness();
			nextGeneration();			
            Frame.m_edges.clear();
        	for(int city=1; city<m_bestPath.length; city++) {
        		Vertex v1 = m_frame.m_vertexs.get(m_bestPath[city-1]);
        		Vertex v2 = m_frame.m_vertexs.get(m_bestPath[city]);
        		Edge edge1 = new Edge(v1, v2);
            	Frame.m_edges.add(edge1);
        	}
        	Rectangle r = new Rectangle(0, 0, m_frame.getWidth(), m_frame.getHeight());
        	m_frame.paintImmediately(r);
		}
	}

	
	public double distance(Vertex v1, Vertex v2) {
		return Math.sqrt((v2.m_x - v1.m_x)*(v2.m_x - v1.m_x)+(v2.m_y - v1.m_y)*(v2.m_y - v1.m_y));
	}
	
	
	public double computeDistance(List<Vertex> vertex, int[] path) {
		double sum = 0;
		for(int i=0; i<path.length-1; i++) {
			int cityAIndex = path[i];
			Vertex cityA = vertex.get(cityAIndex);
			int cityBIndex = path[i+1];
			Vertex cityB = vertex.get(cityBIndex);
			double d = distance(cityA, cityB);
			sum += d;
		}
		return sum;
	}
	
	
	public int[] RandomizeArray(int[] array){
		Random rgen = new Random();	
		for (int i=0; i<array.length; i++) {
		    int randomPosition = rgen.nextInt(array.length);
		    int temp = array[i];
		    array[i] = array[randomPosition];
		    array[randomPosition] = temp;
		}
		return array;
	}
	

	public void calculateFitness() {
		for(int i=0; i<m_populations.size(); i++) {
			double d=computeDistance(m_cities, m_populations.get(i));
			if(d<recordDistance) {
				recordDistance = d;
				m_bestPath=m_populations.get(i);
			}
			m_fitness[i]=1/(Math.pow(d, 8) + 1);
		}

		double sum = 0;
		for(int i=0; i<m_fitness.length; i++)
			sum+=m_fitness[i];
		for (int i=0; i<m_fitness.length; i++)
			m_fitness[i]=m_fitness[i] / sum;
	}
	
	
	public void nextGeneration() {
		List<int[]> newPopulation = new ArrayList<>();
		for (int i = 0; i < m_populations.size(); i++) {
			int[] pathA = randomPath(m_populations, m_fitness);
			int[] pathB = randomPath(m_populations, m_fitness);
			int[] path = cross(pathA, pathB);
			path = mutation(path, 0.01);
			newPopulation.add(path);
		}
		m_populations = newPopulation;
	}
	
	
	public int[] randomPath(List<int[]> list, double[] prob) {
		int index = 0;
		double rand = Math.random();
		while (rand > 0) {
			rand = rand - prob[index];
			index++;
		}
		index--;
		return list.get(index);
	}
	
	
	public int[] cross(int[] pathA, int[] pathB) {
		int start = ThreadLocalRandom.current().nextInt(0, pathA.length-1);
		int end = ThreadLocalRandom.current().nextInt(start+1, pathA.length);
		int[] newPath = Arrays.copyOfRange(pathA, start, end);
		
		List<Integer> newPath2 = new ArrayList<>();
		
		for(int i=0; i<newPath.length; i++)
			newPath2.add(newPath[i]);
		
		for(int i=0; i<pathB.length; i++) {
			int city = pathB[i];
			if (!newPath2.contains(city)) {
				newPath2.add(city);
			}
		}
		
		int[] tmp = new int[newPath2.size()];
		for(int i=0; i<newPath2.size(); i++)
			tmp[i] = newPath2.get(i);
		
		return tmp;
	}
	
	
	public int[] mutation(int[] path, double mutationRate) {
		for(int i=0; i<m_vertexNumber; i++) {
			if(Math.random()<mutationRate) {
				int indexA = ThreadLocalRandom.current().nextInt(0, path.length);
				int indexB = (indexA + 1) % m_vertexNumber;
				int tmp = path[indexA];
				path[indexA] = path[indexB];
				path[indexB] = tmp;
			}
		}
		return path;
	}
}