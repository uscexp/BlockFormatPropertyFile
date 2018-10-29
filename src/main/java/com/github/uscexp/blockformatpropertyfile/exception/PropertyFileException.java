/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.exception;

/**
 * @author haui
 *
 */
public class PropertyFileException extends Exception {

	private static final long serialVersionUID = -8604138106370403044L;

	public PropertyFileException(String message) {
		super(message);
	}

	public PropertyFileException(String message, Throwable cause) {
		super(message, cause);
	}
}
