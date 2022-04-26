We use `demands.txt` to record the forms that backend need and the variables (cookies, ...) that the frontend need. 

index.jsp: home page
login.jsp: login/register page
results.jsp: page contain people associated to user input
write.jsp: write comment page
detail.jsp: page include all comments of a person
chat.jsp: chat room

index.jsp -> login.jsp
index.jsp -> SearchDispatcher.java -> results.jsp
index.jsp -> write.jsp
login.jsp -> Login/Register/GoogleDispatcher.java -> index.jsp
results.jsp -> index.jsp
results.jsp -> DetailDispatcher.java -> detail.jsp
results.jsp -> write.jsp
write.jsp -> index.jsp
write.jsp -> WriteDispatcher.java -> detail.jsp
detail.jsp -> index.jsp
detail.jsp -> chat.jsp
chat.jsp -> index.jsp

LoginDispatcher.java
RegisterDispatcher.java
GoogleDispatcher.java
SearchDispatcher.java
DetailDispatcher.java
WriteDispatcher.java