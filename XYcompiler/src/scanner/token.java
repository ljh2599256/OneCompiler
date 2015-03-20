package scanner;


enum token_type{
	ORIGIN,SCALE,ROT,IS,TO,STEP,DRAW,FOR,FROM,T,SEMICO,L_BRACKET,R_BRACKET,COMMA,PLUS,MINUS,MUL,DIV,
	POWER,FUNC,CONST_ID,EQUAL,NONTOKEN,ERRTOKEN }

public class token {
	token_type type;
	String lexeme;
	double value;
	String action;	
	public token(token_type a,String b,double c,String d){
		type = a;
		lexeme = b;
		value = c;
		action =d;
	}
	
	public void setValue(double value){
		this.value = value;		
	}
	public void setLexeme(String lexeme){
		this.lexeme = lexeme;
	}
	public void setType(token_type t){
		type = t;             
	}    
	public void setAction(String s){
		this.action = s;
	}
	public static token table[]={
		new token(token_type.CONST_ID,"PI",3.1415926,null),
		new token(token_type.CONST_ID,"E",2.71828,null),
		new token(token_type.T,"T",0,null),
		new token(token_type.FUNC,"SIN",0,"sin"),
		new token(token_type.FUNC,"COS",0,"cos"),
		new token(token_type.FUNC,"TAN",0,"tan"),
		new token(token_type.FUNC,"LN",0,"log"),
		new token(token_type.FUNC,"EXP",0,"exp"),
		new token(token_type.FUNC,"SQRT",0,"sqrt"),
		new token(token_type.ORIGIN,"ORIGIN",0,null),
		new token(token_type.SCALE,"SCALE",0,null),
		new token(token_type.ROT,"rot",0,null),
		new token(token_type.IS,"IS",0,null),
		new token(token_type.FOR,"FOR",0,null),
		new token(token_type.FROM,"FROM",0,null),
		new token(token_type.TO,"TO",0,null),
		new token(token_type.STEP,"STEP",0,null),
		new token(token_type.DRAW,"DRAW",0,null)
	};
	
	
}

