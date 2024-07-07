package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate;

import java.io.IOException;

public class Main {

    /**
     * given the path of a java program which you want to parse and the output directory
     * @param args
     * @throws IOException
     */
	/*public static void main(String[] args) throws IOException {
		String inputDir = "input/";
		String outputDir = "output/AST/";
		File iDir = new File(inputDir);
		File o = new File(outputDir);
		if(!o.exists()){
			o.mkdir();
		}
		File[] inputFiles = iDir.listFiles();
		if(inputFiles != null && inputFiles.length>0 ){
			for(File inputFile : inputFiles){
				ASTGenerator astGenerator = new ASTGenerator(inputFile);
				List<MyMethodNode> methodNodeList = astGenerator.getMethodNodeList();
				String canonicalPath = inputFile.getCanonicalPath();
				String jsonStr = ASTtoJSON.ASTtoJsonParser(canonicalPath,methodNodeList);
				FileUtil.writeFile(outputDir + canonicalPath.hashCode() + ".json", jsonStr);
			}
		}
		else {
			System.out.println("Lack input!");
		}
		System.out.println("Done.");
	}*/
}
