import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class DNSServer {

    public static DNSCache serverCache_ = new DNSCache();
    public static ArrayList<DNSRecord> dnsRecordAnswer_;

    public static void main(String[] args) {
        try {
            int server_port = 8053;
            System.out.println("Listening at " + server_port + "\n");

            //MAIN SOCKET? TODO
            DatagramSocket socket = new DatagramSocket(server_port);
            //arraylist of answers records
            while(true) {
                byte[] data = new byte[512];
                DatagramPacket pkt = new DatagramPacket(data, data.length);
                //TODO keep track of how many bytes received in the buffer

                socket.receive(pkt);
                int packtL = pkt.getLength();

//                int newBufferSize = socket.getReceiveBufferSize();
//                System.out.println("this is the new buffer size " + newBufferSize);


                System.out.println("received from: " + pkt.getAddress() + "\n");
                //message tp be decoded from the packet
                DNSMessage dnsMessage = DNSMessage.decodeMessage(pkt.getData());

                //adding the query to an arraylist of questions
                ArrayList<DNSQuestion> questions = dnsMessage.getQuestionsArray();


                System.out.println("checking if question exists in cache \n");

                //checking if the question is already in my cache
                DNSRecord cacheRecord = serverCache_.queryCache(questions.get(0));

                    //if the record is not expired
                    if(cacheRecord != null){
                        System.out.println("question is in cache!\n");
                        dnsRecordAnswer_ = new ArrayList<>();
                        dnsRecordAnswer_.add(cacheRecord);
                    }

                    else{  //when the record is not in the cache
                        System.out.println("not found in cache!\n");
                        //setting the IP address of google for asking google
                        InetAddress googleDNSIP = InetAddress.getByName("8.8.8.8");

                        DatagramPacket googlePkt = new DatagramPacket(data, packtL, googleDNSIP, 53);
                        socket.send(googlePkt);

                        //TODO parse thise presonse
                        //receive from google
                        byte[] googleResponseArray = new byte[512];
                        DatagramPacket googleResponsePacket = new DatagramPacket(googleResponseArray, googleResponseArray.length);

                        int googleLength = googleResponseArray.length;

                        socket.receive(googleResponsePacket);
                        System.out.println("receiving packet from google:" + googlePkt.getAddress() + "\n");

                        DNSMessage googleResponse = DNSMessage.decodeMessage(googleResponsePacket.getData());
                        dnsRecordAnswer_ = googleResponse.getAnswersArray();

                        DNSMessage responseMessage = DNSMessage.buildResponse(dnsMessage, dnsRecordAnswer_);

                        //forming own answer for from the cache
                        byte[] responseBytes = responseMessage.toBytes();

                        //getting the packet address and port of the original sender to send it back
                        DatagramPacket finalSendBackPacket =
                                new DatagramPacket(googleResponseArray, googleLength, pkt.getAddress(), pkt.getPort());

                        //adding to the cache
                        serverCache_.addToCache(questions.get(0), dnsRecordAnswer_.get(0));

                        //sending
                        socket.send(finalSendBackPacket);
                    }
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
