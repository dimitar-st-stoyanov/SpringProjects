/**
 * 
 */

function getGreeting(){
	let name = document.getElementById('nameInput').value;
	let age = Math.trunc(document.getElementById('ageInput').value);
	let town = document.getElementById('townInput').value;
	
	if(age<0||isNaN(age)){
		document.getElementById('response').innerText="Please enter valid age!"
		return;
	}
	
	fetch(`http://localhost:8080/api/hello?name=${name}&age=${age}&town=${town}`)
	.then (response =>response.text())
	.then (data =>{
		document.getElementById('response').innerText=data;
	})
	.catch(err => {
	        document.getElementById('response').innerText = "Error: " + err.message; 
	    });
	
	
	
	
}