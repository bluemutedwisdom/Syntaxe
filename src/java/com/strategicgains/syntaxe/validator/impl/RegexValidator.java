/*
    Copyright 2011, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.syntaxe.validator.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.strategicgains.syntaxe.annotation.RegexValidation;
import com.strategicgains.syntaxe.util.Validations;
import com.strategicgains.syntaxe.validator.AnnotatedFieldValidator;

/**
 * @author toddf
 * @since Mar 17, 2011
 */
public class RegexValidator
extends AnnotatedFieldValidator<RegexValidation>
{
	private Pattern regex;

	public RegexValidator(Field field, RegexValidation annotation)
	{
		super(field, annotation);
		regex = Pattern.compile(annotation.pattern());
	}

	@Override
	public void perform(Object instance, List<String> errors)
	{
		Object value = getValue(instance);
		String name = determineFieldName();
		validate(name, value, getAnnotation().nullable(), regex, errors);
	}

	protected String determineFieldName()
	{
		return (getAnnotation().name().isEmpty() ? getFieldName() : getAnnotation().name());
	}
	
	public static void validate(String name, Object value, boolean isNullable, Pattern regex, List<String> errors)
	{
		if (value != null && !(value instanceof String))
		{
			errors.add(name + " must be a string");
			return;
		}

		String stringValue = (String) value;

		if (!isNullable)
		{
			Validations.require(name, stringValue, errors);
		}

		if (stringValue != null)
		{
			Matcher matcher = regex.matcher(stringValue);
	
			if (!matcher.matches())
			{
				errors.add(name + " does not match the regular expression pattern: " + regex.pattern());
			}
		}
	}
}