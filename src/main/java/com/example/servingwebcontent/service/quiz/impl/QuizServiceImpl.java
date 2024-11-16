package com.example.servingwebcontent.service.quiz.impl;

import com.example.servingwebcontent.exceptions.quiz.QuizCreateException;
import com.example.servingwebcontent.model.quiz.Quiz;
import com.example.servingwebcontent.model.quiz.QuizCreateCommandDto;
import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import com.example.servingwebcontent.model.user.User;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.service.quiz.QuizService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

	private final QuizPersistence quizPersistence;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizWithTaskSize> getQuizList() {
		return quizPersistence.getQuizList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addQuiz(QuizCreateCommandDto quiz) {
		Quiz byShortName = quizPersistence.findByShortName(quiz.getShortName());
		if (byShortName != null) {
			throw QuizCreateException.alreadyExistsByShortName(quiz.getShortName());
		}
		quizPersistence.addQuiz(quiz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQuizById(Long id) {
		quizPersistence.deleteQuizById(id);
	}

	public List<QuizBean> getQuizzes(User user) {
//		final Set<com.example.servingwebcontent.model.quiz.result.QuizResult> results = user.getResults();
//		final List<QuizBean> quizList = new ArrayList<>();
//        for (Quiz quiz : quizPersistence.findAllByOrderByShortName()) {
//            boolean inProgress = false;
//            boolean exists = false;
//			for (com.example.servingwebcontent.model.quiz.result.QuizResult result : results) {
//				if (Objects.equals(quiz.getId(), result.getQuiz().getId())) {
//					exists = true;
//					if (!result.isComplete()) {
//						inProgress = true;
//					}
//				}
//			}
//            quizList.add(new QuizBean(quiz, inProgress, exists));
//        }
//        return quizList;
		return null;
	}

	@Transactional
	public void delete(QuizWithTaskSize quiz) {
//        for (QuizTask quizTask : quiz.getTaskList()) {
//            quizTaskService.delete(quizTask);
//        }
//        for (User user : userRepository.findAll()) {
//            user.getResults().removeIf(result -> Objects.equals(result.getQuiz().getId(), quiz.getId()));
//            userRepository.save(user);
//        }
//        quizResultRepository.deleteQuizResultsByQuiz(quiz);
//        quizRepository.delete(quiz);
	}

//    public int getNextTaskPosition(Quiz quiz) {
//        int max = 1;
//        final List<QuizTask> taskList = quiz.getTaskList().stream()
//                .sorted(Comparator.comparing(QuizTask::getPosition))
//                .toList();
//        for (QuizTask task : taskList) {
//            if (task.getPosition() == max) {
//                max++;
//            } else {
//                return max;
//            }
//        }
//        return max;
//    }

	public record QuizBean(QuizWithTaskSize quiz, boolean inProgress, boolean exists) {
	}

	public enum ResultType {
		SHORT_NAME_FOUND, NAME_FOUND, SUCCESS
	}

	public record QuizResult(ResultType result, QuizWithTaskSize quiz) {
	}

}
