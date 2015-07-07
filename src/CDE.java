import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Need to optimize this part to not be hard coded

public class CDE {
	private List<String> elements;
	private List<String> matched;
	public static final File keyWords = new File("bin//keyWords.txt"); // list of key words to search for, formatted as "key word - key word, syn, syn,..."
	public static Pattern searchPattern; // constructed from searchFields.txt
	public static int full;
	
	public CDE(){
		elements = new ArrayList<String>();
		matched = new ArrayList<String>();
	}
	
	public CDE(List<String> other){
		elements = deepCopy(other);
		matched = new ArrayList<String>();
	}
	
	public void addElement(String s){
		elements.add(s);
	}
	public boolean isFull(){
		return elements.size() == full;
	}
	public boolean isValid() throws Exception{
		//get PREFERREDDEFINITION field
		boolean isValid = false;
		for(String s: elements){
			Matcher m = searchPattern.matcher(s);
			if(m.matches()){
				String temp = s.toLowerCase();
				BufferedReader in = new BufferedReader(new FileReader(keyWords.getAbsolutePath()));
				String curLine = in.readLine().trim();
				while(curLine != null){
					String[] sp = curLine.split("(->)|(\\s+->)|(->\\s+)|(\\s+->\\s)");
					String main = sp[0];
					String[] syns = null;
					if(curLine.indexOf("->") == -1)
						syns = main.split("->");
					else
						syns = sp[1].split("(,\\s+)|(,)");
					for(String syn : syns){
						if(temp.indexOf(syn) != -1){
							matched.add(main);
							isValid = true;
						}
					}
					curLine = in.readLine();
				}
				in.close();
			}
		}
		return isValid;
	}
	
	public List<String> getMatched(){
		return matched;
	}
	public List<String> getElements(){
		return elements;
	}
	
	private List<String> deepCopy(List<String> other){
		List<String> outp = new ArrayList<String>();
		for(String e : other)
			outp.add(e);
		return outp;
	}
	public String toString(){
		String ret = "";
		for(String e : elements)
			ret += e + "\n";
		return ret;
	}
}
