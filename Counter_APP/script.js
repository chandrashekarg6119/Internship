let count = 0;
const countElement = document.getElementById("count");

function updateColor() {
  if (count > 0) {
    countElement.style.color = "green";
  } else if (count < 0) {
    countElement.style.color = "red";
  } else {
    countElement.style.color = "black";
  }
}

function increase() {
  count++;
  countElement.innerText = count;
  updateColor();
}

function decrease() {
  count--;
  countElement.innerText = count;
  updateColor();
}

function reset() {
  count = 0;
  countElement.innerText = count;
  updateColor();
}
