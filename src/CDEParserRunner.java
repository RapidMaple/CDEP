import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.


public class CDEParserRunner {

	private static BufferedReader in;
	private static PrintWriter out;
	private static PrintWriter otherout;
	private static PrintWriter statout;
	//private static StringTokenizer st;
	private static List<CDE> cdeList;
	private static List<CDE> otherCDEList;
	private static boolean write;
	private static Pattern pattern;
	public static Map<String, Integer> freqCount;
	
	public static void main(String[] args) throws Exception{

		int counter = 0;
		pattern = buildPattern("bin//fields.txt");
		CDE.searchPattern = Pattern.compile(searchPattern("bin//searchFields.txt").toString());
		out = new PrintWriter(new BufferedWriter(new FileWriter("parsedCDEs.txt")));
		otherout = new PrintWriter(new BufferedWriter(new FileWriter("cParsedCDEs.txt")));
		statout = new PrintWriter(new BufferedWriter(new FileWriter("parsedCDEStats.txt")));
		write = false;
		cdeList = new ArrayList<>();
		otherCDEList = new ArrayList<>();
		freqCount = new HashMap<>();
		for(File f: new File("xml_files").listFiles()){
			in = new BufferedReader(new FileReader(f.getAbsolutePath()));
			String curLine = in.readLine();
			System.out.println("Begin parsing - " + f.getAbsolutePath());
			CDE element = new CDE();
			while(curLine != null){
				counter++;
				Matcher m = pattern.matcher(curLine);
				/*if(curLine.matches("^.*<DataElement num=\"\\d+\">.*$") || curLine.matches("^.*<(PUBLICID|LONGNAME|PREFERREDDEFINITION)>.*<(/PUBLICID|/LONGNAME|/PREFERREDDEFINITION)>.*$")){
					element.addElement(curLine);
				}
				*/
				if(m.matches()){
					element.addElement(curLine);
				}
				if(element.isFull()){
					//do processing here
					if(element.isValid()){
						out.println(element.getMatched());
						for(String outp : element.getElements())
							out.println(outp);
						out.println();
						cdeList.add(element);
					}
					else{
						for(String outp : element.getElements())
							otherout.println(outp);
						otherout.println();	
						otherCDEList.add(element);
					}
					//clear element
					element = new CDE();
				}
				curLine = in.readLine();
			}
			System.out.println("Finished parsing - " + f.getAbsolutePath());	
		}
		System.out.println(cdeList.size());
		System.out.println(counter);
		Map<List<String>, Integer> counts = countInstances(cdeList);
		for(List<String> list : counts.keySet()){
			out.println(list + " - " + counts.get(list));
		}
		in.close();
		out.close();
		otherout.close();
		
		getStats();
		statout.close();
		generateRandomSamples(3, 300);
	}
	
	/**
	 * Prepares the pattern for the Matcher
	 * @param fields
	 * @throws Exception
	 */
	public static Pattern buildPattern(String fields) throws Exception{
		StringBuilder p = helpBuild(fields);
		StringBuilder outp = new StringBuilder("(^.*<DataElement num=\"\\d+\">$)");
		outp.append("|("+p.toString() + ")");
		System.out.println(outp);
		Pattern pat = Pattern.compile(outp.toString());
		return pat;
	}
	
	public static Pattern searchPattern(String searchFields) throws Exception{
		StringBuilder p = helpBuild(searchFields);
		Pattern pat = Pattern.compile(p.toString());
		return pat;
	}
	
	private static StringBuilder helpBuild(String file) throws Exception{
		BufferedReader read = new BufferedReader(new FileReader(new File(file)));
		StringBuilder p = new StringBuilder("");
		String curLine = read.readLine().trim();
		int ct = curLine.length();
		int count = 0;
		p.append("^.*<("+curLine+")>.*$"); //remove second slash ending?
		while(true){
			curLine = read.readLine();
			if(curLine == null)	break;
			//p.insert(12+ct*2+count, "|/" + curLine);
			p.insert(5+ct, "|" + curLine);
			ct += 1 + curLine.length();
			count++;
		}
		System.out.println(p.toString());
		if(CDE.full == 0)
			CDE.full = count + 2;
		read.close();
		return p;
	}
	
	private static Map<List<String>, Integer> countInstances(List<CDE> cdeList){
		Map<List<String>, Integer> freq = new HashMap<List<String>, Integer>();
		for(CDE cde : cdeList){
			List<String> temp = cde.getMatched();
			if(freq.get(temp) == null)
				freq.put(temp, 0);
			freq.put(temp, freq.get(temp)+1);
		}
		return freq;
	}
	private static void generateRandomSamples(int num, int n) throws Exception{
		for(int i=1;i<=num;i++){
			Random rand = new Random();
			PrintWriter gen = new PrintWriter(new BufferedWriter(new FileWriter("parsedGroup" + i + ".txt")));
			PrintWriter cGen = new PrintWriter(new BufferedWriter(new FileWriter("cParsedGroup" + i + ".txt")));
			List<CDE> curSample = new ArrayList<>();
			Set<Integer> indSet = new HashSet<>();
			while(true){
				if(curSample.size()==n)
					break;
				int oldSize = indSet.size();
				int randNum = rand.nextInt(cdeList.size());
				indSet.add(randNum);
				if(oldSize != indSet.size()){
					CDE temp = cdeList.get(randNum);
					curSample.add(temp);
					for(String ele : temp.getElements())
						gen.println(ele);
					gen.println();
				}
			}
			curSample = new ArrayList<>();
			indSet = new HashSet<>();
			gen.close();
			while(true){
				if(curSample.size()==n)
					break;
				int oldSize = indSet.size();
				int randNum = rand.nextInt(otherCDEList.size());
				indSet.add(randNum);
				if(oldSize != indSet.size()){
					CDE temp = otherCDEList.get(randNum);
					curSample.add(temp);
					for(String ele : temp.getElements())
						cGen.println(ele);
					cGen.println();
				}
			}
			cGen.close();
		}
		
	}
	private static void getStats(){
		int tot = 0;
		for(String op : freqCount.keySet()){
			tot += freqCount.get(op);
		}
		statout.printf("%-15s | %15s | %26s","keyWord","frequency count", "percent composition");
		statout.println();
		statout.println("______________________________________________________________");
		for(String op : freqCount.keySet()){
			statout.printf("%-15s | %15d | %25.5f%s", op, freqCount.get(op), (freqCount.get(op) * 1.0 / tot * 100),"%");
			statout.println();
		}
	}

}
