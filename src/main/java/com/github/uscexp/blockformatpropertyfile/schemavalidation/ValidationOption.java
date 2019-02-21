package com.github.uscexp.blockformatpropertyfile.schemavalidation;

import java.util.Arrays;
import java.util.Optional;

public enum ValidationOption {
	PATTERN("pattern"), LOCALE("locale"), MIN_OCCURES("minOccures"), MAX_OCCURES("maxOccures"), REFERENCE("reference");

	private String option;

	private ValidationOption(String option) {
		this.option = option;
	}

	public String getOption() {
		return option;
	}

	public Optional<String> getOption(String[] options) {
		Optional<String> result = Optional.empty();
		Optional<String> foundOption = Arrays.stream(options).filter(o -> o.toLowerCase().startsWith(option.toLowerCase())).findFirst();
		if (foundOption.isPresent()) {
			String[] split = foundOption.get().split("=", 2);

			if (split.length == 2) {
				result = Optional.of(split[1]);
			}
		}
		return result;
	}
}
