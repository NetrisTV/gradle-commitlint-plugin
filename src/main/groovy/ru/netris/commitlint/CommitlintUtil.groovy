package ru.netris.commitlint

import org.gradle.api.InvalidUserDataException
import java.util.regex.Pattern

@Singleton
class CommitlintUtil {
  final String E_INVALID_TYPE = "Invalid commit type. See https://www.conventionalcommits.org/en/v1.0.0/"
  final String E_LONG_SUBJECT = "Commit message does not follow 50/72 rule. See https://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html"
  final String E_NO_BLANK_LINE = "Use blank line before BODY. https://www.conventionalcommits.org/en/v1.0.0/"
  final String E_LONG_LINE = "Commit message does not follow 50/72 rule. See https://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html"
  // https://semver.org/#is-there-a-suggested-regular-expression-regex-to-check-a-semver-string

  static final Pattern RE_SEMVER = ~/^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(?:-((?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\.(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\+([0-9a-zA-Z-]+(?:\.[0-9a-zA-Z-]+)*))?$/
  
  static final List<Pattern> RE_TO_IGNORE = [
    ~/(?s)^((Merge pull request)|(Merge (.*?) into (.*?)|(Merge branch (.*?)))(?:\r?\n)*$)/,
    ~/(?s)^(R|r)evert (.*)/,
    ~/(?s)^(fixup|squash)! (.*)/,
    ~/(?s)^Merged (.*?)(in|into) (.*)/,
    ~/(?s)^Merge remote-tracking branch (.*)/,
    ~/(?s)^Automatic merge(.*)/,
    ~/(?s)^Auto-merged (.*?) into (.*)/,
  ];
  
  /**
   * ANSII-Colorize string
   * @param {String} s - original string.
   * @param {int} color - ANSII color code.
   * @return {String} "colorized" string.
   */
  def addANSIColor = {String s, int color -> "\033[01;${color}m${s}\033[00m"}

  /**
   * @param {String} msg - commit message.
   * @return {Boolean} whether commit message corresponds to semantic version.
   */
  static Boolean isSemver(String msg) {
    final String firstLine = msg.split("\n").head();

    if (firstLine == null) {
      return false;
    }

    final String stripped = firstLine.replaceFirst(/^chore(\([^)]+\))?:/, '').trim();
    return stripped ==~ RE_SEMVER;
  }
  
  /**
   * @param {String} msg - commit message.
   * @return {Boolean} whether commit message should not be validated.
   */
  static Boolean shouldBeIgnored(String msg) {
    if (isSemver(msg)) return true
    return RE_TO_IGNORE.any { msg ==~ it }
  }
  
  /**
   * Validate commit message against conventional commit rules.
   * See https://www.conventionalcommits.org/en/v1.0.0/
   * See https://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html
   * @param {String} msg - Message to validate.
   * @throws {InvalidUserDataException}
   */
  void validate(String msg) {
    if (shouldBeIgnored(msg)) {
      return
    }
    def lines = msg
      .split("\n")
      .findAll { !it.trim().startsWith("#") }
    
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
