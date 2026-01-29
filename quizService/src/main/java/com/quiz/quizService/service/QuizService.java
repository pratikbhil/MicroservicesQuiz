package com.quiz.quizService.service;


import com.quiz.quizService.Feign.QuizInterface;
import com.quiz.quizService.dao.QuizDao;
import com.quiz.quizService.model.QuestionWrapper;
import com.quiz.quizService.model.Quiz;
import com.quiz.quizService.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.attribute.IntegerSyntax;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;
    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questions = quizInterface.getQuiestionsForQuiz(category, numQ).getBody();
        Quiz quiz= new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);


        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {

        Quiz quiz= quizDao.findById(id).get();
        List<Integer> questionIds= quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questionsForUser= quizInterface.getQuestionsFromIds(questionIds);
        return questionsForUser;
        //return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        ResponseEntity<Integer> score= quizInterface.getScore(responses);
        return score;

    }
}
