@Library('grp.helpers') _

simpleProject {
	config.triggerSetPollSpecEnabled = false
	// stages only on specific project
	config.testEnabled = true
	config.sonarEnabled = true
	config.owaspDependencyCheckEnabled = true
	config.deployEnabled = true
	config.gitlabEnabled = false
	config.githubEnabled = true
	config.githubUrl = 'https://github.com/openwide-java/bindgen/'

	// jenkins choose to poll once by 3 hours
	config.triggerSetPollSpecCronExpression = 'H H/3 * * *'
	config.notificationRecipients = 'grp-jenkins@lists.projects.openwide.fr'
	config.buildBlockerSimpleLock = 'bindgen.*'
	config.jdk = env.JOB_NAME.contains('jdk8') ? 'JDK 1.8' : 'JDK 1.7'
	config.buildTarget = 'install'
	config.defaultMavenArgs = '-Ptest -Dmaven.repo.local="${WORKSPACE}/m2-repository/"'
	config.beforeNotification = {
		util_sh 'rm -rf "${WORKSPACE}/m2-repository/"'
	}
}
