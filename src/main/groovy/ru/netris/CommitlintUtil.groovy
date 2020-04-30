package ru.netris

import org.gradle.api.InvalidUserDataException


@Singleton
class CommitlintUtil {
  final String E_INVALID_TYPE = "Invalid commit type. See https://www.conventionalcommits.org/en/v1.0.0/"
  final String E_LONG_SUBJECT = "Commit message does not follow 50/72 rule. See https://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html"
  final String E_NO_BLANK_LINE = "Use blank line before BODY. https://www.conventionalcommits.org/en/v1.0.0/"
  final String E_LONG_LINE = "Commit message does not follow 50/72 rule. See https://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html"
  
  /**
   * ANSII-Colorize string
   * @param {String} s - original string.
   * @param {int} color - ANSII color code.
   * @return {String} "colorized" string.
   */
  def addANSIColor = {String s, int color -> "\033[01;${color}m${s}\033[00m"}

  /**
   * Validate commit message against conventional commit rules.
   * See https://www.conventionalcommits.org/en/v1.0.0/
   * See https://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html
   * @param {String} msg - Message to validate.
   * @throws {InvalidUserDataException}
   */
  void validate(String msg) {
    def lines = msg.split("\n")
    def re = /^(build|chore|ci|docs|feat|fix|perf|refactor|revert|style|test)(\([a-z ]+\))?!?: .+$/
    if (!(lines[0] =~ re)) {
      throw new InvalidUserDataException(E_INVALID_TYPE)
    }
    if(lines[0]?.size() > 50) {
      throw new InvalidUserDataException(E_LONG_SUBJECT)
    }
    if(lines.size() > 1 && lines[1].size() > 0) {
      throw new InvalidUserDataException(E_NO_BLANK_LINE)
    }
    if(lines.size() > 2 && lines.any { it.size() > 72 }) {
      throw new InvalidUserDataException(E_LONG_LINE);
    }
  }
}
