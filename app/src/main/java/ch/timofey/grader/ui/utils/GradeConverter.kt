package ch.timofey.grader.ui.utils

fun getAverage(grades: List<Double>, weights: List<Double>): Double{
    var gradeSum = 0.0
    var weightSum = 0.0
    for (index in 0..grades.size){
        gradeSum += grades.get(index) * weights.get(index)
        weightSum += weights.get(index)
    }
    return (gradeSum / weightSum)
}