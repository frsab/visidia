package visidia.io.gml.parser;

public interface GMLParserConstants {

	int EOF = 0;

	int STRING = 9;

	int SIGN = 11;

	int DIGIT = 12;

	int INTEGER = 13;

	int MANTISSA = 14;

	int REAL = 15;

	int KEY = 16;

	int ASCII_7_BIT_SET = 17;

	int DEFAULT = 0;

	int WITHING_ISO_8859_1_CHARACTER_NAME = 1;

	int WITHING_STRING = 2;

	String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"",
			"\"\\r\\n\"", "\";\"", "<token of kind 6>", "\"&\"",
			"<token of kind 8>", "\"\\\"\"", "\"\\\"\"", "<SIGN>", "<DIGIT>",
			"<INTEGER>", "<MANTISSA>", "<REAL>", "<KEY>", "<ASCII_7_BIT_SET>",
			"\"[\"", "\"]\"", };

}
