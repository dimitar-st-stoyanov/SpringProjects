package com.example.dss_chat_app.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.dss_chat_app.model.ChatMessage;

@Controller
public class ChatController {

	@MessageMapping("/sendMessage") //send from
	@SendTo("/topic/messages") //presented to
	public ChatMessage sendMessage(ChatMessage message) {
		return message;
	}
	
	@GetMapping("chat")
	public String chat() {
		return "chat";
	}
	
	
}
