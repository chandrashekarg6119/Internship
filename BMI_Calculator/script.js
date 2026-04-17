function calculateBMI() {
    let height = document.getElementById("height").value / 100;
    let weight = document.getElementById("weight").value;

    if (height === "" || weight === "") {
        document.getElementById("result").innerText = "Please enter values";
        return;
    }

    let bmi = (weight / (height * height)).toFixed(2);
    let category = "";

    if (bmi < 18.5) category = "Underweight";
    else if (bmi < 24.9) category = "Normal";
    else if (bmi < 29.9) category = "Overweight";
    else category = "Obese";

    document.getElementById("result").innerText = "BMI: " + bmi + " (" + category + ")";
}
