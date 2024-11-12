package com.example.servingwebcontent.consts;

public interface Mask {

	String PATTERN_END = "\"?\\s?[:=]\\s?\"([^\"]*)\"";
	String PATTERN_ARRAY_END = "\"?\\s?[:=]\\s?\\[(.*)\\]";
	String REPLACE_STRING = "*****";
}
