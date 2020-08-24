package com.example.quizzapp

object Constants{

    const val USER_NAME: String = "user_name"
    const val TOTAL_QUESTIONS: String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"

    fun getQuestions(): ArrayList<Question>{
        val questionsList = ArrayList<Question>()

        // 1
        val que1 = Question(
            1,
            "¿A qué país le pertenece esta bandera?",
            R.drawable.ic_flag_of_argentina,
            "Argentina",
            "Austria",
            "Australia",
            "Armenia",
            1
        )
        questionsList.add(que1)

        // 2
        val que2 = Question(
            2,
            "¿A qué país le pertenece esta bandera?",
            R.drawable.ic_flag_of_australia,
            "Angola",
            "Austria",
            "Australia",
            "Armenia",
            3
        )
        questionsList.add(que2)

        // 3
        val que3 = Question(
            3,
            "¿A qué país le pertenece esta bandera?",
            R.drawable.ic_flag_of_brazil,
            "Belarus",
            "Belize",
            "Brunei",
            "Brazil",
            4
        )
        questionsList.add(que3)

        // 4
        val que4 = Question(
            4,
            "¿A qué país le pertenece esta bandera?",
            R.drawable.ic_flag_of_belgium,
            "Bahamas",
            "Bélgica",
            "Barbados",
            "Belice",
            2
        )
        questionsList.add(que4)

        // 5
        val que5 = Question(
            5,
            "¿A qué país le pertenece esta bandera?",
            R.drawable.ic_flag_of_fiji,
            "Gabón",
            "Francia",
            "Fiji",
            "Finlandia",
            3
        )
        questionsList.add(que5)

        // 6
        val que6 = Question(
            6,
            "¿A qué país le pertenece esta bandera?",
            R.drawable.ic_flag_of_germany,
            "Alemania",
            "Rusia",
            "Grecia",
            "España",
            1
        )
        questionsList.add(que6)

        // 7
        val que7 = Question(
            7,
            "¿A qué país le pertenece esta bandera?",
            R.drawable.ic_flag_of_denmark,
            "Dominica",
            "Egipto",
            "Dinamarca",
            "Etiopía",
            3
        )
        questionsList.add(que7)

        // 8
        val que8 = Question(
            8,
            "¿A qué país le pertenece esta bandera?",
            R.drawable.ic_flag_of_india,
            "Irlanda",
            "Iran",
            "Hungría",
            "India",
            4
        )
        questionsList.add(que8)

        // 9
        val que9 = Question(
            9,
            "¿A qué país le pertenece esta bandera?",
            R.drawable.ic_flag_of_new_zealand,
            "Australia",
            "Nueva Zelanda",
            "Tuvalu",
            "Estados Unidos de América",
            2
        )
        questionsList.add(que9)

        // 10
        val que10 = Question(
            10,
            "¿A qué país le pertenece esta bandera?",
            R.drawable.ic_flag_of_kuwait,
            "Kuwait",
            "Jordania",
            "Sudán",
            "Palestina",
            1
        )
        questionsList.add(que10)

        return questionsList
    }

}