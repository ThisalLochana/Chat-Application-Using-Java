/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.mocktestsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



/**<Question: Import necessary libraries **/





public class ChatServer {
    /**<Question: A static final Logger object is created, 
     * which will be used for logging information and errors. **/
    
    private static final Logger LOG = Logger.getLogger(ChatServer.class.getName());
    
    private static List<Socket> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            /**<Question: Create a new ServerSocket object that listens on port 5000 **/
            ServerSocket serverSocket = new ServerSocket(5000);
            /**<Question: Log an informational message indicating that 
             * the server has started and is waiting for clients **/
            LOG.info("Server started on port \"5000\". Waiting for clients");

            while (true) {
                
                /**<Question: Accept a new client connection and create a Socket object for communication with the client.        **/
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                /**<Question: Log an informational message indicating that a client has connected with showing IP address         **/
                LOG.info("Client connected." + clientSocket.getInetAddress());

                /**<Question: Create a new Thread object for each client. 
                 * The ClientHandler class implements the Runnable interface, so it can be passed to the Thread constructor.
                 * Start the thread
                 **/
                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }
        /**< Question: Catch input output exception and log the error message with severe level **/    
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error occured while running the server.",e);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        
        
        
        
        /**<Question:
        * Create a run method which is void.
            *  Create a try-catch block to handle exceptions.
                * Create a BufferedReader object to read input from the client.
                * Create a PrintWriter object to send output to the client.
                * Declare a String variable to hold the incoming message.
                * A loop that continues as long as there is input from the client.
                    * Log the received message as info containing IP address of the client and incommingMessage
                    * Broadcast the message to all connected clients using PrintWriter object
            
            * Catch any IOExceptions that may occur and log the error message.
            * In a finally block create a try-catch block, attempt to close the client socket in side the try block and 
            * log a message indicating that the client has disconnected.
            * Catch IOExcpetion and log the error in severe level
            
   
        **/
        @Override
        public void run(){
            try(
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(),true)){
                
                String message;
                while((message = input.readLine())!= null){
                    LOG.log(Level.INFO,"Received from " + clientSocket.getInetAddress()+ " : " + message);
                    // Broadcast the message to all connected clients
                    // This is just a placeholder, actual implementation depends on requirements
                    broadcastMessage(message);
                    
                }
                
                input.close();
                output.close();
                
            }catch(IOException e){
                LOG.log(Level.SEVERE,"Error occurred while closing client socket.",e);
            }finally{
                try{
                    clientSocket.close();
                    LOG.log(Level.INFO,"Client Disconnected." + clientSocket.getInetAddress());
                }catch(IOException e){
                    LOG.log(Level.SEVERE, "Error occured while running",e);
                }
            }
        }
    }
    
    private static void broadcastMessage(String message){
        for(Socket client : clients){
            try{
                PrintWriter output = new PrintWriter(client.getOutputStream(), true);
                output.println("Message from server: " + message);
            }catch(IOException e){
                LOG.log(Level.SEVERE,"Error occured while running" , e);
            }
        }
    }
}
