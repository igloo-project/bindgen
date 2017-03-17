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
	config.notificationRecipients = 'grp-jenkins@lists.projects.openwide.fr'
	config.buildBlockerSimpleLock = 'bindgen.*'
	config.jdk = env.JOB_NAME.contains('jdk7') ? 'JDK 1.7' : 'JDK 1.8'
	config.buildTarget = 'install'
	config.defaultMavenArgs = '-Ddistribution=owsi-core-release -DperformRelease=true -Dmaven.repo.local="${WORKSPACE}/m2-repository/"'
	config.beforeNotification = {
		util_sh 'rm -rf "${WORKSPACE}/m2-repository/"'
	}
}
