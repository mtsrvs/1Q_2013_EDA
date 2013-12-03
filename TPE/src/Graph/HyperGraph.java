package Graph;
 
import java.util.*;
 
/**
 * Clase para Hipergrafos
 *
 * @param <V>
 * @param <E>
 */
public class HyperGraph {
 
        private HashMap<String, Node> nodes; // Para ubicar los nodos rápidamente
        private List<Node> nodeList; // Para recorrer la lista de nodos
        private HashMap<String, HyperArc> arcs; // Para ubicar los arcos rápidamente
        private List<HyperArc> arcList; // Para recorrer la lista de arcos
        private String start;
        private String stop;
 

        public Map<String, Node> getMapNodes(){
        	return nodes;
        }
        
        //Constructor del Hipergrafo
        public HyperGraph(String start) {
                this.nodes = new HashMap<String, Node>();
                this.nodeList = new ArrayList<Node>();
                this.arcs = new HashMap<String, HyperArc>();
                this.arcList = new ArrayList<HyperArc>();
                this.setStart(start);
        }
 
        //Pregunta si el hipergrafo esta vacio. Sin usar
        public boolean isEmpty() {
                return nodes.size() == 0;
        }
 
        //Agregar Nodo
        public Node addVertex(String info) {
                        Node node = new Node(info);
                        if(!nodes.containsKey(info)){
                        	nodes.put(info, node);
                        	nodeList.add(node);                   	
                        }
                        return nodes.get(info);
        }
       
        //Agregar HiperArco
        public HyperArc addHyperArc(int weight, String name) {
                HyperArc a = new HyperArc(name, weight);
                arcs.put(name, a);
                arcList.add(a);
                return a;
        }
       
        //Agregar HiperArco entrante al nodo
        public void addHyperArcToNode(Node node, HyperArc arc){
                node.addIn(arc);
        }
       
        //Agregar HiperArco saliente al nodo
        public void addNodeToHyperArc(Node node, HyperArc arc){
                node.addOut(arc);
        }
 
        //Devuelve la cantidad de hiperarcos
        public int hyperArcCount() {
                return arcs.size();
        }
       
        //Devuelve una lista de todos los hiperarcos
        public List<HyperArc> getHyperArcs() {
                return arcList;
        }
 
        //Verifica si hay un hiperarco existente entre dos nodos dados
        public boolean areConnected(String v, String w) {
                Node a = nodes.get(v);
                Node b = nodes.get(w);
               
                for(HyperArc h : a.getOut()){
                        if(b.getIn().contains(h)){
                                return true;
                        }
                }
                for(HyperArc h : a.getIn()){
                        if(b.getOut().contains(h)){
                                return true;
                        }
                }
                return false;
        }
 
        //Devuelve todos los nodos vecinos al del origen(hijos). Sin usar
        public List<Node> neighbors(String v) {
                Node node = nodes.get(v);      
               
                if(node != null){
                        List<Node> resp = new ArrayList<Node>();
                       
                        for(HyperArc h : node.getIn()){
                                resp.addAll(h.getNodoIn());
                        }      
                        for(HyperArc h : node.getOut()){
                                resp.addAll(h.getNodoOut());
                        }
               
                        return resp;
                }
               
                return null;   
        }
 
        //Devuelve catidad de nodos
        public int vertexCount() {
                return nodes.size();
        }
 
        //Devuelve una lista de todos los nodos incluyendo los hiperarcos. Sin Usar
        public List<Node> getNodes() {
                return nodeList;
        }
 
        //Borra todos los visitados. Sin Usar
        public void clearMarks() {
                for (Node n : getNodes()) {
                        n.visited = false;
                }
        }
 
        //Devuelve el nodo root
        public String getStart() {
                return start;
        }
 
        //Setea el nodo root
        public void setStart(String start) {
                this.start = start;
        }
 
        //Devuelve el nodo final
        public String getStop() {
                return stop;
        }
 
        //Setea el nodo final
        public void setStop(String stop) {
                this.stop = stop;
        }
        
        //Devuelve el nodo pedido
        public Node getNode(String info){
        	return nodes.get(info);
        }
                
    	//Hill Climbing
        public Solution HillClimbingSolve(long time){
            
            boolean better = false;
            int neighborEval, localSolutionEval;
            int bestSolutionEval = -1;
            Solution localSolution;
            Solution bestSolution = null;
            long timeStart = System.currentTimeMillis();
           
            while ((System.currentTimeMillis() - timeStart) < time*1000) {
                   
                localSolution = randomSolution();
                localSolutionEval = localSolution.evaluate();
               
                if(bestSolutionEval > localSolutionEval)        {
                              
                    do {
                            better = false;
                           
                            for (Solution neighbor : localSolution.neighbors()) {
                                    neighborEval = neighbor.evaluate();
                                   
                                    if (neighborEval < localSolutionEval) {
                                            localSolution = neighbor;
                                            localSolutionEval = neighborEval;
                                            better = true;
                                    }                     
                            }
                    } while (better);
                }  
                   
                if ((localSolutionEval < bestSolutionEval) || (bestSolutionEval == -1)) {
                    bestSolutionEval = localSolutionEval;
                    bestSolution = localSolution;
                }
            }
   
             System.out.println("Weight: "+ bestSolutionEval);
           
            return bestSolution;
                   
        }
 
       public Solution randomSolution() {
                   
    	   Node bottom = nodes.get(stop);
           Node top = nodes.get(start);
                   
           List<Node> actualNodes = new ArrayList<Node>();
           actualNodes.add(bottom);
                   
           List<HyperArc> sol = new ArrayList<HyperArc>();  
           Random randomGenerator;
                   
           do{
               List<Node> aux = new ArrayList<Node>();
               for(Node n : actualNodes){
            	   aux.add(n);
               }
                           
               for(Node node : aux){
            	   randomGenerator = new Random();
            	   if(node.in.size() != 0){
            		   HyperArc h = node.in.get(randomGenerator.nextInt(node.in.size()));
                                   
            		   if(!sol.contains(h)){
            			   sol.add(h);
            		   }
                   
            		   actualNodes.remove(node);
            		   for(Node n: h.getNodoIn()){
            			   if(!actualNodes.contains(n)){
            				   actualNodes.add(n);
            			   }
            		   } 
            	   }
               }
                           
           }while(actualNodes.size() > 1 || actualNodes.get(0) != top);
           
           return new Solution(sol, this);
       }
            
       public Solution exactSolution(){
    	   Solution sol = new Solution(new ArrayList<HyperArc>(),this);
    	   List<Node> list = new ArrayList<Node>();
    	   
    	   list.add(getNode(getStop()));
    	   Solution bestSolution = new Solution(new ArrayList<HyperArc>(),this);
    	   long timeStart = System.currentTimeMillis();
    	   
    	   exactSolution(sol,getNode(getStart()),list,bestSolution);
    	   
    	   bestSolution.print();
    	   System.out.println("Time: " + (System.currentTimeMillis()-timeStart)/1000 + "s");
           System.out.println("Weight: "+ bestSolution.evaluate());

           return bestSolution;
        }
        
        public void exactSolution(Solution sol ,Node end, List<Node> nodes,Solution bestSolution){
        	if(nodes.get(0)==end){
        		while(nodes.contains(end)){
        			nodes.remove(end);
        		}
        		if(bestSolution.evaluate()<0){
        			for(HyperArc aux :sol.getArcs()){
        				bestSolution.addToSolution(aux);
        			}
        		}
        		else if (sol.evaluate()<=bestSolution.evaluate()){
        			bestSolution.getArcs().clear();
        			for(HyperArc aux :sol.getArcs()){
        				bestSolution.addToSolution(aux);
        			}
        		}
        			
        		return ;
        	}
         
        	Node aux = nodes.get(0);
         
        	for(HyperArc possibleWay : aux.getIn()){
        		List<Node> auxNodes = new ArrayList<Node>();
        		Solution solution = new Solution(sol.getArcs(),this);
        	
        		auxNodes.addAll(nodes);
        		auxNodes.remove(aux);
       		
        		for(Node auxNode : possibleWay.getNodoIn()){
        			if(!auxNodes.contains(auxNode)){
        				auxNodes.add(auxNode);
        			}
        		}
        		
        		solution.addToSolution(possibleWay);
        		if(bestSolution.evaluate() > 0 && (bestSolution.evaluate() < solution.evaluate())){
        			return ;
        		}
        		
        		exactSolution (solution,end,auxNodes,bestSolution);
        	}
       }
        
        public boolean isPossible(Solution sol,List<Node> needed){
        	for(Node aux : needed){
        		if (!sol.getArcs().contains(aux)){
        			return false;
        		}
        	}
        	return true;
        }
}