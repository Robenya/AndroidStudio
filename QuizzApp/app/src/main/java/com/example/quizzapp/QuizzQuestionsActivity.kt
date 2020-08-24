package com.example.quizzapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quizz_questions.*

class QuizzQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition : Int = 1
    private var mQuestionsList : ArrayList<Question>? = null
    private var mSelectedOptionPosition : Int = 0
    private var mCorrectScore: Int = 0
    private var mUserName: String? = null

    private var submitPressed: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)
        mQuestionsList = Constants.getQuestions()
        setQuestion()

        tv_option1.setOnClickListener(this)
        tv_option2.setOnClickListener(this)
        tv_option3.setOnClickListener(this)
        tv_option4.setOnClickListener(this)
        btn_submit.setOnClickListener(this)

    }

    private fun setQuestion(){

        submitPressed = false
        val question: Question = mQuestionsList!![mCurrentPosition - 1]

        defaultOptionsView()

        if(mCurrentPosition == mQuestionsList!!.size){
            btn_submit.text = "FINALIZAR"
        }else{
            btn_submit.text = "CONFIRMAR"
        }

        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition / ${progressBar.max}"


        tv_question.text = question.question
        iv_image.setImageResource(question.image)
        tv_option1.text = question.option1
        tv_option2.text = question.option2
        tv_option3.text = question.option3
        tv_option4.text = question.option4
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        options.add(0, tv_option1)
        options.add(0, tv_option2)
        options.add(0, tv_option3)
        options.add(0, tv_option4)

        for (option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_option1 -> {
                if(!submitPressed)
                selectedOptionView(tv_option1, 1)
            }
            R.id.tv_option2 -> {
                if(!submitPressed)
                selectedOptionView(tv_option2, 2)
            }
            R.id.tv_option3 -> {
                if(!submitPressed)
                selectedOptionView(tv_option3, 3)
            }
            R.id.tv_option4 -> {
                if(!submitPressed)
                selectedOptionView(tv_option4, 4)
            }

            R.id.btn_submit ->{
                if (mSelectedOptionPosition == 0){
                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectScore)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }else{
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectScore++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionsList!!.size) {
                        btn_submit.text = "FINALIZAR"
                    }else{
                        btn_submit.text = "PRÓXIMA PREGUNTA"
                        submitPressed = true
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1 -> {
                tv_option1.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                tv_option2.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                tv_option3.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                tv_option4.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#000000"))
        tv.setTypeface(tv.typeface, Typeface.BOLD )
        tv.background = ContextCompat.getDrawable(
            this, R.drawable.selected_option_border_bg)
    }

}