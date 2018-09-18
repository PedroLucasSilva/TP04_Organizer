package br.cefetmg.inf.organizer.dist;

import br.cefetmg.inf.util.RequestType;
import br.cefetmg.inf.util.PackageShredder;
import br.cefetmg.inf.util.PseudoPackage;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ClientDistribution {

    private static ClientDistribution onlyInstance;

    private DatagramSocket clientSocket;
    private String servidor;
    private int porta;
    private InetAddress IPAddress;

    private ClientDistribution() throws SocketException, UnknownHostException {

        clientSocket = new DatagramSocket();
        servidor = "localhost";
        porta = 22388;
        IPAddress = InetAddress.getByName(servidor);
    }

    public static ClientDistribution getInstance() throws SocketException, UnknownHostException {
        if (onlyInstance == null) {
            onlyInstance = new ClientDistribution();
        }

        return onlyInstance;
    }

    public PseudoPackage request(PseudoPackage pseudoPackage) throws IOException {
        JsonReader reader;
        final int BYTE_LENGTH = 1024;

        byte[][] sendData;
        byte[][] numPackage;
        byte[] receiveData = new byte[BYTE_LENGTH];
        final Charset UTF8_CHARSET = Charset.forName("UTF-8");
        Gson gson = new Gson();

        //transforma o pseudoPackage com os dados a serem enviados em pacotes de byte
        String jsonObject = gson.toJson(pseudoPackage);
        PackageShredder packageShredder = new PackageShredder();

        sendData = packageShredder.fragment(jsonObject);

        //cria um pseudoPackage contendo o numero de pacotes de dados que serao enviados
        PseudoPackage contentPackage;
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(String.valueOf(sendData.length));

        //transforma o pseudoPackage que contem o numero de pacotes em matriz de bytes
        RequestType requestType = RequestType.NUMPACKAGE;
        contentPackage = new PseudoPackage(requestType, jsonContent);
        numPackage = packageShredder.fragment(gson.toJson(contentPackage));

        //envia o numero de pacotes para o servidor
        for (byte[] numPackage1 : numPackage) {
            DatagramPacket sendPacketLenght = new DatagramPacket(numPackage1,
                    numPackage1.length, IPAddress, porta);
            clientSocket.send(sendPacketLenght);
        }

        //recebe um boolean do servidor confirmando se o numero de pacotes chegou com sucesso
        DatagramPacket receiveConfirmation = new DatagramPacket(receiveData,
                receiveData.length);

        clientSocket.receive(receiveConfirmation);

        String receivedPackage = new String(receiveConfirmation.getData(), UTF8_CHARSET);
        reader = new JsonReader(new StringReader(receivedPackage));
        reader.setLenient(true);
        PseudoPackage confirmationPackage = gson.fromJson(reader, PseudoPackage.class);
        reader.close();

        //se o numero de pacotes tiver chegado com sucesso
        if (Boolean.valueOf(confirmationPackage.getContent().get(0))
                && confirmationPackage.getRequestType().equals(RequestType.RESPONSEPACKAGE)) {

            //percorre a matriz de bytes e envia os vetores de bytes contendo 
            //os dados ao servidor
            for (byte[] sendData1 : sendData) {
                DatagramPacket sendPackage = new DatagramPacket(sendData1,
                        sendData1.length, IPAddress, porta);
                clientSocket.send(sendPackage);
            }
        }

        //recebe do servidor o numero de pacotes que serao enviados como resposta
        DatagramPacket receivedFromServerLength = new DatagramPacket(receiveData,
                receiveData.length);

        clientSocket.receive(receivedFromServerLength);

        String strReceivedFromServerLength = new String(receivedFromServerLength.getData(), UTF8_CHARSET);
        reader = new JsonReader(new StringReader(strReceivedFromServerLength));
        reader.setLenient(true);
        PseudoPackage receivedFromServerLengthPackage = gson.fromJson(reader, PseudoPackage.class);
        reader.close();

        ArrayList<String> errorList = new ArrayList<String>();
        errorList.add("false");
        
        PseudoPackage returnPackage = new PseudoPackage(RequestType.RESPONSEPACKAGE , errorList);
        if (receivedFromServerLengthPackage.getRequestType().equals(RequestType.NUMPACKAGE)) {

            //transforma o numero de pacotes de resposta em int
            int numPackages = Integer.parseInt(receivedFromServerLengthPackage.getContent().get(0));
            //matriz de byte que recebera os pacotes de resposta
            byte[][] fragmentedPackage = new byte[numPackages][BYTE_LENGTH];

            //recebe os pacotes e os armazena na matriz de byte
            for (int i = 0; i < numPackages; i++) {
                DatagramPacket receivedFromServer = new DatagramPacket(receiveData,
                        receiveData.length);

                clientSocket.receive(receivedFromServer);
                byte[] aux = receivedFromServer.getData(    );

                System.arraycopy(aux, 0, fragmentedPackage[i], 0, aux.length);
            }

            //desfragmenta, ordena os pacotes recebidos e os converte novamente para
            //um pseudoPackage
            String receivedObject = packageShredder.defragment(fragmentedPackage);
            reader = new JsonReader(new StringReader(receivedObject));
            reader.setLenient(true);
            returnPackage = gson.fromJson(reader, PseudoPackage.class);
            reader.close();
        }
        return returnPackage;
    }
}


