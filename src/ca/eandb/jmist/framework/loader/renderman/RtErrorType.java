/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
public enum RtErrorType {
	
	/*
	Error Codes
	1 - 10 System and File Errors
	11 - 20 Program Limitations
	21 - 40 State Errors
	41 - 60 Parameter and Protocol Errors
	61 - 80 Execution Errors
	*/
	/** No error */									NOERROR			(0),
	/** Out of memory */							NOMEM			(1),
	/** Miscellaneous system error */				SYSTEM			(2),
	/** File nonexistent */							NOFILE			(3),
	/** Bad file format */							BADFILE			(4),
	/** File version mismatch */					VERSION			(5),
	/** Target disk is full */						DISKFULL		(6),
	/** Optional RI feature */						INCAPABLE		(11),
	/** Unimplemented feature */					UNIMPLEMENT		(12),
	/** Arbitrary program limit */					LIMIT			(13),
	/** Probably a bug in renderer */				BUG				(14),
	/** RiBegin not called */						NOTSTARTED		(23),
	/** Bad begin-end nesting */					NESTING			(24),
	/** Invalid state for options */				NOTOPTIONS		(25),
	/** Invalid state for attribs */				NOTATTRIBS		(26),
	/** Invalid state for primitives */				NOTPRIMS		(27),
	/** Other invalid state */						ILLSTATE		(28),
	/** Badly formed motion block */				BADMOTION		(29),
	/** Badly formed solid block */					BADSOLID		(30),
	/** Invalid token for request */				BADTOKEN		(41),
	/** Parameter out of range */					RANGE			(42),
	/** Parameters inconsistent */					CONSISTENCY		(43),
	/** Bad object/light handle */					BADHANDLE		(44),
	/** Can’t load requested shader */				NOSHADER		(45),
	/** Required parameters not provided */			MISSINGDATA		(46),
	/** Declare type syntax error */				SYNTAX			(47),
	/** Zerodivide, noninvert matrix, etc. */		MATH			(61); 
	
	private final int code;
	
	RtErrorType(int code) {
		this.code = code;
	}
	
	public int code() {
		return code;
	}

}
