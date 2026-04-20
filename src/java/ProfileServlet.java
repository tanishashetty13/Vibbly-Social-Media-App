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
public class ProfileServlet extends HttpServlet {

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
            out.println("<title>Servlet ProfileServlet</title>");            
            out.println("</head>");
            out.println("<body>");
   // Session validation
        // Session validation
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("index.html");
            return;
        }
        String username = (String) session.getAttribute("username");

    
            // HTML start
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Vibbly - " + username + "'s Profile</title>");
            out.println("<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap' rel='stylesheet'>");
            out.println("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css'>");

            // CSS: matching homepage
            out.println("<style>");
            out.println("body { margin:0; font-family:'Poppins',sans-serif; background:linear-gradient(135deg,#f5f7fa,#c3cfe2); overflow-x:hidden; position:relative; }");
            out.println(".shape { position:absolute; border-radius:50%; opacity:0.15; animation:float 10s infinite ease-in-out; z-index:-1; }");
            out.println(".shape1 { width:250px; height:250px; background:#ff6f91; top:5%; left:-50px; }");
            out.println(".shape2 { width:300px; height:300px; background:#84fab0; bottom:-100px; right:-100px; animation-delay:2s; }");
            out.println("@keyframes float { 0%,100% {transform:translateY(0) rotate(0deg);} 50% {transform:translateY(30px) rotate(15deg);} }");

            // Navbar
            out.println(".navbar { background:rgba(255,255,255,0.9); display:flex; justify-content:space-between; align-items:center; padding:15px 30px; box-shadow:0 5px 15px rgba(0,0,0,0.1); position:sticky; top:0; z-index:100; backdrop-filter:blur(5px); }");
            out.println(".navbar h1 { margin:0; color:#0095f6; font-size:28px; }");
            out.println(".navbar .logout-btn { padding:10px 20px; background:#ff4d4d; color:white; border:none; border-radius:12px; cursor:pointer; font-weight:500; transition:0.3s; }");
            out.println(".navbar .logout-btn:hover { background:#cc0000; transform:translateY(-2px); }");

            // Container
            out.println(".container { max-width:900px; margin:40px auto; padding:0 20px; }");

            // Post card
            out.println(".post { background:white; padding:28px 32px; border-radius:25px; margin-bottom:34px; box-shadow:0 10px 35px rgba(0,0,0,0.11); animation:fadeInUp 0.8s ease forwards; transform:translateY(20px); opacity:0; }");
            out.println(".post-header { display:flex; align-items:center; margin-bottom:12px; }");
            out.println(".post-user { display:flex; align-items:center; font-size:17px; }");
            out.println(".post-img { max-width:80%; display:block; margin:16px auto; border-radius:18px; box-shadow:0 2px 10px rgba(0,149,246,0.07); transition:transform 0.3s ease; }");
            out.println(".post-img:hover { transform:scale(1.03); }");
            out.println(".caption { color:#444; margin:12px 0 18px; font-size:16px; line-height:1.5; }");
            out.println(".stats { display:flex; gap:20px; color:#276fbf; font-weight:600; margin-top:10px; }");
            out.println(".stats span { display:inline-flex; align-items:center; gap:6px; background:#e6f1ff; border-radius:12px; padding:5px 12px; }");
            out.println(".stats i { color:#0095f6; }");

            // Error message
            out.println(".error { color:red; text-align:center; font-size:1.1em; margin-top:20px; font-weight:600; }");

            // Animations
            out.println("@keyframes fadeInUp { to { transform:translateY(0); opacity:1; } }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            // Floating shapes
            out.println("<div class='shape shape1'></div>");
            out.println("<div class='shape shape2'></div>");

            // Navbar
           out.println("<div class='navbar'>");

out.println("<h1>Vibbly</h1>");

out.println("<div style='display:flex; align-items:center; gap:15px;'>");

// Home button
out.println("<a href='home.jsp' style='text-decoration:none;'>");
out.println("<button style='padding:10px 15px; background:#0095f6; color:white; border:none; border-radius:12px; cursor:pointer;'>");
out.println("<i class='fa-solid fa-house'></i>");
out.println("</button>");
out.println("</a>");

// Logout button (icon only)
out.println("<form action='logout' method='post' style='margin:0;'>");
out.println("<button type='submit' style='padding:10px 15px; background:#ff4d4d; color:white; border:none; border-radius:12px; cursor:pointer;'>");
out.println("<i class='fa-solid fa-right-from-bracket'></i>");
out.println("</button>");
out.println("</form>");

out.println("</div>");

out.println("</div>");
            out.println("<form action='logout' method='post' style='margin:0;'>");
            
            out.println("</form>");
            out.println("</div>");

            // Main content container
            out.println("<div class='container'>");

            // Fetch posts from database (logic intact)
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
                PreparedStatement ps = con.prepareStatement(
                        "SELECT id, caption, image_path, likes, comments, shares FROM posts WHERE username=? ORDER BY id DESC");
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                boolean hasPosts = false;
                while (rs.next()) {
                    hasPosts = true;
                    int likes = rs.getInt("likes");
                    int shares = rs.getInt("shares");
                    String comments = rs.getString("comments");
                    int commentCount = (comments == null ? 0 : comments.split("\n").length);

                    out.println("<div class='post'>");
                    out.println("<div class='post-header'>");
                    out.println("<div class='post-user'><i class='fa-solid fa-user-circle' style='font-size:1.5em; color:#0095f6; margin-right:6px;'></i><b>" + username + "</b></div>");
                    out.println("</div>");
                    out.println("<img src='" + rs.getString("image_path") + "' alt='Post image' class='post-img'>");
                    out.println("<p class='caption'>" + rs.getString("caption") + "</p>");
                    out.println("<div class='stats'>");
                    out.println("<span><i class='fa-regular fa-thumbs-up'></i> " + likes + "</span>");
                    out.println("<span><i class='fa-regular fa-comment'></i> " + commentCount + "</span>");
                    out.println("<span><i class='fa-solid fa-share'></i> " + shares + "</span>");
                    out.println("</div>");
                    out.println("</div>");
                }

                if (!hasPosts) {
                    out.println("<p style='text-align:center; font-size:1.2em; color:#666;'>You haven't posted anything yet.</p>");
                }

                con.close();
            } catch (Exception e) {
                out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
            }

            out.println("</div>"); // container end
            out.println("</body>");
            out.println("</html>");
        
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
