package Graph;

import java.util.*;

public class Node {
       
        public String info;
        public boolean visited;
        public List<HyperArc> out;
        public List<HyperArc> in;
 
        public Node(String info) {
                this.info = info;
                this.visited = false;
                this.out = new ArrayList<HyperArc>();
                this.in = new ArrayList<HyperArc>();
        }
       
        public void addIn(HyperArc a){
                in.add(a);
        }
       
        public void addOut(HyperArc a){
                out.add(a);
        }
       
        public List<HyperArc> getIn(){
                return in;
        }
       
        public List<HyperArc> getOut(){
                return out;
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
 
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((in == null) ? 0 : in.hashCode());
                result = prime * result + ((info == null) ? 0 : info.hashCode());
                result = prime * result + ((out == null) ? 0 : out.hashCode());
                result = prime * result + (visited ? 1231 : 1237);
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
                Node other = (Node) obj;
                if (in == null) {
                        if (other.in != null)
                                return false;
                } else if (!in.equals(other.in))
                        return false;
                if (info == null) {
                        if (other.info != null)
                                return false;
                } else if (!info.equals(other.info))
                        return false;
                if (out == null) {
                        if (other.out != null)
                                return false;
                } else if (!out.equals(other.out))
                        return false;
                if (visited != other.visited)
                        return false;
                return true;
        }

}