import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.


public class CDEParserRunner {

	private static BufferedReader in;
	private static PrintWriter out;
	//private static StringTokenizer st;
	private static ArrayList<CDE> cdeList;
	private static boolean write;
	private static Pattern pattern;
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		/*
		FileInputStream file = new FileInputStream(new File("CDE Temporal Operators.xlsx")); //input path to CDE Temporal Operators here
		//XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		Sheet input = workbook.getSheetAt(0);
		Iterator<Row> it = input.rowIterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
		*/
		pattern = buildPattern("bin//fields.txt");
		out = new PrintWriter(new BufferedWriter(new FileWriter("parsedCDEs.txt")));
		write = false;
		for(File f: new File("xml_files").listFiles()){
			in = new BufferedReader(new FileReader(f.getAbsolutePath()));
			String curLine = in.readLine();
			System.out.println("Begin parsing - " + f.getAbsolutePath());
			CDE element = new CDE();
			while(curLine != null){
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
						for(String outp : element.getElements())
							out.println(outp);
					out.println();
					}
					//clear element
					
					element = new CDE();
				}
				curLine = in.readLine();
			}
			System.out.println("Finished parsing - " + f.getAbsolutePath());	
		}
		in.close();
		out.close();
	}
	
	/**
	 * Prepares the pattern for the Matcher
	 * @param fields
	 * @throws Exception
	 */
	public static Pattern buildPattern(String fields) throws Exception{
		BufferedReader read = new BufferedReader(new FileReader(new File(fields)));
		StringBuilder p = new StringBuilder("");
		String curLine = read.readLine().trim();
		int ct = curLine.length();
		int count = 0;
		p.append("^.*<("+curLine+")>.*<(/"+curLine+")>.*$");
		while(true){
			curLine = read.readLine();
			if(curLine == null)	break;
			p.insert(12+ct*2+count, "|/" + curLine);
			p.insert(5+ct, "|" + curLine);
			ct += 1 + curLine.length();
			count++;
		}
		CDE.full = count + 1;
		StringBuilder outp = new StringBuilder("(^.*<DataElement num=\"\\d+\">.*$)");
		outp.append("|("+p.toString()+")");
		Pattern pat = Pattern.compile(outp.toString());
		return pat;
		
	}

}
