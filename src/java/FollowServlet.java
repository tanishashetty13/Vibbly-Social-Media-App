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
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Tanisha C Shetty
 */
@WebServlet("/FollowServlet")
public class FollowServlet extends HttpServlet {

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
            out.println("<title>Servlet FollowServlet</title>");            
            out.println("</head>");
            out.println("<body>");
          HttpSession session = request.getSession(false);
String currUsername = (String) session.getAttribute("username");

// ✅ SAFE PARSING
String senderParam = request.getParameter("senderId");

if(senderParam == null || senderParam.isEmpty()) {
    response.sendRedirect("home.jsp");
    return;
}

int senderId = Integer.parseInt(senderParam);

String action = request.getParameter("action");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");

            // get current user id
            PreparedStatement ps = con.prepareStatement("SELECT id FROM user WHERE username=?");
            ps.setString(1, currUsername);
            ResultSet rs = ps.executeQuery();

            int currId = 0;
            if (rs.next()) currId = rs.getInt("id");
if ("request".equals(action)) {

    // 🔹 Receiver notification
    PreparedStatement ps2 = con.prepareStatement(
        "INSERT INTO notification(receiver_id, sender_id, type) VALUES (?, ?, ?)"
    );
    ps2.setInt(1, senderId);  // receiver
    ps2.setInt(2, currId);    // sender
    ps2.setString(3, "follow_request");
    
    ps2.executeUpdate();

    // 🔹 Sender notification
    PreparedStatement psSender = con.prepareStatement(
        "INSERT INTO notification(receiver_id, sender_id, type) VALUES (?, ?, ?)"
    );
    psSender.setInt(1, currId);
    psSender.setInt(2, currId);
    psSender.setString(3, "follow_sent");
    psSender.executeUpdate();
}
if ("accept".equals(action)) {

    // follow relation
    PreparedStatement ps2 = con.prepareStatement(
        "INSERT INTO follows(follower_id, following_id, status) VALUES (?, ?, 'accepted')"
    );
    ps2.setInt(1, senderId);
    ps2.setInt(2, currId);
    ps2.executeUpdate();

    // 🔹 Sender ko notify karo (IMPORTANT)
    PreparedStatement ps4 = con.prepareStatement(
        "INSERT INTO notification(receiver_id, sender_id, type) VALUES (?, ?, ?)"
    );
    ps4.setInt(1, senderId);   // sender ko show hoga
    ps4.setInt(2, currId);     // receiver accepted
    ps4.setString(3, "follow_accept");
    ps4.executeUpdate();
}
if ("remove".equals(action)) {

    PreparedStatement psDel = con.prepareStatement(
        "DELETE FROM follows WHERE follower_id=? AND following_id=?"
    );

    psDel.setInt(1, currId);   // YOU
    psDel.setInt(2, senderId); // jisey unfollow kar rahe
    psDel.executeUpdate();
}


            // delete notification after action
          PreparedStatement ps3 = con.prepareStatement(
    "UPDATE notification SET type='handled' WHERE sender_id=? AND receiver_id=? AND type='follow_request'"
);
ps3.setInt(1, senderId);
ps3.setInt(2, currId);
ps3.executeUpdate();

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("notifications.jsp");
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
