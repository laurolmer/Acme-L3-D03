
package acme.features.assistant.assistantDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tutorialSession.TutorialSession;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantDashboardRepository extends AbstractRepository {

	@Query("select a from Assistant a where a.userAccount.id = :userAccountId")
	Assistant findAssistantByUserAccountId(int userAccountId);

	@Query("select avg(ts.finishPeriod - ts.startPeriod)/3600000 from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	double findAverageTutorialSessionLength(int assistantId);

	@Query("select stddev(ts.finishPeriod - ts.startPeriod)/3600000 from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	double findDeviationTutorialSessionLength(int assistantId);

	@Query("select min(ts.finishPeriod - ts.startPeriod)/3600000 from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	double findMinimumTutorialSessionLength(int assistantId);

	@Query("select max(ts.finishPeriod - ts.startPeriod)/3600000 from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	double findMaximumTutorialSessionLength(int assistantId);

	@Query("select count(ts) from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	int findCountTutorialSession(int assistantId);

	@Query("select avg(t.estimatedTime) from Tutorial t where t.assistant.id = :assistantId")
	double findAverageTutorialLength(int assistantId);

	@Query("select stddev(t.estimatedTime) from Tutorial t where t.assistant.id = :assistantId")
	double findDeviationTutorialLength(int assistantId);

	@Query("select min(t.estimatedTime) from Tutorial t where t.assistant.id = :assistantId")
	double findMinimumTutorialLength(int assistantId);

	@Query("select max(t.estimatedTime) from Tutorial t where t.assistant.id = :assistantId")
	double findMaximumTutorialLength(int assistantId);

	@Query("select count(select t from from Tutorial t where t.course.courseType = :THEORY_COURSE) from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	int findCountTheoryTutorial(int assistantId);

	@Query("select count(select t from from Tutorial t where t.course.courseType = :HANDS-ON) from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	int findCountHandsOnTutorial(int assistantId);

	@Query("select count(t) from Tutorial t where t.assistant.id = :assistantId")
	int findCountTutorial(int assistantId);

	@Query("select count(t) from Tutorial t where t.assistant.id = :assistantId")
	Long findTotalNumberOfTutorial(int assistantId);

	@Query("select ts from TutorialSession ts where ts.tutorial.id = :id")
	Collection<TutorialSession> findSessionsByTutorialId(int id);

}
