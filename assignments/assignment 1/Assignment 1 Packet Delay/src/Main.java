import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filePath = "/Users/aidenpratt/Documents/Documents - Aiden’s MacBook Pro/AP-6014/assignments/assignment 1/traceroute_output.txt";
        String outputPath = "/Users/aidenpratt/Documents/Documents - Aiden’s MacBook Pro/AP-6014/assignments/assignment 1/traceroute_parse.txt";

        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputPath));

            Map<String, Double> delays = new HashMap<>();
            String line;

            while((line = fileReader.readLine()) != null){
                String[] tokens = line.split("\\s+");

            if (tokens.length >= 8 && tokens[0].matches("\\d+") && tokens[4].equals("ms")) {

                try {
                    String ipAddress = tokens[2].replace("(", "").replace(")", "");
                    double delay1 = Double.parseDouble(tokens[5]);
                    double delay2 = Double.parseDouble(tokens[8]);
                    double delay3 = Double.parseDouble(tokens[11]);

                    double averageDelay = (delay1 + delay2 + delay3) / 3.0;
                    delays.put(ipAddress, averageDelay);
                }
                catch (NumberFormatException e){
                    System.err.println("Invalid delay format " + line);
                }
            }
        }

        for (Map.Entry<String, Double> entry : delays.entrySet()) {
            fileWriter.write(entry.getKey() + " " + entry.getValue() + " ms");
            fileWriter.newLine();
        }

        fileReader.close();
        fileWriter.close();

        System.out.println("Average delays written to " + outputPath);


    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}