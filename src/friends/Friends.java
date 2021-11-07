package friends;
import java.util.ArrayList;
import structures.Queue;
import structures.Stack;

public class Friends {
	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		ArrayList<String> peopleList = new ArrayList<String>();

		if (g == null || p1 == null || p2 == null || p1.length() == 0 || p2.length() == 0){
			return null;
		}
		p1 = p1.toLowerCase();
		p2 = p2.toLowerCase();
		if (g.map.get(p1) == null || g.map.get(p2) == null){
			return null;
		}
		if (p1.equals(p2)){
			peopleList.add(g.members[g.map.get(p1)].name);
			return peopleList;
		}
		Queue<Integer> q = new Queue<Integer>();
		int[] depthOfGraph = new int[g.members.length];
		int[] personL = new int[g.members.length];
		boolean[] ifVisited = new boolean[g.members.length];

		for (int i = 0; i < depthOfGraph.length; i++){
			ifVisited[i] = false;
			depthOfGraph[i] = Integer.MAX_VALUE;
			personL[i] = -1;
		}

		int beginning = g.map.get(p1);
		ifVisited[beginning] = true;
		depthOfGraph[beginning] = 0;

		q.enqueue(beginning);

		while(!q.isEmpty()){
			int a = q.dequeue();
			Person point = g.members[a];
			for (Friend peopleF = point.first; peopleF != null; peopleF = peopleF.next){
				int fnum = peopleF.fnum;
				if (ifVisited[fnum] == false){
					depthOfGraph[fnum] = depthOfGraph[a] + 1;
					personL[fnum] = a;
					ifVisited[fnum] = true;
					q.enqueue(fnum);
				}
			}
		}

		Stack<String> s = new Stack<String>();
		int ending = g.map.get(p2);
		if (ifVisited[ending] == false){
			return null;
		}
		while (ending != -1){
			s.push(g.members[ending].name);
			ending = personL[ending];
		}
		while (!s.isEmpty()){
			peopleList.add(s.pop());
		}
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return peopleList;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		ArrayList<ArrayList<String>> peopleList = new ArrayList<>();

		if(school == null || school.length() == 0 || g == null){
			return null;
		}
		school = school.toLowerCase();

		boolean[] ifVisited = new boolean[g.members.length];
		for (int i = 0; i < ifVisited.length; i++){
			ifVisited[i] = false;
		}
		for (Person p : g.members){
			if(ifVisited[g.map.get(p.name)] == false && p.school != null && p.school.equals(school)){
				ArrayList<String> c = new ArrayList<>();
				Queue<Integer> q = new Queue<>();

				int beginning = g.map.get(p.name);
				ifVisited[beginning] = true;
				q.enqueue(beginning);
				c.add(p.name);

				while(!q.isEmpty()){
					int a = q.dequeue();
					Person pTwo = g.members[a];
					for (Friend point = pTwo.first; point != null; point = point.next){
						int b = point.fnum;
						Person friendOne = g.members[b];
						if (ifVisited[b] == false && friendOne.school != null && friendOne.school.equals(school)){
							ifVisited[b] = true;
							q.enqueue(b);
							c.add(g.members[b].name);
						}
					}
				}
				peopleList.add(c);
			}
		}
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return peopleList;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		boolean[] ifVisited = new boolean[g.members.length];
		int[] ending = new int[g.members.length];
		int[] finalNums = new int[g.members.length];
        ArrayList<String> peopleList = new ArrayList<>();

		for (Person p : g.members){
			if (ifVisited[g.map.get(p.name)] == false){
				finalNums = new int[g.members.length];
				depthFirstSearch(g, g.map.get(p.name), g.map.get(p.name), ifVisited, finalNums, ending, peopleList);
			}
		}
		for (int i = 0; i < peopleList.size(); i++){
			Friend point = g.members[g.map.get(peopleList.get(i))].first;
			int total = 0;
			while (point != null){
				point = point.next;
				total++;
			}
			if (total == 0 || total == 1){
				peopleList.remove(i);
			}
		}
		for (Person p : g.members){
			if (p.first.next == null && !peopleList.contains(g.members[p.first.fnum].name)){
				peopleList.add(g.members[p.first.fnum].name);
			}
		}
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return peopleList;
	}

	private static void depthFirstSearch(Graph g, int i, int o, boolean[] ifVisited, int[] finalNums, int[] atEnd, ArrayList<String> answer){
		Person personL = g.members[i];
		ifVisited[g.map.get(personL.name)] = true;
		
		int total = lengthOfArray(finalNums) + 1;
		if (finalNums[i] == 0 && atEnd[i] == 0){
			finalNums[i] = total;
			atEnd[i] = finalNums[i];
		}
		for (Friend point = personL.first; point != null; point = point.next){
			if (ifVisited[point.fnum] == false){
				depthFirstSearch(g, point.fnum, o, ifVisited, finalNums, atEnd, answer);
				if (finalNums[i] > atEnd[point.fnum]){
					atEnd[i] = Math.min(atEnd[i], atEnd[point.fnum]);
				} else {
					if ( Math.abs(finalNums[i] - atEnd[point.fnum]) < 1 && Math.abs(finalNums[i] - finalNums[point.fnum]) <= 1 && atEnd[point.fnum] == 1 && i == o){
						continue;
					}
					if (finalNums[i] <= atEnd[point.fnum] && (i != o || atEnd[point.fnum] == 1)){
						if (!answer.contains(g.members[i].name)){
							answer.add(g.members[i].name);
						}
					}
				}
			}
			else {
				atEnd[i] = Math.min(atEnd[i], finalNums[point.fnum]);
			}
		}
	}

	private static int lengthOfArray(int[] sizeOfArray) {
		int total = 0;
		for (int i = 0; i < sizeOfArray.length; i++){
			if (sizeOfArray[i] != 0){
				total++;
			}
		}
		return total;
	}
}

