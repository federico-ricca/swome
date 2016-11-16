package org.swome.impl.java;

public class MethodSignature {
	public static final String CONSTRUCTOR = "<init>";

	private String name = "";
	private String returnExpression = "";
	private String parametersExpression = "";
	private String throwsExpression = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnExpression() {
		return returnExpression;
	}

	public void setReturnExpression(String returnExpression) {
		this.returnExpression = returnExpression;
	}

	public String getParametersExpression() {
		return parametersExpression;
	}

	public void setParametersExpression(String parametersExpression) {
		this.parametersExpression = parametersExpression;
	}

	public String getThrowsExpression() {
		return throwsExpression;
	}

	public void setThrowsExpression(String throwsExpression) {
		this.throwsExpression = throwsExpression;
	}

	public String toString() {
		StringBuilder _builder = new StringBuilder();

		_builder.append(this.getReturnExpression());
		_builder.append(" ");
		_builder.append(this.getName());
		_builder.append("(");
		_builder.append(this.getParametersExpression());
		_builder.append(")");

		return _builder.toString();
	}

	public boolean isConstructor() {
		return this.getName().equals(CONSTRUCTOR);
	}
}
