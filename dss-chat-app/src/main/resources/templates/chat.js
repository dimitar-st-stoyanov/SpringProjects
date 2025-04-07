/**
 * 
 */

//Enable/disable send button based on connection status
function setConnected(connected){
	document.getElementById("sendMessage").disabled = !connected;
}

function connect(){
	const socket = new SockJS('/chat'); //Creates a new SockJS socket connection to /chat (server endpoint).
	stompClient = Stomp.over(socket); // Wraps the socket with STOMP protocol for easier messaging.
	stompClient.connect({}, function (frame){ /*	Connects to the server. The {} is an empty header object.
													Once connected, the function (frame) callback is executed.*/
		setConnected(true); //Enables the send button since we're now connected.
		stompClient.subscribe("/topic/messages", function(message){ //Subscribes to the /topic/messages destination.
			showMessage(JSON.parse(message.body));//display the messages
		});
	});
}

function showMessage(message){
	const chat = document.getElementById("chat"); //Get main div chat container
	const messageElement = document.createElement('div'); // Create new div for the message
	messageElement.textContent = message.sender + " : " + message.content; // Set message text
	messageElement.class = "border-bottom- mb-1"; // Bootstrap class
	chat.appendChild(messageElement); //Add to the main div
	chat.scrollTop = chat.scrollHeight; // Scroll to the latest message
	
}

function sendMessage(message){
	const sender = document.getElementById("senderInput").value; //Get sender name
	const content = document.getElementById("messageInput").value;// Get message input
	
	const chatMessage = {
		sender : sender,
		content : content
	}// Message object
	
	stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage)); // Sends the message to the server at /app/sendMessage
	
	document.getElementById("messageInput").value = ""; // Clear message input field after sending
}

		document.getElementById('sendMessage').onclick = sendMessage; // When the send button is clicked, call sendMessage()
		window.onload = connect; // As soon as the page finishes loading, call connect() to start the WebSocket connection
