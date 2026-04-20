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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tanisha C Shetty
 */
public class ShareServlet1 extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * 
     */
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShareServlet1</title>");            
            out.println("</head>");
            out.println("<body>");

           // Get sender, receiver, postId from request/session
        String senderUsername = (String) request.getSession().getAttribute("username");
        String receiverUsername = request.getParameter("receiver");
        int postId = Integer.parseInt(request.getParameter("postId"));

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");

            // 1. Look up receiver's user ID
            ps = con.prepareStatement("SELECT id FROM user WHERE username = ?");
            ps.setString(1, receiverUsername);
            rs = ps.executeQuery();

            if (rs.next()) {
                int receiverId = rs.getInt("id");

                // 2. Look up sender's user ID
                ps.close();
                rs.close();
                ps = con.prepareStatement("SELECT id FROM user WHERE username = ?");
                ps.setString(1, senderUsername);
                rs = ps.executeQuery();
                
                int senderId = 0;
                if (rs.next()) {
                    senderId = rs.getInt("id");
                }

                // 3. Insert notification for receiver
                String message = senderUsername + " has shared a post with you";
                LocalDateTime timestamp = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedTimestamp = timestamp.format(formatter);

                ps.close();
                ps = con.prepareStatement(
                    "INSERT INTO notification (sender_id, receiver_id, post_id, message, timestamp) VALUES (?, ?, ?, ?, ?)"
                );
                ps.setInt(1, senderId);
                ps.setInt(2, receiverId);
                ps.setInt(3, postId);
                ps.setString(4, message);
                ps.setString(5, formattedTimestamp);
                ps.executeUpdate();

                // 4. Optionally, update post's 'shares' count
                ps.close();
                ps = con.prepareStatement(
                    "UPDATE posts SET shares = shares + 1 WHERE id = ?"
                );
                ps.setInt(1, postId);
                ps.executeUpdate();

                // 5. Redirect or respond with success
                response.sendRedirect("home.jsp?share=success");
            } else {
                // Username not found, handle accordingly
                response.sendRedirect("home.jsp?share=fail"); // You can show an error message on home.jsp
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home.jsp?share=error");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignore) {}
            try { if (ps != null) ps.close(); } catch (Exception ignore) {}
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    
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
