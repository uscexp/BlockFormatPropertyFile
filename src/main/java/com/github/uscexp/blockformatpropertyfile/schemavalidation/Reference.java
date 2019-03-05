package com.github.uscexp.blockformatpropertyfile.schemavalidation;

public class Reference {

	private String nameSpace;
	private String elementName;

	public Reference(String nameSpace, String elementName) {
		super();
		this.nameSpace = nameSpace;
		this.elementName = elementName;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public String getElementName() {
		return elementName;
	}
}
