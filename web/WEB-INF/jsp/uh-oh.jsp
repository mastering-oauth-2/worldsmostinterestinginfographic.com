<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/jsp/inc/html-head.jsp" %>
<body>
<div id="wrapper">
    <%@include file="/WEB-INF/jsp/inc/header.jsp" %>
    <main id="main" role="main">
        <section class="error" style="background-image:url(images/bg-error.jpg);">
            <span class="overlay"></span>
            <div class="text-wrap">
                <div class="text-holder">
                    <h1>Something is definitely wrong!</h1>
                    <p>An error occurred and now you're here.<br> Don't worry.  Top men are on it.</p>
                    <%
                        if (request.getSession().getAttribute("error") != null) {
                            out.println("<p>" + request.getSession().getAttribute("error") + "</p>");
                        }

                        if (request.getSession().getAttribute("errorDescription") != null) {
                            out.println("<p>" + request.getSession().getAttribute("errorDescription") + "</p>");
                        }
                    %>
                    <a href="/" class="btn">Back <span class="thin">to</span> Homepage</a>
                </div>
            </div>
        </section>
    </main>
</div>
<%@include file="/WEB-INF/jsp/inc/scripts.jsp" %>
</body>
</html>
