import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        System.out.println("Server är nu Redo");

        //Init stuff
        ServerSocket serverSocket;
        Socket socket;
        InputStreamReader inputSR;
        OutputStreamWriter outputSW;
        BufferedReader bReader;
        BufferedWriter bWriter;

        //Starta Servern

        try {
            //Kontrollera att Socket nummer är ledig. Avbryt om socket är upptagen
            serverSocket = new ServerSocket(4321);
        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        try {
            //Väntar på specifik socket efter trafik
            socket = serverSocket.accept();

            //Initiera Reader och Writer och koppla dem till socket
            inputSR = new InputStreamReader(socket.getInputStream());
            outputSW = new OutputStreamWriter(socket.getOutputStream());

            bReader = new BufferedReader(inputSR);
            bWriter = new BufferedWriter(outputSW);

            while (true) {
                //Hämta och skriv ut klientens meddelande
                String message = bReader.readLine();
                System.out.println("Client: " + message);

                //Skicka acknoledgement svar tillbaka
                bWriter.write("Message Received");
                bWriter.newLine();
                bWriter.flush();

                //Avsluta om QUIT
                if (message.equalsIgnoreCase("quit")) break;
            }
            //Stäng kopplingar
            socket.close();
            inputSR.close();
            outputSW.close();
            bReader.close();
            bWriter.close();

        } catch (IOException e) {
            System.out.println(e);
        } finally {
            System.out.println("Server Avslutas");
        }
    }

    static String openUpData(String message) throws ParseException, IOException {
        //steg 1. bygg upp json objekt basserat på inkommande string
        JSONParser parser = new JSONParser();
        JSONObject jsonOb = (JSONObject) parser.parse(message);

        // Steg 2. läs av url och HTTP-metod för att veta vad clienten vill
        String url = jsonOb.get("httpURL").toString();
        String method = jsonOb.get("httpMethod").toString();

        // Steg 2.5. Dela upp URL med .split metod
        String[] urls = url.split("/");

        // Steg 3. Använd switch case för att kolla vilken data som ska användas
        switch (urls[0]){
            case "persons":{
                if(method == "GET"){
                    // Vill hämta data om personer
                    // TODO lägg till logik om det är en specifik person som ska hämtas

                    // Hämta data från json fil
                    JSONObject jsonReturn = (JSONObject) parser.parse(new FileReader("data/data.json"));

                    // Returnera JSON String
                    return jsonReturn.toJSONString();
                }
                break;
            }
        }



        return "message received";
    }
}