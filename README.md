# Reddit-Clone

This is a massively-scalable application reddit clone. It follows the microservices architecture.

* Spring Boot framwork Java) is used for each micrservice.
* RabbitMQ is used as a message queue for asynchronous communication between microservices.
* Reddis is used for caching.
* Each microservice team is free to choose which database to use.

Chat Team (Individual and Group Chat):
* Database used is Google Firestore NoSQL database because:
 * It offers real time firestore client which ensures real-time communication.
 * NoSQL database is preferred for denormalization to offer scalablility and high availability over consistency in SQL databases. 


### Functionalities:
* Sign up, login and delete account 
* Edit proﬁle 
* Choose a proﬁle photo 
* Follow and unfollow users 
* Block users 
* Report users 
* Create thread (reddit)
* Create sub thread (sub-reddit)
* Comment on sub-reddit
* Dis/like comment
* Add image to sub-reddit
* Assign moderator to reddit
* Follow reddits
* Report sub-reddit
* Moderator can ban users
* Moderator can see reports being made
* Bookmark sub-reddit
* Bookmark reddit
* Dis/like sub-reddit
* Search for reddits and sub-reddits
* Users can chat with other users, including group chats (real-time)
* Users can see most popular reddits
* Users can see most popular sub-reddits
* Notiﬁcations on:
  * Comments
  * Messages
  * Being tagged in posts, photos or comments
  * Someone joins a group
  * Admin of group is change
  * Photo or comment are liked
  * New followers
* User recommendations based on followers
* Reddits recommendations
* Sub-Reddits recommendations
