import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BacktrackSearch {

	private static Set<String> globalSet = new HashSet<String>();


	public static void solveWordBased(int level, Puzzle puzzle, int numOfCats){

		if((level == numOfCats+1)){
			
			boolean flag = true;
			for(char c : puzzle.getAssignment()){
				if (c == '0')
					flag = false;
			}
			String tmp = new String(puzzle.getAssignment());
			if(flag && globalSet.add(tmp)){
				puzzle.printCharArray(puzzle.getAssignment());
				flag = true;
			}
			return;
		}
		else{

			for(Entry<String, Integer> entry : puzzle.getSortedWordVariables()){

				for(Entry<String, Integer> wordEntry : puzzle.getSortedWordValuesByCategory().get(entry.getKey())){
					String leastConstrainedWord = wordEntry.getKey();
					char[] oldAss = null;
					if((oldAss = constraintChecker(entry.getKey(), leastConstrainedWord, puzzle)) != null){
						solveWordBased(level+1, puzzle, numOfCats);
						puzzle.setAssignment(oldAss);
						

					}
				}
			}
		}
	}

	private static char[] constraintChecker(String cat, String leastConstrainedWord, Puzzle puzzle) {

		char[] oldAss = puzzle.getAssignment().clone();
		char[] newAss = puzzle.getAssignment().clone();
		int count = 0;
		for(int index : puzzle.getCategoryLocs().get(cat)){


			//adding this check -gabby
			if(newAss[index-1] != '0' && newAss[index-1] != leastConstrainedWord.charAt(count))
				return null;

			newAss[index-1] = leastConstrainedWord.charAt(count);
			int size = 0;
			for(String category : puzzle.getCategoryAtIndex().get(index)){
				boolean result = false;

				if (category.equals(cat)){
					size++;
					continue;
				}
				for(String word: puzzle.getWordListMap().get(category)){
					//System.out.println("ugh4 "+word);
					int first = puzzle.getCategoryLocs().get(category)[0];
					int second = puzzle.getCategoryLocs().get(category)[1];
					int third = puzzle.getCategoryLocs().get(category)[2];

					if( (newAss[first-1] == word.charAt(0) || newAss[first-1] == '0') && (newAss[second-1] == word.charAt(1) || newAss[second-1] == '0') && (newAss[third-1] == word.charAt(2) || newAss[third-1] == '0')){
						result = true;
						if(result == true) size++;
						break;
					}	

				}

			}

			if(!(size == puzzle.getCategoryAtIndex().get(index).size())){
				puzzle.setAssignment(oldAss);
				return null;
			}

			count++;
		}

		// :D	
		puzzle.setAssignment(newAss);
		return oldAss;

	}

}
