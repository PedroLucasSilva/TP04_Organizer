package br.cefetmg.inf.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class PackageShredder {

    public byte[][] fragment(String pseudoPackage) throws UnsupportedEncodingException {

        final int BYTE_LENGTH = 1024;
        byte[][] byteMatrix;
        final Charset UTF8_CHARSET = Charset.forName("UTF-8");

        if (pseudoPackage.length() > 1024) {
            int firstIndex = 0, packageIndex = 0, finalIndex = 0, headerSpace = 0;
            int totalLength = 0;
            double packages = 0;
            String auxStr = "", symbol = "&";

            packages = pseudoPackage.length() / BYTE_LENGTH;
            for (int i = 0; i < packages; i++) {
                headerSpace += (i + symbol).length();
            }

            //loop até que a quantidade de pacotes consiga encobrir a quantidade
            //de bytes somada ao espaço ocupado pelos cabeçalhos
            do {
                packages = Math.ceil((pseudoPackage.length() + headerSpace) / BYTE_LENGTH) + 1;

                headerSpace = 0;
                for (int i = 0; i < packages; i++) {
                    headerSpace += (i + symbol).length();
                }

                totalLength = pseudoPackage.length() + headerSpace;
            } while (packages * BYTE_LENGTH < totalLength);

            byteMatrix = new byte[(int) packages][BYTE_LENGTH];
            System.out.println("bytes length : " + pseudoPackage.length());
            System.out.println("bytes+header length : " + totalLength);
            System.out.println("byteMatrix length : " + byteMatrix.length);

            //divide a String recebida em pacotes de bytes e escreve um cabecalho
            //indicando o indice do pacote
            if (totalLength > BYTE_LENGTH) {
                for (int i = 1; i < totalLength; i++) {
                    if (i % BYTE_LENGTH == 0) {
                        auxStr = packageIndex + symbol;
                        finalIndex = firstIndex + BYTE_LENGTH - (packageIndex + symbol).length();
                        auxStr += pseudoPackage.substring(firstIndex, finalIndex);
                        byteMatrix[packageIndex] = auxStr.getBytes(UTF8_CHARSET);
                        firstIndex = finalIndex;
                        packageIndex++;
                    }
                }
            }
            //preenche os bytes que sobraram
            auxStr = packageIndex + symbol;
            auxStr += pseudoPackage.substring(firstIndex, pseudoPackage.length());
            byteMatrix[packageIndex] = auxStr.getBytes(UTF8_CHARSET);
        } else {
            //se apenas 1 pacote for enviado, nao divide nem coloca cabecalho
            byte[][] returnByte = new byte[1][BYTE_LENGTH];
            returnByte[0] = pseudoPackage.getBytes(UTF8_CHARSET);
            return returnByte;
        }

        return byteMatrix;
    }

    public String defragment(byte[][] byteMatrix) throws UnsupportedEncodingException {
        final Charset UTF8_CHARSET = Charset.forName("UTF-8");
        if (byteMatrix.length > 1) {
            //matriz de byte em ordem
            byte[][] byteMatrixSort = new byte[byteMatrix.length][byteMatrix[0].length];

            for (int i = 0; i < byteMatrix.length; i++) {
                String header = "";
                int headerSpace = String.valueOf(byteMatrix.length).length();
                for (int j = 0; j < headerSpace + 1; j++) {
                    char aux = (char) byteMatrix[i][j];
                    header += String.valueOf(aux);
                }

                header = header.substring(0, header.indexOf("&"));
                //e' sabido que o numero no cabecalho de cada pacote e' igual ao
                //indice deste na matriz ordenada
                byteMatrixSort[Integer.parseInt(header)] = byteMatrix[i];

            }

            //transforma a matriz de byte ordenada em uma String
            String concat = "";
            String auxStr = "";
            for (int i = 0; i < byteMatrixSort.length; i++) {
                auxStr = new String(byteMatrixSort[i], UTF8_CHARSET);
                auxStr = auxStr.substring(auxStr.indexOf("&") + 1, auxStr.length());
                concat += auxStr;
                System.out.println("byteMatrixSort[" +i +"]: " +new String(byteMatrixSort[i]));
                System.out.println("auxStr: " +auxStr);
            }
            return concat;
        }else{
            String returnStr = new String(byteMatrix[0], UTF8_CHARSET);
            return returnStr;
        }

    }

}
