package com.simllc.registration.model;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name="error")

public class Error {
	
	protected String code;
	protected String type;
	protected String message;
	protected String field;
	
	public Error(String field,  String message) {
		super();
		this.field = field;
		this.message = message;
	}
	@XmlAttribute(name="code")
	public String getCode() {
		return code;
	}
	@XmlAttribute(name="field")
	public String getField() {
		return field;
	}
	@XmlAttribute(name="message")
	public String getMessage() {
		return message;
	}
	@XmlAttribute(name="type")
	public String getType() {
		return type;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setField(String field) {
		this.field = field;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
