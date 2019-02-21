/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import java.time.ZonedDateTime;
import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import com.github.uscexp.blockformatpropertyfile.schemavalidation.ValidateableDate;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule:
 * stringLiteral.
 */
public class AstDateLiteralTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstDateLiteralTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
			throws Exception {
		super.interpretAfterChilds(id);
		value = value.trim();
		String realValue = value.substring(1, value.length() - 1);
		Calendar calendar = DatatypeConverter.parseDateTime(realValue);
		processStore.getStack().push(new ValidateableDate(realValue, ZonedDateTime.ofInstant(calendar.getTime().toInstant(), calendar.getTimeZone().toZoneId())));
	}

}
