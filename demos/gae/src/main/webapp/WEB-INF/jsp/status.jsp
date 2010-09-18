<%--


    Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>

    ====================================================================
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    ====================================================================

--%>
<%@ page buffer="20kb"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%> 
<html>
<head>
<title>jclouds: multi-cloud framework</title>
<style type="text/css">
<!--
table.staticheader {
text-decoration: none;
border: 1px solid #CCC;
width: 98%;
}

table.staticheader th {
padding: 3px 3px 3px 3px !important;
text-align:center;
}

table.staticheader td {
padding: 3px 3px 3px 3px !important;
}

table.staticheader thead tr {
position: relative;
height: 10px;
background-color: #D7E5F3;
}

table.staticheader tbody {
height:100px;
overflow-x:hidden;
overflow-y: auto; 
overflow:scroll;
}

table.staticheader tbody tr {
height: auto;
white-space: nowrap;
}

table.staticheader tbody tr.odd {
        background-color: #eee
}

table.staticheader tbody tr.tableRowEven,tr.even {
        background-color: #ddd
}

table.staticheader tbody tr td:last-child {
padding-right: 20px;
}

table.staticheader tbody td {
padding: 2px 4px 2px 4px !important;
                
}

div.TableContainer {
height: 100px; 
overflow-x:hidden; 
overflow-y:auto;
}
-->
</style>
</head>
<body>
<h2>This servlet context is registered as node <a href='https://manage.opscode.com/nodes/<c:out value="${node.name}"/>'><c:out value="${node.name}"/></a> in the <a href='https://cookbooks.opscode.com/users/new'>Opscode Platform</a></h2>
    <table width="100%" border="0">
                <tr>
                        <td>

<div class="TableContainer">

<display:table name="node" cellpadding="5" cellspacing="1" class="staticheader">
	<display:column property="name" />
	<display:column property="runList"/>
</display:table>
</div>
</td>
</tr>
</table>
<h2>Contents of <c:out value="${node.name}"/>'s first role</h2>
    <table width="100%" border="0">
             <tr>
                        <td>

<div class="TableContainer">

<display:table name="role" cellpadding="5" cellspacing="1" class="staticheader">
</display:table>
</div>
</td>
</tr>
</table>
<h2>Contents of databag</h2>
    <table width="100%" border="0">
             <tr>
                        <td>

<div class="TableContainer">

<display:table name="items" cellpadding="5" cellspacing="1" class="staticheader">
</display:table>
</div>
</td>
</tr>
</table>

</body>
</html>