package Parser;

import java.io.*;
import java.util.*;

import Graph.HyperArc;
import Graph.HyperGraph;
import Graph.Node;
import Graph.Solution;


public class Parser {
	
		private BufferedWriter bwHg, bwDot;
    	private FileWriter fwHg, fwDot;
		private BufferedReader bufferedReader = null;
		private FileReader fileReader = null;
    	
		private List<HyperArc> hyperArcsSol;
		private Map<String, Node> nodesMin = new HashMap<String, Node>();
		
    	private File file;
		private HyperGraph graph = null;
		private String nameF;

		/**
		 * Receives a file as a parameter and verifies whether
		 * it has been written in the proper manner
		 * 
		 * @param file
		 * @return HyperGraph
		 * @throws IOException
		 * @throws Exception
		 */
	    public HyperGraph readFiles(File file, String nameFile) throws IOException, Exception{
	    	// me guardo el File para despues utilizarlo a la hora de hacer los min.dot y min.hg
	    	this.file = file;

	        String data[];
	        String currentLine;
	        BufferedReader bufferedReader = null;
	        HyperArc hyperarc;
	        
	        int counter = 0;
	        nameF = nameFile.substring(0, (nameFile.length()-3)); 
	        
	    	try{
	    		FileReader fileReader = new FileReader(file);
	    		bufferedReader = new BufferedReader(fileReader);
	    		
	    		//creo el archivo de salida .dot
	    		try {	     
	    			File dir = new File("Result");
	    			dir.mkdir();
	    				    			
	    			File fileOut = new File("./Result/"+nameF+".dot");
	    			
	    			//crea el archivo .dot con el hipergrafo completo
	    			fileOut.createNewFile();
	     
	    			fwDot = new FileWriter(fileOut.getAbsoluteFile());
	    			bwDot = new BufferedWriter(fwDot);
	    			bwDot.write("digraph G { \n");
	     
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    		
	    		while ((currentLine = bufferedReader.readLine()) != null) {
	    			currentLine = eraseComments(currentLine);
//	    			System.out.println("currentLine: "+currentLine);
	    			if(currentLine.length() >= 1){
	    				if(counter==0){
	    					graph = new HyperGraph(currentLine);
	    					counter++;
	    				}else if(counter ==1){
	    					graph.setStop(currentLine);
	    					counter++;
	    				}else{
	    					data = currentLine.split(" ");
	    					hyperarc = validateData(data);
	    			    	//dibuja los vertices entre el hiperarco  y los nodos in y out de hyperarcs
	    			    	createConnections(hyperarc, bwDot);
	    				}
	    			}
	    		}
	    		//renombra hiperarcos
	    		renameHyperarcs(bwDot);
	    		renameNodes(bwDot);
	    		//cierra el archivo fileOut
	    		bwDot.write("}");
    			bwDot.close();
    			System.out.println(nameF+".dot created");
	    		
	    		return graph;
	    	}finally{
	    		if(bufferedReader != null)
	    			bufferedReader.close();
	    	}
	    }
	    
		/**
	     * Validate Data
	     * 
	     * @param data
	     * @throws Exception
	     */
	    private HyperArc validateData(String[] data) throws Exception {
	    	
	    	List<Node> auxIn = new ArrayList<Node>();
	    	List<Node> auxOut = new ArrayList<Node>();
	    	int i=0;
			
	    	String name = data[i++];
	    	int weight = Integer.parseInt(data[i++]);
	    	int sizeIn = Integer.parseInt(data[i++]);
	    	int aux = i;
	    	
	    	HyperArc h = graph.addHyperArc(weight, name);
	    	
	    	while( i < (sizeIn+aux) ){
	    		Node n = graph.addVertex(data[i]);
	    		n.addIn(h);
	    		auxOut.add(graph.addVertex(data[i]));
	    		i++;
	    	}
	    	
	    	int sizeOut = Integer.parseInt(data[i++]);
	    	aux = i;
	    	
	    	while( i < (sizeOut+aux) ){
	    		Node no = graph.addVertex(data[i]);
	    		no.addOut(h);
	    		auxIn.add(graph.addVertex(data[i]));
	    		i++;
	    	}
	    	
	    	if(i != data.length){
	    		throw new ParserException("error with the file: "+nameF+".hg");
	    	}
	    	
	    	h.addNodoIn(auxIn);
	    	h.addNodoOut(auxOut);

	    	return h;
		}

	    //dibuja las conexiones entre el hyperarco y los nodos de entrada y salida
		private void createConnections(HyperArc h, BufferedWriter bw) throws IOException {
			String str = h.info+" [shape=box]; \n";
			String aux;
			
			for(Node node: h.getNodoIn()){
				aux=node.info;

				str +=  "node_"+aux + " -> " + h.info+"; \n";
			}
			
			for(Node node: h.getNodoOut()){
				aux=node.info;

				str +=  h.info + " -> " + "node_"+aux+"; \n";
			}
		
			bw.write(str);
		}
				
		//renombra a los hiperarcos
	    private void renameHyperarcs(BufferedWriter bw) throws IOException {
			for(HyperArc arc: graph.getHyperArcs()){
				bw.write(arc.info+ " [label=\"" +arc.info+ "(" + arc.weight+ ")\"]; \n ");
			}
			
		}
	    
	    //renombra a los nodos
	    private void renameNodes(BufferedWriter bw) throws IOException {
	    	for(Node node: graph.getNodes()){
	    		bw.write("node_"+node.info+ " [label=\"" +node.info+"\"]; \n ");
	    	}
	    }
	    
		/**
	     * Removes Comments from String
	     * 
	     * @param line (A String which holds a line of the given file)
	     * @return string without any comments
	     */
	    public String eraseComments(String s){
	    	StringBuilder aux = new StringBuilder(s);
	    	
	    	for(int i=0; i<s.length() ; i++){
	    		if(s.charAt(i) == '#')
	    			aux.delete(i, s.length());
	    	}	    	
	    	return aux.toString();
	    }
	    
	    
	    public void createFiles(Solution bestSolution) throws Exception{
	    	//list de hiperarcos con referencia a los nodos que pertenecen al camino minimo.
			//me sirve para generar luego el archivo .hg con solo el camino minimo.
			hyperArcsSol = bestSolution.getArcs();

			//en nodesMin guardo todos los nodos que participan del camino minimo.
			nodesMin.put(graph.getStart(), graph.getNode(graph.getStart()));
			nodesMin.put(graph.getStop(), graph.getNode(graph.getStop()));
			collectNodesMin(hyperArcsSol);

			//creo el archivo de salida min.dot y min.hg y leo el file.hg
    		try {
    			
	    		fileReader = new FileReader(file);
	    		bufferedReader = new BufferedReader(fileReader);
	    		
    			File minHgFile = new File("./Result/"+nameF+"_min.hg");
    			File minDotFile = new File("./Result/"+nameF+"_min"+".dot");
    			
    			//crea los archivos minHg y minDot
    			minHgFile.createNewFile();
   				minDotFile.createNewFile();

    			fwHg = new FileWriter(minHgFile.getAbsoluteFile());
    			bwHg = new BufferedWriter(fwHg);
    			
    			fwDot = new FileWriter(minDotFile.getAbsoluteFile());
    			bwDot = new BufferedWriter(fwDot);
    			
    			createMinHg(hyperArcsSol);
    			bwHg.close();
    			System.out.println(nameF+"_min.hg created");

    			createMinDot(hyperArcsSol, bufferedReader);
    			bwDot.close();
    			System.out.println(nameF+"_min.dot created");
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
	    }

	    //dibuja las conexiones entre el hyperarco y los nodos de entrada y salida
		private void createConnectionsMin(HyperArc h) throws IOException {
			String str = h.info;
			String aux;
			boolean hyperarcContained = false;
			
			if(hyperArcsSol.contains(h)){
				str += " [shape=box color=red]; \n";
				hyperarcContained = true;
			}else{
				str += " [shape=box]; \n";
			}
			
			for(Node node: h.getNodoIn()){
				aux=node.info;
				
				if(nodesMin.containsKey(aux) && hyperarcContained){
					str += "node_"+aux +" [color=\"red\"]; \n";
					str += "node_"+aux + " -> " + h.info+" [color=\"red\"]; \n";
				}else{
					str +=  "node_"+aux + " -> " + h.info+"; \n";
				}
			}
			
			for(Node node: h.getNodoOut()){
				aux=node.info;

				if(nodesMin.containsKey(aux) && hyperarcContained){
					str += "node_"+aux +" [color=\"red\"]; \n";

					str += h.info + " -> " + "node_"+aux+" [color=\"red\"]; \n";
				}else{
					str +=  h.info + " -> " + "node_"+aux+"; \n";					
				}
			}
			bwDot.write(str);
		}
	    
	    private void createMinDot(List<HyperArc> hyperArcsSol, BufferedReader br) throws Exception {
	        String data[] = null;
	        String currentLine;
	        HyperArc hyperarc;
	        int counter = 0;
	    	
	    	bwDot.write("digraph HG"+nameF+" { \n");
	    	
	    	while ((currentLine = bufferedReader.readLine()) != null) {
    			currentLine = eraseComments(currentLine);
    			if(currentLine.length() >= 1){
    				if(counter <= 1){
    					counter++;
    				}else{
    					data = currentLine.split(" ");
    					hyperarc = validateData(data);
    			    	//dibuja los vertices entre el hiperarco  y los nodos in y out de hyperarcs
    			    	createConnectionsMin(hyperarc);
    				}
    			}
    		}
	    	renameHyperarcs(bwDot);
	    	renameNodes(bwDot);
	    	bwDot.write("}");
	    	bwDot.close();
		}

		//crea el archivo min.hg con la solucion encontrada.
	    private void createMinHg(List<HyperArc> bestSol) throws IOException {
	    	List<String> nodeIn, nodeOut;

	    	bwHg.write(graph.getStart()+"\n");
	    	bwHg.write(graph.getStop()+"\n");
	    	
	    	for(HyperArc arc: bestSol){
	    		nodeIn = new ArrayList<String>();
	    		nodeOut = new ArrayList<String>();
	    		
	    		for(Node node: arc.getNodoIn()){
	    			if(nodesMin.containsKey(node.info)){
	    				nodeIn.add(node.info);
	    			}
	    		}
	    		
	    		for(Node node: arc.getNodoOut()){
	    			if(nodesMin.containsKey(node.info)){
	    				nodeOut.add(node.info);
	    			}
	    		}
	    		
	    		bwHg.write(arc.info+" "+arc.weight+" "+nodeIn.size()+" ");
	    		
	    		for(String str: nodeIn){
	    			bwHg.write(str+" ");
	    		}
	    		
	    		bwHg.write(nodeOut.size()+" ");
	    		
	    		for(String str: nodeOut){
	    			bwHg.write(str+" ");
	    		}	
	    		bwHg.write("\n");
	    	}
		}

		//agrega al Map "nodes" los nodos que comparten dos hiperarcos
		private void collectNodesMin(List<HyperArc> listHyperArcs) {
			for(HyperArc ha: listHyperArcs){
				for(HyperArc ha2:listHyperArcs){
					if(ha != ha2){
						mutualNodes(ha,ha2);
						mutualNodes(ha2,ha);
					}
				}
			}
		}

		private void mutualNodes(HyperArc ha, HyperArc ha2) {
			List<Node> in = ha.getNodoIn();
			List<Node> out = ha2.getNodoOut();
			for(Node aNode: out){
				if(in.contains(aNode)){
				  nodesMin.put(aNode.info, aNode);
				}
			}
		}
}