/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.exception;

/**
 * @author haui
 *
 */
public class SchemaValidationException extends Exception {

	private static final long serialVersionUID = -3417139319277552574L;

	public SchemaValidationException(String message) {
		super(message);
	}

	public SchemaValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
