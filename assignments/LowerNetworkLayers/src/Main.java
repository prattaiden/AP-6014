import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {

        int state_S = 0;

        Queue<DatagramPacket> dataBuffer = new LinkedList<>();

        while (true){
            switch(state_S){
                case 0:
                    DatagramPacket data = waitForData(dataBuffer);
                    state_S = 1;

                case 1:

                    state_S = 2;
                case 2:
            }
        }
    }
}