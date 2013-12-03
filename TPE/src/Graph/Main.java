package Graph;

import java.io.File;
import java.io.IOException;

import Parser.Parser;

public class Main {

	public static void main(String[] args) {
		Parser p = new Parser();
		HyperGraph hypergraph;
		Solution bestSolution = null;
		
		try {
			
			if(!(new File("./HyperGraphs/"+args[0]).exists())){
				System.out.println("file or folder \"Hypergraphs\" does not exist");
				return;
			}

			if (args[1].toLowerCase().compareTo("exact") == 0) {
				hypergraph = p.readFiles(new File("./HyperGraphs/"+args[0]), args[0]);

				System.out.println("Exact Solution in process..");
				bestSolution = hypergraph.exactSolution();
				p.createFiles(bestSolution);
			} else if (args[1].toLowerCase().compareTo("approx") == 0 && isNumeric(args[2])) {
				hypergraph = p.readFiles(new File("./HyperGraphs/"+args[0]), args[0]);

				System.out.println("Time: " + args[2]
						+ "sec. Approx Solution in process..");
				bestSolution = hypergraph.HillClimbingSolve(Long.valueOf(args[2]));
				p.createFiles(bestSolution);
			} else {
				System.out.println("Error: arguments invalid");
				return;
			}
			System.out.println("Done");

		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	}

	public static boolean isNumeric(String num) {
		if(num != null && num.matches("\\d+"))
			return true;
		return false;
	}

}
