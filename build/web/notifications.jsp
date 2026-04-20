<%@ page session="true" %>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String currUsername = (String) session.getAttribute("username");
    if(currUsername == null) {
        response.sendRedirect("index.html");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Vibbly - Notifications</title>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>
    body {
        margin: 0;
        font-family: 'Poppins', 'Segoe UI Emoji', 'Noto Color Emoji', sans-serif;
        background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
        overflow-x: hidden;
        position: relative;
    }
    .shape { position: absolute; border-radius: 50%; opacity: 0.15; animation: float 10s infinite ease-in-out; z-index: -1; }
    .shape1 { width: 250px; height: 250px; background: #ff6f91; top: 5%; left: -50px; }
    .shape2 { width: 300px; height: 300px; background: #84fab0; bottom: -100px; right: -100px; animation-delay: 2s; }
    @keyframes float { 0%,100% { transform: translateY(0) rotate(0deg); } 50% { transform: translateY(30px) rotate(15deg); } }
    .navbar {
        background: rgba(255,255,255,0.9);
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px 30px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        position: sticky;
        top: 0;
        z-index: 100;
        backdrop-filter: blur(5px);
    }
    .navbar h1 {
        margin: 0;
        color: #0095f6;
        font-size: 28px;
    }
    .nav-right {
        display: flex;
        gap: 12px;
    }
    .navbar .profile-btn {
        padding: 10px 20px;
        background: #0095f6;
        color: white;
        border: none;
        border-radius: 12px;
        cursor: pointer;
        font-weight: 500;
        text-decoration: none;
        transition: 0.3s;
    }
    .navbar .profile-btn:hover {
        background: #0077cc;
        transform: translateY(-2px);
    }
    .container {
        max-width: 700px;
        margin: 40px auto;
        padding: 0 20px;
    }
    .notification-heading {
        font-size: 28px;
        color: #ff6f91;
        text-align: center;
        margin-bottom: 32px;
        font-weight: 600;
        letter-spacing: 1px;
    }
    .notification-card {
        background: white;
        padding: 24px 32px;
        border-radius: 22px;
        box-shadow: 0 6px 22px rgba(0,0,0,0.09);
        margin-bottom: 28px;
        display: flex;
        align-items: flex-start;
        gap: 18px;
        animation: fadeInUp 0.8s ease forwards;
        opacity: 0;
        transform: translateY(20px);
    }
    .notification-card .icon {
        font-size: 2em;
        color: #0095f6;
        margin-right: 12px;
        margin-top: 3px;
    }
    .notification-card .details {
        flex: 1;
    }
    .notification-card .msg {
        font-size: 16px;
        color: #333;
        font-weight: 500;
        margin-bottom: 8px;
    }
    .notification-card .timestamp {
        font-size: 13px;
        color: #666;
        font-style: italic;
        letter-spacing: 0.5px;
    }
    @keyframes fadeInUp {
        to { transform: translateY(0); opacity:1; }
    }
    @media (max-width:600px) {
        .container { max-width: 98%; }
        .notification-card { padding: 16px 10px; flex-direction: column; }
        .notification-heading { font-size: 21px; }
    }
</style>
</head>
<body>
<div class="shape shape1"></div>
<div class="shape shape2"></div>
<div class="navbar">
    <h1>Vibbly</h1>
    <div class="nav-right">
        <a href="home.jsp" class="profile-btn">
            <i class="fa-solid fa-house"></i> 
        </a>
        <a href="ProfileServlet" class="profile-btn">
            <i class="fa-solid fa-user"></i> 
        </a>
    </div>
</div>
<div class="container">
    <div class="notification-heading">
        <i class="fa-solid fa-bell"></i> Notifications
    </div>
             <%
try {
    Class.forName("com.mysql.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");

    PreparedStatement ps = con.prepareStatement("SELECT id FROM user WHERE username = ?");
    ps.setString(1, currUsername);
    ResultSet rs = ps.executeQuery();

    int currId = 0;
    if(rs.next()) currId = rs.getInt("id");
    rs.close();
    ps.close();

    ps = con.prepareStatement(
        "SELECT n.*, sender.username AS sender_name, receiver.username AS receiver_name " +
        "FROM notification n " +
        "LEFT JOIN user sender ON n.sender_id = sender.id " +
        "LEFT JOIN user receiver ON n.receiver_id = receiver.id " +
        "WHERE n.receiver_id = ? OR n.sender_id = ? " +
        "ORDER BY n.timestamp DESC"
    );

    ps.setInt(1, currId);
    ps.setInt(2, currId);

    rs = ps.executeQuery();

    java.time.LocalDate today = java.time.LocalDate.now();
    boolean todayShown = false;
    boolean olderShown = false;
    boolean found = false;

    while(rs.next()) {
        found = true;

        String type = rs.getString("type");
        String senderName = rs.getString("sender_name");
        String receiverName = rs.getString("receiver_name");
        int senderId = rs.getInt("sender_id");

        String timestamp = rs.getString("timestamp");
        java.time.LocalDate notifDate = java.time.LocalDate.parse(timestamp.substring(0,10));
        boolean isToday = notifDate.equals(today);
%>

<% if(isToday && !todayShown) { %>
    <h3 class="section-title">Today</h3>
<% todayShown = true; } %>

<% if(!isToday && !olderShown) { %>
    <h3 class="section-title">Earlier</h3>
<% olderShown = true; } %>

<div class="notification-card <%= type %>">

    <div class="icon">
        <% if("like".equals(type)) { %>
            <i class="fa-solid fa-heart"></i>
        <% } else if("comment".equals(type)) { %>
            <i class="fa-solid fa-comment"></i>
        <% } else { %>
            <i class="fa-solid fa-user-plus"></i>
        <% } %>
    </div>

    <div class="details">

        <div class="msg">
        <% 
            if("like".equals(type)) {
                out.print("<b>" + senderName + "</b> liked your post");
            } else if("comment".equals(type)) {
                out.print("<b>" + senderName + "</b> commented on your post");
            
            } else if ("follow_request".equals(type)) {

                if (currId == senderId) {
                    // 👉 sender view
                    out.print("You sent a follow request to <b>" + receiverName + "</b>");
                } else {
                    // 👉 receiver view
                    out.print("<b>" + senderName + "</b> sent you a follow request");
                }

            } else if ("follow_accept".equals(type)) {

    if (currId == senderId) {
        // sender ko dikhega
        out.print("<b>" + receiverName + "</b> accepted your follow request");
    } else {
        // receiver ko hide karo
        continue;
    }
}

        %>
        </div>

        <% if("follow_request".equals(type) && currId != senderId) { %>
        <div class="action-buttons">
            <form action="FollowServlet" method="post">
                <input type="hidden" name="senderId" value="<%= senderId %>">
                <input type="hidden" name="action" value="accept">
                <button>Accept</button>
            </form>

            <form action="FollowServlet" method="post">
                <input type="hidden" name="senderId" value="<%= senderId %>">
                <input type="hidden" name="action" value="reject">
                <button>Reject</button>
            </form>
        </div>
        <% } %>

        <div class="timestamp"><%= timestamp %></div>

    </div>
</div>

<%
    } // while loop end

    if(!found) {
%>
<div class="notification-card">
    <div class="icon"><i class="fa-solid fa-bell-slash"></i></div>
    <div class="details">
        <div class="msg">No notifications yet!</div>
    </div>
</div>
<%
    }

    con.close();

}
catch(Exception e) {
    e.printStackTrace();
%>
<div class="notification-card">
    <div class="details">
        <div class="msg">Error loading notifications</div>
    </div>
</div>
<%
}
%>
</div>
</body>
</html>
