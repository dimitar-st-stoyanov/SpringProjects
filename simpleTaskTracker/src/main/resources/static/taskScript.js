

function addTask() {
    

    let taskName = document.getElementById('taskName').value;
    let isCompleted = document.getElementById('completed').checked;
	let taskTable = document.querySelector("tbody");
	let taskId = Math.floor(Math.random()*1000);
	
	let url = `http://localhost:8080/api/tasks?id=${taskId}&name=${encodeURIComponent(taskName)}&completed=${isCompleted}`;

	    fetch(url, {
	        method: "POST",
	        headers: {
	            "Content-Type": "application/json"
	        }
	    })
		
    .then(response => response.json())
    .then(task => {
       
        let row = document.createElement('tr');
		row.setAttribute("data-task-id", taskId);
		
        row.innerHTML = `<td>${task.name}</td>
						 <td>${task.completed ? 'Completed' : 'Not Completed'}</td>
						 <td>
						     <button class="complete" onclick="completeTask(${taskId})">Complete</button>
						     <button class="delete" onclick="deleteTask(${taskId})">Delete</button>
						             </td>`
									 ;
        taskTable.appendChild(row)

        // Clear fields
        document.getElementById('taskName').value = '';
        document.getElementById('completed').checked = false;
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function deleteTask(taskId){
	let url = `http://localhost:8080/api/tasks/${taskId}`;
	
	fetch (url,{
		method : "DELETE",
		headers : {
			"Content-Type" : "application/json"
		}	
	})
	
	.then(()=>{
		document.querySelector(`tr[data-task-id='${taskId}']`).remove();
	})
	.catch(error => console.error("Error:",error));
}

function completeTask(taskId){
	
	let url = `http://localhost:8080/api/tasks?id=${taskId}`;
	
	fetch (url, {
		method : "PUT",
		headers : {
			"Content-Type" : "application/json"
		}
	})
	.then(response => {
	        if (!response.ok) {
	            throw new Error("Task not found or update failed");
	        }
	        return response.json();
	    })
	.then (task=>{
		let row = document.querySelector(`tr[data-task-id='${taskId}']`);
		        if (row) {
		            row.innerHTML = `
		                <td>${task.name}</td>
		                <td>${task.completed ? 'Completed' : 'Not Completed'}</td>
		                <td>
		                    <button class="delete" onclick="deleteTask(${taskId})">Delete</button>
		                </td>`;
		            row.setAttribute("data-task-id", taskId);
		        }
		    })
		    .catch(error => console.error("Error:", error));
		}