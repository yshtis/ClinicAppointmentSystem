package com.unknownclinic.appointment.service;

import java.text.Normalizer;

public class NameSanitizer {

	public static String sanitize(String input) {
		if (input == null)
			return null;

		String normalized = Normalizer.normalize(input, Normalizer.Form.NFKC);

		normalized = normalized.replaceAll("[　]", " ");

		normalized = normalized.replaceAll(" +", " ");

		normalized = normalized.trim();

		return normalized;
	}
}
