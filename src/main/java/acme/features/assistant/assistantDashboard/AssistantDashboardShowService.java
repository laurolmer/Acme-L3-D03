
package acme.features.assistant.assistantDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.form.AssistantsDashboard;
import acme.form.Statistic;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantDashboardShowService extends AbstractService<Assistant, AssistantsDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AssistantDashboardRepository repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		final Assistant assistant;
		Principal principal;
		int userAccountId;
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		assistant = this.repository.findAssistantByUserAccountId(userAccountId);
		status = assistant != null && principal.hasRole(Assistant.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final int assistantId;
		final AssistantsDashboard assistantDashboard;
		final Principal principal;
		int userAccountId;
		final Assistant assistant;

		final Statistic sessionLength;
		final double averageSessionLength;
		final double deviationSessionLength;
		final double minimumSessionLength;
		final double maximumSessionLength;
		int countSession;

		final Statistic tutorialLength;
		final double averageTutorialLength;
		final double deviationTutorialLength;
		final double minimumTutorialLength;
		final double maximumTutorialLength;
		final int countTutorial;

		final int totalNumberOfTheoryTutorial;
		final int totalNumOfHandsOnTutorials;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		assistant = this.repository.findAssistantByUserAccountId(userAccountId);
		assistantId = assistant.getId();

		averageSessionLength = this.repository.findAverageTutorialSessionLength(assistantId);
		deviationSessionLength = this.repository.findDeviationTutorialSessionLength(assistantId);
		minimumSessionLength = this.repository.findMinimumTutorialSessionLength(assistantId);
		maximumSessionLength = this.repository.findMaximumTutorialSessionLength(assistantId);
		countSession = this.repository.findCountTutorialSession(assistantId);
		sessionLength = new Statistic(countSession, averageSessionLength, maximumSessionLength, minimumSessionLength, deviationSessionLength);

		averageTutorialLength = this.repository.findAverageTutorialLength(assistantId);
		deviationTutorialLength = this.repository.findDeviationTutorialLength(assistantId);
		minimumTutorialLength = this.repository.findMinimumTutorialLength(assistantId);
		maximumTutorialLength = this.repository.findMaximumTutorialLength(assistantId);
		countTutorial = this.repository.findCountTutorial(assistantId);
		tutorialLength = new Statistic(countTutorial, averageTutorialLength, maximumTutorialLength, minimumTutorialLength, deviationTutorialLength);

		totalNumberOfTheoryTutorial = this.repository.findCountTheoryTutorial(assistantId);
		totalNumOfHandsOnTutorials = this.repository.findCountHandsOnTutorial(assistantId);

		assistantDashboard = new AssistantsDashboard();
		assistantDashboard.setTotalNumTheoryTutorials(totalNumberOfTheoryTutorial);
		assistantDashboard.setTotalNumHandsOnTutorials(totalNumOfHandsOnTutorials);
		assistantDashboard.setSessionTime(sessionLength);
		assistantDashboard.setTutorialTime(tutorialLength);
		super.getBuffer().setData(assistantDashboard);
	}

	@Override
	public void unbind(final AssistantsDashboard assistantDashboard) {
		Tuple tuple;
		tuple = super.unbind(assistantDashboard, "totalNumTheoryTutorials", "sessionTime", "tutorialTime");
		super.getResponse().setData(tuple);
	}
}
