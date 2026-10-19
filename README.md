# SkillBridge 🎓

### A Student Skill-Sharing and Learning Platform

SkillBridge is a Java-based web application designed to help students learn new skills, assess their knowledge, access learning resources, and track their learning progress.

## ✨ Features

- **User Authentication:** Student registration and login.
- **Dashboard:** View learning statistics and access platform features.
- **Explore Skills:** Explore available skills and manage personal skills.
- **Learning Partners:** Discover peer-learning opportunities.
- **Learning Resources:** Access resources to support learning.
- **Online Quizzes:** Beginner, intermediate, and advanced quizzes.
- **Assessment and Scoring:** Answer questions and view quiz scores.
- **Progress Tracking:** Review previous attempts and performance.
- **Leaderboard:** Compare quiz scores with other students.
- **Achievement Badges:** Recognize learning milestones.

## 🛠️ Technologies Used

- **Backend:** Java, Spring Boot
- **Frontend:** HTML, CSS, Thymeleaf
- **Database:** MySQL
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA
- **Version Control:** Git and GitHub

## 🗄️ Database

The application uses MySQL to store application data, including user information, skills, quizzes, questions, learning resources, and quiz attempts.

Database name: `skillbridge_db`

## 🚀 How to Run

### Prerequisites

- Java JDK 21
- MySQL Server 8
- Maven support through the included Maven Wrapper

### Setup

1. Clone this repository:

   ```bash
   git clone YOUR_GITHUB_REPOSITORY_URL
   ```

2. Open the project in IntelliJ IDEA.

3. Create the MySQL database:

   ```sql
   CREATE DATABASE skillbridge_db;
   ```

4. Create a local configuration file at:

   `src/main/resources/application-local.properties`

5. Add your local database configuration to that file:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/skillbridge_db
   spring.datasource.username=YOUR_MYSQL_USERNAME
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   ```

   Use your own local database credentials. Do not commit this file to GitHub.

6. Make sure `application.properties` imports the local configuration:

   ```properties
   spring.application.name=skillbridge
   spring.config.import=optional:classpath:application-local.properties
   ```

7. Run the application using IntelliJ IDEA, or use the Maven Wrapper on Windows:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

8. Open the application in your browser:

   http://localhost:8080

## 🔐 Security Note

Database credentials are stored in a local configuration file that should remain private and excluded from version control.

## 🎯 Project Objective

SkillBridge aims to make student learning more collaborative by combining skill sharing, quizzes, learning resources, and progress monitoring in one platform.

## 🔮 Future Enhancements

- Personalized learning recommendations
- Improved student messaging and collaboration
- Additional quizzes and learning paths
- Administrative tools and analytics

## 👩‍💻 Project Status

The core application features have been implemented and tested locally.

---

**SkillBridge — Learn Together. Grow Together.**
