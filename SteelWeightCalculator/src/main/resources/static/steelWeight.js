function calculate(){
	const type = document.getElementById("materialSelection").value;
	const thickness = document.getElementById("thickness").value;
	const length = document.getElementById("length").value;
	const width = document.getElementById("width").value;
	
	if(!thickness||!length||!width||type==""){
		alert('Please fill all fields');
		return;
	}
	
	let url = `http://localhost:7979/steel-weight-calculator/calculate-weight?type=${encodeURIComponent(type)}&length=${length}&width=${width}&thickness=${thickness}`;
		
	fetch( url,{
		method : "GET",
		headers : {
			"Content-Type": "application/json"
		}
	})
	.then(response => response.json())
	.then(data => {
	               document.getElementById('result').innerText = `Weight: ${data.toFixed(2)} kg.`;
	           })
	           .catch(err => {
	               document.getElementById('result').innerText = "Error: " + err.message;
	           });
	   
}