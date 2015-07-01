import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;
//import org.apache.


public class CDEParserRunner {

	private static BufferedReader in;
	private static PrintWriter out;
	private static StringTokenizer st;
	private static boolean write;
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		//FileInputStream file = new FileInputStream(new File("CDE Temporal Operators.xlsx"));
		//XSSFWorkbook workbook = new XSSFWorkbook(file);
		out = new PrintWriter(new BufferedWriter(new FileWriter("parsedCDEs.txt")));
		write = false;
		for(File f: new File("C:\\Users\\hchen13\\workspace\\CDEParser\\xml_files").listFiles()){
			in = new BufferedReader(new FileReader(f.getAbsolutePath()));
			String curLine = in.readLine();
			System.out.println("Begin parsing - " + f.getAbsolutePath());
			CDE element = new CDE();
			while(curLine != null){
				if(curLine.matches("^.*<DataElement num=\"\\d+\">.*$") || curLine.matches("^.*<(PUBLICID|LONGNAME|PREFERREDDEFINITION)>.*<(/PUBLICID|/LONGNAME|/PREFERREDDEFINITION)>.*$")){
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
	
	public static void recur(File dir) throws Exception{
	}

}
