/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.mocktestsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

/**<Question: Import necessary libraries 


**/


public class ChatClient {
    
    private static final Logger LOG = Logger.getLogger(ChatClient.class.getName());
    
    public static void main(String[] args) {
        
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to the server.");

            try (PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 Scanner scanner = new Scanner(System.in)) {
                
                Thread thread = new Thread(() -> {
                    try {
                        
                        String serverMessage;
                        while ((serverMessage = input.readLine()) != null) {
                            System.out.println("Message from server: " + serverMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            thread.start();
                
                String message;
                while (true) {
                    System.out.println("Please enter your message! ");
                    message = scanner.nextLine();
                    output.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



