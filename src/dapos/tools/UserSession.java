/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dapos.tools;

/**
 *
 * @author Dicko
 */
public class UserSession {
    private static int u_id;
    private static String u_name;
    private static String u_user;
    private static int u_security;
    
    public static int getU_id(){
    return u_id;
    }
    
    public static void setU_id(int u_id){
    UserSession.u_id = u_id;
    }
    
    public static String getU_name(){
    return u_name;
    }
    
    public static void setU_name(String u_name){
    UserSession.u_name = u_name;
    }
    
        public static String getU_user(){
    return u_user;
    }
    
    public static void setU_user(String u_user){
    UserSession.u_user = u_user;
    }
    
    public static int getU_security(){
    return u_security;
    }
    
    public static void setU_security(int u_security){
    UserSession.u_security = u_security;
    }
}
