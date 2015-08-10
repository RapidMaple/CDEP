import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Need to optimize this part to not be hard coded

public class CDE{
	public String id;
	private List<String> elements;
	private List<String> matched;
	public static final File keyWords = new File("bin//keyWordsCondensed.txt"); // list of key words to search for, formatted as "key word - key word, syn, syn,..."
	public static Map<String, List<Pattern>> keyWordPatterns;
	public static Map<String, List<String>> keyWordList;
//	public static final File keyWords2 = new File("bin//keyWordsCondensed2.txt");
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


		boolean isValid = false;
		for(String s: elements){
			Matcher m = searchPattern.matcher(s);
			if(m.matches()){
				String temp = s.toLowerCase();
				id = temp;
				for(String category : keyWordPatterns.keySet()){
					List<Pattern> patterns = keyWordPatterns.get(category);
					for(Pattern pattern : patterns){
						Matcher match = pattern.matcher(temp);
						if(match.matches()){
							String syn = pattern.toString().replaceAll("(\\.\\*\\(\\\\p\\{Punct\\}\\| \\))|(\\(\\\\p\\{Punct\\}\\| \\)\\.\\*)", "");
							if(CDEParserRunner.freqCount.get(syn) == null)
								CDEParserRunner.freqCount.put(syn, 0);
							CDEParserRunner.freqCount.put(syn, CDEParserRunner.freqCount.get(syn)+1);
							
							
							matched.add(category);
							isValid = true;
						}
					}
				}
				
				/*
				if(CDEParserRunner.bool)
				 in = new BufferedReader(new FileReader(keyWords.getAbsolutePath()));
				else
					 in = new BufferedReader(new FileReader(keyWords2.getAbsolutePath()));
				String curLine = in.readLine();
				while(curLine != null){
					String[] sp = curLine.split("(\\s+->\\s)");
					String main = sp[0];
					String[] syns = null;
					if(curLine.indexOf("->") == -1)
						syns = main.split("->");
					else
						syns = sp[1].split(",(\\s+)?");
					for(String syn : syns){
						//System.out.println(temp + " -> " + syn + " -> " + temp.matches(syn));
						if(temp.indexOf(syn) != -1 || temp.matches(syn)){
							// Statistical measure
							if(CDEParserRunner.freqCount.get(syn) == null)
								CDEParserRunner.freqCount.put(syn, 0);
							CDEParserRunner.freqCount.put(syn, CDEParserRunner.freqCount.get(syn)+1);
							
							
							matched.add(main);
							isValid = true;
						}
					}
					curLine = in.readLine();
				}
				in.close();
						*/
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
	
	@Override
	public boolean equals(Object o){
		if(o instanceof CDE){
			CDE comp = (CDE) o;
			return this.id.equals(comp.id);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return id.hashCode();
	}
	
	public String toString(){
		String ret = "";
		for(String e : elements)
			ret += e + "\n";
		return ret;
	}
}
