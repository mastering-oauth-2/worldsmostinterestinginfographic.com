<%@ page import="com.worldsmostinterestinginfographic.model.object.User" %>
<%@ page import="com.worldsmostinterestinginfographic.model.Model" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User)Model.cache.get(request.getSession().getId() + ".profile");
%>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/jsp/inc/html-head.jsp" %>
<body class="infographic">
<div id="wrapper">
<%@include file="/WEB-INF/jsp/inc/header.jsp" %>
    <main id="main" role="main">
        <article class="intro">
            <div class="container">
                <h1>Hello, <%= user.getName() %>!</h1>
                <img src="https://graph.facebook.com/<%= user.getId() %>/picture?width=268&height=268" alt="profile picture" width="268" height="268">
            </div>
        </article>
        <div id="waitscreen" class="waitorerror">
            <h2 align="center" class="blue">Fetching world's most interesting data...</h2>
            <div id="spinner"></div>
        </div>
        <div id="error" class="waitorerror">
            <h2 align="center" class="blue">An error has occurred</h2>
            <p>Sorry, but we need access to your feed data to generate the world's most interesting infographic.  If you decide to allow us to see your feed data, go <a href="https://www.facebook.com/settings?tab=applications">here</a>, find &quot;Most Interesting Infographic&quot;, delete it, and try logging into our site again.</p>
        </div>
        <div id="content" class="container">

            <!-- Top Friends infographic -->
            <section class="block-friends fade-block">
                <h2 class="blue">Top friends who like your posts</h2>
                <div class="holder">
                    <div class="total">
                        <p><span class="number blue" id="friends-likes">703</span>Total likes received from <span class="blue">“Top <span id="friends-amount">4</span> friends”</span></p>
                    </div>
                    <div id="top-friends-hbar-chart">
                        <!-- Insert chart here -->
                    </div>
                </div>
            </section>

            <!-- Post Types infographic -->
            <section class="block-types fade-block">
                <div class="text-holder">
                    <h2 class="green">Your post types</h2>
                    <ul id="post-types-list" class="post-types">
                        <li><span class="description">Post type</span></li>
                        <li><span class="description">Post type</span></li>
                        <li><span class="description">Post type</span></li>
                        <li><span class="description">Post type</span></li>
                    </ul>
                    <p><span id="most-frequent-post-percentage" class="number"></span> of your posts are <span id="most-frequent-post-type"></span></p>
                </div>
                <div id="post-types-donut-chart">
                    <!-- Insert chart here -->
                </div>
            </section>

            <!-- Daily Post Frequency infographic -->
            <section class="block-frequency fade-block">
                <h2 class="orange fade-block">Posts by day of the week</h2>
                <div class="fade-block" id="daily-post-frequency-bar-chart">
                    <!-- Insert chart here -->
                </div>
                <div class="text-holder fade-block">
                    <p><span id="highest-daily-value" class="number blue-light">3.1</span>most active on <span id="highest-daily-day" class="mark">Fridays</span></p>
                    <p><span id="lowest-daily-value" class="number orange">0.5</span>quietest on <span id="lowest-daily-day" class="mark">Mondays</span></p>
                </div>
            </section>

            <!-- Monthly Post Frequency infographic -->
            <section class="block-privacy fade-block">
                <div class="text-holder">
                    <h2 class="blue-light">Your post frequency</h2>
                    <p><span id="highest-monthly-month" class="number blue-light">Dec</span>favorite <span class="mark">month</span></p>
                    <p><span id="monthly-average" class="number blue">2.75</span>average posts <br><span class="mark">per month</span></p>
                </div>
                <div id="monthly-post-frequency-line-chart">
                    <!-- Insert chart here -->
                </div>
            </section>

            <!-- Top Words infographic -->
            <section class="block-words fade-block">
                <div class="text-holder">
                    <h2 class="black">Your most used words</h2>
                    <p><span id="top-word" class="large">&quot;Damn&quot;</span>is your most used word</p>
                </div>
                <div class="htagcloud">
                    <ul id="top-words" class="popularity">
                        <!-- Insert chart here -->
                    </ul>
                </div>
            </section>

        </div>
    </main>
<%@include file="/WEB-INF/jsp/inc/footer.jsp" %>
</div>
<%@include file="/WEB-INF/jsp/inc/scripts.jsp" %>
<script>
    $.getJSON( "/stats", function( data ) {

        // Get rid of the spinner
        $('#content').show();
        $('#waitscreen').hide();
        $(this).spin(false);

        // Start rendering the graphs
        initTopFriendsChart(data.TOP_FRIENDS);
        initPostTypesChart(data.POST_TYPES);
        initDailyPostFrequencyChart(data.DAILY_POST_FREQUENCY);
        initMonthlyPostFrequencyChart(data.MONTHLY_POST_FREQUENCY);
        initWordChart(data.TOP_WORDS);

    });
</script>
</body>
</html>
