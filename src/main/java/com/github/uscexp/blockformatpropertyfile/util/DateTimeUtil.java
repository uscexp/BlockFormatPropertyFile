package com.github.uscexp.blockformatpropertyfile.util;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

public final class DateTimeUtil {

	private DateTimeUtil() {
	}

	public static DateTimeFormatter dateFormatter() {
		return dateFormatter(null, null);
	}

	public static DateTimeFormatter dateFormatter(String pattern, String locale) {
		DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder()
				.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
				.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
				.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0);
		if (pattern != null) {
			dateTimeFormatterBuilder.appendPattern(pattern);
		}
		DateTimeFormatter dateTimeFormatter = null;
		if (locale != null) {
			dateTimeFormatter = dateTimeFormatterBuilder.toFormatter(Locale.forLanguageTag(locale));
		} else {
			dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();
		}
		return dateTimeFormatter;
	}
}
