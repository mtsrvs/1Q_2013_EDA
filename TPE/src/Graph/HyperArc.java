package Graph;
 
import java.util.*;
 
public class HyperArc{
       
        public int weight;
        public String info;
        private List<Node> nodoIn;
        private List<Node> nodoOut;
        public boolean visited;
        
 
        public HyperArc(String info, int weight) {
                this.info = info;
                this.weight = weight;
                nodoIn = new ArrayList<Node>();
                nodoOut = new ArrayList<Node>();
                this.visited = false;
        }
 
        public int getWeight() {
                return weight;
        }
 
        public String getInfo() {
                return info;
        }
       
        public boolean isVisited() {
                return visited;
        }
 
        public void setVisited(boolean visited) {
                this.visited = visited;
        }
 
        public List<Node> getNodoIn() {
                return nodoIn;
        }
 
        public void addNodoIn(List<Node> nodos) {
                nodoIn = nodos;
        }
       
        public List<Node> getNodoOut() {
                return nodoOut;
        }
       
        public void addNodoOut(List<Node> nodos) {
                nodoOut = nodos;
        }
 
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((info == null) ? 0 : info.hashCode());
                result = prime * result + ((nodoIn == null) ? 0 : nodoIn.hashCode());
                result = prime * result + ((nodoOut == null) ? 0 : nodoOut.hashCode());
                result = prime * result + (visited ? 1231 : 1237);
                result = prime * result + weight;
                return result;
        }
 
        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                HyperArc other = (HyperArc) obj;
                if (info == null) {
                        if (other.info != null)
                                return false;
                } else if (!info.equals(other.info))
                        return false;
                if (nodoIn == null) {
                        if (other.nodoIn != null)
                                return false;
                } else if (!nodoIn.equals(other.nodoIn))
                        return false;
                if (nodoOut == null) {
                        if (other.nodoOut != null)
                                return false;
                } else if (!nodoOut.equals(other.nodoOut))
                        return false;
                if (visited != other.visited)
                        return false;
                if (weight != other.weight)
                        return false;
                return true;
        }

		public void print() {
			System.out.println(this.info);
			
		}
       
}