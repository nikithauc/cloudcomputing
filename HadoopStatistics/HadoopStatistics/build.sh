if [ ! -d classes ]; then
        mkdir classes;
fi

# Compile Statistics
javac -classpath $HADOOP_HOME/hadoop-core-1.1.2.jar:$HADOOP_HOME/lib/commons-cli-1.2.jar -d ./classes Statistics.java

# Create the Jar
jar -cvf statistics.jar -C ./classes/ .
 
# Copy the jar file to the Hadoop distributions
cp statistics.jar $HADOOP_HOME/bin/ 

