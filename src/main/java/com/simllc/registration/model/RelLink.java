package com.simllc.registration.model;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="link")
public class RelLink {
	protected String relationship;
	protected String href;
	protected String type;
	
	public RelLink(){
	}
	
	public RelLink(String relationship, String href, String type) {
		super();
		this.relationship = relationship;
		this.href = href;
		this.type = type;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("<");
		sb.append(href).append(">; rel=").append(relationship);
		return sb.toString();
	}
	
	private static Pattern parse = Pattern.compile("<(.+)>\\s*;\\s*(.+)");
	
	public static RelLink valueOf(String val){
		Matcher matcher = parse.matcher(val);
		if(!matcher.matches()){
			throw new RuntimeException("Failed to parse link: " + val);
		}
		RelLink link = new RelLink();
		link.href = matcher.group(1);
		String[] props = matcher.group(2).split(";");
		HashMap<String, String> map = new HashMap();
		for(String prop : props){
			String[] split = prop.split("=");
			map.put(split[0].trim(), split[2].trim());
		}
		if(map.containsKey("rel")){
			link.relationship = map.get("rel");
		}
		if(map.containsKey("type")){
			link.type = map.get("type");
		}
		return link;
	}
	
	@XmlAttribute(name="rel")
	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	@XmlAttribute(name="href")
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@XmlAttribute(name="type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}