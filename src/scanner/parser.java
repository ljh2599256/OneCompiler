package scanner;


public class parser {
	static token xy;        ///////////////////static token is called  xy
    double parameter;
	static int ci = 0;
	public static void main(String args[]){
		String fpath = "D:\\xytest01.txt";
		scanner01 scanxy2 = new scanner01();
		scanxy2.initScanner(fpath);
		scanxy2.getToken();
		fetchToken();
		scanxy2.arr.add(new token(token_type.NONTOKEN,"End!",0,null));
		program();
	}
	
	
	static void syntaxError(int c){
		switch(c){
			case 1:
				System.out.println("错误记号！");
				break;
			case 2:
				System.out.println("不是预期记号！");
				break;	
		}		
	}
	
	static void fetchToken(){
		xy = scanner01.arr.get(ci);
		ci++;
	}
	static void matchToken(token_type name){
		if(name!=xy.type)
			syntaxError(2);
		fetchToken();		
	}
	
	static void program(){
		System.out.println("enter program  ");
		while(xy.type != token_type.NONTOKEN){
			statement();
			matchToken(token_type.SEMICO);
			pp(";");
		}
		System.out.println("exit from program ");	
	}
	
	static void statement(){
		System.out.println("enter statement ");
		switch(xy.type){
			case ORIGIN: originStatement();    break;
			case SCALE:  scaleStatement();    break;
			case ROT:    rotStatement();      break;
			case FOR:    forStatement();      break;
			default:	 syntaxError(2);
		}
		System.out.println("exit from statement");
	}
	
	static void originStatement(){
		node tempNode;
		System.out.println("enter  originStatement");
		matchToken(token_type.ORIGIN);    pp("origin");
		matchToken(token_type.IS); 		  pp("is");
		matchToken(token_type.L_BRACKET);      pp("(");
		tempNode = expression();
		matchToken(token_type.COMMA);      pp(",");
		tempNode = expression();
		matchToken(token_type.R_BRACKET);      pp(")");
		System.out.println("exit from originStatement ");		
	}
	
	
	static void rotStatement(){
		node tempNode;
		System.out.println("enter rotStatement ");
		matchToken(token_type.ROT);			pp("rot");
		matchToken(token_type.IS);  		pp("is");
		tempNode = expression();
		System.out.println("exit from rotStatement ");		
	}
	
	static void forStatement(){
		node startNode,endNode,xNode,yNode,stepNode;
		System.out.println("enter forStatement ");
		matchToken(token_type.FOR);     pp("for");
		matchToken(token_type.T);        pp("T");
		matchToken(token_type.FROM);     pp("from");
		startNode = expression();
		matchToken(token_type.TO);          pp("to");
		endNode = expression();
		matchToken(token_type.STEP);    pp("step");
		stepNode = expression();
		matchToken(token_type.DRAW);    pp("draw");
		matchToken(token_type.L_BRACKET);    pp("(");
		xNode = expression();
		matchToken(token_type.COMMA);      pp(",");
		yNode = expression();
		matchToken(token_type.R_BRACKET);   pp(")");
		System.out.println("exit from forStatement");		
	}
	
	static void scaleStatement(){
		node tempNode;
		System.out.println("enter scaleStatement  ");
		matchToken(token_type.SCALE);   pp("scale");
		matchToken(token_type.IS);		pp("is");
		matchToken(token_type.L_BRACKET);	pp("(");
		tempNode = expression();
		matchToken(token_type.COMMA);		pp(",");
		tempNode = expression();
		matchToken(token_type.R_BRACKET);	pp("");
		System.out.println("exit from scaleStatement ");		
	}
	
	static node expression(){
		node leftNode,rightNode;
		token_type token_now;
		System.out.println("enter expression");
		leftNode = term();
		while(xy.type==token_type.PLUS||xy.type==token_type.MINUS){
			token_now = xy.type;
			matchToken(token_now);
			rightNode = expression();
			leftNode = makeExprNode(token_now,leftNode,rightNode);
		}
		printTree(leftNode,0);
		System.out.println("exit from expression ");
		return leftNode;		
	}

	static node term(){
		//System.out.println("enter term ");
		node leftNode,rightNode;
		token_type token_now;
		leftNode = factor();
		while(xy.type==token_type.MUL||xy.type==token_type.DIV){
			token_now = xy.type;
			matchToken(token_now);
			rightNode = factor();
			leftNode = makeExprNode(token_now,leftNode, rightNode);			
		}
		//System.out.println("exit from term ");
		return leftNode;
		
	}
	
	static node factor(){
		//System.out.println("enter factor");
		node leftNode,rightNode;
		if(xy.type==token_type.PLUS){
			matchToken(token_type.PLUS);
			rightNode = factor();
		}
		else if(xy.type==token_type.MINUS){
			matchToken(token_type.MINUS);
			rightNode = factor();
			leftNode = new caseConst();
			caseConst left2 = (caseConst)leftNode;
			left2.setType(token_type.CONST_ID);
			left2.setV(0.0);
			rightNode = makeExprNode(token_type.MINUS,left2,rightNode);					
		}
		else {
			rightNode = component();
		}
		//System.out.println("exit from factor");
		return rightNode;
	}
	
	
	static node component(){
		//System.out.println("enter component");
		node leftNode,rightNode;
		leftNode = atom();
		if(xy.type==token_type.POWER){
			matchToken(token_type.POWER);
			rightNode = component();
			leftNode = makeExprNode(token_type.POWER, leftNode, rightNode);
		}
		//System.out.println("exit from component");
		return leftNode;
	}
	
	
	static node atom(){
		//System.out.println("enter atom");
		token hi = xy;
		//System.out.println(hi.type.ordinal()+"  "+hi.value+hi.action);
		node tmpNode;
		node address = new node();
		switch(hi.type){
		case CONST_ID:
			matchToken(token_type.CONST_ID);
			tmpNode = new caseConst(hi.value);   //caseConst  is son of node
			address = makeExprNode(token_type.CONST_ID,tmpNode,null);
			break;
		case T:
			matchToken(token_type.T);
			tmpNode = new caseT();
			address = makeExprNode(token_type.T,tmpNode,null);
			break;
		case FUNC:
			matchToken(token_type.FUNC);
			matchToken(token_type.L_BRACKET);
			tmpNode = expression();
			node fNode = new caseFun(hi.action);
			address = makeExprNode(token_type.FUNC,tmpNode,fNode);
			matchToken(token_type.R_BRACKET);
			break;
		case L_BRACKET:
			matchToken(token_type.L_BRACKET);
			address = expression();
			matchToken(token_type.R_BRACKET);
			break;
		default:
			syntaxError(2);
		}
		//System.out.println("exit from atom");
		return address;
	}

	
	static node makeExprNode(token_type tp,node first,node second){
		node vNode;       //new node() was just deleted by me
		switch(tp){
		case CONST_ID:
			vNode = new caseConst();
			vNode = first;
			break;
		case T:
			vNode = new caseT();
			vNode = first;
			break;			
		case FUNC:
			vNode = new caseFun();
			vNode = second;
			((caseFun)vNode).setChild(first);
			break;
		default:
			vNode = new caseTwo(tp,first,second);
			break;		
		}
		
		return vNode;		
	}
	
	static void printTree(node a,int count){
		int k = 0;
		for(k=0;k<count;k++)
			System.out.print("\t");
		switch(a.operator){
			case PLUS:	System.out.println("+");	break;
			case MINUS:	System.out.println("-");	break;
			case POWER:	System.out.println("**");	break;
			case FUNC:	
				System.out.println(((caseFun)a).name);
				break;
			case DIV:	System.out.println("/");	break;
			case MUL:	System.out.println("*");	break;
			case CONST_ID:	System.out.println(((caseConst)a).constV);	break;
			case T:		System.out.println("T");	break;
			default:	System.out.println("Error!!");		
			}
			if(a.operator==token_type.T||a.operator==token_type.CONST_ID)
				return;
			else if(a.operator==token_type.FUNC)
				printTree(((caseFun)a).child,count+1);
			else{
				printTree(((caseTwo)a).left,count+1);
				printTree(((caseTwo)a).right,count+1);
			}
	}
				
	
	static void pp(String s){
		System.out.println(s);
	}
	
}
