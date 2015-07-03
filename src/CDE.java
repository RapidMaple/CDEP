import java.util.ArrayList;
import java.util.List;

// Need to optimize this part to not be hard coded

public class CDE {
	private List<String> elements;
	public static String pattern;
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
		String eval = elements.get(3);
		if(eval.matches(".*(?i)(day)(?-i).*"))
			return true;
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
