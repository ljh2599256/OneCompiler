package scanner;
 

public class node{ 
	token_type operator;
	public node(){
		operator = null;
	}
	public void setType(token_type e){
		operator = e;
	}
}


class caseTwo extends node{
	node left;
	node right;
	public caseTwo(token_type p, node a, node b){
		operator = p;
		left = a;
		right = b;
	}
}


class caseFun extends node{
	node child;
	String name;
	public caseFun(){
		operator = token_type.FUNC;
		child = null;
		name = null;
	}
	public caseFun(String n){
		operator = token_type.FUNC;
		child = null;
		name = n;
	}
	public void setChild(node k){
		child = k;
	}
	public String getName(){
		return name;
	}
}



class caseConst extends node{
	double constV;
	public caseConst(){
		operator = token_type.CONST_ID;
		constV = 0;
	}
	public caseConst(double c){
		operator = token_type.CONST_ID;
		constV = c;
	}
	public void setV(double c){
		constV = c;
	}
}



class caseT extends node{
	public caseT(){
		operator = token_type.T;
	}
}


