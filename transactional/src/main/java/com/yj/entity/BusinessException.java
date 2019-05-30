package com.yj.entity;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = -8138602623241348983L;

	/**
	 * 错误代码,默认为未知错
	 */
	private String errorCode = "UNKNOW_ERROR";

	/**
	 * 错误信息中的参数
	 */
	protected String[] errorArgs = null;

	/**
	 * 兼容纯错误信息，不含error code,errorArgs的情
	 */
	private String errorMessage = null;
	
	public BusinessException() {
		super();
	}

	public BusinessException(Throwable e) {
		super(e);
		this.errorMessage = ExceptionUtils.getStackTrace(e);
	}

	public BusinessException(String errorCode, String[] errorArgs) {
		super(errorCode);
		this.errorCode = errorCode;
		this.errorArgs = errorArgs;
	}

	public BusinessException(String errorCode, String errorMessage) {
		super(errorCode);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public BusinessException(String errorCode, String errorMessage, String[] errorArgs) {
		super(errorCode);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorArgs = errorArgs;
	}

	public BusinessException(String errorCode, String[] errorArgs, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.errorArgs = errorArgs;
	}

	public BusinessException(String errorCode, String errorArg, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.errorArgs = new String[] { errorArg };
	}

	public BusinessException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	/**
	 * 获得出错信息. 读取i18N properties文件中Error Code对应的message,并组合参数获得i18n的出错信
	 */
	public String getMessage() {
		if (errorMessage != null) {
			return errorMessage;
		}
		if (super.getMessage() != null)
			return super.getMessage();
		return errorMessage;
	}

	public String getMessageDetail() {
		return errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}
}