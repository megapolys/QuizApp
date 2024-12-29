package com.example.servingwebcontent.service.quiz.impl;

import com.example.servingwebcontent.model.quiz.result.QuizResult;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResult;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.service.quiz.QuizInvokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizInvokeServiceImpl implements QuizInvokeService {

	private final QuizPersistence quizPersistence;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createNewQuizResult(Long userId, Long quizId) {
		quizPersistence.createNewQuizResult(userId, quizId);
	}

	public List<QuizResultBean> getQuizResults(long userId) {
//        final User user = userRepository.findById(userId).orElseThrow(); // нужно для актуализации данных из бд
//        final List<QuizResultBean> quizzes = new ArrayList<>();
//        for (QuizResult result : user.getResults()) {
//            final boolean inProgress = result.getTaskList().stream().anyMatch(QuizTaskResult::isComplete);
//            quizzes.add(new QuizResultBean(result.getQuiz().getName(), inProgress, result.isComplete(), result.getId(), getProgress(result), result.getCompleteDate()));
//        }
//        quizzes.sort(Comparator.comparing(QuizResultBean::completeDate, Comparator.nullsFirst(Comparator.reverseOrder())));
//        return quizzes;
		return null;
	}


	public String getProgress(QuizResult quizResult) {
//        final long countCompleted = quizResult.getTaskList().stream().filter(QuizTaskResult::isComplete).count();
//        final int taskCount = quizResult.getTaskList().size();
//        return countCompleted + "/" + taskCount;
		return null;
	}

	public void completeQuiz(QuizResult quizResult) {
//        quizResult.setComplete(true);
//        quizResult.setCompleteDate(new Date());
//        quizResultRepository.save(quizResult);
	}

	public void saveTask(QuizTaskResult task) {
//        quizResultRepository.save(task.getQuiz());
	}

	public boolean userNotContainsQuiz(Long userId, Long quizResultId) {
//        final User user = userRepository.findById(userId).orElseThrow(); // нужно для актуализации данных из бд
//        return user.getResults().stream().noneMatch(r -> r.getId().equals(quizResultId));
		return false;
	}


	public record QuizResultBean(String name, boolean inProgress, boolean complete, Long quizId, String progress,
								 Date completeDate) {
	}

}
