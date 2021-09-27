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
 * Time: 9:39 PM
 */
public class LineColumn {
 
    private List<String> columns = new ArrayList<String>();
    public LineColumn(){
 
    }
 
    public LineColumn(String s, int width){
        this.addColumn(s, width);
    }
 
    public LineColumn addBlankSpace(int width){
        return this.addColumn(" ", width);
    }
 
    public LineColumn(String s, int width, int align){
        addColumn(s, width, align);
    }
 
    public LineColumn addColumn(String s, int width){
        return addColumn(s, width, LineFormatter.LEFT_ALIGN);
    }
 
    public LineColumn addColumn(String s, int width, int align){
        if(s.length() > width)
            s = s.substring(0, width);
 
        switch (align){
            case LineFormatter.LEFT_ALIGN:
                columns.add(String.format("%-" + width + "s", s));
            break;
            case LineFormatter.RIGHT_ALIGN:
                columns.add(String.format("%" + width + "s", s));
            break;
            case LineFormatter.CENTER_ALIGN:
                int sw = s.length();
                int pad = (width - sw) / 2;
                String l = String.format("%" + pad + "s%s%" + pad + "s", "", s, "");
                return addColumn(l, width, LineFormatter.RIGHT_ALIGN);
        }
 
        return this;
    }
 
    public String getLine(){
        String line = "";
        for(int i = 0; i < columns.size(); i++){
            line += columns.get(i);
        }
 
        return line;
    }
}