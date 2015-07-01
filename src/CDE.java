import java.util.ArrayList;
import java.util.List;



public class CDE {
	private List<String> elements;
	public static String pattern;
	
	public CDE(){
		elements = new ArrayList<String>();
	}
	
	public void addElement(String s){
		elements.add(s);
	}
	public boolean isFull(){
		return elements.size() == 4;
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
	public String toString(){
		String ret = "";
		for(String e : elements)
			ret += e + "\n";
		return ret;
	}
}
