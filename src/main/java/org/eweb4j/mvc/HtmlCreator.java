package org.eweb4j.mvc;

public class HtmlCreator {
	public static String create(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 ");
		sb.append("Transitional//EN\">");
		sb.append("<HTML>");
		sb.append("<style><!--");
		sb.append(" div {text-align:left; font-size:14px;");
		sb.append(" line-height:2em; padding:10px; color:black;}");
		sb.append("</style>");
		sb.append("  <HEAD><TITLE>EWeb4J信息</TITLE></HEAD>");
		sb.append("  <BODY style='text-align:center;'> ");
		sb.append("<center><div style='margin:100px auto;width:70%;text-align:left; ");
		sb.append("padding:20px; height:600px; overflow:scroll; background:#eeffff;");
		sb.append(" border:solid 1px #b1b1b1;'>");
		sb.append(message);
		sb.append("</div></center></BODY>");
		sb.append("</HTML>");

		return sb.toString();
	}
}
