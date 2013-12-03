package Graph;
 
import java.util.*;
 
public class Solution {
       
        private List<HyperArc> sol;
        private HyperGraph graph;
       
        public Solution(List<HyperArc> sol, HyperGraph graph) {
        		this.sol = new ArrayList<HyperArc>(sol);
                this.graph = graph;
        }
 
        public int evaluate() {
        		if(this.sol.size()==0){
        			return -1;
        		}
                int result = 0;
               
                for(HyperArc h : sol){
                        result += h.getWeight();
                }
                return result;
        }
       
        public List<HyperArc> getArcs(){
                return sol;
        }
 
        public List<Solution> neighbors() {
               
                List<Solution> ret = new ArrayList<Solution>();
               
                for(int i=0 ; i<sol.size() ; i++){
                       
                        List<HyperArc> auxList = new ArrayList<HyperArc>();
                       
                        for(HyperArc h : sol){
                                auxList.add(h);
                        }
                       
                        auxList.remove(i);
                        if(isSolution(auxList)){
                                ret.add(new Solution(auxList, graph));
                        }
                }
                return ret;
        }
 
        public boolean isSolution(List<HyperArc> aux) {
               
                Node bottom = graph.getMapNodes().get(graph.getStop());
                Node top = graph.getMapNodes().get(graph.getStart());
               
                List<Node> actualNodes = new ArrayList<Node>();
                actualNodes.add(bottom);
               
                do{
                        List<Node> list = new ArrayList<Node>();
                        for(Node n : actualNodes){
                                list.add(n);
                        }
                       
                        for(Node node : list){
                               
                                HyperArc h = null;
                               
                                for(int i=0 ; i<aux.size() && h==null ; i++){
                                        if(node.getIn().contains(aux.get(i))){
                                                h = aux.get(i);
                                        }
                                }
                               
                                if(h==null){
                                        return false;
                                }
                               
                                actualNodes.remove(node);
                                for(Node n: h.getNodoIn()){
                                        if(!actualNodes.contains(n)){
                                                actualNodes.add(n);
                                        }
                                }                              
                        }
                       
                }while(actualNodes.size() > 1 || actualNodes.get(0) != top);
               
                return true;
        }
       
        public boolean addToSolution(HyperArc possibleSol){
                if(!sol.contains(possibleSol)){
                sol.add(possibleSol);
                return true;
                }
                else
                        return false;
               
        }
        
        public boolean isPartialSolution(List<Node> verify){
        	boolean present;
        	for(Node aux : verify){
        		present=false;
        		for(HyperArc aux2 : aux.getIn()){
        			if(sol.contains(aux2)){
        				present=true;
        			}
        		if(!present){
        			return false;
        		}
        	}
        }
        	return true;
        }
        
        public Solution combineSolutions(Solution a, Solution b){
        	Solution auxSol=new Solution(new ArrayList<HyperArc>(),a.graph);
        	auxSol.sol.addAll(a.sol);
        	for(HyperArc aux : b.sol){
        		if(!a.sol.contains(aux)){
        			auxSol.sol.add(aux);
        		}
        	}
        	return auxSol;
        }
        
        public boolean isSolution(){
        	return isSolution(sol);
             
        }
       
        public void print() {  
        	for(HyperArc aux : sol){
        		aux.print();
        	}
        }
}