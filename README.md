# note-manager

1. What is required for running the project:<br>
-Java (at least 8) SDK, Gradle, MySQL, Apache

2. Steps how to run scripts that will setup database for the project:<br>
-You need to create MySQL Database and provide its properties to file: "application.properties" in 'resources' catalog<br>
-DataBase structure will be created automaticly with Hibernate help

3. Steps how to build and run the project:<br>
- download the project's source code<br>
- Gradle will automaticly download required libraries thanks to existing dependencies<br>
- run project with Gradle<br>

4. Example usages:<br>

 - Examples of PowerShell's Invoke-RestMethod:<br>
 
GET ALL NOTES: Invoke-RestMethod ‘http://localhost:8080/api/notes’

GET SINGLE NOTE: Invoke-RestMethod ‘http://localhost:8080/api/notes/2’

ADD NEW NOTE:<br>
 $note = @{<br>
 title='abc'<br>
content='abc'<br>
}<br>
$json = $note | ConvertTo-Json<br>
Invoke-RestMethod 'http://localhost:8080/api/notes' -Method Post -Body $json -ContentType 'application/json'<br>

DELETE NOTE: Invoke-RestMethod 'http://localhost:8080/api/notes/2' -Method Delete

UPDATE EXISTING NOTE:<br>
$newnote  =@{<br>
title='xyz'<br>
content='xyz'<br>
}<br>
 $json=$newnote | ConvertTo-Json<br>
 Invoke-RestMethod 'http://localhost:8080/api/notes/1' -Method Put -Body $json -ContentType 'application/json'<br>
 
GET NOTES NOT MODIFIED FOR MORE THAN MONTH:
Invoke-RestMethod ‘http://localhost:8080/api/notes/not_updated’

GET PAGINATED SORTED NOTES:<br>
$pagination_info = @{<br>
pageSize = 3<br>
pageOffset = 2<br>
sortColumn = 'title'<br>
sortDirection = 'asc'<br>
}<br>
$json = $pagination_info | ConvertTo-Json<br>
Invoke-RestMethod 'http://localhost:8080/api/notes/pagination' -Method Post -Body $json -ContentType 'application/json'<br>

-you can also request mentioned above ENDPOINTS in easy way by api development environment like POSTMAN



