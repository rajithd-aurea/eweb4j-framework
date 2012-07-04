package org.eweb4j.mvc.view;

public class THeadCell {
	private String label;
	private String width;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@Override
	public String toString() {
		return "THeadCell [label=" + label + ", width=" + width + "]";
	}

}
