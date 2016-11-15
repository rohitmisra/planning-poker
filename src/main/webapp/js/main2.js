function voteAndWait(){
		console.log('reached here!');
		document.getElementById("waitingMsg").innerHTML='waiting...';
		$.ajax({
				url : 'http://localhost:8080/vote.json?user='
					+ document.getElementById('user').value + '&vote='
					+ document.getElementById('vote').value + '&time=0',
				cache : false,
				type: 'GET',
				success : function(resp) {
					console.log(resp);
					if(resp.resultcode=="C"){//Conflict
						document.getElementById("errorMsg").innerHTML=resp.result;
						var resultString="";
						for(var vote in resp.votes){
							if(resultString!="")resultString+=", ";
							resultString += vote.user +" : "+ vote.vote;
						}
							
						document.getElementById("waitingMsg").innerHTML=resultString.message;
					}else if(resp.resultcode=="S"){
						document.getElementById("waitingMsg").innerHTML=resp.average;
					}
					
				},
				error : function(xhr, status){
					document.getElementById("waitingMsg").innerHTML='';
					document.getElementById("errorMsg").innerHTML='Error: ' + xhr.responseJSON.message;
				}
			});
		console.log('done!');
	}


function register(){
	$.ajax({
			url : 'http://localhost:8080/create.json?user='
				+ document.getElementById('user').value,
			cache : false,
			type: 'GET',
			success : function(resp) {
				console.log(resp);
				$("#voteBtn").toggle();
				$("#registerBtn").toggle();
				$("#user").toggle();
			},
			error : function(xhr, status){
				document.getElementById("waitingMsg").innerHTML='';
				document.getElementById("errorMsg").innerHTML='Error: ' + xhr.responseJSON.message;
			}
		});
	console.log('done!');
}
