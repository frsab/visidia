package visidia.io.gml.parser;

public class GMLParserTokenManager {

	static final int[] jjnextStates = { 8, 9, 0, 3, 4, };

	public static final String[] jjstrLiteralImages = { "", null, null, null,
			null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, "\133", "\135", };

	public static final String[] lexStateNames = { "DEFAULT",
			"WITHING_ISO_8859_1_CHARACTER_NAME", "WITHING_STRING", };

	public static final int[] jjnewLexState = { -1, -1, -1, -1, -1, 2, -1, 1,
			-1, 0, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, };

	static final long[] jjtoToken = { 0xda201L, };

	static final long[] jjtoSkip = { 0x1eL, };

	static final long[] jjtoMore = { 0x5e0L, };

	private ASCIICharStream input_stream;

	private final int[] jjrounds = new int[11];

	private final int[] jjstateSet = new int[22];

	StringBuffer image;

	int jjimageLen;

	int lengthOfMatch;

	protected char curChar;

	int curLexState = 0;

	int defaultLexState = 0;

	int jjnewStateCnt;

	int jjround;

	int jjmatchedPos;

	int jjmatchedKind;

	public GMLParserTokenManager(ASCIICharStream stream) {
		if (ASCIICharStream.staticFlag) {
			throw new Error(
					"ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
		}
		this.input_stream = stream;
	}

	public GMLParserTokenManager(ASCIICharStream stream, int lexState) {
		this(stream);
		this.SwitchTo(lexState);
	}

	private final int jjStopStringLiteralDfa_0(int pos, long active0) {
		switch (pos) {
		default:
			return -1;
		}
	}

	private final int jjStartNfa_0(int pos, long active0) {
		return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(pos, active0),
				pos + 1);
	}

	private final int jjStopAtPos(int pos, int kind) {
		this.jjmatchedKind = kind;
		this.jjmatchedPos = pos;
		return pos + 1;
	}

	private final int jjMoveStringLiteralDfa0_0() {
		switch (this.curChar) {
		case 13:
			return this.jjMoveStringLiteralDfa1_0(0x10L);
		case 34:
			return this.jjStopAtPos(0, 10);
		case 91:
			return this.jjStopAtPos(0, 18);
		case 93:
			return this.jjStopAtPos(0, 19);
		default:
			return this.jjMoveNfa_0(5, 0);
		}
	}

	private final int jjMoveStringLiteralDfa1_0(long active0) {
		try {
			this.curChar = this.input_stream.readChar();
		} catch (java.io.IOException e) {
			this.jjStopStringLiteralDfa_0(0, active0);
			return 1;
		}
		switch (this.curChar) {
		case 10:
			if ((active0 & 0x10L) != 0L) {
				return this.jjStopAtPos(1, 4);
			}
			break;
		default:
			break;
		}
		return this.jjStartNfa_0(0, active0);
	}

	private final void jjCheckNAdd(int state) {
		if (this.jjrounds[state] != this.jjround) {
			this.jjstateSet[this.jjnewStateCnt++] = state;
			this.jjrounds[state] = this.jjround;
		}
	}

	private final void jjAddStates(int start, int end) {
		do {
			this.jjstateSet[this.jjnewStateCnt++] = GMLParserTokenManager.jjnextStates[start];
		} while (start++ != end);
	}

	private final void jjCheckNAddTwoStates(int state1, int state2) {
		this.jjCheckNAdd(state1);
		this.jjCheckNAdd(state2);
	}

	private final void jjCheckNAddStates(int start, int end) {
		do {
			this.jjCheckNAdd(GMLParserTokenManager.jjnextStates[start]);
		} while (start++ != end);
	}

	private final int jjMoveNfa_0(int startState, int curPos) {
		// int[] nextStates;
		int startsAt = 0;
		this.jjnewStateCnt = 11;
		int i = 1;
		this.jjstateSet[0] = startState;
		// int j,
		int kind = 0x7fffffff;
		for (;;) {
			if (++this.jjround == 0x7fffffff) {
				this.ReInitRounds();
			}
			if (this.curChar < 64) {
				long l = 1L << this.curChar;
				// MatchLoop:
				do {
					switch (this.jjstateSet[--i]) {
					case 5:
						if ((0x3ff000000000000L & l) != 0L) {
							if (kind > 13) {
								kind = 13;
							}
							this.jjCheckNAddStates(0, 2);
						} else if ((0x280000000000L & l) != 0L) {
							this.jjCheckNAddStates(0, 2);
						} else if (this.curChar == 46) {
							if (kind > 15) {
								kind = 15;
							}
							this.jjCheckNAddTwoStates(1, 2);
						}
						break;
					case 0:
						if (this.curChar != 46) {
							break;
						}
						kind = 15;
						this.jjCheckNAddTwoStates(1, 2);
						break;
					case 1:
						if ((0x3ff000000000000L & l) == 0L) {
							break;
						}
						if (kind > 15) {
							kind = 15;
						}
						this.jjCheckNAddTwoStates(1, 2);
						break;
					case 3:
						if ((0x280000000000L & l) != 0L) {
							this.jjstateSet[this.jjnewStateCnt++] = 4;
						}
						break;
					case 4:
						if (((0x3ff000000000000L & l) != 0L) && (kind > 15)) {
							kind = 15;
						}
						break;
					case 6:
						if ((0x3ff000000000000L & l) == 0L) {
							break;
						}
						if (kind > 16) {
							kind = 16;
						}
						this.jjstateSet[this.jjnewStateCnt++] = 6;
						break;
					case 7:
						if ((0x280000000000L & l) != 0L) {
							this.jjCheckNAddStates(0, 2);
						}
						break;
					case 8:
						if ((0x3ff000000000000L & l) == 0L) {
							break;
						}
						if (kind > 13) {
							kind = 13;
						}
						this.jjCheckNAdd(8);
						break;
					case 9:
						if ((0x3ff000000000000L & l) != 0L) {
							this.jjCheckNAddTwoStates(9, 0);
						}
						break;
					case 10:
						if ((0x3ff000000000000L & l) == 0L) {
							break;
						}
						if (kind > 13) {
							kind = 13;
						}
						this.jjCheckNAddStates(0, 2);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (this.curChar < 128) {
				long l = 1L << (this.curChar & 077);
				// MatchLoop:
				do {
					switch (this.jjstateSet[--i]) {
					case 5:
					case 6:
						if ((0x7fffffe07fffffeL & l) == 0L) {
							break;
						}
						if (kind > 16) {
							kind = 16;
						}
						this.jjCheckNAdd(6);
						break;
					case 2:
						if (this.curChar == 69) {
							this.jjAddStates(3, 4);
						}
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				// MatchLoop:
				do {
					switch (this.jjstateSet[--i]) {
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				this.jjmatchedKind = kind;
				this.jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = this.jjnewStateCnt) == (startsAt = 11 - (this.jjnewStateCnt = startsAt))) {
				return curPos;
			}
			try {
				this.curChar = this.input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_1() {
		switch (this.curChar) {
		case 59:
			return this.jjStopAtPos(0, 5);
		default:
			return this.jjMoveNfa_1(0, 0);
		}
	}

	static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL,
			0xffffffffffffffffL };

	private final int jjMoveNfa_1(int startState, int curPos) {
		// int[] nextStates;
		int startsAt = 0;
		this.jjnewStateCnt = 1;
		int i = 1;
		this.jjstateSet[0] = startState;
		// int j,
		int kind = 0x7fffffff;
		for (;;) {
			if (++this.jjround == 0x7fffffff) {
				this.ReInitRounds();
			}
			if (this.curChar < 64) {
				long l = 1L << this.curChar;
				// MatchLoop:
				do {
					switch (this.jjstateSet[--i]) {
					case 0:
						if ((0xf7fffffbffffffffL & l) == 0L) {
							break;
						}
						kind = 6;
						this.jjstateSet[this.jjnewStateCnt++] = 0;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (this.curChar < 128) {
				// MatchLoop:
				do {
					switch (this.jjstateSet[--i]) {
					case 0:
						kind = 6;
						this.jjstateSet[this.jjnewStateCnt++] = 0;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int i2 = (this.curChar & 0xff) >> 6;
				long l2 = 1L << (this.curChar & 077);
				// MatchLoop:
				do {
					switch (this.jjstateSet[--i]) {
					case 0:
						if ((GMLParserTokenManager.jjbitVec0[i2] & l2) == 0L) {
							break;
						}
						if (kind > 6) {
							kind = 6;
						}
						this.jjstateSet[this.jjnewStateCnt++] = 0;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				this.jjmatchedKind = kind;
				this.jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = this.jjnewStateCnt) == (startsAt = 1 - (this.jjnewStateCnt = startsAt))) {
				return curPos;
			}
			try {
				this.curChar = this.input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_2() {
		switch (this.curChar) {
		case 34:
			return this.jjStopAtPos(0, 9);
		case 38:
			return this.jjStopAtPos(0, 7);
		default:
			return this.jjMoveNfa_2(0, 0);
		}
	}

	private final int jjMoveNfa_2(int startState, int curPos) {
		// int[] nextStates;
		int startsAt = 0;
		this.jjnewStateCnt = 1;
		int i = 1;
		this.jjstateSet[0] = startState;
		// int j,
		int kind = 0x7fffffff;
		for (;;) {
			if (++this.jjround == 0x7fffffff) {
				this.ReInitRounds();
			}
			if (this.curChar < 64) {
				long l = 1L << this.curChar;
				// MatchLoop:
				do {
					switch (this.jjstateSet[--i]) {
					case 0:
						if ((0xffffffbbffffffffL & l) == 0L) {
							break;
						}
						kind = 8;
						this.jjstateSet[this.jjnewStateCnt++] = 0;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (this.curChar < 128) {
				// MatchLoop:
				do {
					switch (this.jjstateSet[--i]) {
					case 0:
						kind = 8;
						this.jjstateSet[this.jjnewStateCnt++] = 0;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int i2 = (this.curChar & 0xff) >> 6;
				long l2 = 1L << (this.curChar & 077);
				// MatchLoop:
				do {
					switch (this.jjstateSet[--i]) {
					case 0:
						if ((GMLParserTokenManager.jjbitVec0[i2] & l2) == 0L) {
							break;
						}
						if (kind > 8) {
							kind = 8;
						}
						this.jjstateSet[this.jjnewStateCnt++] = 0;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				this.jjmatchedKind = kind;
				this.jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = this.jjnewStateCnt) == (startsAt = 1 - (this.jjnewStateCnt = startsAt))) {
				return curPos;
			}
			try {
				this.curChar = this.input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	public void ReInit(ASCIICharStream stream) {
		this.jjmatchedPos = this.jjnewStateCnt = 0;
		this.curLexState = this.defaultLexState;
		this.input_stream = stream;
		this.ReInitRounds();
	}

	private final void ReInitRounds() {
		int i;
		this.jjround = 0x80000001;
		for (i = 11; i-- > 0;) {
			this.jjrounds[i] = 0x80000000;
		}
	}

	public void ReInit(ASCIICharStream stream, int lexState) {
		this.ReInit(stream);
		this.SwitchTo(lexState);
	}

	public void SwitchTo(int lexState) {
		if ((lexState >= 3) || (lexState < 0)) {
			throw new TokenMgrError("Error: Ignoring invalid lexical state : "
					+ lexState + ". State unchanged.",
					TokenMgrError.INVALID_LEXICAL_STATE);
		} else {
			this.curLexState = lexState;
		}
	}

	private final Token jjFillToken() {
		Token t = Token.newToken(this.jjmatchedKind);
		t.kind = this.jjmatchedKind;
		String im = GMLParserTokenManager.jjstrLiteralImages[this.jjmatchedKind];
		t.image = (im == null) ? this.input_stream.GetImage() : im;
		t.beginLine = this.input_stream.getBeginLine();
		t.beginColumn = this.input_stream.getBeginColumn();
		t.endLine = this.input_stream.getEndLine();
		t.endColumn = this.input_stream.getEndColumn();
		return t;
	}

	public final Token getNextToken() {
		// int kind;
		// Token specialToken = null;
		Token matchedToken;
		int curPos = 0;

		EOFLoop: for (;;) {
			try {
				this.curChar = this.input_stream.BeginToken();
			} catch (java.io.IOException e) {
				this.jjmatchedKind = 0;
				matchedToken = this.jjFillToken();
				return matchedToken;
			}
			this.image = null;
			this.jjimageLen = 0;

			for (;;) {
				switch (this.curLexState) {
				case 0:
					try {
						this.input_stream.backup(0);
						while ((this.curChar <= 32)
								&& ((0x100000600L & (1L << this.curChar)) != 0L)) {
							this.curChar = this.input_stream.BeginToken();
						}
					} catch (java.io.IOException e1) {
						continue EOFLoop;
					}
					this.jjmatchedKind = 0x7fffffff;
					this.jjmatchedPos = 0;
					curPos = this.jjMoveStringLiteralDfa0_0();
					break;
				case 1:
					this.jjmatchedKind = 0x7fffffff;
					this.jjmatchedPos = 0;
					curPos = this.jjMoveStringLiteralDfa0_1();
					break;
				case 2:
					this.jjmatchedKind = 0x7fffffff;
					this.jjmatchedPos = 0;
					curPos = this.jjMoveStringLiteralDfa0_2();
					break;
				}
				if (this.jjmatchedKind != 0x7fffffff) {
					if (this.jjmatchedPos + 1 < curPos) {
						this.input_stream
								.backup(curPos - this.jjmatchedPos - 1);
					}
					if ((GMLParserTokenManager.jjtoToken[this.jjmatchedKind >> 6] & (1L << (this.jjmatchedKind & 077))) != 0L) {
						matchedToken = this.jjFillToken();
						this.TokenLexicalActions(matchedToken);
						if (GMLParserTokenManager.jjnewLexState[this.jjmatchedKind] != -1) {
							this.curLexState = GMLParserTokenManager.jjnewLexState[this.jjmatchedKind];
						}
						return matchedToken;
					} else if ((GMLParserTokenManager.jjtoSkip[this.jjmatchedKind >> 6] & (1L << (this.jjmatchedKind & 077))) != 0L) {
						if (GMLParserTokenManager.jjnewLexState[this.jjmatchedKind] != -1) {
							this.curLexState = GMLParserTokenManager.jjnewLexState[this.jjmatchedKind];
						}
						continue EOFLoop;
					}
					this.MoreLexicalActions();
					if (GMLParserTokenManager.jjnewLexState[this.jjmatchedKind] != -1) {
						this.curLexState = GMLParserTokenManager.jjnewLexState[this.jjmatchedKind];
					}
					curPos = 0;
					this.jjmatchedKind = 0x7fffffff;
					try {
						this.curChar = this.input_stream.readChar();
						continue;
					} catch (java.io.IOException e1) {
					}
				}
				int error_line = this.input_stream.getEndLine();
				int error_column = this.input_stream.getEndColumn();
				String error_after = null;
				boolean EOFSeen = false;
				try {
					this.input_stream.readChar();
					this.input_stream.backup(1);
				} catch (java.io.IOException e1) {
					EOFSeen = true;
					error_after = curPos <= 1 ? "" : this.input_stream
							.GetImage();
					if ((this.curChar == '\n') || (this.curChar == '\r')) {
						error_line++;
						error_column = 0;
					} else {
						error_column++;
					}
				}
				if (!EOFSeen) {
					this.input_stream.backup(1);
					error_after = curPos <= 1 ? "" : this.input_stream
							.GetImage();
				}
				throw new TokenMgrError(EOFSeen, this.curLexState, error_line,
						error_column, error_after, this.curChar,
						TokenMgrError.LEXICAL_ERROR);
			}
		}
	}

	final void MoreLexicalActions() {
		this.jjimageLen += (this.lengthOfMatch = this.jjmatchedPos + 1);
		switch (this.jjmatchedKind) {
		case 5:
			if (this.image == null) {
				this.image = new StringBuffer(new String(this.input_stream
						.GetSuffix(this.jjimageLen)));
			} else {
				this.image.append(new String(this.input_stream
						.GetSuffix(this.jjimageLen)));
			}
			this.jjimageLen = 0;
			// replace the name by the corresponding character.
			int start = this.image.toString().lastIndexOf('&');
			int len = this.image.length();
			if ((start == -1) || (len < 2)) {
				throw new RuntimeException(
						"a bug in ISO-8859-1 character name (&name;) parsing");
			}
			int character = ISO_8859_1.specToChar(this.image.substring(start,
					len - 1));
			if (character != -1) {
				this.image.delete(start, len);
				this.image.append((char) character);
			}
			break;
		default:
			break;
		}
	}

	final void TokenLexicalActions(Token matchedToken) {
		switch (this.jjmatchedKind) {
		case 9:
			if (this.image == null) {
				this.image = new StringBuffer(
						new String(this.input_stream.GetSuffix(this.jjimageLen
								+ (this.lengthOfMatch = this.jjmatchedPos + 1))));
			} else {
				this.image
						.append(new String(
								this.input_stream
										.GetSuffix(this.jjimageLen
												+ (this.lengthOfMatch = this.jjmatchedPos + 1))));
			}
			matchedToken.image = this.image.toString();
			break;
		default:
			break;
		}
	}

}

class ISO_8859_1 {
	/**
	 * return the character named by the string <code> spec </code>. if any
	 * character isn't named by <code> spec </code>, it return -1.
	 */
	static int specToChar(String name) {
		return '*';
	}

	/**
	 * return the character name. If the character does not have any name it
	 * return null.
	 */
	static String charToSpec(char c) {
		return "no yet implemented";
	}
}
