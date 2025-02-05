package com.more9810.todo.tasks

data class Student(
    val name: String,
    val age: Int,
    val gender: String,
    val gradeLevel: Int,
    val hasFailedBefore: Boolean,
    val pastGrades: List<Int>,
) {
    class StudentBuilder {
        private var name: String = ""
        private var age: Int = 0
        private var gender: String = ""
        private var gradeLevel: Int = 0
        private var isHasFailedBefore: Boolean = false
        private var pastGrades: List<Int> = emptyList()


        fun setName(name: String) = apply { this.name = name }

        fun setAge(age: Int) = apply { this.age = age }
        fun setGender(gender: String) = apply { this.gender = gender }
        fun setGradeLevel(gradeLevel: Int) = apply { this.gradeLevel = gradeLevel }
        fun setIsHasFailedBefore(isHasFailedBefore: Boolean) =
            apply { this.isHasFailedBefore = isHasFailedBefore }

        fun setPastGrade(pastGrades: List<Int>) = apply { this.pastGrades = pastGrades }

        fun build(): Student {
            return Student(name, age, gender, gradeLevel, isHasFailedBefore, pastGrades)
        }

    }
}

fun main() {
    val student =
        Student.StudentBuilder().setName("mohamed").setAge(18).setGender("Male").setGradeLevel(3)
            .setPastGrade(listOf(50, 90, 918, 72)).build()
    println(student.toString())


}