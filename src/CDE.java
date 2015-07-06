import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Need to optimize this part to not be hard coded

public class CDE {
	private List<String> elements;
	public static Pattern searchPattern; // constructed from searchFields.txt
	public static Pattern keyWordsPattern; //constructed from Temporal CDE Operators.xls
	public static int full;
	
	public CDE(){
		elements = new ArrayList<String>();
	}
	
	public CDE(List<String> other){
		elements = deepCopy(other);
	}
	
	public void addElement(String s){
		elements.add(s);
	}
	public boolean isFull(){
		return elements.size() == full;
	}
	public boolean isValid(){
		//get PREFERREDDEFINITION field
		for(String s: elements){
			Matcher m = searchPattern.matcher(s);
			if(m.matches()){
				m = keyWordsPattern.matcher(s);
				if(m.matches()) return true;
			}
		}
		return false;
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
