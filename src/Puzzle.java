import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;


public class Puzzle {

	private char[] charAssignment;
	private Map<String, int[]> categoryLocs;
	private Map<String, List<String>> wordListMap;
	private List<Entry<Character, Integer>> sortedLetterValues;
	private List<Entry<Integer, Integer>> sortedAssignmentIndexMostConstrain;
	private List<Entry<String, Integer>> sortedWordVariables;

	private Map<String, List<Entry<String, Integer>>> sortedWordValuesByCategory;
	
	public Map<String, List<Entry<String, Integer>>> getSortedWordValuesByCategory() {
		return sortedWordValuesByCategory;
	}

	public void setSortedWordValuesByCategory(Map<String, List<Entry<String, Integer>>> sortedWordValuesByCategory) {
		this.sortedWordValuesByCategory = sortedWordValuesByCategory;
	}

	private Map<Character, Integer> letterCounts;
	private List<Entry<String, Integer>> wordValues;

	private Map<Integer, List<String>> indexToCategories;
	private Map<Integer, List<String>> categoryAtIndex;
	
	private List<Entry<String, Integer>> categoryRank;

	private int size;
	
	public int GetSize(){
		return size;
	}
	
	public void SetSize(int size){
		this.size = size;
	}
	
	public Puzzle(){	
	}
	
	public char[] getAssignment() {
		if(charAssignment == null)
			return null;
		return charAssignment;
	}

	public void setAssignment(char[] assignment) {
		this.charAssignment = assignment;
	}
	
	public void setCharAssignment(char[] charAssignment) {
		this.charAssignment = charAssignment;
	}

	public Map<Integer, List<String>> getCategoryAtIndex() {
		return categoryAtIndex;
	}

	
	public void setAssignmentspot(int spot, char c){
		this.charAssignment[spot-1] = c;
	}

	public void setCategoryAtIndex(Map<Integer, List<String>> categoryAtIndex2) {
		this.categoryAtIndex = categoryAtIndex2;
	}

	public char getAssignmentspot(int spot){
		return this.charAssignment[spot];
	}
	
	public Map<String, int[]> getCategoryLocs() {
		return categoryLocs;
	}

	public void setCategoryLocs(Map<String, int[]> categoryLocs2) {
		this.categoryLocs = categoryLocs2;
	}
	
	public Map<String, List<String>> getWordListMap() {
		return wordListMap;
	}

	public void setWordListMap(Map<String, List<String>> wordListMap) {
		this.wordListMap = wordListMap;
	}

	public List<Entry<Character, Integer>> getSortedLetterValues() {
		return sortedLetterValues;
	}

	public void setSortedLetterValues(List<Entry<Character, Integer>> letterValues) {
		this.sortedLetterValues = letterValues;
	}

	public List<Entry<String, Integer>> getWordValues() {
		return wordValues;
	}

	public void setWordValues(List<Entry<String, Integer>> wordValues) {
		this.wordValues = wordValues;
	}
	
	public Map<Character, Integer> getLetterCounts() {
		return letterCounts;
	}

	public void setLetterCounts(Map<Character, Integer> letterCounts) {
		this.letterCounts = letterCounts;
	}
	
	public Map<Integer, List<String>> getIndexToCategories() {
		return indexToCategories;
	}

	public void setIndexToCategories(Map<Integer, List<String>> IndexToCategories) {
		this.indexToCategories = IndexToCategories;
	}
	
	public void setcategoryRank(List<Entry<String, Integer>> rankcats){
		
		this.categoryRank = rankcats;
	}
	
	public List<Entry<String, Integer>> getcategoryRank(){
		return categoryRank;
	}
	
	public List<Entry<Integer, Integer>> getSortedIndexValue(){
		return sortedAssignmentIndexMostConstrain;
	}

	public void setSortedIndexValue(ArrayList<Entry<Integer,Integer>> list){
		this.sortedAssignmentIndexMostConstrain = list;
	}
	
	public List<Entry<String, Integer>> getSortedWordVariables() {
		return sortedWordVariables;
	}

	public void setSortedWordVariables(List<Entry<String, Integer>> sortedWordVariables) {
		this.sortedWordVariables = sortedWordVariables;
	}
	
	public void solveLetterBased(){
		System.out.println("Started solving letter based: \n");
		solveLetterHelper(1,charAssignment.length, charAssignment);
	}
	
	
	
	
	
	
	
	
	
	//level is from 1 to size
	private void solveLetterHelper(int level, int size, char[] charAssignment)
	{
		
		if(level == size+1)
		{
			printCharArray(charAssignment);
		}
		else
		{
			for(Entry<Character, Integer> entry : sortedLetterValues)
			{
				if(constantChecker(entry.getKey(), charAssignment, level))
				{
					solveLetterHelper(level+1, size, charAssignment);
					Entry<Integer, Integer> e = sortedAssignmentIndexMostConstrain.get(level-1);
					charAssignment[e.getKey()-1] = '0';
				}
			}
		}
	}
	
	

	private boolean constantChecker(Character c, char[] charAssignment, int level)
	{
		Entry<Integer, Integer> e = sortedAssignmentIndexMostConstrain.get(level-1);
		charAssignment[e.getKey()-1] = c;
		
		int size = 0;
		
		for(String category: this.categoryAtIndex.get(e.getKey()))
		{  
			boolean result = false;
			//for loop through the categories at a given index
			for( String word: wordListMap.get(category) )
			{
				int first = categoryLocs.get(category)[0];
				int second = categoryLocs.get(category)[1];
				int third = categoryLocs.get(category)[2];

				if( (charAssignment[first-1] == word.charAt(0) || charAssignment[first-1] == '0') && (charAssignment[second-1] == word.charAt(1) || charAssignment[second-1] == '0') && (charAssignment[third-1] == word.charAt(2) || charAssignment[third-1] == '0')){
					result = true;
					break;
				}
			}
			
			if(result == true) size++;  //For each category, as along as one word fits the charAssignment, assign result to true
		}
		
		//If size = number of categories, it passes the checker
		if(size == categoryAtIndex.get(e.getKey()).size())
			return true;
		
		charAssignment[e.getKey()-1] = '0';
		return false;
	}
	
	public void printCharArray(char[] charArray){
		System.out.println("printing charAssignment: ");
		for(int i=0; i<charArray.length; i++)
			System.out.print(charArray[i]+" ");
		System.out.println();
	}
	
	public void solveWordBased(){
		System.out.println("Solving word based\n");
	}

	public void PrintSolution(){
		

		System.out.println(this.charAssignment);
		
	}
	
	public void printPuzzle(){
		System.out.println(this.charAssignment.length);
		for(String key : this.categoryLocs.keySet()){
			int[] vals = this.categoryLocs.get(key);
			System.out.println(key+": "+vals[0]+", "+vals[1]+", "+vals[2]);		}
	}
	
	
	
}
