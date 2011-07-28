<%-- 
    Document   : AMI interface
    Created on : Jul 26, 2011, 12:15:15 PM
    Author     : prashanth_p
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AMI</title>
        <script>
            
            function submitPage(btnValue){
                document.managerForm.actionValue.value=btnValue;
                document.managerForm.submit();
                
            }
        </script>
    </head>
    <body bgcolor="white">
        <h1 align="center"> AMI INTERFACE </h1><br/>
        <form action="ManagerServlet" method="get" name="managerForm">
            <center>
                START:
                <input type="button"  value="Login" onclick="submitPage('Login')"/><br/>
                PARK:
                <input type="button"  value="Park" onclick="submitPage('Park')"/><br/>
                BRIDGE:
                <input type="button"  value="Bridge" onclick="submitPage('Bridge')"/><br/>
                JOIN-CONF:
                <input type="button"  value="Join" onclick="submitPage('Join')"/><br/>
                SUPERVISE:
                <input type="button"  value="SuperVise" onclick="submitPage('Super')"/><br/>
                EXIT:
                <input type="button"  value="Exit" onclick="submitPage('Exit')"/><br/>
                STATUS:<br/>                
                <%if (request.getAttribute("status") != null) {
                        out.println("<b>" + request.getAttribute("status") + "</b>");
                    }%>
            </center>
            <input type="hidden" name="actionValue"/>
        </form>
        <h2 align="center"> THANK YOU!!VISIT AGAIN</h2>


    </body>
</html>