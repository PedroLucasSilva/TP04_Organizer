package br.cefetmg.inf.organizer.dist;

import br.cefetmg.inf.organizer.adapter.ServiceAdapterThread;
import br.cefetmg.inf.util.PackageShredder;
import br.cefetmg.inf.util.RequestType;
import br.cefetmg.inf.util.PseudoPackage;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ServerDistribution {

    private static DatagramSocket serverSocket;
    private static final int port = 22388;
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    public static void main(String args[]) throws SocketException, IOException, PersistenceException, BusinessException {
        serverSocket = new DatagramSocket(port);
        while (true) {
            receiveData();
        }
    }

    private static synchronized void receiveData() throws IOException, PersistenceException, BusinessException {
        JsonReader reader;
        final int BYTE_LENGTH = 1024;
        byte[] receiveData = new byte[BYTE_LENGTH];
        byte[][] sendData;
        Gson gson = new Gson();
        PackageShredder packageShredder = new PackageShredder();

        //espera ate que algum cliente envie uma requisicao
        DatagramPacket receivePacket = new DatagramPacket(receiveData,
                receiveData.length);
        serverSocket.receive(receivePacket);

        InetAddress IPAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();

        //transforma o pacote recebido em um pseudoPackage contendo o numero
        //de pacotes que serao enviados pelo cliente posteriormente
        String receivedLength = new String(receivePacket.getData(), UTF8_CHARSET);
        reader = new JsonReader(new StringReader(receivedLength));
        reader.setLenient(true);
        PseudoPackage lengthPackage = gson.fromJson(reader, PseudoPackage.class);
        reader.close();
        System.out.println("Recebeu número de pacotes de entrada");
        //se o tipo de requisicao recebida for o numero de pacotes
        if (lengthPackage.getRequestType().equals(RequestType.NUMPACKAGE)) {
            //transforma o numero de pacotes em int
            int numPackages = Integer.parseInt(lengthPackage.getContent().get(0));

            PseudoPackage confirmationPackage;
            List<String> jsonContent;
            jsonContent = new ArrayList();
            jsonContent.add("true");

            //se tiver recebido corretamente o numero de pacotes, envia ao
            //cliente um booleano confirmando esta acao
            confirmationPackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);
            sendData = packageShredder.fragment(gson.toJson(confirmationPackage));

            DatagramPacket sendPacketConfirmation = new DatagramPacket(sendData[0],
                    sendData[0].length, IPAddress, clientPort);

            serverSocket.send(sendPacketConfirmation);
            
            System.out.println("Enviou confirmação p/ número de pacotes de entrada");
            //matriz de byte que recebera os pacotes com os dados
            byte[][] fragmentedPackage = new byte[numPackages][BYTE_LENGTH];

            //recebe os pacotes enviados pelo cliente contendo os dados
            for (int i = 0; i < numPackages; i++) {
                DatagramPacket receivedFromClient = new DatagramPacket(receiveData,
                        receiveData.length);

                serverSocket.receive(receivedFromClient);
                fragmentedPackage[i] = receivedFromClient.getData();
            }
            System.out.println("Recebeu pacotes de entrada");
            //desfragmenta, ordena os pacotes recebidos e os converte novamente para
            //um pseudoPackage
            String receivedObject = packageShredder.defragment(fragmentedPackage);
            reader = new JsonReader(new StringReader(receivedObject));
            reader.setLenient(true);
            PseudoPackage returnPackage = gson.fromJson(reader, PseudoPackage.class);
            reader.close();
            
            //cria uma Thread para tratar a requisicao do cliente
            ServiceAdapterThread adapter = new ServiceAdapterThread(IPAddress, clientPort,port, returnPackage);
            adapter.evaluateRequest();
            Thread adapterThread = new Thread(adapter);
            System.out.println("Enviou para a Thread");
            adapterThread.start();
        } else {
            //caso o tipo da requisicao recebida nao seja o numero de pacotes,
            //envia ao cliente uma confirmacao de erro
            System.out.println("Não recebeu num de pacotes");
            PseudoPackage confirmationPackage;
            List<String> jsonContent;
            jsonContent = new ArrayList();
            jsonContent.add("false");

            confirmationPackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);
            sendData = packageShredder.fragment(gson.toJson(confirmationPackage));

            DatagramPacket sendPacketConfirmation = new DatagramPacket(sendData[0],
                    sendData[0].length, receivePacket.getAddress(), receivePacket.getPort());

            serverSocket.send(sendPacketConfirmation);

            return;
        }
    }

    public static synchronized void sendData(InetAddress IPAddress, int clientPort, PseudoPackage responsePackage) throws IOException {
        byte[][] sendData;
        PackageShredder packageShredder = new PackageShredder();
        Gson gson = new Gson();

        //fragmenta o pseudoPackage contendo a resposta em uma matriz de byte
        sendData = packageShredder.fragment(gson.toJson(responsePackage));

        //envia a resposta ao cliente
        if (sendData.length > 1) {
            for (byte[] sendDataAux : sendData) {
                DatagramPacket sendPacketConfirmation = new DatagramPacket(sendDataAux,
                        sendDataAux.length, IPAddress, clientPort);
                serverSocket.send(sendPacketConfirmation);
            }
        } else {
            DatagramPacket sendPacketConfirmation = new DatagramPacket(sendData[0],
                    sendData[0].length, IPAddress, clientPort);
            serverSocket.send(sendPacketConfirmation);
        }
        
    }
}
