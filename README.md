[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18218738&assignment_repo_type=AssignmentRepo)
### **📌 Document Similarity Using Hadoop MapReduce**  

# Objective
The goal of this assignment is to compute the Jaccard Similarity between pairs of documents using MapReduce in Hadoop. You will implement a MapReduce job that:

- Extracts words from multiple text documents.
- Identifies which words appear in multiple documents.
- Computes the Jaccard Similarity between document pairs.
- Outputs document pairs with similarity above 50%.

## 💞 Example Input
You will be given multiple text documents. Each document will contain several words. Your task is to compute the Jaccard Similarity between all pairs of documents based on the set of words they contain.

### Example Documents
#### doc1.txt

apple banana orange grape kiwi

#### doc2.txt

apple banana orange mango kiwi

#### doc3.txt

apple banana grape kiwi watermelon


## 📏 Jaccard Similarity Calculator

### Overview
The Jaccard Similarity is a statistic used to gauge the similarity and diversity of sample sets. It is defined as the size of the intersection divided by the size of the union of two sets.

### Formula
The Jaccard Similarity between two sets A and B is calculated as:


Jaccard Similarity = |A ∩ B| / |A ∪ B|

Where:
- |A ∩ B| is the number of words common to both documents.
- |A ∪ B| is the total number of unique words in both documents.

### Example Calculation
Consider two documents:

doc1.txt words: {apple, banana, orange, grape, kiwi} 
doc2.txt words: {apple, banana, orange, mango, kiwi}


Common words: {apple, banana, orange, kiwi}
Total unique words: {apple, banana, orange, grape, kiwi, mango}

Jaccard Similarity calculation:


|A ∩ B| = 4 (common words)
|A ∪ B| = 6 (total unique words)


Jaccard Similarity = 4/6 = 0.67 or 67%

## 👤 Expected Output
The output should show the Jaccard Similarity between document pairs in the following format:

(doc1, doc2) -> 67.7%  
(doc1, doc3) -> 67.7%  


## 🛠 Environment Setup: Running Hadoop in Docker

### Step 1: Install Docker & Docker Compose
- *Windows*: Install Docker Desktop and enable WSL 2 backend.
- *macOS/Linux*: Install Docker using the official guide: [Docker Installation](https://docs.docker.com/get-docker/).

### Step 2: Start the Hadoop Cluster
Navigate to the project directory where docker-compose.yml is located and run:

docker-compose up -d

This will start the Hadoop NameNode, DataNode, and ResourceManager services.

### Step 3: Access the Hadoop Container
Once the cluster is running, enter the Hadoop master node container:

docker exec -it hadoop-master /bin/bash


## 🛀 Building and Running the MapReduce Job with Maven

### Step 1: Build the JAR File
Ensure Maven is installed, then navigate to your project folder and run:

mvn clean package

This will generate a JAR file inside the target directory.

### Step 2: Copy the JAR File to the Hadoop Container
Move the compiled JAR into the running Hadoop container:

docker cp target/similarity.jar hadoop-master:/opt/hadoop-3.2.1/share/hadoop/mapreduce/similarity.jar


## 📂 Uploading Data to HDFS

### Step 1: Create an Input Directory in HDFS
Inside the Hadoop container, create the directory where input files will be stored:

hdfs dfs -mkdir -p /input


### Step 2: Upload Dataset to HDFS
Copy your local dataset into the Hadoop cluster’s HDFS:

hdfs dfs -put /path/to/local/input/* /input/


## 🚀 Running the MapReduce Job
Run the Hadoop job using the JAR file inside the container:

hadoop jar similarity.jar DocumentSimilarityDriver /input /output_similarity /output_final


## 📊 Retrieving the Output
To view the results stored in HDFS:

hdfs dfs -cat /output_final/part-r-00000

If you want to download the output to your local machine:

hdfs dfs -get /output_final /path/to/local/output


## 🚧 Challenges Faced & Solutions

### 1. Controller Running Error
- *Issue:* The MapReduce job failed due to controller issues in the Hadoop cluster.
- *Solution:* Restarted the Hadoop cluster using docker-compose down followed by docker-compose up -d. Ensured all services were properly initialized before running the job again.

### 2. Mapper Not Emitting Expected Values
- *Issue:* The Mapper was not correctly emitting key-value pairs.
- *Solution:* Debugged using logs and ensured correct tokenization of input text.

### 3. Memory Issues in Reducer
- *Issue:* Large document sets caused memory overflow in the reducer.
- *Solution:* Optimized the reducer logic to process documents in smaller batches.

### 4. HDFS Write Permission Errors
- *Issue:* Permission issues prevented writing output to HDFS.
- *Solution:* Adjusted HDFS permissions using hdfs dfs -chmod -R 777 /output_final.

### 5. Inconsistent Jaccard Similarity Calculation
- *Issue:* Results were inconsistent due to leading/trailing spaces in word sets.
- *Solution:* Trimmed spaces from words before adding them to sets.

### 6. Incorrect Output Formatting
- *Issue:* The similarity percentages were not correctly formatted in the output.
- *Solution:* Updated the reducer logic to ensure percentages are rounded to two decimal places.

This README ensures the repository is well-documented and self-explanatory for users.
