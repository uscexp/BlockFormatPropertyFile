/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.schemavalidation;

import java.util.Date;

/**
 * @author haui
 *
 */
public class ValidateableDate extends Date {

	private static final long serialVersionUID = 9162985720321056805L;

	private String stringRepresentation;

	public ValidateableDate(String stringRepresentation, long date) {
		super(date);
		this.stringRepresentation = stringRepresentation;
	}

	public String getStringRepresentation() {
		return stringRepresentation;
	}

	public void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

}
