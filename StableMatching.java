import java.util.*;
import java.io.*;
import java.io.IOException;

public class StableMatching {
	List<String> men = null;
	List<String> women = null;
	Map<String, List<String>> menRanking = null;
	Map<String, List<String>> womenRanking = null;
	
	public static void main(String[] args) {
		System.out.println("\n");
		new StableMatching("data.txt");
	}
	
	public StableMatching(String filePath) {
		BufferedReader fileReader = null;
		men = new ArrayList<String>();
		women = new ArrayList<String>();
		menRanking = new HashMap<String, List<String>>();
		womenRanking = new HashMap<String, List<String>>();

		try {
			String currentLineString = null;
			String[] currentLineArray = null;
			fileReader = new BufferedReader(new FileReader(filePath));
			
			while ((currentLineString = fileReader.readLine()) != null) {
				currentLineArray = currentLineString.split(" ");
				int numberOfPeople = currentLineArray.length - 1;
				String ranker = currentLineArray[0];
				
				List<String> prefList = Arrays.asList(Arrays.copyOfRange(currentLineArray, 1, currentLineArray.length));
				
				if (women.size() == 0)
					women.addAll(prefList);
				
				boolean isWoman = false;
				
				for(String currentWoman : women) 
					if (ranker.equals(currentWoman)) 
						isWoman = true;
				
				if (isWoman == false) {
					men.add(ranker);
					menRanking.put(ranker, prefList);
				} else 
					womenRanking.put(ranker, prefList);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
					Map<String, String> matches = doMatching();

					for(Map.Entry<String, String> matching:matches.entrySet())
			            System.out.println(matching.getKey() + " " + matching.getValue());
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private Map<String, String> doMatching() {
		Map<String, String> matches = new TreeMap<String, String>();
		List<String> freeMen = new LinkedList<String>();
		freeMen.addAll(men);
		
		while(!freeMen.isEmpty()) {
			String currentMan = freeMen.remove(0);
			List<String> currentManPrefers = menRanking.get(currentMan);
			
			for(String woman : currentManPrefers) {
				if(matches.get(woman) == null) { 
	                matches.put(woman, currentMan);
	                break;
	            } else {
	                String otherMan = matches.get(woman);
	                List<String> currentWomanRanking = womenRanking.get(woman);
	                if(currentWomanRanking.indexOf(currentMan) < currentWomanRanking.indexOf(otherMan)){
	                    matches.put(woman, currentMan);
	                    freeMen.add(otherMan);
	                    break;
	                }
	            }
	        }
		}
		
		return matches;
	}
}