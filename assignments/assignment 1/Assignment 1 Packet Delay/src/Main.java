import java.io.*;
import java.util.*;

import static java.util.Collections.min;
import static java.util.Collections.sort;

public class Main {
    public static void main(String[] args) {
        String filePathTraceRoute = "/Users/aidenpratt/Documents/Documents - Aiden’s MacBook Pro/AP-6014/assignments/assignment 1/traceroute2_output.txt";
        String filePathPing = "/Users/aidenpratt/Documents/Documents - Aiden’s MacBook Pro/AP-6014/assignments/assignment 1/ping1_output.txt";

        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filePathPing));
            String line;
            String[] tokens;
            //List which will store all the ping times from the file
            List<Double> allPingTimes = new ArrayList<>();
            while((line = fileReader.readLine()) != null) {

                //using hte while loop to find the times for the ping which will later be
                //used to find the queuing delay average
                // \\s+ splitting by white space
                tokens = line.split("\\s+");
                for (int i = 1; i < tokens.length; i++) {
                    if (tokens[i].contains("time=")) {
                        String[] parts = tokens[i].split("time=");
                        double timeValue = Double.parseDouble(parts[1]);
                        allPingTimes.add(timeValue);
                    }
                }
            }

            //finding the min value in the allpingtimes collection
            double min = Collections.min(allPingTimes);

            //FINDING THE AVERAGE QUEUING DELAY PING TIME
            double pingTimesAvg = findAveragePing(allPingTimes, min);
            System.out.println(pingTimesAvg);


            //--------------------ip address and timing---------------------------
            //String newIPAddress = extractIPAddress(line);
            //Double newTimeAverage = extractAverageTime(tokens);
            //System.out.print(newIPAddress + "\t");
            // System.out.println(newTimeAverage + "\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }//end of MAIN


    //functions for IP address
    public static String extractIPAddress(String line){
        int start = line.indexOf('(');
        int end = line.indexOf(')');

        if(start != -1 && end != -1) {
            return line.substring(start + 1, end);
        }
        return "";
    }

    public static double extractAveragePingTime(String[] tokens){
        double averageValue = 0;
        double sum = 0;
        int count = 0;
        for(int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token.contains("ms")){
                double timeValue = Double.parseDouble( tokens[i -1]);
                count++;
                sum += timeValue;
            }
        }
        if(count != 0) {
            averageValue = sum / count;
        }
        return averageValue;
    }


    //function for finding the avera
    // ge ping
    public static double findAveragePing(List<Double> Times, double min){
        double queueAvg = 0;
        List<Double> queuingDelays = new ArrayList<>();
        for(double delay : Times) {
            queuingDelays.add(delay - min);
        }
        double queueSum = 0;

        int count = 0;
        for(double queueDelay : queuingDelays){
            queueSum += queueDelay;
            count++;
        }
        queueAvg = queueSum/count;
        return queueAvg;
    }

    //helper to print the array list
    public static void printArray(List<Double> pingTime){
        for(double time: pingTime){
            System.out.println(time);
        }
    }
}