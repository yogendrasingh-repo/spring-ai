package org.springframework.ai.convert;

import org.springframework.ai.parser.FormatProvider;
import org.springframework.core.convert.converter.Converter;

public interface ChatConverter<T> extends Converter<String, T>, FormatProvider {

}