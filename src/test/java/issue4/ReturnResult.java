package issue4;

import java.io.Serializable;

public class ReturnResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private String desc = null;
	private int code = 0;
	private T returnContent;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getReturnContent() {
		return returnContent;
	}

	public void setReturnContent(T returnContent) {
		this.returnContent = returnContent;
	}

}
