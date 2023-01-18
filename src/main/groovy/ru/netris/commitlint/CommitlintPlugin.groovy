package ru.netris.commitlint

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.InvalidUserDataException

/**
 * Plugin to validate git commit messages against Conventional Commits rules
 * See https://www.conventionalcommits.org/en/v1.0.0/
 * See https://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html
 */
class CommitlintPlugin implements Plugin<Project> {
  void apply(Project project) {
    def extension = project.extensions.create('commitlint', CommitlintPluginExtension)
    project.task("commitlint") {
      group = "Verification"
      description = "commit lint"
      
      doLast {
        def util = CommitlintUtil.instance
        String msg = new File(System.getProperty("msgfile", ".git/COMMIT_EDITMSG")).text
        try {
          util.validate(msg, extension.enforceRefs.getOrElse(false))
        } catch (InvalidUserDataException e) {
          throw new InvalidUserDataException(util.addANSIColor(e.getMessage(), 31))
        }
        println("commitlint finished successfully")
      }
    }
  }
}
