// Get the modal
var modalA = document.getElementById('myModalAbs');

// Get the button that opens the modal
var btnA = document.getElementById("abstractUploadButton");

// Get the <span> element that closes the modal
var spanA = document.getElementsByClassName("closeAbs")[0];

// When the user clicks the button, open the modal 
btnA.onclick = function() {
    modalA.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
spanA.onclick = function() {
    modalA.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modalA) {
        modalA.style.display = "none";
    }
}