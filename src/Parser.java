import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import Part2.Game;

import java.util.Map.Entry;


public class Parser {

	public static Map<String, List<String>>buildDictionaries(Set<String> set){

		Map<String,List<String>> wordListDicts = new HashMap<String,List<String>>();
		for(String catName : set){
			File dir = new File("data/wordlist/"+catName+".txt");
			wordListDicts.put(catName, getWords(dir));
		}

		return wordListDicts;

	}
	//Aaron Here, helper function for building index to categories structure
	public static Map<Integer, List<String>>buildIndexToCategories(Map<Integer, List<String>> I_, Puzzle p){
		Set<String> set = p.getCategoryLocs().keySet();
		Map<String, int[]> categoryLocs = p.getCategoryLocs();

		int[] indexes = new int [3];
		List<String> cats = new ArrayList<String>();
		
		for(String spots: set)
		{
			indexes = categoryLocs.get(spots);
			
			for(int i = 0; i<3; i++){
				
				
				if(I_.get(indexes[i])==null){
					List<String> stuff = new ArrayList<String>();
					stuff.add(spots);
					I_.put(indexes[i], stuff);
				}
				
				else{
					I_.get(indexes[i]).add(spots);
					
				}
			
			
			}
		}
		
		return I_;
	}

	private static List<String> getWords(File filepath){

		List<String> words = new ArrayList<String>();

		try (Scanner scanner =  new Scanner(filepath)){
			scanner.useDelimiter("\n");
			while (scanner.hasNextLine()){
				words.add(scanner.nextLine());
			}      
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words;
	}
/*
	public static Game parseGame(File filepath) throws FileNotFoundException{
		Game game = new Game();
		
		
		//Parse the value map
		int[][] valueMap = new int[6][6];
		int index = 0;
		Scanner scanner = new Scanner(filepath);
		while(scanner.hasNextLine()){
			for(int i=0; i<6; i++){
				valueMap[index][i] = scanner.nextInt();
			}
			index++;
		}
		
		game.valueMap = valueMap;
		return game;
	}
	*/
	
	public static Puzzle parsePuzzle(File filepath){
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		scanner.useDelimiter("\n");
		int size = new Integer(scanner.nextLine());
		
		//Initialize the assignment array. Should this be char array?
		char[] charassignment = new char[size];
		for(int i=0; i<size; i++)
			charassignment[i] = '0';
		
		//Parse the input file into hashmap <category,three indexes>
		Map<String, int[]> categoryLocs = new HashMap<String, int[]>();

		//set categoryAtIndex--Haolin
		Map<Integer, List<String>> categoryAtIndex = new HashMap<Integer, List<String>>();

		while(scanner.hasNextLine()){
			int[] locs = new int[3];
			String line = scanner.nextLine();
			
			String[] parsed = line.split(": ");
			String category = parsed[0];
			String[] index = parsed[1].split(", ");
			locs[0] = new Integer(index[0]);
			locs[1] = new Integer(index[1]);
			locs[2] = new Integer(index[2]);
			categoryLocs.put(category, locs);
			
			//set categoryAtIndex--Haolin
			for(int i=0; i<3; i++)
			{
				if(categoryAtIndex.containsKey(locs[i]))
				{
					if(!categoryAtIndex.get(locs[i]).contains(category))
					{
						List<String> l = categoryAtIndex.get(locs[i]);
						l.add(category);
						categoryAtIndex.put(locs[i], l);
					}
				}
				else 
				{
					List<String> list = new ArrayList<String>();
					list.add(category);
					categoryAtIndex.put(locs[i], list);
				}
			}
			

		}

		
		Puzzle puzzle = new Puzzle();
		puzzle.setCategoryLocs(categoryLocs);
		puzzle.setCategoryAtIndex(categoryAtIndex); //set categoryAtIndex--Haolin
		puzzle.setCharAssignment(charassignment);
		puzzle.setWordListMap(buildDictionaries(categoryLocs.keySet())); //WordListMap <category, list of words in input dictionary>
		
		//Aaron Here I am trying to make the data structure that will give the categories at a given index
		Map<Integer, List<String>> IndexToCategories_ = new HashMap<Integer, List<String>>();
		//Aaron Here using function build above
		puzzle.setIndexToCategories(buildIndexToCategories(IndexToCategories_, puzzle));
		setLetterValues(puzzle);
		sortAssignmentIndexMostConstraining(puzzle);
		
		gabbyiswrong(puzzle);
		/*since all words are unique, we don't need to sort them*/
		setWordValues(puzzle);
		puzzle.SetSize(size);
		
		//sort word varibles
		sortWordVariables(puzzle);
		return puzzle;
	}
	
	//Aaron Oct 20th
	//More comments to make this easier to find
	//stuff
	//More stuff
	
	private static void gabbyiswrong(Puzzle puzzle){
		
		Map<String, Integer > r_cats = new HashMap<String,Integer>();
		
		int number = 0;
		int enter = 0;
		
		for(Entry<Integer, Integer> index: puzzle.getSortedIndexValue()){
			
			for(String cat: puzzle.getIndexToCategories().get(index.getKey()))	{
						number = number + 1;
						if(r_cats.get(cat)== null)
							r_cats.put(cat, 0);
						
						//System.out.print(cat +" " + index.getKey() +  "\n");
			}
		
			for(String cat: puzzle.getIndexToCategories().get(index.getKey()))	{
				//System.out.print(cat + "\n");
				//System.out.print("again\n");

				if(r_cats.get(cat)== null)
						//System.out.print("ugh\n");
					enter = number + r_cats.get(cat);
					r_cats.put(cat, enter);
					
					//System.out.print(cat + ", " + r_cats.get(cat) + "\n");

			}		

				number = 0;
			
			
		}
		
		ArrayList<Entry<String,Integer>> sorted = new ArrayList<Entry<String,Integer>>();
		sorted.addAll(r_cats.entrySet());
		
		Collections.sort(sorted, new Comparator<Entry<String, Integer>>(){
			@Override
			public int compare(Entry<String, Integer> a, Entry<String, Integer> b){
				return a.getValue() <= b.getValue()?1:-1;
			}
		
		});
		
		puzzle.setcategoryRank(sorted);
	}
	
	
	

	//gabby here, proving aaron's method was worse/took longer/still he didn't finish it. this sorts variables for word-based from most contraining to lease
	private static void sortWordVariables(Puzzle puzzle) {
		//take the letter variables and their scores (how constrained they are) and turn it into a map
		List<Entry<Integer, Integer>> sortedLetterVars = puzzle.getSortedIndexValue();
		Map<Integer,Integer> letterVarsMap = new HashMap<Integer,Integer>();
		for(Entry<Integer,Integer> e : sortedLetterVars){
			letterVarsMap.put(e.getKey(), e.getValue());
		}
		
		//use this map to add up the 3 scores of indicies for each word, store it in a new map
		Map<String, Integer> wordVarsUnsorted = new HashMap<String, Integer>();
		int sum = 0;
		for(String cat : puzzle.getWordListMap().keySet()){
			for(int index : puzzle.getCategoryLocs().get(cat)){
				sum += letterVarsMap.get(index);
			}
			wordVarsUnsorted.put(cat, sum);
			sum = 0;
		}
		
		//now sort the collection of categories and scores
		List<Entry<String, Integer>> sorted = new ArrayList<Entry<String, Integer>>();		
		sorted.addAll(wordVarsUnsorted.entrySet());
		
		Collections.sort(sorted, new Comparator<Entry<String, Integer>>(){
			
			@Override
			public int compare(Entry<String, Integer> a, Entry<String, Integer> b){
				return a.getValue() >= b.getValue()?1:-1;
			}
			
			
		});
		
		puzzle.setSortedWordVariables(sorted);
		
	}
	private static void sortAssignmentIndexMostConstraining(Puzzle puzzle){
		Map<String, int[]> categoryLocs = puzzle.getCategoryLocs();
		Collection<int[]> list = categoryLocs.values();
		ArrayList<Integer> arraylist = new ArrayList<Integer>();
		for(int[] a:list){
			arraylist.add(a[0]);
			arraylist.add(a[1]);
			arraylist.add(a[2]);
		}
		
		Map<Integer, Integer> mapCounter = new HashMap<Integer, Integer>();
		for(Integer i:arraylist){
			if(mapCounter.containsKey(i))
				mapCounter.put(i, mapCounter.get(i)+1);
			else 
				mapCounter.put(i, 1);
		}
		
		ArrayList<Entry<Integer,Integer>> sorted = new ArrayList<Entry<Integer,Integer>>();
		sorted.addAll(mapCounter.entrySet());
		
		Collections.sort(sorted, new Comparator<Entry<Integer, Integer>>(){
			@Override
			public int compare(Entry<Integer, Integer> a, Entry<Integer, Integer> b){
				return a.getValue() <= b.getValue()?1:-1;
			}
		});
		puzzle.setSortedIndexValue(sorted);
	}
		
		
	private static void setWordValues(Puzzle puzzle) {
		
		Map<String, List<Entry<String, Integer>>> wordValuesByCategories = new HashMap<String, List<Entry<String, Integer>>>();
		for(String category : puzzle.getWordListMap().keySet()){
			//map to store each words value for this category
			Map<String, Integer> wordValues = new HashMap<String, Integer>();
			
			//get the words for this category
			List<String> words  = puzzle.getWordListMap().get(category);

			//iterate through each word, and add the value of each character in the word
			Integer wordValue = 0;
			for(String word : words){
				for(char c : word.toCharArray()){
					wordValue += puzzle.getLetterCounts().get(c);
				}
				wordValues.put(word, wordValue);
				//reset wordValue for next word in list
				wordValue = 0;
			}
	
			//sort the entries in the map from high to low of the words' values
			List<Entry<String, Integer>> sorted = new ArrayList<Entry<String, Integer>>();		
			sorted.addAll(wordValues.entrySet());
			
			Collections.sort(sorted, new Comparator<Entry<String, Integer>>(){
				
				@Override
				public int compare(Entry<String, Integer> a, Entry<String, Integer> b){
					return a.getValue() <= b.getValue()?1:-1;
				}
				
				
			});
			wordValuesByCategories.put(category, sorted);
		}
		
		puzzle.setSortedWordValuesByCategory(wordValuesByCategories);
		
	}

	private static void setLetterValues(Puzzle puzzle){
		Map<Character, Integer> letterValues = new HashMap<Character, Integer>();
		List<String> words  = new ArrayList<String>();
		for(List<String> list : puzzle.getWordListMap().values()){
			words.addAll(list);
		}
		
		for(String word : words){
			getLetterValuesHelper(word.toCharArray(), letterValues);
		}
		
		puzzle.setLetterCounts(letterValues);
		
		//sort the entries in the map from high to low of the letters' values
		List<Entry<Character, Integer>> sorted = new ArrayList<Entry<Character, Integer>>();		
		sorted.addAll(puzzle.getLetterCounts().entrySet());
		
		Collections.sort(sorted, new Comparator<Entry<Character, Integer>>(){
			
			@Override
			public int compare(Entry<Character, Integer> a, Entry<Character, Integer> b){
				return a.getValue() <= b.getValue()?1:-1;
			}
			
		});
		
		puzzle.setSortedLetterValues(sorted);

	}
	
	private static Map<Character, Integer> getLetterValuesHelper(char[] letters, Map<Character, Integer> letterValues){
		for(char c : letters){
			if(letterValues.get(c) == null){
				letterValues.put(c, new Integer(1));
			}
			else{
				Integer val = letterValues.get(c);
				val++;
				letterValues.put(c, val);
			}
		}
		return letterValues;
	}
	
	
	

}
