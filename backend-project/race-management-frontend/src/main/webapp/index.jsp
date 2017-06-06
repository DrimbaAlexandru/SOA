<%--
  Created by IntelliJ IDEA.
  User: AlexandruD
  Date: 06-Jun-17
  Time: 10:30 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Races management</title>
</head>
<link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css"
      href="http://www.prepbootstrap.com/Content/shieldui-lite/dist/css/light/all.min.css" />

<body>

<div class="container">
    <!-- Races Container -->
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="text-center">Races<span class="fa fa-edit pull-right bigicon"></span></h4>
                </div>
                <div class="panel-body text-center">
                    <div id="grid-race"></div>
                </div>
            </div>
        </div>
    </div>

    <!-- Age Group container -->
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="text-center">Age Groups<span class="fa fa-edit pull-right bigicon"></span></h4>
                </div>
                <div class="panel-body text-center">
                    <div id="grid-age-group"></div>
                </div>
            </div>
        </div>
    </div>

    <!-- Length -->
    <div class="row">
        <div class="col-md-12">
            <div class="input-group">
                  <span class="input-group-btn">
                      <label>Length</label>
                  </span>
                <input type="text" class="form-control" placeholder="Length" id="createLength">
            </div>
        </div>
    </div>

    <!-- AgeGroup id -->
    <div class="row">
        <div class="col-md-12">
            <div class="input-group">
                <span class="input-group-btn">
                    <label>Age Group id</label>
                </span>
                <input type="text" class="form-control" placeholder="Age Group" id="createAgeGroupId">
            </div>
        </div>
    </div>

    <!-- Button -->
    <div class="row">
        <div class="col-md-12">
            <button class="btn btn-default" type="button" id="goCreate">Add race</button>
        </div>
    </div>
</div>

<script
        src="https://code.jquery.com/jquery-3.2.1.min.js"
        integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript"
        src="http://www.prepbootstrap.com/Content/shieldui-lite/dist/js/shieldui-lite-all.min.js"></script>
<script type="text/javascript"
        src="js/script.js"></script>
</body>
</html>
