function calculate(){
	const type = document.getElementById("materialSelection").value;
	const thickness = document.getElementById("thickness").value;
	const length = document.getElementById("length").value;
	const width = document.getElementById("width").value;
	const resultContainer = document.getElementById("result");
	const loadingSpinner = document.getElementById("loading");
	
	resultContainer.innerText = "";
	   resultContainer.classList.remove("error");
	
	if(!thickness||!length||!width||type==""){
		alert('Please fill all fields');
		return;
	}
	
	let url = `http://localhost:7979/steel-weight-calculator/calculate-weight?type=${encodeURIComponent(type)}&length=${length}&width=${width}&thickness=${thickness}`;
	
	loadingSpinner.classList.remove("hidden");
	
	fetch( url,{
		method : "GET",
		headers : {
			"Content-Type": "application/json"
		}
	})
	.then(response => response.json())
	.then(data => {
			loadingSpinner.classList.add("hidden");

		        // Display calculated weight
		        resultContainer.innerText = `🔹 Weight: ${data.toFixed(2)} kg`;
	              
	           })
	           .catch(err => {
	               document.getElementById('result').innerText = "Error: " + err.message;
	           });
	   
}