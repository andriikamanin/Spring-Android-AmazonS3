Here’s the README in English:

⸻

S3 Image API

This project is an API for working with AWS S3. It allows you to upload and download images to/from S3 cloud storage using Spring Boot.

Description

The API provides two main endpoints:
	1.	Upload File: Uploads an image to S3 cloud storage.
	2.	Download File: Allows you to download a file from S3 by its name.

The project uses the AWS SDK to interact with S3 and Spring Boot to create the RESTful service.

Technologies Used
	•	Spring Boot — for creating the REST API.
	•	AWS S3 — for file storage.
	•	Maven — for dependency management.
	•	dotenv — for loading sensitive data from the .env file.
	•	Java 17+ — JDK version.

Setup
	1.	Clone the repository:

git clone https://github.com/andriikamanin/s3-image-api.git

	2.	Navigate to the project directory:

cd s3-image-api

	3.	Make sure you have JDK 17 or higher installed.
	4.	Add your AWS parameters in the .env file at the root of the project:

AWS_ACCESS_KEY_ID=your-access-key-id
AWS_SECRET_ACCESS_KEY=your-secret-access-key
AWS_REGION=your-region
AWS_S3_BUCKET=your-bucket-name

	5.	Build the project using Maven:

mvn clean install

	6.	Run the application:

mvn spring-boot:run

API

1. Upload File to S3
	•	Method: POST
	•	URL: /s3/upload
	•	Description: Uploads a file to S3.
	•	Request Parameters:
	•	file: the file you want to upload.

2. Download File from S3
	•	Method: GET
	•	URL: /s3/download/{fileName}
	•	Description: Downloads a file from S3 by its name.
	•	Path Parameter:
	•	fileName: the name of the file you want to download.

.env Example

You can use a .env file to store your AWS credentials. An example .env file:

AWS_ACCESS_KEY_ID=your-access-key-id
AWS_SECRET_ACCESS_KEY=your-secret-access-key
AWS_REGION=your-region
AWS_S3_BUCKET=your-bucket-name

.gitignore

Make sure to add .env to your .gitignore to avoid uploading sensitive credentials to your version control system.

# .gitignore
.env

