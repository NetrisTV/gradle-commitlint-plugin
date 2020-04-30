## Overview

Provides support for linting git commit messages against [Conventional Commits][2] rules.

## Configuration
See [tags][1] to determine the
latest available version. Then configure the plugin in your project as
follows:

`build.gradle`
```groovy
plugins {
  id "com.star-zero.gradle.githook" version "1.2.0"
  id "ru.netris:commitlint-plugin" version "1.0"
}

githook {
  failOnMissingHooksDir = false
  createHooksDirIfNotExist = false
  hooks {
    "commit-msg" {
      task = "commitlint -Dmsgfile=\$1"
    }
  }
}
```

`settings.gradle`
```groovy
pluginManagement {
  repositories {
    maven { url "http://repo.netris.ru/repository/maven-releases/" }
	gradlePluginPortal()
  }
  resolutionStrategy {
	eachPlugin {
      if (requested.id.id == "ru.netris.commitlint") {
        useModule("ru.netris:commitlint:${requested.version}")
      }
	}
  }
}

```

## See also

<https://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html>


[1]: https://gitlab.netris.ru/common/commitlint-plugin/-/tags
[2]: https://www.conventionalcommits.org/en/v1.0.0/
