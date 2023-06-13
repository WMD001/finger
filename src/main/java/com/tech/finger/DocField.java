package com.tech.finger;

public class DocField {
	/**字段*/
	private String field;
	/**内容*/
	private String content;
	/**权重*/
	private int score = 1;
	public DocField(){
		
	}
	public DocField(String field, String content, int score) {
		this.field = field;
		this.content = content;
		this.score = score;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
