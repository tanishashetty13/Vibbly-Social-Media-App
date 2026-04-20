<%@ page session="true" %>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String username = (String) session.getAttribute("username");
    if(username == null) {
        response.sendRedirect("index.html");
        return;
    }
%>
<%
int userId = 0;

try {
    Class.forName("com.mysql.jdbc.Driver");
    Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/test", "root", "root"
    );

    PreparedStatement ps = con.prepareStatement(
        "SELECT id FROM user WHERE username=?"
    );
    ps.setString(1, username);

    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
        userId = rs.getInt("id");
    }

    con.close();
} catch (Exception e) {
    out.println("Error fetching userId: " + e.getMessage());
}
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Vibbly - Profile</title>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>
    
  .profile-btn {
    background: #6c5ce7;
}
.nav-btn {
    width: 45px;
    height: 45px;
    border-radius: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    text-decoration: none;
    border: none;
    cursor: pointer;
    transition: 0.25s;
}

/* Icon size fix */
.nav-btn i {
    font-size: 18px;
    color: white;
}

/* Home */
.home-btn {
    background: #0095f6;
}

/* Notification */
.notif-btn {
    background: #ff9800;
}

/* Logout */
.logout-btn {
    background: #ff4d4d;
}

/* Hover */
.nav-btn:hover {
    transform: translateY(-3px);
    opacity: 0.9;
}body {
        margin: 0;
        font-family: 'Poppins', 'Segoe UI Emoji', 'Noto Color Emoji', sans-serif;
        background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
        overflow-x: hidden;
        position: relative;
    }
    .shape { position: absolute; border-radius: 50%; opacity: 0.15; animation: float 10s infinite ease-in-out; z-index: -1; }
    .shape1 {
    width: 250px;
    height: 250px;
    background: #ff4d6d;  /* brighter pink */
    top: 5%;
    left: -50px;
}

.shape2 {
    width: 300px;
    height: 300px;
    background: #00e6a8;  /* neon green */
    bottom: -100px;
    right: -100px;
    animation-delay: 2s;
}
    @keyframes float { 0%,100% { transform: translateY(0) rotate(0deg); } 50% { transform: translateY(30px) rotate(15deg); } }
.navbar {
    background: rgba(255,255,255,0.9);
    display: flex;
    justify-content: space-between;  /* logo left, buttons right */
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

/* Container for buttons */
.nav-right {
    display: flex;
    gap: 12px;   /* space between Profile & Notifications */
}


.navbar .profile-btn:hover {
    background: #0077cc;
    transform: translateY(-2px);
}

.container {
    flex: 1;
    max-width: 800px;
}


    /* Upload Form */
   form.upload {
       text-align: center;
    background: white;
    padding: 25px;
    border-radius: 16px;
    margin-bottom: 25px;
    box-shadow: 0 8px 25px rgba(0,0,0,0.08);
}
    .file-drop {
        border: 2px dashed #0095f6;
        border-radius: 12px;
        padding: 20px;
        cursor: pointer;
        transition: background 0.3s ease;
    }
    .file-drop:hover { background: rgba(0,149,246,0.05); }
    .file-drop i { font-size: 40px; color: #0095f6; margin-bottom: 10px; }
    .input-group {
        display: flex;
        align-items: center;
        background: #f0f2f5;
        border-radius: 12px;
        padding: 8px 12px;
        margin: 10px 0;
        transition: box-shadow 0.3s ease;
    }
    .input-group:focus-within { box-shadow: 0 0 8px rgba(0,149,246,0.4); }
    .input-group i { color: #0095f6; margin-right: 8px; }
    .input-group input[type=text] {
        flex: 1; border: none; background: transparent; font-size: 15px; outline: none;
    }
    button {
        padding: 10px 16px; background: #0095f6; border: none; color: white; border-radius: 12px;
        cursor: pointer; font-weight: 500; transition: 0.3s;
        display: inline-flex; align-items: center; gap: 6px;
    }
    button:hover { background: #0077cc; transform: translateY(-2px); }
    button.delete { background: #ff4d4d; }
    button.delete:hover { background: #cc0000; }

    /* Post Card */
    .post {
        background: white;
        padding: 28px 32px;
        border-radius: 25px;
        margin-bottom: 34px;
        box-shadow: 0 10px 35px rgba(0,0,0,0.11);
        animation: fadeInUp 0.8s ease forwards;
        transform: translateY(20px);
        opacity: 0;
    }
    .post-header { display: flex; align-items: center; margin-bottom: 12px; }
    .post-user { display: flex; align-items: center; font-size: 17px; }
    .post-img {
        max-width: 80%; display: block; margin: 16px auto;
        border-radius: 18px; box-shadow: 0 2px 10px rgba(0,149,246,0.07);
    }
    .caption { color: #444; margin: 12px 0 18px; font-size: 16px; line-height: 1.5; }

    .post-actions {
        display: flex;
        align-items: center;
        gap: 10px;
        flex-wrap: nowrap; /* force same line */
    }
    .share-form, .comment-form {
        display: flex;
        align-items: center;
        gap: 8px;
    }
    .input-group.compact {
        background: #f7fafd;
        border-radius: 9px;
        padding: 4px 10px;
        display: flex;
        align-items: center;
        flex: 1;
        min-width: 160px;
        max-width: 220px;
    }
    .input-group.compact i { color: #0095f6; }
    .input-group.compact input {
        border: none;
        outline: none;
        background: transparent;
        font-size: 14px;
        margin-left: 6px;
        width: 100%;
        padding: 7px 0;
    }
    .post-btn {
        border: none;
        border-radius: 9px;
        padding: 9px 18px;
        font-size: 15px;
        font-weight: 500;
        cursor: pointer;
        transition: background 0.22s, transform 0.22s;
        display: flex;
        align-items: center;
        gap: 5px;
    }
    .post-btn.like, .post-btn.share, .post-btn.comment { background: #0095f6; color: white; }
    .post-btn.like:hover, .post-btn.share:hover, .post-btn.comment:hover {
        background: #0077cc; transform: translateY(-2px);
    }
    .post-btn.delete { background: #ff4d4d; color: white; }
    .post-btn.delete:hover { background: #cc0000; }

    .follow-actions {
    display: flex;
    align-items: center;
    gap: 12px;
}
    .comments-box {
        background: #f8fafb;
        padding: 13px;
        border-radius: 11px;
        white-space: pre-line;
        margin: 15px 0 8px;
        font-size: 14.5px;
        color: #262626;
        min-height: 38px;
    }
  .main-wrapper {
    display: flex;
    gap: 25px;
    max-width: 1200px;
    margin: 30px auto;
    align-items: flex-start;
}
.user-card {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 8px;
    border-radius: 10px;
    transition: 0.2s;
}

.user-card:hover {
    background: #f5f7fa;
}

.user-card span {
    font-size: 14px;
    font-weight: 500;
}

/* Button styles */
.user-card button {
    border: none;
    border-radius: 20px;
    padding: 6px 14px;
    cursor: pointer;
    font-size: 12px;
    font-weight: 500;
    min-width: 75px;
}

/* Follow */
.user-card button[type="submit"] {
    background: #0095f6;
    color: white;
}

/* Following */
.user-card button[disabled] {
    background: #e4e6eb;
    color: #333;
}

/* Remove */
.user-card form button[style] {
    background: #ff4d4d !important;
    color: white;
}

.sidebar {
    width: 260px;
    background: white;
    padding: 18px;
    border-radius: 16px;
    height: fit-content;
    box-shadow: 0 8px 25px rgba(0,0,0,0.08);
}

.sidebar h3 {
    margin-bottom: 15px;
    font-size: 16px;
    color: #444;
}

.user-card {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
}
    @media (max-width: 600px) {
        .post-img { max-width: 98%; }
        .post-actions { flex-direction: column; align-items: stretch; }
        .share-form, .comment-form { flex-direction: column; align-items: stretch; }
        .input-group.compact { width: 100%; max-width: none; }
    }
    @keyframes fadeIn { from {opacity:0; transform:translateY(20px);} to {opacity:1; transform:translateY(0);} }
    @keyframes fadeInUp { to { transform: translateY(0); opacity:1; } }
</style>
</head>
<body>
<div class="shape shape1"></div>
<div class="shape shape2"></div>
<div class="navbar">
    <h1>Vibbly</h1>
    <div class="nav-right">
 <a href="ProfileServlet" class="nav-btn profile-btn">
    <i class="fa-solid fa-user"></i>
</a>

<a href="notifications.jsp" class="nav-btn notif-btn">
    <i class="fa-solid fa-bell"></i>
</a>

<form action="logout" method="post" style="margin:0;">
    <button type="submit" class="nav-btn logout-btn">
        <i class="fa-solid fa-right-from-bracket"></i>
    </button>
</form>
</div>
</div>


</div>

<div class="main-wrapper">

<!-- SIDEBAR -->
<div class="sidebar">
    <h3>All Users</h3>

    <%
    try {
        Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/test", "root", "root"
        );

        PreparedStatement ps2 = con.prepareStatement(
    "SELECT u.id, u.username, f.status " +
    "FROM user u " +
    "LEFT JOIN follows f ON f.following_id = u.id AND f.follower_id = ? " +
    "WHERE u.id != ?"
);

ps2.setInt(1, userId);
ps2.setInt(2, userId);

        ResultSet rs2 = ps2.executeQuery();

        
    while (rs2.next()) {
%>
<div class="user-card">
    <span><%= rs2.getString("username")%></span>

    <%
        String status = rs2.getString("status");

        if (status == null) {
    %>
    <!-- FOLLOW BUTTON -->
    <form action="FollowServlet" method="post">
        <input type="hidden" name="senderId" value="<%= rs2.getInt("id")%>">
        <input type="hidden" name="action" value="request">
        <button type="submit">Follow</button>
    </form>

    <%
        } else if ("accepted".equals(status)) {
    %>
    <!-- FOLLOWING + REMOVE -->
   <div class="follow-group">

        <button disabled style="background:#ccc; color:black;">
            Following
        </button>

        <form action="FollowServlet" method="post" style="margin:0;">
            <input type="hidden" name="senderId" value="<%= rs2.getInt("id")%>">
            <input type="hidden" name="action" value="remove">
            <button type="submit" style="background:#ff4d4d; color:white; padding-top: 1px">
                Remove
            </button>
        </form>

    </div>
    <%
        }
    %>

</div> <!-- 🔥 VERY IMPORTANT CLOSE -->
<%
    }  // 🔥 LOOP CLOSE
    rs2.close();
    ps2.close();
    con.close();
} catch(Exception e) {
    out.println(e);
}
%>
</div>

<!-- MAIN CONTENT -->
<div class="container">

    <!-- Upload Form -->
    <form class="upload" action="UploadServlet" method="post" enctype="multipart/form-data">
        <label class="file-drop">
            <i class="fa-solid fa-cloud-arrow-up"></i>
            <p>Click to choose image</p>
            <input type="file" name="image" accept="image/*" style="display:none" required>
        </label>
        <div class="input-group">
            <i class="fa-solid fa-pen"></i>
            <input type="text" name="caption" placeholder="Write a caption..." required>
        </div>
        <button type="submit"><i class="fa-solid fa-upload"></i> Upload</button>
    </form>

    <!-- Feed Posts -->
    <%
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
         PreparedStatement ps = con.prepareStatement(
    "SELECT p.* FROM posts p " +
    "JOIN user u ON p.username = u.username " +
    "LEFT JOIN follows f ON f.following_id = u.id AND f.follower_id = ? " +
    "WHERE (f.status = 'accepted' OR u.id = ?) " +
    "ORDER BY p.id DESC"
);

ps.setInt(1, userId);
ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int postId = rs.getInt("id");
    %>
    <div class="post">
        <div class="post-header">
            <div class="post-user">
                <i class="fa-solid fa-user-circle" style="font-size:1.5em; color:#0095f6; margin-right:6px;"></i>
                <b><%= rs.getString("username") %></b>
            </div>
        </div>

        <img src="<%= rs.getString("image_path") %>" alt="Post image" class="post-img">
        <p class="caption"><%= rs.getString("caption") %></p>

        <!-- Like + Share in one line -->
        <div class="post-actions">
            <form action="LikeServlet1" method="post">
                <input type="hidden" name="postId" value="<%= postId %>">
                <button type="submit" class="post-btn like">
                    <i class="fa-solid fa-thumbs-up"></i> Like (<%= rs.getInt("likes") %>)
                </button>
            </form>
            <form action="ShareServlet1" method="post" class="share-form">
                <div class="input-group compact">
                    <i class="fa-solid fa-user-plus"></i>
                    <input type="text" name="receiver" placeholder="Username to share..." required>
                </div>
                <input type="hidden" name="postId" value="<%= postId %>">
                <button type="submit" class="post-btn share">
                    <i class="fa-solid fa-share"></i> Share (<%= rs.getInt("shares") %>)
                </button>
            </form>
        </div>

        <!-- Comments -->
        <div class="comments-box">
            <%= rs.getString("comments") == null ? "" : rs.getString("comments") %>
        </div>

        <!-- Comment box + button inline -->
        <form action="CommentServlet1" method="post" class="comment-form">
            <div class="input-group compact">
                <i class="fa-solid fa-comment-dots"></i>
                <input type="text" name="commentText" placeholder="Add a comment..." required>
            </div>
            <input type="hidden" name="postId" value="<%= postId %>">
            <button type="submit" class="post-btn comment">
                <i class="fa-solid fa-comment"></i> Comment
            </button>
        </form>

        <% if (username.equals(rs.getString("username"))) { %>
            <form action="DeleteServlet" method="post" style="margin-top:15px;">
                <input type="hidden" name="postId" value="<%= postId %>">
                <button type="submit" class="post-btn delete" onclick="return confirm('Delete this post?')">
                    <i class="fa-solid fa-trash"></i> Delete
                </button>
            </form>
        <% } %>
    </div>
    <%
            }
            con.close();
        } catch(Exception e) {
            out.println(e);
        }
    %>
</div>
</body>
</html>

