/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tanisha C Shetty
 */
public class LikeServlet1 extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LikeServlet1</title>");            
            out.println("</head>");
            out.println("<body>");
 
       
          HttpSession session = request.getSession();
String senderUsername = (String) session.getAttribute("username");
 String postId = request.getParameter("postId");
   
try {
    Class.forName("com.mysql.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");

    // 1. Update likes count
    PreparedStatement ps = con.prepareStatement("UPDATE posts SET likes = likes + 1 WHERE id = ?");
    ps.setString(1, postId);
    ps.executeUpdate();
    ps.close();

    // 2. Find post owner
    ps = con.prepareStatement("SELECT username FROM posts WHERE id = ?");
    ps.setString(1, postId);
    ResultSet rs = ps.executeQuery();

    String receiverUsername = null;
    if(rs.next()){
        receiverUsername = rs.getString("username");
    }
    rs.close();
    ps.close();

    // 3. Insert notification only if not self-like
    if(receiverUsername != null && !receiverUsername.equals(senderUsername)){
        // Get sender id
        ps = con.prepareStatement("SELECT id FROM user WHERE username=?");
        ps.setString(1, senderUsername);
        rs = ps.executeQuery();
        int senderId = rs.next() ? rs.getInt("id") : 0;
        rs.close();
        ps.close();

        // Get receiver id
        ps = con.prepareStatement("SELECT id FROM user WHERE username=?");
        ps.setString(1, receiverUsername);
        rs = ps.executeQuery();
        int receiverId = rs.next() ? rs.getInt("id") : 0;
        rs.close();
        ps.close();

        // Insert notification
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String timestamp = now.toString().replace("T", " ").substring(0,19);

       String message = senderUsername + " Liked on your post";
String type = "like";

ps = con.prepareStatement(
"INSERT INTO notification (sender_id, receiver_id, post_id, message, type, timestamp) VALUES (?, ?, ?, ?, ?, ?)"
);

ps.setInt(1, senderId);
ps.setInt(2, receiverId);
ps.setString(3, postId);
ps.setString(4, message);
ps.setString(5, type);
ps.setString(6, timestamp);
ps.executeUpdate();
        ps.executeUpdate();
        ps.close();
    }

    con.close();
} catch(Exception e) {
    e.printStackTrace();
}
        response.sendRedirect("home.jsp?like=success");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
