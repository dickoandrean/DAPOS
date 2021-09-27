/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dapos.tools;

import java.util.ArrayList;
import java.util.List;
 
/**
 * Created with IntelliJ IDEA.
 * User: lee
 * Date: 11/29/13
 * Time: 9:28 PM
 * <p/>
 * Java Plain Text Line Formatter
 */
public class LineFormatter {
    public static final int LEFT_ALIGN = -1;
    public static final int CENTER_ALIGN = 0;
    public static final int RIGHT_ALIGN = 1;
 
    private int lineWidth;
    private List<String> lines;
    private String newLine = System.getProperty("line.separator");
 
    public static String LEFT_MARGIN = ""; // nothing
 
    /**
     * Initialize LineFormatter with line width initialized as 40 (epson POS printer)
     */
    public LineFormatter() {
        init(40);
    }
 
    /**
     * Initialize LineFormatter with specified line width
     *
     * @param l : line width
     */
    public LineFormatter(int l) {
        init(l);
    }
 
    /**
     * Set new line character, this is not necessary as LineFormatter automatically detect System new line character
     *
     * @param n : new line character
     * @return current LineFormatter instance
     */
    public LineFormatter setNewLine(String n) {
        this.newLine = n;
        return this;
    }
 
    private void init(int l) {
        this.lineWidth = l;
        lines = new ArrayList<String>();
    }
 
    /**
     * add new line
     *
     * @param cols LineColumn Class instance from whose columns constructed as line
     * @return current LineFormatter instance
     */
    public LineFormatter addLine(LineColumn cols) {
        return addLine(cols.getLine());
    }
 
    /**
     * add new line, with LineFormatter.LEFT_ALIGN
     *
     * @param s line to be added
     * @return current LineFormatter instance
     */
    public LineFormatter addLine(String s) {
        return addLine(s, LEFT_ALIGN);
    }
 
    /**
     * add new line, with specified alignment
     *
     * @param s     line to be added
     * @param align
     * @return current LineFormatter instance
     */
    public LineFormatter addLine(String s, int align) {
        switch (align) {
            case LEFT_ALIGN:
                return saveLine(String.format("%-" + this.lineWidth + "s", s));
            case RIGHT_ALIGN:
                return saveLine(String.format("%" + this.lineWidth + "s", s));
            case CENTER_ALIGN:
                int sw = s.length();
                int pad = (this.lineWidth - sw) / 2;
                String l = String.format("%" + pad + "s%s%" + pad + "s", "", s, "");
                return addLine(l, RIGHT_ALIGN);
        }
        return null;
    }
 
    private LineFormatter saveLine(String s) {
        if (s.length() > this.lineWidth)
            s = s.substring(0, this.lineWidth);
        this.lines.add(LEFT_MARGIN + s);
        return this;
    }
 
    /**
     * add blank line
     *
     * @return current LineFormatter instance
     */
    public LineFormatter addDivider() {
        return addLine(" ");
    }
 
    /**
     * add line with repeated character
     *
     * @param s single string to be repeated
     * @return current LineFormatter instance
     */
    public LineFormatter addDivider(String s) {
        return addLine(
                String.format("%" + lineWidth + "s", s)
                        .replace(' ', s.charAt(0))
        );
    }
 
    /**
     * produce formatted lines
     *
     * @return rendered string
     */
    public String render() {
        String text = "";
        if (lines.size() > 0) {
            text = lines.get(0);
        }
        for (int i = 1; i < lines.size(); i++) {
            text += this.newLine + lines.get(i);
        }
 
        return text;
    }
 
    /**
     * clear all buffered line
     *
     * @return current LineFormatter instance
     */
    public LineFormatter reset() {
        lines.clear();
        return this;
    }
}