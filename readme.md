Notification App.

It was required to create a system capable of receiving messages, which will have a
category and the body of the message. These messages will need to be forwarded to
the system's users, who will already be pre-populated. In addition to being subscribed
to message categories, these users will have specified the channels through which they
would like to be notified, such as SMS, Email or Push Notification.
With this configuration, users will only receive notifications of messages that fall within
the categories they are subscribed to and through the channels they have specified.
There will be three message categories:

    • Sports
    • Finance
    • Movies

And there will be three types of notifications, each requiring its own class to manage
the sending logic independently:

    • SMS
    • E-Mail
    • Push Notification

It was necessary to design the architecture for sending notifications through various
channels. At a minimum, there should be one class for each channel, along with a
strategy to select the appropriate channel. Real messages need not be sent using third-
party services; the focus is on establishing a structure to implement the logic in the
future.

Additionally, it was essential to store all the relevant information required to verify that the
notification has been successfully delivered to the respective subscriber. This includes
details such as the message type, notification type, user data, timestamp, and any
other pertinent information.

No user administration is required, it is used a Mock of users in the source code, and
they must have the following information:

    • ID
    • Name
    • Email
    • Phone number
    • Subscribed [ ] here you need to list all the categories where the user issubscribed.
    • Channels [ ] a list of the notification's channels (SMS | E-Mail | Push Notification).

As a user interface, you must display 2 main elements.

1. Submission form. A simple form to send the message that contains 2 fields:
  
    • Category. List of available categories.

    • Message. Text area, confirm that the message is not empty.

2. Log history. A list of all data records in the log, sorted from newest to oldest. They are the Sent Notifications.

-----------------------------------------------------------------------------------------------

For buildeng the application uses:

      .   ____          _            __ _ _
     /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
     \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
      '  |____| .__|_| |_|_| |_\__, | / / / /
     =========|_|==============|___/=/_/_/_/
     
     :: Spring Boot ::                (v3.3.4)

    -> Docker Compose : SpringBoot App - KafKa - KAfKa Ui - MySql - MongoDb - (NodeJs / ReactJs)

    -> Docker Compose Image : https://hub.docker.com/r/alvartaco/notification-docker-compose

    -> RUNNING the Apps:

        -> Clone or unzip this Project code.

        -> Start Docker Compose from this Github Project's (cloned/downloaded) sorce folder: 
        
            /notification-docker-compose$ docker-compose up -d  

        -> Restart ALL Docker Container Services.

        -> Wait for all containers to be up and running.

        ==>> GO TO : http://localhost:3000

    -> Building the Apps:

        -> Build the Spring Boot Application with: 
        
            /notification-docker-compose$ mvn clean install

        -> Start the Application: 

            java -jar target/notification-docker-compose-0.0.3-SNAPSHOT.jar

            Note: If application fails to start, try restaring Docker Services.

        -> Build the NodeJS/ReactJS Application with:

            /notification-docker-compose/src/main/node_server$ npm install
            /notification-docker-compose/src/main/node_server$ npm run build

        -> Start the NodeJS Application:
            
            /notification-docker-compose/src/main/node_server$ npm start

       ==>> GO TO : http://localhost:3000

    -> JDK : 21

    -> HTTP Ports External App: 8082 / 3000

    -> HTTP Ports Docker Internal App: 8082 / 3000

    -> KafKa UI : 8081

    -> MAVEN
   
    -> JDBC Client for DB Connection - MySQL at Docker Compose

    -> Dpting Data / Reository - Mongo-DB at Docker Compose

    -> Thymeleaf Java engine for processing and creating HTML, XML, JavaScript, CSS and text

    -> HTMX a small JavaScript library that allows you to use custom attributes in HTML

    -> NodeJS and ReactJS for the Login and Registration of Users in Mongo-DB.

    -> Dessign Pattern: Factory / Strategy for different notification channels like SMS, e-mail, Push notifications. 
    And Observer for the main objetive of Users receiving updates in their subsctipyed Message Categories.

    -> Git / Github :D and Git Flow

    -> Responsive Flex Design

    -> API REST

    -> Junit for Tests


![img.png](img.png)

Adding some testing results of the first Iteration:


![categoryTable](https://github.com/user-attachments/assets/cbddbf3c-6f00-4518-9d6c-15b82f4d867c)
![messageTable](https://github.com/user-attachments/assets/ad6966c4-e187-4451-8d56-cbc6838baf67)
![notificationTable](https://github.com/user-attachments/assets/950ed279-7d70-4de3-bfe3-897bf3cc285a)

The Log and some json files, related to users and categories are also uploaded in the root/xDocuments

Without authentication, it was possible to test creation of Messages, that fire the Notifications the User's that are Subscribed, using the REST API; with this command:

    curl -X POST localhost:8082/api/messages -H 'Content-type:application/json' -d '{"categoryId": "2", "messageBody": "the boddy"}'

Now it is suggested to use Postman:

* Send a Message to /api/messages (using the JWT token / SECRET_KEY / CSRF Token):
* It needs to be more detailed.!!!! 

Create a new POST request in Postman:

URL: http://localhost:8082/api/messages
Headers:
Content-Type: application/json
Authorization: Bearer your_jwt_token
Replace your_jwt_token with the token you copied in the previous step.

Body (raw JSON):

{
"categoryId": "2",
"messageBody": "This is a test message from Postman"
}

Important: categoryId and messageBody are mandatory.

Click Send.

Expected Response: A 201 Created response.

Check the logs: you should see the message #NOTIFICATIONS-D-C - Sent message to kafka, category:2, message:This is a test message from Postman! Additional Scenarios to Test (Very Important)

Sent Notifications List:

![img_1.png](img_1.png)

Latest Version Includes Login + Registration pages implemented with NodeJS and ReactJS.

Users are stored in MongoDB.

Security is Implemented with Spring JWT Security.

Error pages are implemented too.
