package service;

import java.io.File;
import java.io.IOException;
import java.util.Random;
//import com.cbrc.feedback.GenericFeedBack;

import com.cbrc.gdt.builder.CASTCodeAnnotator;
import com.cbrc.nodes.TranslationUnitNode;

public class CBRCIntegration 
{
	
	public CASTCodeAnnotator annotateFile(File f)
	{
		CASTCodeAnnotator castca = new CASTCodeAnnotator(f);
		try 
		{
			castca.annotateCode();
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InstantiationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return castca;
	}
	
	public TranslationUnitNode getHeadNode(CASTCodeAnnotator castca)
	{
		return castca.getHeadNode();
	}
	
	public void getStudentList()
	{
		// database
	}
	
	public void addTestCaseInput(File f)
	{
		// upload test case to specified input folder
	}
	
	public void addTestCaseOutput(File f)
	{
		// upload test case to specified output folder
	}

	public String feedback0(){
		Random r = new Random();
		int randNum = r.nextInt(4);
		/*
		String[] feedbacks = {"Feedback Level: 0\n"
				+ "\n"
				+ "Here is the solution to further assist you in your programming issue:\n\n"
				+ "#include <stdio.h>\n"
				+ "int main()\n"
				+ "{\n"
				+ "\tint x, y, z;\n"
				+ "\tx = 4;\n"
				+ "\ty = 1;\n"
				+ "\tz = x * y;\n"
				+ "\n"
				+ "\twhile (z <= 496)\n"
				+ "\t{\n"
				+ "\t\tprintf(\"%d \", z);\n"
				+ "\t\ty++;\n"
				+ "\t\tz = x*y;\n"
				+ "\t}\n"
				+ "\tgetchar();\n"
				+ "\treturn 0;\n"
				+ "}\n",
				"Feedback Level: 1"
				+ "\n"
				+ "Remove the error found by the compiler as indicated above.",
				"Feedback Level: 2"
				+ "\n"
				+ "The getch() method in your code is not recognized by the compiler, or is missing a required header to be able to find the desired method to be executed.",
				"Feedback Level: 3"
				+ "\n"
				+ "The only way to solve this issue is to remove getch(); in your code in line 16, or replace it with a better alternative such as getchar();."};
		*/
		String[] feedbacks = {"Feedback Level: 1\n"
				+ "\n"
				+ "Wrong code. Can you identify the error?",
				"Feedback Level: 2\n"
				+ "\n"
				+ "These are the parts of your code that are potentially wrong:\n\n"
				+ "getch()",
				"Feedback Level: 3\n"
				+ "The correct solution has the following pattern but your code does not:\n\n"
				+ "- getch()",
				"Feedback Level: 4\n"
				+ "This is the correct code: \n\n"
				+ "#include <stdio.h>\n"
				+ "int main()\n"
				+ "{\n"
				+ "\tint x, y, z;\n"
				+ "\tx = 4;\n"
				+ "\ty = 1;\n"
				+ "\tz = x * y;\n"
				+ "\n"
				+ "\twhile (z <= 496)\n"
				+ "\t{\n"
				+ "\t\tprintf(\"%d \", z);\n"
				+ "\t\ty++;\n"
				+ "\t\tz = x*y;\n"
				+ "\t}\n"
				+ "\tgetchar();\n"
				+ "\treturn 0;\n"
				+ "}\n"};
		return feedbacks[randNum];
	}
}
