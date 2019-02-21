/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.schemavalidation;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;

/**
 * @author haui
 *
 */
public final class ValidateableDate
		implements Temporal, ChronoZonedDateTime<LocalDate>, Serializable {

	private static final long serialVersionUID = 9162985720321056805L;

	private String stringRepresentation;
	private ZonedDateTime zonedDateTime;

	public ValidateableDate(String stringRepresentation, ZonedDateTime localDateTime) {
		this.zonedDateTime = localDateTime;
		this.stringRepresentation = stringRepresentation;
	}

	public String getStringRepresentation() {
		return stringRepresentation;
	}

	public void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

	public ZonedDateTime getZonedDateTime() {
		return zonedDateTime;
	}

	@Override
	public boolean isSupported(TemporalField field) {
		return zonedDateTime.isSupported(field);
	}

	@Override
	public long getLong(TemporalField field) {
		return zonedDateTime.getLong(field);
	}

	@Override
	public LocalDate toLocalDate() {
		return zonedDateTime.toLocalDate();
	}

	@Override
	public LocalTime toLocalTime() {
		return zonedDateTime.toLocalTime();
	}

	@Override
	public boolean isSupported(TemporalUnit unit) {
		return zonedDateTime.isSupported(unit);
	}

	@Override
	public long until(Temporal endExclusive, TemporalUnit unit) {
		return zonedDateTime.until(endExclusive, unit);
	}

	@Override
	public ChronoLocalDateTime<LocalDate> toLocalDateTime() {
		return zonedDateTime.toLocalDateTime();
	}

	@Override
	public ZoneOffset getOffset() {
		return zonedDateTime.getOffset();
	}

	@Override
	public ZoneId getZone() {
		return zonedDateTime.getZone();
	}

	@Override
	public ChronoZonedDateTime<LocalDate> withEarlierOffsetAtOverlap() {
		return zonedDateTime.withEarlierOffsetAtOverlap();
	}

	@Override
	public ChronoZonedDateTime<LocalDate> withLaterOffsetAtOverlap() {
		return zonedDateTime.withLaterOffsetAtOverlap();
	}

	@Override
	public ChronoZonedDateTime<LocalDate> withZoneSameLocal(ZoneId zone) {
		return zonedDateTime.withZoneSameLocal(zone);
	}

	@Override
	public ChronoZonedDateTime<LocalDate> withZoneSameInstant(ZoneId zone) {
		return zonedDateTime.withZoneSameInstant(zone);
	}

	@Override
	public ChronoZonedDateTime<LocalDate> with(TemporalField field, long newValue) {
		return zonedDateTime.with(field, newValue);
	}

	@Override
	public ChronoZonedDateTime<LocalDate> plus(long amountToAdd, TemporalUnit unit) {
		return zonedDateTime.plus(amountToAdd, unit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((zonedDateTime == null) ? 0 : zonedDateTime.hashCode());
		result = prime * result + ((stringRepresentation == null) ? 0 : stringRepresentation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ValidateableDate other = (ValidateableDate) obj;
		if (zonedDateTime == null) {
			if (other.zonedDateTime != null) {
				return false;
			}
		} else if (!zonedDateTime.equals(other.zonedDateTime)) {
			return false;
		}
		if (stringRepresentation == null) {
			if (other.stringRepresentation != null) {
				return false;
			}
		} else if (!stringRepresentation.equals(other.stringRepresentation)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ValidateableDate [stringRepresentation=" + stringRepresentation + ", localDateTime=" + zonedDateTime + "]";
	}
}
