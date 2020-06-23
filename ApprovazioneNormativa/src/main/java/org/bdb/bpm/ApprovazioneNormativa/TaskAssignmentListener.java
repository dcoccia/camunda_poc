package org.bdb.bpm.ApprovazioneNormativa;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.task.IdentityLink;

public class TaskAssignmentListener implements TaskListener {

	// TODO: Set Mail Server Properties
	private static final String HOST = "mail.example.org";
	private static final String USER = "admin@example.org";
	private static final String PWD = "toomanysecrets";

	private final static Logger LOGGER = Logger.getLogger(TaskAssignmentListener.class.getName());

	public void notify(DelegateTask delegateTask) {
		
		LOGGER.info("Start TaskAssignmentListener");
		
		Set<IdentityLink> assignee = delegateTask.getCandidates();
		Iterator<IdentityLink> identityLinks = assignee.iterator();
		while (identityLinks.hasNext()) {
			IdentityLink link = identityLinks.next();

			String taskId = delegateTask.getId();

			if (assignee != null) {

				// Get User Profile from User Management
				IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();

				LOGGER.info("Retrive group " + link.getGroupId());

				Group group = identityService.createGroupQuery().groupId(link.getGroupId()).singleResult();

				LOGGER.info("Retrive users from group name " + group.getName());
				
				List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();

				for (User user : users) {
					
					LOGGER.info("Retrive user " + user.getId() + " Email " + user.getEmail());
					
					if (user != null) {

						// Get Email Address from User Profile
						String recipient = user.getEmail();

						if (recipient != null && !recipient.isEmpty()) {

							Email email = new SimpleEmail();
							email.setCharset("utf-8");
							email.setHostName(HOST);
							email.setAuthentication(USER, PWD);

							try {
								email.setFrom("noreply@camunda.org");
								email.setSubject("Task assigned: " + delegateTask.getName());
								email.setMsg(
										"Titolo: "+delegateTask.getVariable("titolo")+" Please complete: http://localhost:8080/camunda/app/tasklist/default/#/task="
												+ taskId);

								email.addTo(recipient);

								// da abilitare per l'invio delle email
								// email.send();
								LOGGER.info("Task Assignment Email successfully sent to user '" + user.getId()
										+ "' with address '" + recipient + "'.");

							} catch (Exception e) {
								LOGGER.log(Level.WARNING, "Could not send email to assignee", e);
							}

						} else {
							LOGGER.warning("Not sending email to user " + user.getId()+ "', user has no email address.");
						}

					} else {
						LOGGER.warning("Not sending email to user " + user.getId() + "', user is not enrolled with identity service.");
					}

				}

			}

		}

	}

}
