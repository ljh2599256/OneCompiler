package scanner;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.PushbackReader;
import java.util.ArrayList;

public class scanner01 {
	static int LENGTH = 100;
	File file;
	PushbackReader pr;
	StringBuffer buffer;
	static ArrayList<token> arr;
	
	public static void main(String args[]){
		String fpath = "D:\\xytest01.txt";
		scanner01 scanxy = new scanner01();
		//token hh;
		//token t = new token(token_type.NONTOKEN,"",0,null);
		scanxy.initScanner(fpath);
		scanxy.getToken();
		System.out.println("记号类别	        字符串           常数值       函数名   ");
		for(token t: scanxy.arr){
			System.out.println(t.type.ordinal()+"\t\t\t"+t.lexeme+"\t\t\t"+t.value+"\t\t\t"+t.action);
		}
		
	} 

		
	public void initScanner(String s){
		try{
			file = new File(s);
			pr = new PushbackReader(new FileReader(file));
			buffer = new StringBuffer(LENGTH);
			arr = new ArrayList<token>(LENGTH);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	
	public token judgeInTable(String s){
		for(int i=0;i<18;i++){
			if(s.equalsIgnoreCase(token.table[i].lexeme)){
				return token.table[i];
			}
			else
				continue;
		}
		token errtokenToken = new token(token_type.ERRTOKEN,"Error!",0,null);
		return errtokenToken;	
	}
	

	public void getToken(){	
		int c;
		int y=0;
		try{
			while((c=pr.read())!=-1){   
				token mark = new token(token_type.NONTOKEN,null,0,null);
				buffer.delete(0,buffer.length());
				while(Character.isWhitespace(c))
					c = pr.read();
				if(Character.isLetter(c)){
					buffer.append((char)c);
					while(true){ 
						c = pr.read();
						if(Character.isLetter(c))
							buffer.append((char)c);						    
						else 
							break;
					}
					pr.unread(c);
					mark = judgeInTable(buffer.toString());
					arr.add(mark);
					//System.out.println(mark.type.name()+"     "+mark.value);
				}
				else if(Character.isDigit(c)){
					buffer.append((char)c);
					while(true){
						c = pr.read();
						if(Character.isDigit(c))
							buffer.append((char)c);
						else if(c =='.'){
							buffer.append((char)c);
							while(true){
								c = pr.read();
								if(Character.isDigit(c))
									buffer.append((char)c);
								else 
									pr.unread(c);
									break;				
							}
						}
						else{
							pr.unread(c);
							break;
						}						
					}
					double v = Double.parseDouble(buffer.toString());
					mark.setType(token_type.CONST_ID);
					mark.setLexeme(buffer.toString());
					mark.setValue(v);
					mark.setAction(null);
					arr.add(mark);
					//return mark;
				}
				else{
					switch(c){
					case '+': mark.setType(token_type.PLUS);  mark.setLexeme("+");      break;
					case '(': mark.setType(token_type.L_BRACKET); mark.setLexeme("(");  break;
					case ')': mark.setType(token_type.R_BRACKET); mark.setLexeme(")");  break;
					case '=': mark.setType(token_type.EQUAL); mark.setLexeme("=");	    break;
					case ',': mark.setType(token_type.COMMA);  mark.setLexeme(","); 	break;
					case ';': mark.setType(token_type.SEMICO); mark.setLexeme(";");		break;
					case '*': 
						c=pr.read();
						if(c=='*'){
							mark.setType(token_type.POWER); 
							mark.setLexeme("**");
						}
						else{
							pr.unread(c);
							mark.setType(token_type.MUL); 
							mark.setLexeme("*");
						}
						break;
					case '/': 
						c=pr.read();
						if(c=='/'){
							while((c=pr.read())!=-1){
								if(c!='\n')
									continue;
								else
									break;
							}						
						}
						else{
							pr.unread(c);
							mark.setType(token_type.DIV);
							mark.setLexeme("/");
						}
						break;
					case '-': 
						c=pr.read();
						if(c=='-'){
							while((c=pr.read())!=-1){
								if(c!='\n')   continue;
								else      	  break;
							}
						}
						else{
							pr.unread(c);
							mark.setType(token_type.MINUS);
							mark.setLexeme("-");
						}
						break;
					 default: 
						 mark.setType(token_type.ERRTOKEN);
						 break;
					}
					arr.add(mark);	
				}
							
			}
			
		}catch(IOException e){
			e.printStackTrace();
    	}finally {
            try {
                pr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
}
