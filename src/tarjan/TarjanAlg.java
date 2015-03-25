package tarjan;

import java.util.ArrayList;
import java.util.List;

import data_obj.AdjacencyList;
import data_obj.Edge;
import data_obj.Node;

/**
 * This was slightly modified to handle the case where a node does not have any
 * transitions. The for loop can not handle a null list.
 * 
 * @author wiki-algorithms
 *
 */
public class TarjanAlg {

	private int index = 0; // DFS node number counter
	private ArrayList<Node> stack = new ArrayList<Node>(); // stack of nodes
	private ArrayList<ArrayList<Node>> SCC = new ArrayList<ArrayList<Node>>(); // output

	public TarjanAlg() {
		super();
		this.index = 0;
		this.stack = new ArrayList<Node>();
		SCC = new ArrayList<ArrayList<Node>>();
	}

	public ArrayList<ArrayList<Node>> tarjan(Node v, AdjacencyList list) {
		v.setIndex(index); // set depth of index of node
		v.setLowlink(index);
		index++;
		stack.add(0, v); // push it onto the stack
		List<Edge> l = list.getAdjacent(v);
		if (l != null) {
			for (Edge e : l) { // look at all successors of node
				Node n = e.getTo();
				if (n.getIndex() == -1) { // was successor visited
					tarjan(n, list); // recurse
					v.setLowlink(Math.min(v.getLowlink(), n.getLowlink()));
				} else if (stack.contains(n)) { // successor is on the stack
					v.setLowlink(Math.min(v.getLowlink(), n.getIndex()));
				}
			}
		} // list is null

		if (v.getLowlink() == v.getIndex()) { // are we at the root node
			Node n;
			ArrayList<Node> component = new ArrayList<Node>();
			do {
				n = stack.remove(0);
				component.add(n);
			} while (n != v);
			SCC.add(component);
		}
		return SCC;
	}

} // class TarjanAlg
